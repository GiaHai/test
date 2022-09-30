package com.company.truonghoc.web.screens.lophoc;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.Tenlop;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Lophoc;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@UiController("truonghoc_Lophoc.edit")
@UiDescriptor("lophoc-edit.xml")
@EditedEntityContainer("lophocDc")
@LoadDataBeforeShow
public class LophocEdit extends StandardEditor<Lophoc> {
    @Inject
    protected CollectionContainer<Tenlop> tenlopsDc;
    @Inject
    protected CollectionLoader<Tenlop> tenlopsDl;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected LookupField<Giaovien> giaovien;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected Button commitAndCloseBtn;
    @Inject
    protected Button closeBtn;
    @Inject
    protected LookupField<Donvi> DvField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField<Tenlop> tenlopField;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected TextField<String> thanghocField;

    @Subscribe
    protected void onInit(InitEvent event) {
//        DvField.setEditable(false);
//        giaovien.setEditable(false);
        DvField.setOptionsList(loaddonvi());
        tenlopField.setRequired(true);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (getEditedEntity().getCreatedBy() == null) {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                    DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                    giaovien.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                    tenlopField.setOptionsList(loadlop(DvField.getValue(), giaovien.getValue()));
                }
            } else {
//                phân quyền
//                DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
//                giaovien.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
            }
        } else {
            tenlopField.setOptionsList(loadlop(DvField.getValue(), giaovien.getValue()));
        }
    }

    @Subscribe("DvField")
    protected void onDvFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        giaovien.setOptionsList(loadgiaovien(DvField.getValue()));
    }

    @Subscribe("giaovien")
    protected void onGiaovienValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        tenlopField.setOptionsList(searchedService.loadlopDK(DvField.getValue().getTendonvi(), giaovien.getValue().getTengiaovien()));
    }

    private List<Giaovien> loadgiaovien(Object donvi) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :donvi")
                .parameter("donvi", donvi)
                .list();
    }

    private List<Tenlop> loadlop(Object donvi, Object giaoviencn) {
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn = :giaoviencn and e.tinhtranglop = true")
                .parameter("donvi", donvi)
                .parameter("giaoviencn", giaoviencn)
                .list();
    }

    private List<Donvi> loaddonvi() {
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .list();
    }

    @Subscribe("tenlopField")
    protected void onTenlopFieldValueChange(HasValue.ValueChangeEvent<Tenlop> event) {
        if (tenlopField.getValue() == null) {
            thanghocField.clear();
        } else {
            thanghocField.setValue(tenlopField.getValue().getThanghoc());
        }
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = hocsinhsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}