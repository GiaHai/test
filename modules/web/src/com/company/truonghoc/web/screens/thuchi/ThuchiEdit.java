package com.company.truonghoc.web.screens.thuchi;

import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Thuchi;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@UiController("truonghoc_Thuchi.edit")
@UiDescriptor("thuchi-edit.xml")
@EditedEntityContainer("thuchiDc")
@LoadDataBeforeShow
public class ThuchiEdit extends StandardEditor<Thuchi> {
    @Inject
    protected TextField<Long> dongiaField;
    @Inject
    protected TextField<Integer> soluongField;
    @Inject
    protected TextField<Long> thanhtienField;
    @Inject
    protected Notifications notifications;
    @Inject
    protected Button commitAndCloseBtn;
    @Inject
    protected TextField<String> usertao_thuchiField;
    @Inject
    protected TextField<String> donvitao_thuchiField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DateField<Date> hanchiField;
    @Inject
    protected DateField<Date> ngaychiField;
    @Inject
    protected TextField<String> tinhtrangchiField;
    @Inject
    protected LookupField<String> hinhthucthanhtoanField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Tiền mặt", "Chuyển khoản");
        hinhthucthanhtoanField.setOptionsList(list);
//        thanhtienField.setVisible(false);
        usertao_thuchiField.setEditable(false);
        donvitao_thuchiField.setEditable(false);
        hanchiField.setRequired(true);
        tinhtrangchiField.setEditable(false);
        thanhtienField.setEditable(false);
        hinhthucthanhtoanField.setVisible(false);
        tinhtrangchiField.setVisible(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        donvitao_thuchiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
        usertao_thuchiField.setValue(userSession.getUser().getLogin());
        tinhtrangchiField.setValue("Chưa thanh toán");

    }

    @Subscribe("dongiaField")
    protected void onDongiaFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        if (soluongField.getValue() == null) {
            thanhtienField.setValue(dongiaField.getValue());
        } else {
            thanhtienField.setValue(dongiaField.getValue() * soluongField.getValue());
        }

    }

    @Subscribe("soluongField")
    protected void onSoluongFieldValueChange(HasValue.ValueChangeEvent<Integer> event) {
        if (soluongField.getValue() == null) {
            thanhtienField.setValue(dongiaField.getValue());
        } else {
            if (thanhtienField.getValue() != null) {
                thanhtienField.setValue(dongiaField.getValue() * soluongField.getValue());
            }
        }
    }

    @Subscribe("ngaychiField")
    protected void onNgaychiFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (ngaychiField.getValue() != null){
            hanchiField.setVisible(false);
            hanchiField.clear();
            tinhtrangchiField.setValue("Đã thanh toán");
            hinhthucthanhtoanField.setVisible(true);
            hinhthucthanhtoanField.setRequired(true);
            dongiaField.setRequired(true);
        }else {
            hanchiField.setRequired(true);
            hanchiField.setVisible(true);
            tinhtrangchiField.setValue("Chưa thanh toán");
            hinhthucthanhtoanField.setVisible(false);
            hinhthucthanhtoanField.setRequired(false);
        }
    }

    @Subscribe("hanchiField")
    protected void onHanchiFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        tinhtrangchiField.setValue("Chưa thanh toán");
    }



    

}