package com.company.truonghoc.web.screens.thuchi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.company.truonghoc.service.XuatFileExcelService;
import com.company.truonghoc.web.screens.utils.ExtendExcelExporter;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Thuchi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import com.vaadin.ui.components.grid.FooterRow;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Thuchi.browse")
@UiDescriptor("thuchi-browse.xml")
@LookupComponent("thuchisTable")
//@LoadDataBeforeShow
public class ThuchiBrowse extends StandardLookup<Thuchi> {
    @Inject
    protected CollectionLoader<Thuchi> thuchisDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected Table<Thuchi> thuchisTable;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected TextField<String> khoanchiField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected CollectionContainer<Thuchi> thuchisDc;
    @Inject
    protected XuatFileExcelService xuatFileExcelService;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ExportDisplay exportDisplay;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField<Donvi> donViField;
    @Inject
    protected LookupField trangthaiField;
    private Donvi donViSession = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("???? thanh to??n", "Ch??a thanh to??n");
        trangthaiField.setOptionsList(list);
        //????n v???
        donViSession = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);

    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = thuchisDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        if (!donViSession.getDonvitrungtam()) {
            //Xo??
            khoanchiField.clear();
            trangthaiField.clear();
            tungayField.clear();
            denngayField.clear();
        } else {
            //xo??
            donViField.clear();
            khoanchiField.clear();
            trangthaiField.clear();
            tungayField.clear();
            denngayField.clear();
        }
        excuteSearch(true);
    }

    //??i???u ki???n login
    private void dkphanquyen() {
        //??i???u ki???n ????n v??? trung t??m n???u
        if (!donViSession.getDonvitrungtam()) {
            donViField.setEditable(false);
            donViField.setValue(donViSession); //Ch??n ????n v??? t??? user v??o text
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                donViField.setEditable(false);
                khoanchiField.clear();
                trangthaiField.clear();
                tungayField.clear();
                denngayField.clear();
                donViField.setValue(donViSession); //Ch??n ????n v??? t??? user v??o text
            }
        } else {
            donViField.setEditable(true);
            //l???y d??? li???u string cho lookup
            donViField.setOptionsList(searchedService.loaddonvi());
        }
    }

    public Component checkhanchi(Thuchi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaychi() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">???? THANH TO??N</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHanchi();
            if (now.compareTo(han) >= 0) {
                String body = "<a style=\"background-color: black; color: red ; width: 100%;\">QU?? H???N</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            } else {
                String body = "<a style=\"color: red; width: 100%;\">?????N H???N</a>\n";
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
        Object donvi = donViField.getValue();
        String khoanchi = khoanchiField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, khoanchi, trangthai, tungay, denngay, params);
        thuchisDl.setQuery(query);
        thuchisDl.setParameters(params);
        thuchisDl.load();
    }

    private String returnQuery(Object donvi, String khoanchi, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Thuchi e ";
        String where = " where 1=1 ";
        String orderBy = " order by e.ngaychi desc";
        //????n v???
        if (donvi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donvi);
        }
        //kho???n chi
        if (!StringUtils.isEmpty(khoanchi)) {
            where += "and e.khoanchi = :khoanchi ";
            params.put("khoanchi", khoanchi);
        }
        //tr???ng th??i
        if (trangthai != null) {
            where += "and e.tinhtrangchi = :trangthai ";
            params.put("trangthai", trangthai);
        }
        //t??? ng??y
        if (tungay != null) {
            where += "and e.ngaychi >= :tungay ";
            params.put("tungay", tungay);
        }
        //?????n ng??y
        if (denngay != null) {
            where += "and :denngay >= e.ngaychi ";
            params.put("denngay", denngay);
        }
        query = query + where + orderBy;
        return query;
    }


    /********* XU???T FILE EXCEL ********/

    @Subscribe("excelBtn")
    protected void onExcelBtnClick(Button.ClickEvent event) {
        dialogs.createOptionDialog()
                .withCaption("X??c nh???n")
                .withMessage("B???n c?? mu???n xu???t c??c h??ng kh??ng?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("T???t c??? c??c h??ng").withHandler(e -> {
                            xuatExcel(xuatFileExcelService.layDanhSachThuchi(donViField.getValue(), khoanchiField.getValue(), trangthaiField.getValue(), tungayField.getValue(), denngayField.getValue()));
                        }),
                        new DialogAction(DialogAction.Type.NO).withCaption("H???y")
                )
                .show();
    }

    private void xuatExcel(List<Thuchi> layDanhSachThuchi) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Table table = thuchisTable;
        Map<String, String> columns = new HashMap<>();
        Map<Integer, String> properties = new HashMap<>();
        List<KeyValueEntity> collection = new ArrayList<>();
        int count = 1;

        for (Thuchi e : layDanhSachThuchi) {
            KeyValueEntity row = metadata.create(KeyValueEntity.class);
            row.setValue("stt", count);
            row.setValue("donvi", e.getValue("donvi"));
            if (e.getValue("ngaychi") != null) {
                row.setValue("ngaychi", simpleDateFormat.format(e.getValue("ngaychi")));
            }
            row.setValue("khoanchi", e.getValue("khoanchi"));
            row.setValue("soluong", e.getValue("soluong"));
            row.setValue("thanhtien", e.getValue("thanhtien"));
            row.setValue("ghichu", e.getValue("ghichu"));
            if (e.getNgaychi() != null) {
                row.setValue("checkhandong", "???? thanh to??n");
            } else {
                Date now = new Date();
                Date han = e.getHanchi();
                if (now.compareTo(han) >= 0) {
                    row.setValue("checkhandong", "Qu?? h???n");
                } else {
                    row.setValue("checkhandong", "?????n h???n");
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

        ExtendExcelExporter exporter = new ExtendExcelExporter("Danh s??ch chi");

        exporter.exportDataCollectionTitleInFile(collection, columns, properties, exportDisplay, "Danh s??ch chi");
    }
}