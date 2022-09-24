package com.company.truonghoc.web.screens.giaovien;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.LookupField;
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
    protected LookupField<Donvi> donvitao_giaovienField;
    @Inject
    protected LookupField<String> gioitinhgiaovienField;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected DataManager dataManager;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Nam", "Nữ", "Tùy chỉnh");
        gioitinhgiaovienField.setOptionsList(list);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null ){
            donvitao_giaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donvitao_giaovienField.setEditable(false);
        }else {
            donvitao_giaovienField.setOptionsList(test());
        }
    }

    private List<Donvi> test(){
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .list();
    }
}