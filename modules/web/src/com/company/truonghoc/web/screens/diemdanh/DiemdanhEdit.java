package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.DateField;
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
    protected TextField<Donvi> donviField;
    @Inject
    protected TextField<Giaovien> nguoitaoField;
    @Inject
    protected DateField<Date> ngaynghiField;
    @Inject
    protected TextField<Lophoc> lopField;

    @Subscribe
    protected void onInit(InitEvent event) {
        nguoitaoField.setEditable(false);
        donviField.setEditable(false);
        ngaynghiField.setRequired(true);
        lopField.setEditable(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        nguoitaoField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
        donviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());

        ngaynghiField.setValue(new Date());

    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
//        System.out.println(loadlopdd().getTenlop());
        lopField.setValue(loadlopdd(donviField.getValue().getTendonvi() , nguoitaoField.getValue().getTengiaovien()));
    }

    private Lophoc loadlopdd(Object donvi, Object tengv) {
        String query = "select e from truonghoc_Lophoc e where e.donvi.tendonvi = :donvi and e.giaoviencn.tengiaovien = :tengv";
        return dataManager.load(Lophoc.class)
                .query(query)
                .parameter("donvi", donvi)
                .parameter("tengv", tengv)
                .one();
    }

    public List<Hocsinh> test(String hocsinh, String donvitao) {
        return dataManager.load(Hocsinh.class)
                .query("select e from truonghoc_Hocsinh e where e.usertao_hocsinh = :user and e.donvitao_hocsinh = :donvitao")
                .parameter("user", hocsinh)
                .parameter("donvitao", donvitao)
                .list();
    }
}