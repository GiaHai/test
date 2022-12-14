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
//@LoadDataBeforeShow
public class TenlopBrowse extends StandardLookup<Tenlop> {
    @Inject
    protected LookupField<Donvi> searchDvField;
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
    protected Table<Tenlop> tenlopsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    private Donvi donViSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        //????n v???
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //load ????n v???
        searchDvField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        // ??i???u ki???n
        if (!donViSession.getDonvitrungtam()) {
            searchDvField.setEditable(false);
            searchDvField.setValue(donViSession);
        }
        excuteSearch(true);

    }

    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        if (donViSession.getDonvitrungtam() == true) {
            searchLopField.clear();
            searchGvcnField.clear();
            searchDvField.clear();
        } else {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() == null) {
                searchLopField.clear();
                searchGvcnField.clear();
            } else {
                searchLopField.clear();
            }
        }
        excuteSearch(true);
    }

    /***T??m ki???m***/
    public void timkiemExcute() {
        try {
            excuteSearch(true);
        } catch (NullPointerException ex) {
            notifications.create()
                    .withCaption("B???n ch??a nh???p th??ng tin c???n t??m ki???m")
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
    }

    private void excuteSearch(boolean isFromSearchBtn) {

        Object donvi = searchDvField.getValue();
        Tenlop tenlop = searchLopField.getValue();
        Object tengv = searchGvcnField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);

        tenlopsDl.setQuery(query);
        tenlopsDl.setParameters(params);
        tenlopsDl.load();
    }

    private String returnQuery(Object donvi, Tenlop tenlop, Object tengv, Map<String, Object> params) {

        String query = "select e from truonghoc_Tenlop e ";
        String where = " where 1=1 ";
        String orderBy = " order by e.thanghoc desc";

        //????n v???
        if (donvi != null) {
            where += "and e.dovi = :donvi ";
            params.put("donvi", donvi);
        }
        //T??n l???p
        if (tenlop != null) {
            where += "and e.tenlop like :tenlop ";
            params.put("tenlop", "%" + tenlop.getTenlop() + "%");
        }
        //T??n gi??o vi??n
        if (tengv != null) {
            where += "and e.giaoviencn = :giaovien ";
            params.put("giaovien", tengv);
        }

        query = query + where + orderBy;
        return query;
    }

    /*** S??? th??? t???***/
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

    /*** T??nh tr???ng l???p h???c ***/
    public Component tinhtranglop(Tenlop entity) {
        Label label = uiComponents.create(Label.class);
        if (entity.getTinhtranglop() != null) {
            if (entity.getTinhtranglop() == true) {
                label.setValue("M???");
            } else {
                label.setValue("????ng");
            }
        } else {
            label.setValue("????ng");
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
                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn.tengiaovien = :giaovien")
                .parameter("donvi", donvi)
                .parameter("giaovien", giaovien)
                .list();
    }

    /*** Xu???t file excel***/
    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("X??c nh???n")
                .withMessage("B???n c?? mu???n ch??? xu???t c??c h??ng ???? ch???n kh??ng?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("T???t c??? c??c h??ng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachTenlop(searchDvField.getValue(), searchGvcnField.getValue(), searchLopField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("H???y")
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh s??ch t??n l???p");
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh s??ch t??n l???p");
    }

}