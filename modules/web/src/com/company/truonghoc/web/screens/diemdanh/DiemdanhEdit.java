package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

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
    protected LookupField<Lophoc> lopField;
    @Inject
    protected SearchedService searchedService;

    @Subscribe
    protected void onInit(InitEvent event) {
//        quyền
//        nguoitaoField.setEditable(false);
//        donviField.setEditable(false);
        ngaynghiField.setRequired(true);

    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
//        quyền
//        donviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
//        nguoitaoField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());

        ngaynghiField.setValue(new Date());
        donviField.setOptionsList(searchedService.loaddonvi());
//        nguoitaoField.setOptionsList(searchedService.loadgiaovien(donviField.getValue().getTendonvi()));
    }

    private List<Lophoc> loadlopdd(Object donvi, Object tengv) {
        String query = "select e from truonghoc_Lophoc e where e.donvi.tendonvi = :donvi and e.giaoviencn.tengiaovien = :tengv and e.tenlop.tinhtranglop = true";
        return dataManager.load(Lophoc.class)
                .query(query)
                .parameter("donvi", donvi)
                .parameter("tengv", tengv)
                .list();
    }

    @Subscribe("donviField")
    protected void onDonviFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        nguoitaoField.setOptionsList(searchedService.loadgiaovien(donviField.getValue().getTendonvi()));
    }

    @Subscribe("nguoitaoField")
    protected void onNguoitaoFieldValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        lopField.setOptionsList(loadlopdd(donviField.getValue().getTendonvi(), nguoitaoField.getValue().getTengiaovien()));
    }

}