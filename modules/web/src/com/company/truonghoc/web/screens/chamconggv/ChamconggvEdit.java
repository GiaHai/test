package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@UiController("truonghoc_Chamconggv.edit")
@UiDescriptor("chamconggv-edit.xml")
@EditedEntityContainer("chamconggvDc")
@LoadDataBeforeShow
public class ChamconggvEdit extends StandardEditor<Chamconggv> {
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Giaovien> hotenGvField;
    @Inject
    protected LookupField<Donvi> donvigvField;
    @Inject
    protected LookupField<String> buoilamField;
    @Inject
    protected DateField<Date> ngaylamField;

    @Subscribe
    protected void onInit(InitEvent event) {
//        donvigvField.setEditable(false);
        hotenGvField.setRequired(true);
        ngaylamField.setRequired(true);
        buoilamField.setRequired(true);
        List<String> list = Arrays.asList("Làm cả ngày", "Ca sáng", "Ca chiều");
        buoilamField.setOptionsList(list);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null){
            donvigvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donvigvField.setEditable(false);
        }else {
            donvigvField.setOptionsList(load());
        }
    }


    @Subscribe("donvigvField")
    protected void onDonvigvFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        hotenGvField.setOptionsList(timtengiaovien(donvigvField.getValue()));
    }

    public List<Giaovien> timtengiaovien(Donvi donvitao){
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :donvitao")
                .parameter("donvitao", donvitao)
                .list();
    }

    private List<Donvi> load(){
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .list();
    }
}