package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocsinh;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("truonghoc_Hocsinh.edit")
@UiDescriptor("hocsinh-edit.xml")
@EditedEntityContainer("hocsinhDc")
@LoadDataBeforeShow
public class HocsinhEdit extends StandardEditor<Hocsinh> {
    @Inject
    protected LookupField<String> gioitinhhocsinhField;
    @Inject
    protected TextField<String> usertaoField;
    @Inject
    protected UserSession userSession;
    @Inject
    protected TextField<String> donvitao_hocsinhField;
    @Inject
    protected DulieuUserService dulieuUserService;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Nam", "Nữ", "Tùy chỉnh");
        gioitinhhocsinhField.setOptionsList(list);
        usertaoField.setEditable(false);
        donvitao_hocsinhField.setEditable(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        donvitao_hocsinhField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
    }
    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (usertaoField.getValue() == null){
            usertaoField.setValue(userSession.getUser().getLogin());
        }
    }
}