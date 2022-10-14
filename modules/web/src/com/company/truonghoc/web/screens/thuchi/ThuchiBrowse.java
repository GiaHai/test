package com.company.truonghoc.web.screens.thuchi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Thuchi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.ui.components.grid.FooterRow;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Thuchi.browse")
@UiDescriptor("thuchi-browse.xml")
@LookupComponent("thuchisTable")
@LoadDataBeforeShow
public class ThuchiBrowse extends StandardLookup<Thuchi> {
    @Inject
    protected CollectionLoader<Thuchi> thuchisDl;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected GroupTable<Thuchi> thuchisTable;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected TextField<String> khoanchiField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected CollectionContainer<Thuchi> thuchisDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    private DecimalFormat percentFormat;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField donvitao_thuchiField;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected LookupField trangthaiField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Đã thanh toán", "Chưa thanh toán");
        trangthaiField.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        excuteSearch(true);
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = thuchisDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donvitao_thuchiField.setEditable(false);
            donvitao_thuchiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
            //Xoá
            khoanchiField.clear();
            trangthaiField.clear();
            tungayField.clear();
            denngayField.clear();

            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                donvitao_thuchiField.setEditable(false);
                khoanchiField.clear();
                trangthaiField.clear();
                tungayField.clear();
                denngayField.clear();
                donvitao_thuchiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
            }
        } else {
            donvitao_thuchiField.setEditable(true);
            //lấy dữ liệu string cho lookup
            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            donvitao_thuchiField.setOptionsList(sessionTypeNames);
            //xoá
            donvitao_thuchiField.clear();
            khoanchiField.clear();
            trangthaiField.clear();
            tungayField.clear();
            denngayField.clear();
        }
    }

    public Component checkhanchi(Thuchi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaychi() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ THANH TOÁN</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHanchi();
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

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = donvitao_thuchiField.getValue();
        String khoanchi = khoanchiField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, khoanchi, trangthai, tungay, denngay, params);
        thuchisDl.setQuery(query);
        thuchisDl.setParameters(params);
        thuchisDl.load();
    }

    private String returnQuery(Object donvi, String khoanchi, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Thuchi e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_thuchi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //khoản chi
        if (!StringUtils.isEmpty(khoanchi)) {
            where += "and e.khoanchi = :khoanchi ";
            params.put("khoanchi", khoanchi);
        }
        //trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangchi = :trangthai ";
            params.put("trangthai", trangthai);
        }
        //từ ngày
        if (tungay != null) {
            where += "and e.ngaychi >= :tungay ";
            params.put("tungay", tungay);
        }
        //đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.ngaychi ";
            params.put("denngay", denngay);
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
                            xuatExcel(thuchisDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachThuchi());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Thuchi> layDanhSachThuchi) {

        Table table = thuchisTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Thuchi e : layDanhSachThuchi) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvitao_thuchi", e.getValue("donvitao_thuchi"));
            row.setValue("ngaychi", e.getValue("ngaychi"));
            row.setValue("khoanchi", e.getValue("khoanchi"));
            row.setValue("soluong", e.getValue("soluong"));
            row.setValue("thanhtien", e.getValue("thanhtien"));
            row.setValue("ghichu", e.getValue("ghichu"));
            if (e.getNgaychi() != null) {
                row.setValue("checkhandong", "Đã thanh toán");
            } else {
                Date now = new Date();
                Date han = e.getHanchi();
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách chi");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách chi");
    }
}