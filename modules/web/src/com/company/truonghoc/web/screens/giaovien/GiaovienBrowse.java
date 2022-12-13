package com.company.truonghoc.web.screens.giaovien;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.UserExt;
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
//@LoadDataBeforeShow
public class GiaovienBrowse extends StandardLookup<Giaovien> {
    @Inject
    protected UserSession userSession;
    @Inject
    protected CollectionLoader<Giaovien> giaoviensDl;
    @Inject
    protected LookupField<Donvi> donViField;
    @Inject
    protected Table<Giaovien> giaoviensTable;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected TextField<String> tenGvField;
    @Inject
    protected DataManager dataManager;
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
    protected SearchedService searchedService;
    @Inject
    private ExportDisplay exportDisplay;
    private Donvi donViSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }
    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            donViField.setEditable(false);
            donViField.setValue(donViSession);
        } else {
            donViField.setEditable(true);
        }
        //Tìm đơn vị
        donViField.setOptionsList(searchedService.loaddonvi());
        excuteSearch(true);
    }

    @Subscribe("donViField")
    protected void ondonViFieldValueChange(HasValue.ValueChangeEvent event) {
        if (donViField.getValue() == null) {
            tenGvField.clear();
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

    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        if (!donViSession.getDonvitrungtam()){
            tenGvField.clear();
        }else {
            donViField.clear();
            tenGvField.clear();
        }
    }
    
    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = donViField.getValue();
        String giaovien = tenGvField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, params);
        giaoviensDl.setQuery(query);
        giaoviensDl.setParameters(params);
        giaoviensDl.load();
    }

    private String returnQuery(Object donvi, String giaovien, Map<String, Object> params) {
        String query = "select e from truonghoc_Giaovien e ";
        String where = " where 1=1 ";
        String orderBy = " order by e.donvi";

        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }
        if (!StringUtils.isEmpty(giaovien)) {
            where += "and e.tengiaovien like :tengv ";
            params.put("tengv", "%" + giaovien + "%");
        }

        query = query + where + orderBy;

        return query;
    }

    /********* XUẤT FILE EXCEL ********/

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachGiaovien(donViField.getValue(), tenGvField.getValue()));
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

        for (Giaovien e : layDanhSachGiaovien) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvi", e.getValue("donvi"));
            row.setValue("tengiaovien", e.getValue("tengiaovien"));
            row.setValue("ngaysinhgiaovien", e.getValue("ngaysinhgiaovien"));
            row.setValue("quequangiaovien", e.getValue("quequangiaovien"));
            row.setValue("gioitinhgiaovien", e.getValue("gioitinhgiaovien"));
            row.setValue("luongcoban", e.getValue("luongcoban"));
            row.setValue("ghichu", e.getValue("ghichu"));

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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách giáo viên");
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách giáo viên");
    }
}