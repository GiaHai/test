package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.enums.BuoiLamEnum;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

@UiController("truonghoc_Chamconggv.browse")
@UiDescriptor("chamconggv-browse.xml")
@LookupComponent("chamconggvsTable")
//@LoadDataBeforeShow
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
    protected LookupField<Donvi> tendonviField;
    @Inject
    protected LookupField<Giaovien> tengiaovienField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Integer> buoilamField;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Table<Chamconggv> chamconggvsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    @Inject
    protected SearchedService searchedService;
    private Donvi donViSession = null;
    private Giaovien giaoVienSession = null;
    @Inject
    private HBoxLayout infoMain;
    Date denNgay = new Date();
    Integer caNgay;
    Integer caSang;
    Integer caChieu;
    Integer caChuNhat;
    Integer caNgoai1;
    Integer caNgoai2;
    Date tuNgay;
    @Inject
    private Label<Double> tongNgayCong;
    @Inject
    private Label<Double> tongSoBuoiLamChinh;
    @Inject
    private Label<Double> tongTienLamThemNgoaiGio;
    @Inject
    private Label<Integer> tongBuoiLamNgoaiGio;
    @Inject
    private ButtonsPanel buttonsPanel;

    @Subscribe
    protected void onInit(InitEvent event) {
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
        giaoVienSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();

        excuteSearch(true);
        if (giaoVienSession == null) {
            infoMain.setVisible(false);
        } else {
            tinhBuoiLam();
        }
    }

    private void tinhBuoiLam() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        simpleDateFormat.format(cal.getTime());
        tuNgay = cal.getTime();

        caNgay = searchedService.tinhca(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.LAM_CA_NGAY.getId()).size();
        caSang = searchedService.tinhca(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_SANG.getId()).size();
        caChieu = searchedService.tinhca(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_CHIEU.getId()).size();
        caChuNhat = searchedService.tinhca(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_CHU_NHAT.getId()).size();
        caNgoai1 = searchedService.tinhca(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_CHIEU_5H_6H.getId()).size();
        caNgoai2 = searchedService.tinhca(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_CHIEU_6H_7H.getId()).size();

        tongSoBuoiLamChinh.setValue(caNgay + caSang * 0.5 + caChieu * 0.5);
        tongBuoiLamNgoaiGio.setValue(caNgoai1 + caNgoai2);
        tongNgayCong.setValue(caNgay + caSang * 0.5 + caChieu * 0.5 + caNgoai1 * 0.5 + caNgoai2 * 0.5);

        List<KeyValueEntity> caChieuDaythem1 = searchedService.caChieudaythem(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_CHIEU_5H_6H.getId());
        List<KeyValueEntity> caChieuDaythem2 = searchedService.caChieudaythem(donViSession, giaoVienSession, tuNgay, denNgay, BuoiLamEnum.CA_CHIEU_6H_7H.getId());
        Object cachieu2 = 0;
        Object cachieu1 = 0;
        for (KeyValueEntity item : caChieuDaythem2) {
            cachieu2 = item.getValue("tienBuoi");
        }
        for (KeyValueEntity item : caChieuDaythem1) {
            cachieu1 = item.getValue("tienBuoi");
        }
        if (cachieu1 == null) {
            cachieu1 = 0;
        }
        if (cachieu2 == null) {
            cachieu2 = 0;
        }
        tongTienLamThemNgoaiGio.setValue((double) (cachieu1.hashCode() + cachieu2.hashCode()));
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
        excuteSearch(true);
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm
        // lấy dữ liệu buổi làm
        Map<String, Integer> buoiLam = new LinkedHashMap<>();
        buoiLam.put("Làm cả ngày", BuoiLamEnum.LAM_CA_NGAY.getId());
        buoiLam.put("Ca sáng", BuoiLamEnum.CA_SANG.getId());
        buoiLam.put("Ca chiều", BuoiLamEnum.CA_CHIEU.getId());
        buoiLam.put("Ca chủ nhật", BuoiLamEnum.CA_CHU_NHAT.getId());
        buoiLam.put("Ca chiều 5h-6h", BuoiLamEnum.CA_CHIEU_5H_6H.getId());
        buoiLam.put("Ca chiều 6h-7h", BuoiLamEnum.CA_CHIEU_6H_7H.getId());
        buoilamField.setOptionsMap(buoiLam);

        if (!donViSession.getDonvitrungtam()) {
            tendonviField.setEditable(false);
            tendonviField.setValue(donViSession); //Chèn đơn vị từ user vào text
            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            buoilamField.clear();
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                tendonviField.setValue(donViSession);
                tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                tengiaovienField.setEditable(false);
            }
        } else {
            tendonviField.setEditable(true);
            //lấy dữ liệu string cho lookup
            tendonviField.setOptionsList(searchedService.loaddonvi());

            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            buoilamField.clear();
            tendonviField.clear();
        }
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() == null) {
            tengiaovienField.setOptionsList(searchedService.loadgiaovien(tendonviField.getValue()));
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
        Integer buoilam = buoilamField.getValue();

        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, giaovien, ngaylam, buoilam, params);

        chamconggvsDl.setQuery(query);
        chamconggvsDl.setParameters(params);
        chamconggvsDl.load();
    }

    private String returnQuery(Object donvi, Object giaovien, Date ngaylam, Integer
            buoilam, Map<String, Object> params) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = " where 1=1 ";

        //đơn vị
        if (donvi != null) {
            where += "and e.donvigv = :donvi ";
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
        String orderBy = " order by e.ngaylam desc";
        query = query + where + orderBy;
        return query;
    }

    /********* XUẤT FILE EXCEL ********/

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn xuất các hàng không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachChamconggv(tendonviField.getValue(), tengiaovienField.getValue(), ngaylamField.getValue(), buoilamField.getValue()));
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvigv", e.getValue("donvigv"));
            row.setValue("hotengv", e.getValue("hotengv"));
            row.setValue("ngaylam", simpleDateFormat.format(e.getNgaylam()));
            String buoilam = "";
            switch (e.getBuoilam()) {
                case 1:
                    buoilam = "Làm cả ngày";
                    break;
                case 2:
                    buoilam = "Ca Sáng";
                    break;
                case 3:
                    buoilam = "Ca Chiều";
                    break;
                case 4:
                    buoilam = "Ca Chủ nhật";
                    break;
                case 5:
                    buoilam = "Ca Chiều 5h - 6h";
                    break;
                case 6:
                    buoilam = "Ca Chiều 6h - 7h";
                    break;
                default:
                    buoilam = "Chưa chọn ngày làm";
            }
            row.setValue("buoilam", buoilam);
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

    public Component buoiLam(Chamconggv e) {
        Label label = uiComponents.create(Label.class);
        switch (e.getBuoilam()) {
            case 1:
                label.setValue("Làm cả ngày");
                return label;
            case 2:
                label.setValue("Ca Sáng");
                return label;
            case 3:
                label.setValue("Ca Chiều");
                return label;
            case 4:
                label.setValue("Ca Chủ nhật");
                return label;
            case 5:
                label.setValue("Ca Chiều 5h - 6h");
                return label;
            case 6:
                label.setValue("Ca Chiều 6h - 7h");
                return label;
            default:
                label.setValue("Chưa chọn ngày làm");
        }
        return label;
    }
}