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

@UiController("truonghoc_Lophoc.edit")
@UiDescriptor("lophoc-edit.xml")
@EditedEntityContainer("lophocDc")
@LoadDataBeforeShow
public class LophocEdit extends StandardEditor<Lophoc> {
    @Inject
    protected CollectionContainer<Tenlop> tenlopsDc;
    @Inject
    protected CollectionLoader<Tenlop> tenlopsDl;
    //    @Inject
//    protected LookupField<Giaovien> giaovienField;
    @Inject
    protected DataManager dataManager;
    //    @Inject
//    protected TextField<String> tenlopField;
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
    //    @Inject
//    protected LookupField donviFiled;
    @Inject
    protected TextField<Giaovien> giaovien;
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

    @Subscribe
    protected void onInit(InitEvent event) {
//        tenlopField.setRequired(true);
        DvField.setEditable(false);
        giaovien.setEditable(false);
        DvField.setOptionsList(loaddonvi());
//        tenlopField.setEditable(false);
//        donvisDl.load();
//        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
//                .map(Donvi::getTendonvi)
//                .collect(Collectors.toList());
//        donviFiled.setOptionsList(sessionTypeNames);

    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
//        if (tenlopField.getValue() != null && giaovien.getValue() != null){
//            giaovienField.setRequired(false);
//        }else {
//            giaovienField.setRequired(true);
//        }

//        dkhienthiok();

        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
//            donviFiled.setEditable(false);
//            donviFiled.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
            DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
//                donviFiled.setEditable(false);
//                donviFiled.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
//                giaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
//                giaovienField.setEditable(false);
                DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                giaovien.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                tenlopField.setOptionsList(loadlop(DvField.getValue(), giaovien.getValue()));
            }
        }

    }
    private List<Tenlop> loadlop(Object donvi, Object giaoviencn){
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn = :giaoviencn")
                .parameter("donvi", donvi)
                .parameter("giaoviencn", giaoviencn)
                .list();
    }
//    @Subscribe("donviFiled")
//    protected void onDonviFiledValueChange(HasValue.ValueChangeEvent event) {
//        giaovienField.setOptionsList(loadgiaovien());
//    }
    // Điều kiện
//    @Subscribe("timkiemBtn")
//    protected void onTimkiemBtnClick(Button.ClickEvent event) {
//        try {
//            if (loadLop().getTenlop() != null) {
//                tenlopField.setValue(loadLop().getTenlop());
//                tenlopField.setEditable(false);
//                giaovien.setValue(giaovienField.getValue());
//                DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
//            }
//        } catch (IllegalStateException ex) {
//            tenlopField.clear();
//            tenlopField.setEditable(false);
//        }catch (NullPointerException ec){}
//    }
//    private void dkhienthiok(){
//        if (tenlopField.getValue() != null && giaovien.getValue() != null){
//            commitAndCloseBtn.setVisible(true);
//        }else {
//            commitAndCloseBtn.setVisible(false);
//        }
//    }
    // SQL
//    private List<Giaovien> loadgiaovien() {
//        return dataManager.load(Giaovien.class)
//                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien.tendonvi = :donvi")
//                .parameter("donvi", donviFiled.getValue())
//                .list();
//    }

//    private Tenlop loadLop() {
//        return dataManager.load(Tenlop.class)
//                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn.tengiaovien = :tengiaovien")
//                .parameter("tengiaovien", giaovienField.getValue().getTengiaovien())
//                .parameter("donvi", donviFiled.getValue())
//                .one();
//    }

//    @Subscribe("xoaBtn")
//    protected void onXoaBtnClick(Button.ClickEvent event) {
//        donviFiled.clear();
//        giaovienField.clear();
//    }

    //    @Subscribe("tenlopField")
//    protected void onTenlopFieldValueChange(HasValue.ValueChangeEvent<String> event) {
//        dkhienthiok();
//    }
//
//    @Subscribe("giaovien")
//    protected void onGiaovienValueChange(HasValue.ValueChangeEvent<String> event) {
//        dkhienthiok();
//    }
    private List<Donvi> loaddonvi() {
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .list();
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