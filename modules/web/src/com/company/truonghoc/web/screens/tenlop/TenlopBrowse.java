package com.company.truonghoc.web.screens.tenlop;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Tenlop;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Tenlop.browse")
@UiDescriptor("tenlop-browse.xml")
@LookupComponent("tenlopsTable")
@LoadDataBeforeShow
public class TenlopBrowse extends StandardLookup<Tenlop> {
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected LookupField searchDvField;
    @Inject
    protected LookupField<Giaovien> searchGvcnField;
    @Inject
    protected LookupField<Tenlop> searchLopField;
    @Inject
    protected CollectionLoader<Tenlop> tenlopsDl;
    @Inject
    protected Notifications notifications;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected CollectionContainer<Tenlop> tenlopsDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected GroupTable<Tenlop> tenlopsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        // load dữ liệu đơn vị
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        searchDvField.setOptionsList(sessionTypeNames);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        // điều kiện
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            searchDvField.setEditable(false);
            searchDvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
            excuteSearch(true);
        }
    }


    /***Tìm kiếm***/
    public void timkiemExcute() {
        try {
            excuteSearch(true);
        } catch (NullPointerException ex) {
            notifications.create()
                    .withCaption("Bạn chưa nhập thông tin cần tìm kiếm")
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }

    }

    private void excuteSearch(boolean isFromSearchBtn) {

        String donvi = searchDvField.getValue().toString();
        Object tenlop = searchLopField.getValue();
        Object tengv = searchGvcnField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);

        tenlopsDl.setQuery(query);
        tenlopsDl.setParameters(params);
        tenlopsDl.load();
    }

    private String returnQuery(String donvi, Object tenlop, Object tengv, Map<String, Object> params) {

        String query = "select e from truonghoc_Tenlop e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.dovi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //Tên lớp
        if (tenlop != null) {
            where += "and e.tenlop = :tenlop ";
            params.put("tenlop", searchLopField.getValue().getTenlop());
        }
        //Tên giáo viên
        if (tengv != null) {
            where += "and e.giaoviencn.tengiaovien like :giaovien ";
            params.put("giaovien", searchGvcnField.getValue().getTengiaovien());
        }

        query = query + where;
        return query;
    }

    /*** Số thứ tự***/
    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = tenlopsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    /*** Tình trạng lớp học ***/
    public Component tinhtranglop(Tenlop entity) {
        Label label = uiComponents.create(Label.class);
        if (entity.getTinhtranglop() != null) {
            if (entity.getTinhtranglop() == true) {
                label.setValue("Mở");
            } else {
                label.setValue("Đóng");
            }
        } else {
            label.setValue("Đóng");
        }
        return label;
    }

    @Subscribe("searchDvField")
    protected void onSearchDvFieldValueChange(HasValue.ValueChangeEvent event) {
        if (searchDvField.getValue() != null) {
            searchGvcnField.setOptionsList(searchedService.loadgiaovien(searchDvField.getValue()));
        } else {
            searchGvcnField.clear();
        }
    }

    @Subscribe("searchGvcnField")
    protected void onSearchGvcnFieldValueChange(HasValue.ValueChangeEvent event) {
        if (searchGvcnField.getValue() != null) {
            searchLopField.setOptionsList(loadlop(searchDvField.getValue(), searchGvcnField.getValue().getTengiaovien()));
        } else {
            searchLopField.clear();
        }
    }

    private List<Tenlop> loadlop(Object donvi, Object giaovien) {
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi.tendonvi = :donvi and e.giaoviencn.tengiaovien = :giaovien")
                .parameter("donvi", donvi)
                .parameter("giaovien", giaovien)
                .list();
    }

    /*** Xuất file excel***/
    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng đã chọn không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Hàng đã chọn").withHandler(e -> {
                            xuatExcel(tenlopsDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachTenlop());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Tenlop> layDanhSachTenlop) {

        Table table = tenlopsTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Tenlop e : layDanhSachTenlop) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("dovi", e.getValue("dovi"));
            row.setValue("tenlop", e.getValue("tenlop"));
            row.setValue("giaoviencn", e.getValue("giaoviencn"));
            row.setValue("thanghoc", e.getValue("thanghoc"));
            row.setValue("namhoc", e.getValue("namhoc"));

            if (e.getTinhtranglop() == null) {
                row.setValue("tinhtranglopss", "");
            } else {
                row.setValue("tinhtranglopss", e.getTinhtranglop());
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách tên lớp");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách tên lớp");
    }

}