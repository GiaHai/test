package com.company.truonghoc.web.screens.luongthang;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.ExcelAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Luongthang;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Luongthang.browse")
@UiDescriptor("luongthang-browse.xml")
@LookupComponent("luongthangsTable")
@LoadDataBeforeShow
public class LuongthangBrowse extends StandardLookup<Luongthang> {
    @Inject
    protected CollectionLoader<Luongthang> luongthangsDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField donvitao_luongthangField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected LookupField trangthaiField;
    @Inject
    protected LookupField<Giaovien> giaovienField;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected CollectionContainer<Luongthang> luongthangsDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected GroupTable<Luongthang> luongthangsTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Đã nhận lương", "Chưa nhận lương");
        trangthaiField.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        excuteSearch(true);
    }

    // đánh số thứ tự
    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = luongthangsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {

            if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
                donvitao_luongthangField.setEditable(false);
                donvitao_luongthangField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());

                //Xoá
                giaovienField.clear();
                trangthaiField.clear();
                tungayField.clear();
                denngayField.clear();
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                    giaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                    giaovienField.setEditable(false);
                }
            } else {
                donvitao_luongthangField.setEditable(true);
                //lấy dữ liệu string cho lookup
                donvisDl.load();
                List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                        .map(Donvi::getTendonvi)
                        .collect(Collectors.toList());
                donvitao_luongthangField.setOptionsList(sessionTypeNames);
                //xoá
                donvitao_luongthangField.clear();
                giaovienField.clear();
                trangthaiField.clear();
                tungayField.clear();
                denngayField.clear();
            }
        }
    }

    public Component checkhannhanluong(Luongthang entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaynhan() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ ĐÓNG</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHannhanluong();
            if (now.compareTo(han) >= 0) {
                String body = "<a style=\"background-color: black; color: red ; width: 100%;\">QUÁ HẠN</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            } else {
                String body = "<a style=\"color: red; width: 100%;\">ĐẾN HẠN</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            }
            return htmlBoxLayout1;
        }
        return htmlBoxLayout;
    }

    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = donvitao_luongthangField.getValue();
        Object giaovien = giaovienField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, giaovien, trangthai, tungay, denngay, params);
        luongthangsDl.setQuery(query);
        luongthangsDl.setParameters(params);
        luongthangsDl.load();
    }

    private String returnQuery(Object donvi, Object giaovien, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Luongthang e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_luongthang.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //Giáo viên
        if (giaovien != null) {
            where += "and e.hovaten.tengiaovien = :giaovien ";
            params.put("giaovien", giaovienField.getValue().getTengiaovien());
        }
        //Trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangnhanluong = :trangthai ";
            params.put("trangthai", trangthai);
        }
        //Từ ngày
        if (tungay != null) {
            where += "and e.ngaynhan >= :tungay ";
            params.put("tungay", tungay);
        }
        //Đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.ngaynhan";
            params.put("denngay", denngay);
        }
        query = query + where;
        return query;
    }

    @Subscribe("donvitao_luongthangField")
    protected void onDonvitao_luongthangFieldValueChange(HasValue.ValueChangeEvent event) {
        giaovienField.setOptionsList(searchedService.loadgiaovien(donvitao_luongthangField.getValue()));
    }
    /********* XUẤT FILE EXCEL ********/

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng đã chọn không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Hàng đã chọn").withHandler(e -> {
                            xuatExcel(luongthangsDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachLuongthang());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Luongthang> layDanhSachLuongthang) {
        Table table = luongthangsTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Luongthang e : layDanhSachLuongthang) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvitao_luongthang", e.getValue("donvitao_luongthang"));
            row.setValue("hovaten", e.getValue("hovaten"));
            row.setValue("ngaynhan", e.getValue("ngaynhan"));
            row.setValue("hannhanluong", e.getValue("hannhanluong"));
            row.setValue("luongcoban", e.getValue("luongcoban"));
            row.setValue("buoilam", e.getValue("buoilam"));
            row.setValue("cangoai", e.getValue("cangoai"));
            row.setValue("casang", e.getValue("casang"));
            row.setValue("cachunhat", e.getValue("cachunhat"));
            row.setValue("thuclinh", e.getValue("thuclinh"));
            row.setValue("tienBh", e.getValue("tienBh"));
            row.setValue("daythem", e.getValue("daythem"));
            row.setValue("trocap", e.getValue("trocap"));
            row.setValue("trachnhiem", e.getValue("trachnhiem"));
            row.setValue("chuyencan", e.getValue("chuyencan"));
            row.setValue("thuong", e.getValue("thuong"));
            row.setValue("tonglinh", e.getValue("tonglinh"));

            if (e.getNgaynhan() != null) {
                row.setValue("checkhannhanluong", "Đã nhận");
            } else {
                Date now = new Date();
                Date han = e.getHannhanluong();
                if (now.compareTo(han) >= 0) {
                    row.setValue("checkhannhanluong", "Quá hạn");
                } else {
                    row.setValue("checkhannhanluong", "Đến hạn");
                }
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
        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách lương");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties,exportDisplay,"Danh sách lương");
    }
}