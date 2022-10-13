package com.company.truonghoc.web.screens.giaovien;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.ExcelAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Giaovien;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("truonghoc_Giaovien.browse")
@UiDescriptor("giaovien-browse.xml")
@LookupComponent("giaoviensTable")
@LoadDataBeforeShow
public class GiaovienBrowse extends StandardLookup<Giaovien> {
    @Inject
    protected UserSession userSession;
    @Inject
    protected CollectionLoader<Giaovien> giaoviensDl;
    @Inject
    protected LookupField donvitao_giaovienField;
    @Inject
    protected GroupTable<Giaovien> giaoviensTable;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected TextField<String> searchTenGvField;
    @Inject
    protected DataManager dataManager;
    @Named("giaoviensTable.excel")
    protected ExcelAction giaoviensTableExcel;
    @Inject
    protected Button excelBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected CollectionContainer<Giaovien> giaoviensDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Metadata metadata;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        try {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                donvitao_giaovienField.setEditable(false);
                donvitao_giaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
                excuteSearch(true);
            } else {
                donvitao_giaovienField.setEditable(true);
                donvisDl.load();
                List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                        .map(Donvi::getTendonvi)
                        .collect(Collectors.toList());
                donvitao_giaovienField.setOptionsList(sessionTypeNames);
            }
        } catch (NullPointerException ex) {

        }
    }

    @Subscribe("donvitao_giaovienField")
    protected void onDonvitao_giaovienFieldValueChange(HasValue.ValueChangeEvent event) {
        System.out.println(donvitao_giaovienField.getValue());
        if (donvitao_giaovienField.getValue() == null){
            searchTenGvField.clear();
        }
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = giaoviensDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    public void timkiem() {
        try {
            excuteSearch(true);
        } catch (NullPointerException ex) {
        }
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = donvitao_giaovienField.getValue();
        String giaovien = searchTenGvField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, params);
        giaoviensDl.setQuery(query);
        giaoviensDl.setParameters(params);
        giaoviensDl.load();
    }

    private String returnQuery(Object donvi, String giaovien, Map<String, Object> params) {
        String query = "select e from truonghoc_Giaovien e ";
        String where = " where 1=1 ";

        if (donvi != null) {
            where += "and e.donvitao_giaovien.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        if (!StringUtils.isEmpty(giaovien)) {
            where += "and e.tengiaovien like :tengv ";
            params.put("tengv", "%" + giaovien + "%");
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
                            xuatExcel(giaoviensDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachGiaovien());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Giaovien> layDanhSachGiaovien) {
        Table table = giaoviensTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Giaovien e: layDanhSachGiaovien){
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvitao_giaovien", e.getValue("donvitao_giaovien"));
            row.setValue("tengiaovien", e.getValue("tengiaovien"));
            row.setValue("ngaysinhgiaovien", e.getValue("ngaysinhgiaovien"));
            row.setValue("quequangiaovien", e.getValue("quequangiaovien"));
            row.setValue("gioitinhgiaovien", e.getValue("gioitinhgiaovien"));
            row.setValue("ghichu", e.getValue("ghichu"));

            collection.add(row);
            count ++;
        }

        List<Table.Column> tableColumns = table.getColumns();
        int i = 0;
        for (Table.Column column : tableColumns) {
            columns.put(column.getIdString(), column.getCaption());
            properties.put(i, column.getIdString());
            i++;
        }

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách giáo viên");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties,exportDisplay,"Danh sách giáo viên");
    }
}