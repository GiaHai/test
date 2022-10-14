package com.company.truonghoc.web.screens.thutienhocphi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Thutienhocphi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Thutienhocphi.browse")
@UiDescriptor("thutienhocphi-browse.xml")
@LookupComponent("thutienhocphisTable")
@LoadDataBeforeShow
public class ThutienhocphiBrowse extends StandardLookup<Thutienhocphi> {
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected LookupField donvitao_thutienhocphiField;
    @Inject
    protected LookupField trangthaiField;
    @Inject
    protected TextField<String> tenHsField;
    @Inject
    protected TextField<String> tenKhField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected CollectionLoader<Thutienhocphi> thutienhocphisDl;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected CollectionContainer<Thutienhocphi> thutienhocphisDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected GroupTable<Thutienhocphi> thutienhocphisTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Đã thanh toán", "Chưa thanh toán");
        trangthaiField.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donvitao_thutienhocphiField.setEditable(false);
            donvitao_thutienhocphiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
            //Xoá
            tenHsField.clear();
            tenKhField.clear();
            tungayField.clear();
            denngayField.clear();
            trangthaiField.clear();

            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                donvitao_thutienhocphiField.setEditable(false);
                tenHsField.clear();
                tenKhField.clear();
                tungayField.clear();
                denngayField.clear();
                trangthaiField.clear();
                donvitao_thutienhocphiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
            }
        } else {
            donvitao_thutienhocphiField.setEditable(true);

            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            donvitao_thutienhocphiField.setOptionsList(sessionTypeNames);

            //Xoá
            donvitao_thutienhocphiField.clear();
            tenHsField.clear();
            tenKhField.clear();
            tungayField.clear();
            denngayField.clear();
            trangthaiField.clear();
        }
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = thutienhocphisDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    public Component checkhan(Thutienhocphi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getHinhthucthanhtoan() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ THANH TOÁN</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getDenngay();
            if (now.compareTo(han) >= 0) {
                String body = "<a style=\"background-color: black; color: red ; width: 100%;\">QUÁ HẠN</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            } else {
                String body = "<a style=\"color: red; width: 100%;\">ĐẾN HẠN</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            }
            return htmlBoxLayout1;
        }
        return htmlBoxLayout;
    }


    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean b) {
        Object donvi = donvitao_thutienhocphiField.getValue();
        String Kh = tenKhField.getValue();
        String tenHs = tenHsField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Object trangthai = trangthaiField.getValue();

        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, Kh, tenHs, tungay, denngay, trangthai, params);
        thutienhocphisDl.setQuery(query);
        thutienhocphisDl.setParameters(params);
        thutienhocphisDl.load();
    }

    private String returnQuery(Object donvi, String kh, String tenHs, Date tungay, Date denngay, Object trangthai, Map<String, Object> params) {
        String query = "select e from truonghoc_Thutienhocphi e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_thutienhocphi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }

        //Khách hàng
        if (!StringUtils.isEmpty(kh)) {
            where += "and e.tenkhachhang = :kh ";
            params.put("kh", kh);
        }
        //học sinh
        if (!StringUtils.isEmpty(tenHs)) {
            where += "and e.tenhocsinh.tenhocsinh = :tenHs ";
            params.put("tenHs", tenHs);
        }
        //từ ngày
        if (tungay != null) {
            where += "and e.tungay >= :tungay ";
            params.put("tungay", tungay);
        }
        //đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.denngay ";
            params.put("denngay", denngay);
        }
        //trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangthai);
        }
        query = query + where;
        return query;
    }

    /********* XUẤT FILE EXCEL ********/

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng đã chọn không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Hàng đã chọn").withHandler(e -> {
                            xuatExcel(thutienhocphisDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachThutienHocphi());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Thutienhocphi> layDanhSachThutienHocphi) {
        Table table = thutienhocphisTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Thutienhocphi e : layDanhSachThutienHocphi) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvitao_thutienhocphi", e.getValue("donvitao_thutienhocphi"));
            row.setValue("tenkhachhang", e.getValue("tenkhachhang"));
            row.setValue("diachi", e.getValue("diachi"));
            row.setValue("tungay", e.getValue("tungay"));
            row.setValue("denngay", e.getValue("denngay"));
            row.setValue("tenhocsinh", e.getValue("tenhocsinh"));
            row.setValue("thanhtien", e.getValue("thanhtien"));
            if (e.getHinhthucthanhtoan() != null) {
                row.setValue("checkhandong", "Đã đóng");
            } else {
                Date now = new Date();
                Date han = e.getDenngay();
                if (now.compareTo(han) >= 0) {
                    row.setValue("checkhandong", "Quá hạn");
                } else {
                    row.setValue("checkhandong", "Đến hạn");
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách thu tiền học phí");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách thu tiền học phí");
    }


}