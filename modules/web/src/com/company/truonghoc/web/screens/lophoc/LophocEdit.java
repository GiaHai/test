package com.company.truonghoc.web.screens.lophoc;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.Tenlop;
import com.company.truonghoc.service.DulieuUserService;
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
import java.util.List;
import java.util.stream.Collectors;

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
    protected LookupField<Giaovien> giaovienField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TextField<String> tenlopField;
    @Inject
    protected Table<Hocsinh> dshocsinhTable;
    @Inject
    protected GroupBoxLayout dshocsinhBox;
    @Inject
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected LookupField donviFiled;
    @Inject
    protected TextField<String> giaovien;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected Button commitAndCloseBtn;
    @Inject
    protected Button closeBtn;
    @Inject
    protected TextField<String> DvField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;

    @Subscribe
    protected void onInit(InitEvent event) {
        tenlopField.setRequired(true);
        DvField.setEditable(false);
        giaovien.setEditable(false);
        tenlopField.setEditable(false);
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        donviFiled.setOptionsList(sessionTypeNames);

    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (tenlopField.getValue() != null && giaovien.getValue() != null){
            giaovienField.setRequired(false);
        }else {
            giaovienField.setRequired(true);
        }

        dkhienthiok();

        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0){
            donviFiled.setEditable(false);
            donviFiled.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
        }
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null){
            donviFiled.setEditable(false);
            donviFiled.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
            
        }
    }

    @Subscribe("donviFiled")
    protected void onDonviFiledValueChange(HasValue.ValueChangeEvent event) {
        giaovienField.setOptionsList(loadgiaovien());
    }
    // Điều kiện
    @Subscribe("timkiemBtn")
    protected void onTimkiemBtnClick(Button.ClickEvent event) {
        try {
            if (loadLop().getTenlop() != null) {
                tenlopField.setValue(loadLop().getTenlop());
                tenlopField.setEditable(false);
                giaovien.setValue(giaovienField.getValue().getTengiaovien());
                DvField.setValue(donviFiled.getValue().toString());
            }
        } catch (IllegalStateException ex) {
            tenlopField.clear();
            tenlopField.setEditable(false);
        }catch (NullPointerException ec){}
    }
    private void dkhienthiok(){
        if (tenlopField.getValue() != null && giaovien.getValue() != null){
            commitAndCloseBtn.setVisible(true);
        }else {
            commitAndCloseBtn.setVisible(false);
        }
    }
    // SQL
    private List<Giaovien> loadgiaovien() {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :donvi")
                .parameter("donvi", donviFiled.getValue())
                .list();
    }

    private Tenlop loadLop() {
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn.tengiaovien = :tengiaovien")
                .parameter("tengiaovien", giaovienField.getValue().getTengiaovien())
                .parameter("donvi", donviFiled.getValue())
                .one();
    }

    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        donviFiled.clear();
        giaovienField.clear();
    }

    @Subscribe("tenlopField")
    protected void onTenlopFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        dkhienthiok();
    }

    @Subscribe("giaovien")
    protected void onGiaovienValueChange(HasValue.ValueChangeEvent<String> event) {
        dkhienthiok();
    }
    
    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = hocsinhsDl.getContainer().getItemIndex(entity.getId()) + 1;
        }
        catch (Exception ex)
        {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}