package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.*;
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
@LoadDataBeforeShow
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
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
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
    @Named("hocsinhsTable.edit")
    protected EditAction<Hocsinh> hocsinhsTableEdit;
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

    @Subscribe
    protected void onInit(InitEvent event) {
        //Load giới tính
        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");
        gioiTinhField.setOptionsList(gioitinh);

        //Đơn vị
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //Tìm đơn vị
        donViField.setOptionsList(searchedService.loaddonvi());
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            if (lookupActions.isVisible() == true) {
                donViField.setValue(donViSession);
                donViField.setEditable(false);
                excuteSearch(true);
            }
            donViField.setValue(donViSession);
            excuteSearch(true);
            donViField.setEditable(false);
        }
    }

    @Subscribe("donViField")
    protected void onDonViFieldValueChange(HasValue.ValueChangeEvent event) {
        if (donViField.getValue() == null) {
            hocSinhField.clear();
            gioiTinhField.clear();
        }
    }

    //    Điều kiện là giáo viên login vào
    @Subscribe("editBtn")
    protected void onEditBtnClick(Button.ClickEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            this.hocsinhsTableEdit.execute();
        } else {
            dialogs.createMessageDialog()
                    .withCaption("THÔNG BÁO")
                    .withMessage("Bạn không có quyền")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
        }
    }

    /***
     * Xoá
     * ***/
    @Subscribe("xoaBtn")
    protected void onXoaBtnClick(Button.ClickEvent event) {
        if (donViSession.getDonvitrungtam()) {
            donViField.clear();
        }
        gioiTinhField.clear();
        hocSinhField.clear();
    }


    /*** tìm kiếm ***/
    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String hocsinh = hocSinhField.getValue();
        String gioitinh = gioiTinhField.getValue();
        Object donvi = donViField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, params, hocsinh, gioitinh);

        hocsinhsDl.setQuery(query);
        hocsinhsDl.setParameters(params);
        hocsinhsDl.load();
    }

    private String returnQuery(Object donvi, Map<String, Object> params, String hocsinh, String gioitinh) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = " where 1=1 ";

        // Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_hocsinh.tendonvi = :donvi ";
            params.put("donvi", donViField.getValue().getTendonvi());
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
        query = query + where;
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
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachHocsinh(donViField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
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
            row.setValue("donvitao_hocsinh", e.getValue("donvitao_hocsinh"));
            row.setValue("tenhocsinh", e.getValue("tenhocsinh"));
            row.setValue("ngaysinhhocsinh", e.getNgaysinhhocsinh());
            row.setValue("gioitinhhocsinh", e.getValue("gioitinhhocsinh"));
            row.setValue("quequanhocsinh", e.getValue("quequanhocsinh"));
            row.setValue("noiSinh_XaPhuong", e.getValue("noiSinh_XaPhuong"));
            row.setValue("noiSinh_QuanHuyen", e.getValue("noiSinh_QuanHuyen"));
            row.setValue("noiSinh_TinhThanh", e.getValue("noiSinh_TinhThanh"));
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách học sinh");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách học sinh");
    }
}