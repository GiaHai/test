package com.company.truonghoc.web.screens.lophoc;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.Tenlop;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
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
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected DataManager dataManager;
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
    protected TextField<Integer> thanghocField;
    @Inject
    protected DataContext dataContext;
    @Inject
    protected PickerField<Hocsinh> dsHocsinhField;
    @Inject
    protected Notifications notifications;
    private Donvi donViSession = null;
    private Giaovien giaoVienSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        DvField.setOptionsList(searchedService.loaddonvi());
        tenlopField.setRequired(true);
        tenlopField.setRequiredMessage("Nhập tên lớp");
        DvField.setRequired(true);
        DvField.setRequiredMessage("Nhập đơn vị");
        giaovienField.setRequired(true);
        giaovienField.setRequiredMessage("Nhập giáo viên");
        dsHocsinhField.setRequired(true);
        dsHocsinhField.setRequiredMessage("Chọn học sinh");
        //Đơn vị
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
        //Giáo viên
        giaoVienSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            DvField.setEditable(false);
            DvField.setValue(donViSession);
            if (giaoVienSession != null) {
                DvField.setValue(donViSession);
                giaovienField.setValue(giaoVienSession);
                giaovienField.setEditable(false);
            }
        }
    }

    @Subscribe("DvField")
    protected void onDvFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (DvField.getValue() != null) {
            giaovienField.setOptionsList(searchedService.loadgiaovien(DvField.getValue()));
        } else {
            giaovienField.clear();
        }
    }

    @Subscribe("giaovienField")
    protected void onGiaovienFieldValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        if (giaovienField.getValue() != null) {
            if (giaoVienSession == null) {
                tenlopField.setOptionsList(searchedService.loadlop(DvField.getValue(), giaovienField.getValue()));
            } else {
                tenlopField.setOptionsList(searchedService.loadlopDK(DvField.getValue(), giaovienField.getValue()));
            }
        } else {
            tenlopField.clear();
        }
    }

    @Subscribe("tenlopField")
    protected void onTenlopFieldValueChange(HasValue.ValueChangeEvent<Tenlop> event) {
        if (tenlopField.getValue() == null) {
            thanghocField.clear();
        } else {
            thanghocField.setValue(tenlopField.getValue().getThanghoc().intValue());
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
        if (dkTrungten().size() == 0) {
            commitChanges().then(() -> {
                dataContext.clear();
                Lophoc lophoc = dataContext.create(Lophoc.class);
                lophoc.setDonvi(getEditedEntity().getDonvi());
                lophoc.setGiaoviencn(getEditedEntity().getGiaoviencn());
                lophoc.setTenlop(getEditedEntity().getTenlop());
                getEditedEntityContainer().setItem(lophoc);
            });
        } else {
            dsHocsinhField.clear();
            notifications.create()
                    .withCaption("Không thể thêm 2 học sinh giống nhau vào 1 lớp")
                    .withType(Notifications.NotificationType.ERROR)
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .show();
        }
    }

    /*** sự kiện không thể thêm 2 học sinh giống nhau vào 1 lớp***/
    private List<Lophoc> dkTrungten() {
        return dataManager.load(Lophoc.class)
                .query("select e from truonghoc_Lophoc e where e.tenlop = :tenlop and e.dshocsinh = :hocsinh")
                .parameter("tenlop", tenlopField.getValue())
                .parameter("hocsinh", dsHocsinhField.getValue())
                .list();
    }
}