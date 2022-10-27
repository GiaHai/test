package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UiController("truonghoc_Diemdanh.edit")
@UiDescriptor("diemdanh-edit.xml")
@EditedEntityContainer("diemdanhDc")
@LoadDataBeforeShow
public class DiemdanhEdit extends StandardEditor<Diemdanh> {

    @Inject
    protected DataManager dataManager;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected LookupField<Donvi> donviField;
    @Inject
    protected LookupField<Giaovien> nguoitaoField;
    @Inject
    protected DateField<Date> ngaynghiField;
    @Inject
    protected LookupField<Tenlop> lopField;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected LookupField<Lophoc> hocsinhField;
    private Donvi donViSession = null;
    private Giaovien giaoVienSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        ngaynghiField.setRequired(true);
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
        giaoVienSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            donviField.setValue(donViSession);
            donviField.setEditable(false);
            if (giaoVienSession != null) {
                nguoitaoField.setValue(giaoVienSession);
                nguoitaoField.setEditable(false);
            }
        }
        ngaynghiField.setValue(new Date());
        donviField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
//        if (!donViSession.getDonvitrungtam()) {
//            donviField.setValue(donViSession);
//            donviField.setEditable(false);
//            if (giaoVienSession != null) {
//                nguoitaoField.setValue(giaoVienSession);
//                nguoitaoField.setEditable(false);
//            }
//        }
//
//        ngaynghiField.setValue(new Date());
//        donviField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe("donviField")
    protected void onDonviFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (donviField.getValue() != null) {
            nguoitaoField.setOptionsList(searchedService.loadgiaovien(donviField.getValue()));
        } else {
            nguoitaoField.clear();
        }
    }

    @Subscribe("nguoitaoField")
    protected void onNguoitaoFieldValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        if (nguoitaoField.getValue() != null) {
            lopField.setOptionsList(searchedService.loadlopDK(donviField.getValue(), nguoitaoField.getValue()));
        } else {
            lopField.clear();
        }
    }

    @Subscribe("lopField")
    protected void onLopFieldValueChange(HasValue.ValueChangeEvent<Tenlop> event) {
        hocsinhField.setOptionsList(loadHs());
    }

    private List<Lophoc> loadHs() {
        String query = "select e from truonghoc_Lophoc e ";
        String where = " where 1=1 ";

        Object donvi = donviField.getValue();
        Object giaovien = nguoitaoField.getValue();
        Object lophoc = lopField.getValue();
        Map<String, Object> params = new HashMap<>();

        where += " and e.donvi = :donvi and e.giaoviencn = :giaovien and e.tenlop = :tenlop";
        params.put("donvi", donvi);
        params.put("giaovien", giaovien);
        params.put("tenlop", lophoc);


        return dataManager.load(Lophoc.class)
                .query(query + where)
                .setParameters(params)
                .list();
    }
}