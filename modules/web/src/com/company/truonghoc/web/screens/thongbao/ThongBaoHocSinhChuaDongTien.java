package com.company.truonghoc.web.screens.thongbao;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

@UiController("truonghoc_ThongBaoHocSinhChuaDongTien")
@UiDescriptor("thong-bao-hoc-sinh-chua-dong-tien.xml")
public class ThongBaoHocSinhChuaDongTien extends Screen {
    protected Donvi donvi = null;
    @Inject
    private DulieuUserService dulieuUserService;
    @Inject
    private UserSession userSession;
    @Inject
    private SearchedService searchedService;
    @Inject
    private KeyValueCollectionContainer thongBaoHocSinhChuaDongTiensDc;
    @Inject
    private DateField<Date> tungayField;
    @Inject
    private DateField<Date> denngayField;
    @Inject
    private GroupTable thongBaoHsChuaDongTienTable;
    @Inject
    private LookupField<Donvi> donviField;
    @Inject
    private Notifications notifications;
    @Inject
    private Dialogs dialogs;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Button excelBtn;


    @Subscribe
    public void onInit(InitEvent event) {
        donvi = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        donviField.setOptionsList(searchedService.loaddonvi());
        donviField.setResponsive(true);
        if (!donvi.getDonvitrungtam()) {
            donviField.setValue(donvi);
            donviField.setEditable(false);
        }
    }

    @Subscribe("timkiemBtn")
    public void onTimkiemBtnClick(Button.ClickEvent event) {
        if (donviField.getValue() != null) {
            thongBaoHsChuaDongTienTable.setVisible(true);
            List<KeyValueEntity> result = thongBaoHsChuaDongTien();
            thongBaoHocSinhChuaDongTiensDc.setItems(result);
        } else {
            notifications.create()
                    .withCaption("Bạn chưa chọn Đơn vị")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
        }
        excelBtn.setVisible(true);
    }

    @Subscribe("xoaBtn")
    public void onXoaBtnClick(Button.ClickEvent event) {
        if (donvi.getDonvitrungtam()) {
            donviField.clear();
            thongBaoHsChuaDongTienTable.setVisible(false);
        } else {
            List<KeyValueEntity> result = thongBaoHsChuaDongTien();
            thongBaoHocSinhChuaDongTiensDc.setItems(result);
            thongBaoHsChuaDongTienTable.setVisible(true);
        }
        tungayField.clear();
        denngayField.clear();
    }

    private List<KeyValueEntity> thongBaoHsChuaDongTien() {
        List<KeyValueEntity> result = new ArrayList<>();
        List<Hocsinh> hocsinhList = searchedService.getthongBaoHsChuaDongTien(tungayField.getValue(), denngayField.getValue(), donviField.getValue());
        int stt = 1;
        for (Hocsinh hocsinh : hocsinhList) {
            KeyValueEntity entity = new KeyValueEntity();

            entity.setValue("stt", stt);
            entity.setValue("hoVaTen", hocsinh.getTenhocsinh());

            result.add(entity);
            stt++;
        }
        return result;
    }

    @Subscribe("excelBtn")
    public void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(searchedService.getthongBaoHsChuaDongTien(tungayField.getValue(), denngayField.getValue(), donviField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Hocsinh> getthongBaoHsChuaDongTien) {
        Table table = thongBaoHsChuaDongTienTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = thongBaoHocSinhChuaDongTiensDc.getItems();

        List<Table.Column> tableColumns = table.getColumns();
        int i = 0;
        for (Table.Column column : tableColumns) {
            columns.put(column.getIdString(), column.getCaption());
            properties.put(i, column.getIdString());
            i++;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String tuNgay;
        String denNgay;
        String donVi;
        if (donviField.getValue() == null){
            donVi = "";
        }else {
            donVi = donviField.getValue().getTendonvi();
        }
        String title = "Danh sách học sinh chưa đóng tiền của Trung tâm " + donVi + " ";

        if (tungayField.getValue() == null) {
            tuNgay = "";
        } else {
            tuNgay = " Khoảng thời gian từ " + simpleDateFormat.format(tungayField.getValue()) + "";
        }
        if (denngayField.getValue() == null) {
            denNgay = "";
        } else {
            denNgay = " đến " + simpleDateFormat.format(denngayField.getValue()) + "";
        }
        ExtendExcelExporter exporter = new ExtendExcelExporter(title);
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, title + tuNgay + denNgay);
    }

}