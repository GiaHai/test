package com.company.truonghoc.web.screens.hocphi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocphi;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.gui.components.JavaScriptComponent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import javax.inject.Inject;
import java.io.File;
import java.io.StringWriter;
import java.util.*;
import java.util.Calendar;

@UiController("truonghoc_Hocphi.edit")
@UiDescriptor("hocphi-edit.xml")
@EditedEntityContainer("hocphiDc")
@LoadDataBeforeShow
public class HocphiEdit extends StandardEditor<Hocphi> {
    @Inject
    protected LookupField<Donvi> donViField;
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
    @Inject
    protected TextField<Long> sotienthutheohdField;
    @Inject
    protected UiComponents uiComponents;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Ti???n m???t", "Chuy???n kho???n");
        hinhthucthanhtoanField.setOptionsList(list);
        tinhtrangthanhtoanFiedl.setVisible(false);

        hovatenField.setRequired(true);
        sotienthutheohdField.setRequired(true);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
//        quy???n
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donViField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donViField.setEditable(false);
        }
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        handongField.setValue(calendar.getTime());

        tinhtrangthanhtoanFiedl.setValue("Ch??a thanh to??n");
        donViField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe("hinhthucthanhtoanField")
    protected void onHinhthucthanhtoanFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (hinhthucthanhtoanField.getValue() == null) {
            tinhtrangthanhtoanFiedl.setValue("Ch??a thanh to??n");
        } else {
            tinhtrangthanhtoanFiedl.setValue("???? thanh to??n");
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

    @Subscribe("donViField")
    protected void onDonViFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        hovatenField.setOptionsList(searchedService.loadHs(donViField.getValue().getTendonvi()));
    }

}