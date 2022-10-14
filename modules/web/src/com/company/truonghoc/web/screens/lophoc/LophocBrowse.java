package com.company.truonghoc.web.screens.lophoc;

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
import com.company.truonghoc.entity.Lophoc;
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

@UiController("truonghoc_Lophoc.browse")
@UiDescriptor("lophoc-browse.xml")
@LookupComponent("lophocsTable")
@LoadDataBeforeShow
public class LophocBrowse extends StandardLookup<Lophoc> {
    @Inject
    protected LookupField<Giaovien> searchGvcnField;
    @Inject
    protected LookupField<Tenlop> searchLopField;
    @Inject
    protected CollectionLoader<Lophoc> lophocsDl;
    @Inject
    protected LookupField<Donvi> searchDvField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected CollectionContainer<Lophoc> lophocsDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected GroupTable<Lophoc> lophocsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //load đơn vị
        searchDvField.setOptionsList(searchedService.loaddonvi());
    }


    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        //Điều kiện
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            searchDvField.setEditable(false);
            searchDvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            excuteSearch(true);
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                searchGvcnField.setValue((dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien()));
                searchGvcnField.setEditable(false);
                excuteSearch(true);
            }
        }
    }


    /*** Tìm kiếm **/
    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = searchDvField.getValue();
        Object tengv = searchGvcnField.getValue();
        Object tenlop = searchLopField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);
        lophocsDl.setQuery(query);
        lophocsDl.setParameters(params);
        lophocsDl.load();
    }

    private String returnQuery(Object donvi, Object tenlop, Object tengv, Map<String, Object> params) {
        String query = "select e from truonghoc_Lophoc e ";
        String where = " where 1=1 ";
        //Đơn vị
        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }
        // Tên lớp
        if (tenlop != null) {
            where += "and e.tenlop.tenlop = :tenlop ";
            params.put("tenlop", searchLopField.getValue().getTenlop());
        }
        // Tên giáo viên
        //load nếu lớp học bằng true thì giáo viên mới xem được
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            if (tengv != null) {
                where += "and e.giaoviencn.tengiaovien like :tengv and e.tenlop.tinhtranglop = true ";
                params.put("tengv", searchGvcnField.getValue().getTengiaovien());
            }
        } else {
            if (tengv != null) {
                where += "and e.giaoviencn.tengiaovien like :tengv ";
                params.put("tengv", searchGvcnField.getValue().getTengiaovien());
            }
        }
        query = query + where;
        return query;
    }

    /*** Số thứ tự ***/
    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = lophocsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    /***Điều kiện***/
    @Subscribe("searchDvField")
    protected void onSearchDvFieldValueChange(HasValue.ValueChangeEvent event) {
        if (searchDvField.getValue() != null) {
            searchGvcnField.setOptionsList(searchedService.loadgiaovien(searchDvField.getValue().getTendonvi()));
        } else {
            searchGvcnField.clear();
        }
    }

    @Subscribe("searchGvcnField")
    protected void onSearchGvcnFieldValueChange(HasValue.ValueChangeEvent event) {
        if (searchGvcnField.getValue() != null) {
            searchLopField.setOptionsList(searchedService.loadlop(searchDvField.getValue().getTendonvi(), searchGvcnField.getValue().getTengiaovien()));
        } else {
            searchLopField.clear();
        }
    }

    /*** Xuất file excel ***/
    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng đã chọn không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Hàng đã chọn").withHandler(e -> {
                            xuatExcel(lophocsDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachLophoc());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Lophoc> layDanhSachLophoc) {
        Table table = lophocsTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Lophoc e : layDanhSachLophoc) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvi", e.getValue("donvi"));
            row.setValue("tenlop", e.getValue("tenlop"));
            row.setValue("giaoviencn", e.getValue("giaoviencn"));
            row.setValue("dshocsinh", e.getValue("dshocsinh"));

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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách lớp học");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách lớp học");
    }
}