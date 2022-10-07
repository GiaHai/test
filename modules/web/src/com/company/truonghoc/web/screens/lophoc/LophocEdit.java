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
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Lophoc;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected LookupField<Giaovien> giaovienField;
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
    @Inject
    protected DataContext dataContext;
    @Inject
    protected PickerField<Hocsinh> dsHocsinhField;

    @Subscribe
    protected void onInit(InitEvent event) {
        DvField.setOptionsList(loaddonvi());
        tenlopField.setRequired(true);
        tenlopField.setRequiredMessage("Nhập tên lớp");
        DvField.setRequired(true);
        DvField.setRequiredMessage("Nhập đơn vị");
        giaovienField.setRequired(true);
        giaovienField.setRequiredMessage("Nhập giáo viên");
        dsHocsinhField.setRequired(true);
        dsHocsinhField.setRequiredMessage("Chọn học sinh");
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                DvField.setEditable(false);
                DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                    DvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                    giaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                    giaovienField.setEditable(false);
                }
            }
        }
    }

    @Subscribe("DvField")
    protected void onDvFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (DvField.getValue() != null) {
            giaovienField.setOptionsList(loadgiaovien(DvField.getValue()));
        } else {
            giaovienField.clear();
        }
    }

    @Subscribe("giaovienField")
    protected void onGiaovienFieldValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        if (giaovienField.getValue() != null) {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() == null){
                tenlopField.setOptionsList(searchedService.loadlop(DvField.getValue().getTendonvi(), giaovienField.getValue().getTengiaovien()));
            }else {
                tenlopField.setOptionsList(searchedService.loadlopDK(DvField.getValue().getTendonvi(), giaovienField.getValue().getTengiaovien()));
            }
        } else {
            tenlopField.clear();
        }
    }



    private List<Giaovien> loadgiaovien(Object donvi) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :donvi")
                .parameter("donvi", donvi)
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

    @Subscribe("commitAndCloseBtn")
    protected void onCommitAndCloseBtnClick(Button.ClickEvent event) {
        commitChanges().then(() -> {
            dataContext.clear();
            Lophoc lophoc = dataContext.create(Lophoc.class);
            lophoc.setDonvi(getEditedEntity().getDonvi());
            lophoc.setGiaoviencn(getEditedEntity().getGiaoviencn());
            lophoc.setTenlop(getEditedEntity().getTenlop());
            getEditedEntityContainer().setItem(lophoc);

        });
    }
}