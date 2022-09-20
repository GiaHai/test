package com.company.truonghoc.web.screens.giaovien;

import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Giaovien;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("truonghoc_Giaovien.edit")
@UiDescriptor("giaovien-edit.xml")
@EditedEntityContainer("giaovienDc")
@LoadDataBeforeShow
public class GiaovienEdit extends StandardEditor<Giaovien> {
    @Inject
    protected LookupField<String> gioitinhgiaovienField;
    @Inject
    protected UserSession userSession;
    @Inject
    protected TextField<String> donvitao_giaovienField;
    @Inject
    protected DulieuUserService dulieuUserService;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Nam", "Nữ", "Tùy chỉnh");
        gioitinhgiaovienField.setOptionsList(list);
        donvitao_giaovienField.setEditable(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        donvitao_giaovienField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
    }
    

}