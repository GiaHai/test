package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Chamconggv.browse")
@UiDescriptor("chamconggv-browse.xml")
@LookupComponent("chamconggvsTable")
@LoadDataBeforeShow
public class ChamconggvBrowse extends StandardLookup<Chamconggv> {
    @Inject
    protected CollectionContainer<Chamconggv> chamconggvsDc;
    @Inject
    protected CollectionLoader<Chamconggv> chamconggvsDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected LookupField tendonviField;
    @Inject
    protected LookupField tengiaovienField;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField buoilamField;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected GroupTable<Chamconggv> chamconggvsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm
        // lấy dữ liệu buổi làm
        List<String> list = Arrays.asList("Làm cả ngày", "Ca sáng", "Ca chiều");
        buoilamField.setOptionsList(list);
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            tendonviField.setEditable(false);
            tendonviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
//            tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien().getTengiaovien());
            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            buoilamField.clear();
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                tendonviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
                tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                tengiaovienField.setEditable(false);
            }
        } else {
            tendonviField.setEditable(true);
            //lấy dữ liệu string cho lookup
            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            tendonviField.setOptionsList(sessionTypeNames);

            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            buoilamField.clear();
            tendonviField.clear();
        }
    }

    private List<Giaovien> tengiaovien(Object dvgiaovien) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien.tendonvi = :dvgiaovien")
                .parameter("dvgiaovien", dvgiaovien)
                .list();
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() == null) {
            try {
                tengiaovienField.setOptionsList(tengiaovien(tendonviField.getValue()));
            } catch (NullPointerException ex) {
            }
        }
    }


    public Component stt(Chamconggv entity) {
        int lineNumber = 1;
        try {
            lineNumber = chamconggvsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }


    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = tendonviField.getValue();
        Object giaovien = tengiaovienField.getValue();
        Date ngaylam = ngaylamField.getValue();
        Object buoilam = buoilamField.getValue();

        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, giaovien, ngaylam, buoilam, params);

        chamconggvsDl.setQuery(query);
        chamconggvsDl.setParameters(params);
        chamconggvsDl.load();
    }

    private String returnQuery(Object donvi, Object giaovien, Date ngaylam, Object buoilam, Map<String, Object> params) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = " where 1=1 ";

        //đơn vị
        if (donvi != null) {
            where += "and e.donvigv.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //giáo viên
        if (giaovien != null) {
            where += "and e.hotengv = :giaovien ";
            params.put("giaovien", giaovien);
        }
        //ngày làm
        if (ngaylam != null) {
            where += "and e.ngaylam = :ngaylam ";
            params.put("ngaylam", ngaylam);
        }
        //ngày làm
        if (buoilam != null) {
            where += "and e.buoilam = :buoilam ";
            params.put("buoilam", buoilam);
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
                            xuatExcel(chamconggvsDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachChamconggv());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Chamconggv> layDanhSachChamconggv) {

        Table table = chamconggvsTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Chamconggv e : layDanhSachChamconggv) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvigv", e.getValue("donvigv"));
            row.setValue("hotengv", e.getValue("hotengv"));
            row.setValue("ngaylam", e.getValue("ngaylam"));
            row.setValue("buoilam", e.getValue("buoilam"));
            row.setValue("tienBuoi", e.getValue("tienBuoi"));

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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách chấm công giáo viên");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách chấm công giáo viên");
    }


}