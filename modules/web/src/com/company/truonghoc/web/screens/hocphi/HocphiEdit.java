package com.company.truonghoc.web.screens.hocphi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocphi;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@UiController("truonghoc_Hocphi.edit")
@UiDescriptor("hocphi-edit.xml")
@EditedEntityContainer("hocphiDc")
@LoadDataBeforeShow
public class HocphiEdit extends StandardEditor<Hocphi> {
    @Inject
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected LookupField<Donvi> dovitao_hocphiField;
    @Inject
    protected CollectionContainer<Hocsinh> hocsinhsDc;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected LookupField<String> hinhthucthanhtoanField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Hocsinh> hovatenField;
    @Inject
    protected TextArea<String> ghichuField;
    @Inject
    protected DateField<Date> handongField;
    @Inject
    protected TextField<String> tinhtrangthanhtoanFiedl;
    @Inject
    protected DateField<Date> ngaydongField;
    @Inject
    protected SearchedService searchedService;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Tiền mặt", "Chuyển khoản");
        hinhthucthanhtoanField.setOptionsList(list);
        tinhtrangthanhtoanFiedl.setVisible(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
//        quyền
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            dovitao_hocphiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            dovitao_hocphiField.setEditable(false);
        }
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        handongField.setValue(calendar.getTime());

        tinhtrangthanhtoanFiedl.setValue("Chưa thanh toán");
        dovitao_hocphiField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe("hinhthucthanhtoanField")
    protected void onHinhthucthanhtoanFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (hinhthucthanhtoanField.getValue() == null) {
            tinhtrangthanhtoanFiedl.setValue("Chưa thanh toán");
        } else {
            tinhtrangthanhtoanFiedl.setValue("Đã thanh toán");
        }
    }

    @Subscribe("ngaydongField")
    protected void onNgaydongFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (ngaydongField.getValue() != null) {
            handongField.setVisible(false);
            handongField.clear();
            hinhthucthanhtoanField.setRequired(true);
            hinhthucthanhtoanField.setVisible(true);
        } else {
            handongField.setRequired(true);
            handongField.setVisible(true);
            hinhthucthanhtoanField.setRequired(false);
            hinhthucthanhtoanField.setVisible(false);
        }

    }

    @Subscribe("handongField")
    protected void onHandongFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        hinhthucthanhtoanField.setVisible(false);
    }

    @Subscribe("dovitao_hocphiField")
    protected void onDovitao_hocphiFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        hovatenField.setOptionsList(searchedService.loadHs(dovitao_hocphiField.getValue().getTendonvi()));
    }
}