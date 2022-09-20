package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Diemdanh;
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
    protected TextField<String> nguoitaoField;
    @Inject
    protected TextField<String> donviField;
    @Inject
    protected DateField<Date> ngaynghiField;

    @Subscribe
    protected void onInit(InitEvent event) {
        nguoitaoField.setEditable(false);
        donviField.setEditable(false);
        ngaynghiField.setRequired(true);

    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        nguoitaoField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv());
        donviField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());

        ngaynghiField.setValue(new Date());

    }


//    @Subscribe
//    protected void onAfterShow(AfterShowEvent event) {
//        hotenhsField.setOptionsList(test(userSession.getUser().getLogin() , dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()));
//    }

    public List<Hocsinh> test(String hocsinh , String donvitao){
        return dataManager.load(Hocsinh.class)
                .query("select e from truonghoc_Hocsinh e where e.usertao_hocsinh = :user and e.donvitao_hocsinh = :donvitao")
                .parameter("user", hocsinh)
                .parameter("donvitao", donvitao)
                .list();
    }
}