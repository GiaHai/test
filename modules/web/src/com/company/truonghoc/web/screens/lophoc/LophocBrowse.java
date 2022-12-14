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
//@LoadDataBeforeShow
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
    protected Table<Lophoc> lophocsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    private Donvi donViSession = null;
    private Giaovien giaoVienSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        //????n v???
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
        giaoVienSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //load ????n v???
        searchDvField.setOptionsList(searchedService.loaddonvi());
    }


    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        //??i???u ki???n
        if (!donViSession.getDonvitrungtam()) {
            searchDvField.setEditable(false);
            searchDvField.setValue(donViSession);
            if (giaoVienSession != null) {
                searchGvcnField.setValue(giaoVienSession);
                searchGvcnField.setEditable(false);
            }
        }
        excuteSearch(true);
    }

    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        if (donViSession.getDonvitrungtam() == false){
            if (giaoVienSession == null){
                searchGvcnField.clear();
            }
            searchLopField.clear();
        }else {
            searchDvField.clear();
            searchLopField.clear();
            searchGvcnField.clear();
        }
    }


    /*** T??m ki???m **/
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
        String orderBy = " order by e.tenlop.thanghoc desc";
        //????n v???
        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }
        // T??n l???p
        if (tenlop != null) {
            where += "and e.tenlop = :tenlop ";
            params.put("tenlop", tenlop);
        }
        // T??n gi??o vi??n
        //load n???u l???p h???c b???ng true th?? gi??o vi??n m???i xem ???????c
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            if (tengv != null) {
                where += "and e.giaoviencn = :tengv and e.tenlop.tinhtranglop = true ";
                params.put("tengv", tengv);
            }
        } else {
            if (tengv != null) {
                where += "and e.giaoviencn = :tengv ";
                params.put("tengv", tengv);
            }
        }
        query = query + where + orderBy;
        return query;
    }

    /*** S??? th??? t??? ***/
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

    /***??i???u ki???n***/
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
            searchLopField.setOptionsList(searchedService.loadlop(searchDvField.getValue(), searchGvcnField.getValue()));
        } else {
            searchLopField.clear();
        }
    }

    /*** Xu???t file excel ***/
    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("X??c nh???n")
                .withMessage("B???n c?? mu???n xu???t c??c h??ng kh??ng?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("T???t c??? c??c h??ng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachLophoc(searchDvField.getValue(), searchGvcnField.getValue(), searchLopField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("H???y")
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh s??ch l???p h???c");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh s??ch l???p h???c");
    }
}