package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.entity.tienich.Namsinh;
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
import com.haulmont.cuba.gui.actions.list.EditAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Hocsinh.browse")
@UiDescriptor("hocsinh-browse.xml")
@LookupComponent("hocsinhsTable")
//@LoadDataBeforeShow
public class HocsinhBrowse extends StandardLookup<Hocsinh> {
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField<Donvi> donViField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected TextField<String> hocSinhField;
    @Inject
    protected LookupField<String> gioiTinhField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected CollectionContainer<Hocsinh> hocsinhsDc;
    @Inject
    protected Button timkiemBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected HBoxLayout lookupActions;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Table<Hocsinh> hocsinhsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    private Donvi donViSession = null;
    @Inject
    protected LookupField<Namsinh> namSinhField;
    @Inject
    private LookupField<Boolean> tinhTrangField;

    @Subscribe
    protected void onInit(InitEvent event) {
        //Load gi???i t??nh
        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("N???");
        gioiTinhField.setOptionsList(gioitinh);

        Map<String, Boolean> tinhtrang = new LinkedHashMap<>();
        tinhtrang.put("C??n ??i h???c", false);
        tinhtrang.put("???? ngh??? h???c", true);
        tinhTrangField.setOptionsMap(tinhtrang);

        //????n v???
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //T??m ????n v???
        donViField.setOptionsList(searchedService.loaddonvi());
        namSinhField.setOptionsList(searchedService.loadNamSinh());

        if (!donViSession.getDonvitrungtam()) {
            if (lookupActions.isVisible() == true) {
                tinhTrangField.setValue(false);
                tinhTrangField.setEditable(false);
            }
            donViField.setValue(donViSession);
            donViField.setEditable(false);
        }
        excuteSearch(true);
    }

    @Subscribe("donViField")
    protected void onDonViFieldValueChange(HasValue.ValueChangeEvent event) {
        if (donViField.getValue() == null) {
            hocSinhField.clear();
            gioiTinhField.clear();
        }
    }


    /***
     * Xo??
     * ***/
    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        if (donViSession.getDonvitrungtam()) {
            donViField.clear();
        }
        gioiTinhField.clear();
        hocSinhField.clear();
        namSinhField.clear();
        excuteSearch(true);
    }


    /*** t??m ki???m ***/
    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String hocsinh = hocSinhField.getValue();
        String gioitinh = gioiTinhField.getValue();
        Object donvi = donViField.getValue();
        Object namsinh = namSinhField.getValue();
        Boolean tinhTrang = tinhTrangField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, params, hocsinh, gioitinh, namsinh, tinhTrang);

        hocsinhsDl.setQuery(query);
        hocsinhsDl.setParameters(params);
        hocsinhsDl.load();
    }

    private String returnQuery(Object donvi, Map<String, Object> params, String hocsinh, String gioitinh, Object namsinh, Boolean tinhTrang) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = " where 1=1 ";
        String orderBy = " order by e.donvi";
        // ????n v???
        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }

        //H???c sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.tenhocsinh like :tenhocsinh ";
            params.put("tenhocsinh", "%" + hocsinh + "%");
        }
        if (gioitinh != null) {
            where += "and e.gioitinhhocsinh = :gioitinh ";
            params.put("gioitinh", gioitinh);
        }
        if (namsinh != null){
            where += "and e.ngaysinhhocsinh = :namsinh ";
            params.put("namsinh", namsinh);
        }
        if (tinhTrang != null){
            where += "and e.tinhtranghocsinh = :tinhtrang ";
            params.put("tinhtrang", tinhTrang);
        }
        query = query + where + orderBy;
        return query;
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = hocsinhsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("X??c nh???n")
                .withMessage("B???n c?? mu???n ch??? xu???t c??c h??ng kh??ng?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("T???t c??? c??c h??ng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachHocsinh(donViField.getValue(), hocSinhField.getValue(), gioiTinhField.getValue(), namSinhField.getValue(), tinhTrangField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("H???y")
                )
                .show();
    }

    private void xuatExcel(List<Hocsinh> layDanhSachHocsinh) {

        Table table = hocsinhsTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Hocsinh e : layDanhSachHocsinh) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvi", e.getValue("donvi"));
            row.setValue("tenhocsinh", e.getValue("tenhocsinh"));
            row.setValue("ngaysinhhocsinh", e.getNgaysinhhocsinh());
            row.setValue("gioitinhhocsinh", e.getValue("gioitinhhocsinh"));
            row.setValue("quequanhocsinh", e.getValue("quequanhocsinh"));
            row.setValue("noiSinh_XaPhuong", e.getValue("noiSinh_XaPhuong"));
            row.setValue("noiSinh_QuanHuyen", e.getValue("noiSinh_QuanHuyen"));
            row.setValue("noiSinh_TinhThanh", e.getValue("noiSinh_TinhThanh"));
            row.setValue("ghichu", e.getValue("ghichu"));
            if (e.getTinhtranghocsinh() == true){
                row.setValue("tinhtranghocsinh", "???? ngh??? h???c");
            }else {
                row.setValue("tinhtranghocsinh", "C??n ??i h???c");
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
        String title = "Danh s??ch h???c sinh";
        ExtendExcelExporter exporter = new ExtendExcelExporter(title);

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, title);
    }
}