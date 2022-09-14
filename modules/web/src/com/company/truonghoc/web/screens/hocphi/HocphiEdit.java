package com.company.truonghoc.web.screens.hocphi;

import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
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
    protected DateField<Date> namsinhField;
    @Inject
    protected CollectionContainer<Hocsinh> hocsinhsDc;
    @Inject
    protected TextField<String> dovitao_hocphiField;
    @Inject
    protected TextField<String> usertaoField;
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

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Tiền mặt", "Chuyển khoản");
        hinhthucthanhtoanField.setOptionsList(list);
        dovitao_hocphiField.setEditable(false);
        usertaoField.setEditable(false);
        namsinhField.setEditable(false);
        tinhtrangthanhtoanFiedl.setVisible(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        usertaoField.setValue(userSession.getUser().getLogin());
        dovitao_hocphiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        handongField.setValue(calendar.getTime());

        tinhtrangthanhtoanFiedl.setValue("Chưa thanh toán");

    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
         hovatenField.setOptionsList(hocsinhList(getEditedEntity().getDovitao_hocphi()));
        if (hovatenField.getValue() != null){
            hovatenField.setEditable(false);
            namsinhField.setEditable(false);
        }
    }
    @Subscribe("hovatenField")
    protected void onHovatenFieldValueChange(HasValue.ValueChangeEvent<Hocsinh> event) {
        hovatenField.setValue((Hocsinh) hovatenField.getValue());
        namsinhField.setValue(((Hocsinh) hovatenField.getValue()).getNgaysinhhocsinh());
    }

    private List<Hocsinh> hocsinhList(String dovitao_hocphi) {
        return
                dataManager.load(Hocsinh.class)
                        .query("select e from truonghoc_Hocsinh e where e.donvitao_hocsinh = :donvitao_hocphiField")
                        .parameter("donvitao_hocphiField", dovitao_hocphi)
                        .list();
    }

    @Subscribe("sotienthutheohdField")
    protected void onSotienthutheohdFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
//        hinhthucthanhtoanField.setRequired(true);
//        ngaydongField.setRequired(true);
//        làm lại
    }

    @Subscribe("hinhthucthanhtoanField")
    protected void onHinhthucthanhtoanFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (hinhthucthanhtoanField.getValue() ==null){
            tinhtrangthanhtoanFiedl.setValue("Chưa thanh toán");
        }else {
            tinhtrangthanhtoanFiedl.setValue("Đã thanh toán");
        }
    }

    @Subscribe("ngaydongField")
    protected void onNgaydongFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (ngaydongField.getValue() != null){
            handongField.setVisible(false);
            handongField.clear();
            hinhthucthanhtoanField.setRequired(true);
            hinhthucthanhtoanField.setVisible(true);
        }else {
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


}