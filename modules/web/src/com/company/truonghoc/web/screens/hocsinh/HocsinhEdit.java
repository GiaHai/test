package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.InstanceContainer;
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
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected InstanceContainer<Hocsinh> hocsinhDc;
    @Inject
    protected LookupField<Donvi> donvitao_hocsinhField;
    @Inject
    protected TextField<String> tenhocsinhField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Nam", "Nữ", "Tùy chỉnh");
        gioitinhhocsinhField.setOptionsList(list);
        donvitao_hocsinhField.setRequired(true);
        donvitao_hocsinhField.setRequiredMessage("Nhập đơn vị");
        tenhocsinhField.setRequired(true);
        tenhocsinhField.setRequiredMessage("Nhập tên học sinh");
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                donvitao_hocsinhField.setEditable(false);
                donvitao_hocsinhField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            }
        }
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {

//        usertaoField.setValue(hocsinhDc.getItem().getLophoc().getGiaoviencn());
    }
}