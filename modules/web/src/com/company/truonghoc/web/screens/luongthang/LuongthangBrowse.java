package com.company.truonghoc.web.screens.luongthang;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.entity.Entity;
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
    protected TextField<String> nguoichiField;
    @Inject
    protected TextField<String> giaovienField;

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
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0) {
            donvitao_luongthangField.setEditable(false);
            donvitao_luongthangField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());

            //Xoá
            nguoichiField.clear();
            giaovienField.clear();
            trangthaiField.clear();
            tungayField.clear();
            denngayField.clear();
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
            nguoichiField.clear();
            giaovienField.clear();
            trangthaiField.clear();
            tungayField.clear();
            denngayField.clear();
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
                String body = "<a style=\"background-color: black; color: red ; width: 100%;\">QUÁ HẠN)</a>\n";
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
        String ngthanhtoan = nguoichiField.getValue();
        String giaovien = giaovienField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, ngthanhtoan, giaovien, trangthai, tungay, denngay, params);
        luongthangsDl.setQuery(query);
        luongthangsDl.setParameters(params);
        luongthangsDl.load();
    }

    private String returnQuery(Object donvi, String ngthanhtoan, String giaovien, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Luongthang e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_luongthang = :donvi ";
            params.put("donvi", donvi);
        }
        //Người chi
        if (ngthanhtoan != null) {
            where += "and e.usertao_luongthang like :ngthanhtoan ";
            params.put("ngthanhtoan", "%" + ngthanhtoan + "%");
        }
        //Giáo viên
        if (giaovien != null) {
            where += "and e.hovaten.tengiaovien like :giaovien ";
            params.put("giaovien", "%" + giaovien + "%");
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
}