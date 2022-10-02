package com.company.truonghoc.web.screens.thuchi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
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
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Thuchi.browse")
@UiDescriptor("thuchi-browse.xml")
@LookupComponent("thuchisTable")
@LoadDataBeforeShow
public class ThuchiBrowse extends StandardLookup<Thuchi> {
    @Inject
    protected CollectionLoader<Thuchi> thuchisDl;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected GroupTable<Thuchi> thuchisTable;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected LookupField<Giaovien> nguoichiField;
    @Inject
    protected TextField<String> khoanchiField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected Button createBtn;
    @Named("thuchisTable.create")
    protected CreateAction<Thuchi> thuchisTableCreate;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;
    private DecimalFormat percentFormat;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField donvitao_thuchiField;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected LookupField trangthaiField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Đã thanh toán", "Chưa thanh toán");
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
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {

            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                donvitao_thuchiField.setEditable(false);
                donvitao_thuchiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
                //Xoá
                nguoichiField.clear();
                khoanchiField.clear();
                trangthaiField.clear();
                tungayField.clear();
                denngayField.clear();

                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                    donvitao_thuchiField.setEditable(false);
                    nguoichiField.setEditable(false);
                    khoanchiField.clear();
                    trangthaiField.clear();
                    tungayField.clear();
                    denngayField.clear();
                    donvitao_thuchiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
                    nguoichiField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());  //chèn tên giáo viên từ user vào text
                }
            } else {
                donvitao_thuchiField.setEditable(true);
                //lấy dữ liệu string cho lookup
                donvisDl.load();
                List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                        .map(Donvi::getTendonvi)
                        .collect(Collectors.toList());
                donvitao_thuchiField.setOptionsList(sessionTypeNames);
                //xoá
                donvitao_thuchiField.clear();
                nguoichiField.clear();
                khoanchiField.clear();
                trangthaiField.clear();
                tungayField.clear();
                denngayField.clear();
            }

        }
    }

    public Component checkhanchi(Thuchi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaychi() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ THANH TOÁN</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHanchi();
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
        Object donvi = donvitao_thuchiField.getValue();
        Object ngthanhtoan = nguoichiField.getValue();
        String khoanchi = khoanchiField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, ngthanhtoan, khoanchi, trangthai, tungay, denngay, params);
        thuchisDl.setQuery(query);
        thuchisDl.setParameters(params);
        thuchisDl.load();
    }

    private String returnQuery(Object donvi, Object ngthanhtoan, String khoanchi, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Thuchi e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_thuchi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //người thanh toán
        if (ngthanhtoan != null) {
            where += "and e.usertao_thuchi.tengiaovien = :ngthanhtoan ";
            params.put("ngthanhtoan", nguoichiField.getValue().getTengiaovien());
        }
        //khoản chi
        if (!StringUtils.isEmpty(khoanchi)) {
            where += "and e.khoanchi = :khoanchi ";
            params.put("khoanchi", khoanchi);
        }
        //trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangchi = :trangthai ";
            params.put("trangthai", trangthai);
        }
        //từ ngày
        if (tungay != null) {
            where += "and e.ngaychi >= :tungay ";
            params.put("tungay", tungay);
        }
        //đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.ngaychi ";
            params.put("denngay", denngay);
        }
        query = query + where;
        return query;
    }

    @Subscribe("donvitao_thuchiField")
    protected void onDonvitao_thuchiFieldValueChange(HasValue.ValueChangeEvent event) {
        nguoichiField.setOptionsList(searchedService.loadgiaovien(donvitao_thuchiField.getValue()));
    }


}