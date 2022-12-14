package com.company.truonghoc.web.screens.thutienhocphi;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.ServerConfigService;
import com.company.truonghoc.utils.*;
import com.company.truonghoc.web.screens.utils.WebFunctionHelper;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.gui.components.JavaScriptComponent;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.Calendar;

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
    protected LookupField<Donvi> dovitao_thutienhocphiField;
    @Inject
    protected TextField<Long> thanhtienField;
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
    protected GroupBoxLayout lkchitietthuBox;
    @Inject
    protected Table<Chitietthu> chitietthusTable;
    @Inject
    protected Button InphieuBtn;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected DateField<Date> ngaythanhtoanField;
    @Inject
    protected ServerConfigService serverConfigService;
    @Inject
    protected TextField<String> tenkhachhangField;
    @Inject
    protected TextField<String> diachiField;
    @Inject
    protected Notifications notifications;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Ti???n m???t", "Chuy???n kho???n");
        hinhthucthanhtoanField.setOptionsList(list);
//        quy???n
        thanhtienField.setEditable(false);
        tinhtrangthanhtoanField.setVisible(false);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            dovitao_thutienhocphiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            dovitao_thutienhocphiField.setEditable(false);
        }
//        quy???n
        Calendar calendar = Calendar.getInstance();
        tungayField.setValue(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        denngayField.setValue(calendar.getTime());

        tinhtrangthanhtoanField.setValue("Ch??a thanh to??n");

        dovitao_thutienhocphiField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe(id = "lkchitietthu", target = Target.DATA_CONTAINER)
    protected void onLkchitietthuItemChange(InstanceContainer.ItemChangeEvent<Chitietthu> event) {
        Map<Object, Object> resluts = chitietthusTable.getAggregationResults();

        Object priceId = chitietthusTable.getColumn("tonggia").getId();
        thanhtienField.setValue((Long) resluts.get(priceId));
    }

    @Subscribe("dovitao_thutienhocphiField")
    protected void onDovitao_thutienhocphiFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        tenhocsinhField.setOptionsList(searchedService.loadHs(dovitao_thutienhocphiField.getValue().getTendonvi()));
    }

    @Subscribe("hinhthucthanhtoanField")
    protected void onHinhthucthanhtoanFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (hinhthucthanhtoanField.getValue() == null) {
            tinhtrangthanhtoanField.setValue("Ch??a thanh to??n");
        } else {
            tinhtrangthanhtoanField.setValue("???? thanh to??n");
            ngaythanhtoanField.setVisible(true);
            tungayField.setVisible(false);
            denngayField.setVisible(false);
            ngaythanhtoanField.setRequired(true);
            tungayField.clear();
            denngayField.clear();
        }
    }

    private void dkIn() {
        if (dovitao_thutienhocphiField.getValue() != null &&
                tenkhachhangField.getValue() != null &&
                diachiField.getValue() != null &&
                ngaythanhtoanField.getValue() != null &&
                tenhocsinhField.getValue() != null &&
                thanhtienField.getValue() != null &&
                hinhthucthanhtoanField.getValue() != null) {
            InphieuBtn.setEnabled(true);
        }
    }

    //    -----------------In phi???u-----------------
    private String pathPdf;

    @Subscribe("InphieuBtn")
    protected void onInphieuBtnClick(Button.ClickEvent event) {
        if (dovitao_thutienhocphiField.getValue() != null &&
                tenkhachhangField.getValue() != null &&
                diachiField.getValue() != null &&
                ngaythanhtoanField.getValue() != null &&
                tenhocsinhField.getValue() != null &&
                thanhtienField.getValue() != null &&
                hinhthucthanhtoanField.getValue() != null) {
            Thutienhocphi thutienhocphi = getEditedEntity();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("donvithanhtoan", thutienhocphi.getDonvi().getTendonvi());
            parameters.put("tenkhachhang", thutienhocphi.getTenkhachhang());
            parameters.put("diachi", thutienhocphi.getDiachi());
            parameters.put("tenhocsinh", thutienhocphi.getTenhocsinh().getTenhocsinh());
            parameters.put("thanhtien", thutienhocphi.getThanhtien());
            parameters.put("hinhthucthanhtoan", thutienhocphi.getHinhthucthanhtoan());
            parameters.put("nguoitao", userSession.getUser().getName());
            String path = AppContext.getProperty("truonghoc.template");

            String fileTemplate = WebFunctionHelper.modifiedTemplate(path + "/phieuinthanhtoanhocphi.docx", serverConfigService, parameters);
            String fileName = WebFunctionHelper.convertDocToPdf(fileTemplate, pathPdf, true);
            if (!StringUtils.isEmpty(fileName)) {
                List<String> filesPrint = new ArrayList<>();
                filesPrint.add(fileName);
                WebFunctionHelper.printFiles(printerPdf, filesPrint, callbackEvent -> {
                    if (callbackEvent.getArguments() != null) {
                        String urlFile = callbackEvent.getArguments().getString(0);
                        if (!org.apache.commons.lang3.StringUtils.isBlank(urlFile) && !StringUtils.isEmpty(webBaseFolder)) {
                            GlobalFunctionHelper.deleteFile(webBaseFolder + "/" + urlFile);
                        }
                    }
                });
            }
        } else {
            notifications.create()
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .withCaption("B???n nh???p thi???u tr?????ng th??ng tin ????? IN")
                    .show();
        }
    }

    private String webBaseFolder;
    @Inject
    private JavaScriptComponent printerPdf;

    @Subscribe
    protected void onAfterShow1(AfterShowEvent event) {
        pathPdf = AppContext.getProperty("knkx.temp.printer");
        webBaseFolder = AppContext.getProperty("knkx.printer.web.base.folder");

    }

    @Inject
    protected InstanceContainer<Thutienhocphi> thutienhocphiDc;

}