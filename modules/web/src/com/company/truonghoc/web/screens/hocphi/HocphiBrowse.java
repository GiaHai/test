package com.company.truonghoc.web.screens.hocphi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Thutienhocphi;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.ServerConfigService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.utils.GlobalFunctionHelper;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.company.truonghoc.web.screens.utils.WebFunctionHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocphi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.gui.components.JavaScriptComponent;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@UiController("truonghoc_Hocphi.browse")
@UiDescriptor("hocphi-browse.xml")
@LookupComponent("hocphisTable")
//@LoadDataBeforeShow
public class HocphiBrowse extends StandardLookup<Hocphi> {
    @Inject
    protected CollectionLoader<Hocphi> hocphisDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField<Donvi> donViField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected LookupField trangthaiField;
    @Inject
    protected TextField<String> hovstenField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected CollectionContainer<Hocphi> hocphisDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Table<Hocphi> hocphisTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    @Inject
    protected Notifications notifications;
    @Inject
    protected ServerConfigService serverConfigService;
    private Donvi donViSession = null;
    @Inject
    protected JavaScriptComponent printerJsLb;
    @Inject
    protected JavaScriptComponent printerPdf;
    private String pathPdf;
    private String webBaseFolder;

    @Subscribe
    protected void onInit(InitEvent event) {
        pathPdf = AppContext.getProperty("knkx.temp.printer");
        webBaseFolder = AppContext.getProperty("knkx.printer.web.base.folder");

        //t??nh tr???ng thanh to??n
        List<String> list = Arrays.asList("???? thanh to??n", "Ch??a thanh to??n");
        trangthaiField.setOptionsList(list);
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //load ph??n quy???n
        dkphanquyen();
        //load t??m ki???m
        excuteSearch(true);
    }


    /*** T??m ki???m***/
    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = hocphisDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            denngayField.clear();
            tungayField.clear();
            trangthaiField.clear();
            hovstenField.clear();
        } else {
            denngayField.clear();
            tungayField.clear();
            trangthaiField.clear();
            hovstenField.clear();
            donViField.clear();
        }
        excuteSearch(true);
    }


    //??i???u ki???n login
    private void dkphanquyen() {
        //??i???u ki???n ????n v??? trung t??m n???u
        if (!donViSession.getDonvitrungtam()) {
            donViField.setEditable(false);
            donViField.setValue(donViSession); //Ch??n ????n v??? t??? user v??o text
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                donViField.setEditable(false);
                denngayField.clear();
                tungayField.clear();
                trangthaiField.clear();
                hovstenField.clear();
                donViField.setValue(donViSession); //Ch??n ????n v??? t??? user v??o text
            }

        } else {
            donViField.setEditable(true);
            //l???y d??? li???u string cho lookup
            donViField.setOptionsList(searchedService.loaddonvi());
        }
    }

    /*** Check h???n ????ng***/
    public Component checkhandong(Hocphi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaydong() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">???? ????NG</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHandong();
            if (now.compareTo(han) >= 0) {
                String body = "<a style=\"background-color: black; color: red ; width: 100%;\">QU?? H???N</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            } else {
                String body = "<a style=\"color: red; width: 100%;\">?????N H???N</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            }
            return htmlBoxLayout1;
        }
        return htmlBoxLayout;
    }

    /*** T??m ki???m ***/
    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = donViField.getValue();
        String hocsinh = hovstenField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, hocsinh, trangthai, tungay, denngay, params);
        hocphisDl.setQuery(query);
        hocphisDl.setParameters(params);
        hocphisDl.load();
    }

    private String returnQuery(Object donvi, String hocsinh, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Hocphi e ";
        String where = " where 1=1 ";


        //H???c sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.hovaten.tenhocsinh like :hocsinh ";
            params.put("hocsinh", "%" + hocsinh + "%");
        }

        //Tr???ng th??i
        if (trangthai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangthai);
        }

        //????n v???
        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }
        //T??? ng??y
        if (tungay != null) {
            where += "and e.ngaydong >= :tungay ";
            params.put("tungay", tungay);
        }
        //?????n ng??y
        if (denngay != null) {
            where += "and :denngay >= e.ngaydong ";
            params.put("denngay", denngay);
        }
        String orderBy = " order by e.ngaydong desc";

        query = query + where + orderBy;
        return query;
    }

    /*** Xu???t file excel***/
    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("X??c nh???n")
                .withMessage("B???n c?? mu???n xu???t c??c h??ng kh??ng?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("T???t c??? c??c h??ng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachHocphi(donViField.getValue(), hovstenField.getValue(), trangthaiField.getValue(), tungayField.getValue(), denngayField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("H???y")
                )
                .show();
    }

    private void xuatExcel(List<Hocphi> layDanhSachHocphi) {

        Table table = hocphisTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Hocphi e : layDanhSachHocphi) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvi", e.getValue("donvi"));
            row.setValue("hovaten", e.getValue("hovaten"));
            row.setValue("ghichu", e.getValue("ghichu"));
            row.setValue("sotientamtinh", e.getValue("sotientamtinh"));
            row.setValue("sotienthutheohd", e.getValue("sotienthutheohd"));

            if (e.getValue("handong") != null){
                row.setValue("handong", simpleDateFormat.format(e.getValue("handong")));
            }
            if (e.getValue("ngaydong") !=null){
                row.setValue("ngaydong", simpleDateFormat.format(e.getNgaydong()));
            }
            row.setValue("hinhthucthanhtoan", e.getValue("hinhthucthanhtoan"));
//            row.setValue("ngaydong", e.getValue("ngaydong"));

            if (e.getNgaydong() != null) {
                row.setValue("checkhandong", "???? ????ng");
            } else {
                Date now = new Date();
                Date han = e.getHandong();
                if (now.compareTo(han) >= 0) {
                    row.setValue("checkhandong", "Qu?? h???n");
                } else {
                    row.setValue("checkhandong", "?????n h???n");
                }
            }
            collection.add(row);
            count++;
        }
        List<Table.Column> tableColumns = table.getColumns();
        int i = 0;
        for (Table.Column column : tableColumns) {
            columns.put(column.getIdString(), column.getCaption());
            properties.put(i, column.getIdString());
            i++;
        }
        String tuNgay;
        String denNgay;
        if (tungayField.getValue() == null) {
            tuNgay = "";
        } else {
            tuNgay = simpleDateFormat.format(tungayField.getValue());
        }
        if (denngayField.getValue() == null) {
            denNgay = "";
        } else {
            denNgay = simpleDateFormat.format(denngayField.getValue());
        }
        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh s??ch h???c ph??");
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh s??ch h???c ph??" +
                " Kho???ng th???i gian t??? " + tuNgay + " ?????n " + denNgay + "");
    }

    @Subscribe("printBtn")
    protected void onPrintBtnClick(Button.ClickEvent event) {
        Calendar calendar = Calendar.getInstance();
        DecimalFormat currency = new DecimalFormat ("##,###,###.##");

        Hocphi hocphi = hocphisTable.getSingleSelected();
        Map<String, Object> parameters = new HashMap<>();
        Date ngaydong = hocphi.getNgaydong();
        calendar.setTime(ngaydong);
        parameters.put("donvithanhtoan", donViSession.getTendonvi());
        parameters.put("tenhocsinh", hocphi.getHovaten().getTenhocsinh());
        parameters.put("thanhtien", currency.format(hocphi.getSotienthutheohd()));
        parameters.put("hinhthucthanhtoan", hocphi.getHinhthucthanhtoan());
        parameters.put("ngay", calendar.get(Calendar.DATE));
        parameters.put("thang", calendar.get(Calendar.MONTH) + 1 );
        parameters.put("nam", calendar.get(Calendar.YEAR));
        parameters.put("sodienthoaicoso", donViSession.getSotienthoai());
        parameters.put("nguoitao", donViSession.getTenquanly());
        parameters.put("ghichu", hocphi.getGhichu());

        String path = AppContext.getProperty("knkx.template");

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


    }




}