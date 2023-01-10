package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.tienich.Namsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UiController("truonghoc_ManHinhDanhSachHsNghi")
@UiDescriptor("man-hinh-danh-sach-hs-nghi.xml")
public class ManHinhDanhSachHsNghi extends Screen {
    @Inject
    private KeyValueCollectionContainer danhSachHSDaNghisDc;
    @Inject
    private SearchedService searchedService;
    @Inject
    private XuatFileExcelService xuatFileExcelService;
    @Inject
    private UserSession userSession;
    @Inject
    private DulieuUserService dulieuUserService;
    private Donvi donViSession = null;
    @Inject
    private LookupField<Donvi> donViField;
    @Inject
    private KeyValueCollectionLoader danhSachHSDaNghisDl;
    @Inject
    private LookupField<String> gioiTinhField;
    @Inject
    private TextField<String> hocSinhField;
    @Inject
    private LookupField<Namsinh> namSinhField;
    private Donvi donvi = null;
    @Inject
    private Dialogs dialogs;
    @Inject
    private GroupTable danhSachHSDaNghisTable;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe
    public void onInit(InitEvent event) {
        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");
        gioiTinhField.setOptionsList(gioitinh);

        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        donViField.setOptionsList(searchedService.loaddonvi());
        namSinhField.setOptionsList(searchedService.loadNamSinh());
        if (!donViSession.getDonvitrungtam()) {
            donViField.setValue(donViSession);
            donViField.setEditable(false);
            donvi = donViSession;
        }
        List<KeyValueEntity> result = danhSachHSDaNghi();
        danhSachHSDaNghisDc.setItems(result);
    }

    private List<KeyValueEntity> danhSachHSDaNghi() {
        List<KeyValueEntity> result = new ArrayList<>();
        List<Hocsinh> hocsinhList = xuatFileExcelService.layDanhSachHocsinh(donvi, null, null, null, true);
        int stt = 1;
        for (Hocsinh hocsinh : hocsinhList) {
            KeyValueEntity entity = new KeyValueEntity();

            entity.setValue("stt", stt);
            entity.setValue("donvi", hocsinh.getDonvi().getTendonvi());
            entity.setValue("tenhocsinh", hocsinh.getTenhocsinh());
            if (hocsinh.getNgaysinhhocsinh() != null){
                entity.setValue("ngaysinhhocsinh", hocsinh.getNgaysinhhocsinh().getNamSinh());
            }else {
                entity.setValue("ngaysinhhocsinh", "");
            }
            entity.setValue("gioitinhhocsinh", hocsinh.getGioitinhhocsinh());
            entity.setValue("quequanhocsinh", hocsinh.getQuequanhocsinh());
            entity.setValue("ghichu", hocsinh.getGhichu());

            result.add(entity);
            stt++;
        }
        return result;
    }

    /*** tìm kiếm ***/
    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String hocsinh = hocSinhField.getValue();
        String gioitinh = gioiTinhField.getValue();
        Object donvi = donViField.getValue();
        Object namsinh = namSinhField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, params, hocsinh, gioitinh, namsinh);

        danhSachHSDaNghisDl.setQuery(query);
        danhSachHSDaNghisDl.setParameters(params);
        danhSachHSDaNghisDl.load();
    }

    private String returnQuery(Object donvi, Map<String, Object> params, String hocsinh, String gioitinh, Object namsinh) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = " where 1=1 ";
        String orderBy = " order by e.donvi";
        // Đơn vị
        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }

        //Học sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.tenhocsinh like :tenhocsinh ";
            params.put("tenhocsinh", "%" + hocsinh + "%");
        }
        if (gioitinh != null) {
            where += "and e.gioitinhhocsinh = :gioitinh ";
            params.put("gioitinh", gioitinh);
        }
        if (namsinh != null) {
            where += "and e.ngaysinhhocsinh = :namsinh ";
            params.put("namsinh", namsinh);
        }
        query = query + where + orderBy;
        return query;
    }

    @Subscribe("xuatExcelBtn")
    public void onXuatExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachHocsinh(donViField.getValue(), hocSinhField.getValue(), gioiTinhField.getValue(), namSinhField.getValue(), true));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Hocsinh> layDanhSachHocsinh) {
        Table table = danhSachHSDaNghisTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = danhSachHSDaNghisDc.getItems();

        List<Table.Column> tableColumns = table.getColumns();
        int i = 0;
        for (Table.Column column : tableColumns) {
            columns.put(column.getIdString(), column.getCaption());
            properties.put(i, column.getIdString());
            i++;
        }
        String donVi;
        if (donViField.getValue() == null) {
            donVi = "";
        } else {
            donVi = donViField.getValue().getTendonvi();
        }

        String title = "Danh sách học sinh nghỉ học " + donVi + "";

        ExtendExcelExporter exporter = new ExtendExcelExporter(title);
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, title);
    }

}