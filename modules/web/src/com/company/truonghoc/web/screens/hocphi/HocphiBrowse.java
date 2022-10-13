package com.company.truonghoc.web.screens.hocphi;

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
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocphi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Hocphi.browse")
@UiDescriptor("hocphi-browse.xml")
@LookupComponent("hocphisTable")
@LoadDataBeforeShow
public class HocphiBrowse extends StandardLookup<Hocphi> {
    @Inject
    protected CollectionLoader<Hocphi> hocphisDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField dovitao_hocphiField;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected LookupField trangthaiField;
    @Inject
    protected TextField<String> hovstenField;
    @Inject
    protected Button createBtn;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected CollectionContainer<Hocphi> hocphisDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected GroupTable<Hocphi> hocphisTable;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;

    @Subscribe
    protected void onInit(InitEvent event) {
        //tình trạng thanh toán
        List<String> list = Arrays.asList("Đã thanh toán", "Chưa thanh toán");
        trangthaiField.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        //load phân quyền
        dkphanquyen();
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        //load tìm kiếm
        excuteSearch(true);
    }

    /*** Tìm kiếm***/
    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = hocphisDl.getContainer().getItemIndex(entity.getId()) + 1;
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
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                dovitao_hocphiField.setEditable(false);
                dovitao_hocphiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
                //Xoá
                denngayField.clear();
                tungayField.clear();
                trangthaiField.clear();
                hovstenField.clear();
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                    dovitao_hocphiField.setEditable(false);
                    denngayField.clear();
                    tungayField.clear();
                    trangthaiField.clear();
                    hovstenField.clear();
                    dovitao_hocphiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
                }

            } else {
                dovitao_hocphiField.setEditable(true);
                //lấy dữ liệu string cho lookup
                donvisDl.load();
                List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                        .map(Donvi::getTendonvi)
                        .collect(Collectors.toList());
                dovitao_hocphiField.setOptionsList(sessionTypeNames);
                //xoá
                hovstenField.clear();
                dovitao_hocphiField.clear();
                denngayField.clear();
                tungayField.clear();
                trangthaiField.clear();
            }
        }
    }

    /*** Check hạn đóng***/
    public Component checkhandong(Hocphi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaydong() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ ĐÓNG</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHandong();
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

    /*** Tìm kiếm ***/
    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = dovitao_hocphiField.getValue();
        String hocsinh = hovstenField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, hocsinh, trangthai, tungay, denngay, params);
        hocphisDl.setQuery(query);
        hocphisDl.setParameters(params);
        hocphisDl.load();
    }

    private String returnQuery(Object donvi, String hocsinh, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Hocphi e ";
        String where = " where 1=1 ";


        //Học sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.hovaten.tenhocsinh like :hocsinh ";
            params.put("hocsinh", "%" + hocsinh + "%");
        }

        //Trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangthai);
        }

        //Đơn vị
        if (donvi != null) {
            where += "and e.dovitao_hocphi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //Từ ngày
        if (tungay != null) {
            where += "and e.ngaydong >= :tungay ";
            params.put("tungay", tungay);
        }
        //Đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.ngaydong ";
            params.put("denngay", denngay);
        }

        query = query + where;
        return query;
    }

    /*** Xuất file excel***/
    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("Xác nhận")
                .withMessage("Bạn có muốn chỉ xuất các hàng đã chọn không?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Hàng đã chọn").withHandler(e -> {
                            xuatExcel(hocphisDc.getItems());
                        }),
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("Tất cả các hàng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachHocphi());
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("Hủy")
                )
                .show();
    }

    private void xuatExcel(List<Hocphi> layDanhSachHocphi) {

        Table table = hocphisTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Hocphi e : layDanhSachHocphi) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("dovitao_hocphi", e.getValue("dovitao_hocphi"));
            row.setValue("hovaten", e.getValue("hovaten"));
            row.setValue("ghichu", e.getValue("ghichu"));
            row.setValue("sotientamtinh", e.getValue("sotientamtinh"));
            row.setValue("sotienthutheohd", e.getValue("sotienthutheohd"));
            row.setValue("ngaydong", e.getValue("ngaydong"));
            row.setValue("handong", e.getValue("handong"));
            row.setValue("hinhthucthanhtoan", e.getValue("hinhthucthanhtoan"));
            row.setValue("ngaydong", e.getValue("ngaydong"));
            if (e.getNgaydong() != null) {
                row.setValue("checkhandong", "Đã đóng");
            } else {
                Date now = new Date();
                Date han = e.getHandong();
                if (now.compareTo(han) >= 0) {
                    row.setValue("checkhandong", "Quá hạn");
                } else {
                    row.setValue("checkhandong", "Đến hạn");
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
        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh sách học phí");
        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh sách học phí");
    }

    // Điều kiện

}