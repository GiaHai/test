package com.company.truonghoc.web.screens.thutienhocphi;

import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Thutienhocphi;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

@UiController("truonghoc_Thutienhocphi.edit")
@UiDescriptor("thutienhocphi-edit.xml")
@EditedEntityContainer("thutienhocphiDc")
@LoadDataBeforeShow
public class ThutienhocphiEdit extends StandardEditor<Thutienhocphi> {
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected TextField<Long> thanhtienField;
    @Inject
    protected TextField<Long> soluongField;
    @Inject
    protected TextField<Long> dongiaField;
    @Inject
    protected TextField<String> usertaoField;
    @Inject
    protected TextField<String> dovitao_thutienhocphiField;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected LookupField<String> hinhthucthanhtoanField;
    @Inject
    protected TextField<String> tinhtrangthanhtoanField;
    @Inject
    protected LookupField<Hocsinh> tenhocsinhField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TextField<String> tenphiField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Tiền mặt", "Chuyển khoản");
        hinhthucthanhtoanField.setOptionsList(list);

        dovitao_thutienhocphiField.setEditable(false);
        usertaoField.setEditable(false);
        dongiaField.setRequired(true);
        tenphiField.setRequired(true);
        thanhtienField.setEditable(false);
        tinhtrangthanhtoanField.setVisible(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        usertaoField.setValue(userSession.getUser().getLogin());
        dovitao_thutienhocphiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());

        Calendar calendar = Calendar.getInstance();
        tungayField.setValue(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        denngayField.setValue(calendar.getTime());

        tinhtrangthanhtoanField.setValue("Chưa thanh toán");
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        tenhocsinhField.setOptionsList(hocsinhList(getEditedEntity().getDonvitao_thutienhocphi()));
    }

    private List<Hocsinh> hocsinhList(String dovitao_hocphi) {
        return
                dataManager.load(Hocsinh.class)
                        .query("select e from truonghoc_Hocsinh e where e.donvitao_hocsinh = :donvitao_hocphiField")
                        .parameter("donvitao_hocphiField", dovitao_hocphi)
                        .list();
    }

    protected void thanhtien() {
        if (dongiaField.getValue() != null) {
            if (soluongField.getValue() != null) {
                thanhtienField.setValue(soluongField.getValue() * dongiaField.getValue());
            } else {
                thanhtienField.setValue(dongiaField.getValue());
            }
        }
    }

    @Subscribe("dongiaField")
    protected void onDongiaFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        thanhtien();
    }

    @Subscribe("soluongField")
    protected void onSoluongFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        thanhtien();
    }

    @Subscribe("hinhthucthanhtoanField")
    protected void onHinhthucthanhtoanFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (hinhthucthanhtoanField.getValue() ==null){
            tinhtrangthanhtoanField.setValue("Chưa thanh toán");
        }else {
            tinhtrangthanhtoanField.setValue("Đã thanh toán");
        }
    }

}