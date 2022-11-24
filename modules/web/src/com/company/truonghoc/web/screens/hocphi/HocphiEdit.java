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
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected LookupField<Donvi> donViField;
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
    @Inject
    protected TextField<Long> sotienthutheohdField;
//    @Inject
//    protected JavaScriptComponent printCuba;
//    @Inject
//    protected VBoxLayout vboxHtmlLayout;
    @Inject
    protected UiComponents uiComponents;
//    private String webBaseFolder;
//    private String pathPdf;
//    private HtmlBoxLayout htmlBoxReport;
//    private String PATH_TEMPLATE = "";

    @Subscribe
    protected void onInit(InitEvent event) {
//        PATH_TEMPLATE = AppContext.getProperty("knkx.report.template.html");

        List<String> list = Arrays.asList("Tiền mặt", "Chuyển khoản");
        hinhthucthanhtoanField.setOptionsList(list);
        tinhtrangthanhtoanFiedl.setVisible(false);

        hovatenField.setRequired(true);
        sotienthutheohdField.setRequired(true);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
//        quyền
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

        tinhtrangthanhtoanFiedl.setValue("Chưa thanh toán");
        donViField.setOptionsList(searchedService.loaddonvi());
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

    @Subscribe("donViField")
    protected void onDonViFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        hovatenField.setOptionsList(searchedService.loadHs(donViField.getValue().getTendonvi()));
    }


//    @Subscribe("printBtn")
//    protected void onPrintBtnClick(Button.ClickEvent event) {
//        Map<String, Object> input = new HashMap<>();
//
//
//        loadTemplate(input);
//        printCuba.callFunction("printNow");
//    }

//    private void loadTemplate(Map<String, Object> input) {
//        try {
//            if (htmlBoxReport != null) {
//                vboxHtmlLayout.remove(htmlBoxReport);
//            }
//
//            htmlBoxReport = uiComponents.create(HtmlBoxLayout.NAME);
//
//            Version version = new Version("2.3.0");
//            Configuration cfg = new Configuration(version);
//
//            // Where do we load the templates from:
//            cfg.setDirectoryForTemplateLoading(new File(PATH_TEMPLATE));
//
//            // Some other recommended settings:
//            cfg.setIncompatibleImprovements(new Version(2, 3, 20));
//            cfg.setDefaultEncoding("UTF-8");
//            cfg.setLocale(Locale.US);
//            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//
//            Template template = cfg.getTemplate("phieuin.html");
//
//            StringWriter sw = new StringWriter();
//            template.process(input, sw);
//
//
//            htmlBoxReport.setWidth("100%");
//            htmlBoxReport.setHeight("100%");
//            htmlBoxReport.setTemplateContents(sw.toString());
//
//            vboxHtmlLayout.add(htmlBoxReport);
//        } catch (Exception ex) {
//        }
//    }
}