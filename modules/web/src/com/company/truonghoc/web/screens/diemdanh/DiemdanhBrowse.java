package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Tenlop;
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
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Diemdanh;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Diemdanh.browse")
@UiDescriptor("diemdanh-browse.xml")
@LookupComponent("diemdanhsTable")
@LoadDataBeforeShow
public class DiemdanhBrowse extends StandardLookup<Diemdanh> {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Donvi> tendonviField;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionLoader<Diemdanh> diemdanhsDl;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected LookupField<Tenlop> lopField;
    @Inject
    protected LookupField<Giaovien> tengiaovienField;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected CollectionContainer<Diemdanh> diemdanhsDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Table<Diemdanh> diemdanhsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    private Donvi donViSession = null;
    private Giaovien giaoVienSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        //Đơn vị
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
        //Giáo viên
        giaoVienSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            lopField.clear();
        } else {
            //Xoá
            tendonviField.clear();
            tengiaovienField.clear();
            ngaylamField.clear();
            lopField.clear();
        }
        excuteSearch(true);
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (!donViSession.getDonvitrungtam()) {
            tendonviField.setEditable(false);
            tendonviField.setValue(donViSession); //Chèn đơn vị từ user vào text


            if (giaoVienSession != null) {
                tendonviField.setEditable(false);
                tengiaovienField.setEditable(false);
                ngaylamField.clear();
                tendonviField.setValue(donViSession); //Chèn đơn vị từ user vào text
                tengiaovienField.setValue(giaoVienSession);  //chèn tên giáo viên từ user vào text
            }
        } else {
            donvisDl.load();
            tendonviField.setOptionsList(searchedService.loaddonvi());


        }
    }

    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = tendonviField.getValue();
        Object giaovien = tengiaovienField.getValue();
        Date ngaylam = ngaylamField.getValue();
        Object lop = lopField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, ngaylam, lop, params);

        diemdanhsDl.setQuery(query);
        diemdanhsDl.setParameters(params);
        diemdanhsDl.load();
    }

    private String returnQuery(Object donvi, Object giaovien, Date ngaylam, Object lop, Map<String, Object> params) {
        String query = "select e from truonghoc_Diemdanh e ";
        String where = " where 1=1 ";


        //Tên đơn vị
        if (donvi != null) {
            where += "and e.donvidd = :donvi ";
            params.put("donvi", donvi);
        }
        //giáo viên
        if (giaovien != null) {
            where += "and e.giaoviendd = :giaovien ";
            params.put("giaovien", giaovien);
        }
        //Ngày làm
        if (ngaylam != null) {
            where += "and e.ngaynghi = :ngaylam ";
            params.put("ngaylam", ngaylam);
        }
        //Lớp
        if (giaoVienSession != null) {
            if (lop == null) {
                where += "and e.lopdd.tinhtranglop = true ";
            } else {
                if (lop != null) {
                    where += "and e.lopdd = :lop ";
                    params.put("lop", lop);
                }
            }
        } else {
            if (lop != null) {
                where += "and e.lopdd = :lop ";
                params.put("lop", lop);
            }
        }
        query = query + where;
        return query;
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        tengiaovienField.setOptionsList(searchedService.loadgiaovien(tendonviField.getValue()));
    }

    @Subscribe("tengiaovienField")
    protected void onTengiaovienFieldValueChange(HasValue.ValueChangeEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            lopField.setOptionsList(searchedService.loadlopDK(tendonviField.getValue(), tengiaovienField.getValue()));
        } else {
            lopField.setOptionsList(searchedService.loadlop(tendonviField.getValue(), tengiaovienField.getValue()));
        }
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = diemdanhsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    /********* XUẤT FILE EXCEL ********/

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn xuất các hàng không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachDiemdanh(tendonviField.getValue(), tengiaovienField.getValue(), ngaylamField.getValue(), lopField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Diemdanh> layDanhSachDiemdanh) {

        Table table = diemdanhsTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Diemdanh e : layDanhSachDiemdanh) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvidd", e.getValue("donvidd"));
            row.setValue("giaoviendd", e.getValue("giaoviendd"));
            row.setValue("hotenhs.dshocsinh.tenhocsinh", e.getValue("hotenhs"));
            row.setValue("lopdd", e.getValue("lopdd"));
            row.setValue("ngaynghi", e.getValue("ngaynghi"));
            row.setValue("ngayHocbu", e.getValue("ngayHocbu"));

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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách điểm danh");
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách điểm danh");
    }
}