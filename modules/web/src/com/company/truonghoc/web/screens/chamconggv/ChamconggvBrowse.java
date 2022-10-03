package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Chamconggv.browse")
@UiDescriptor("chamconggv-browse.xml")
@LookupComponent("chamconggvsTable")
@LoadDataBeforeShow
public class ChamconggvBrowse extends StandardLookup<Chamconggv> {
    @Inject
    protected CollectionContainer<Chamconggv> chamconggvsDc;
    @Inject
    protected CollectionLoader<Chamconggv> chamconggvsDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected LookupField tendonviField;
    @Inject
    protected LookupField tengiaovienField;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField buoilamField;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm
        // lấy dữ liệu buổi làm
        List<String> list = Arrays.asList("Làm cả ngày", "Ca sáng", "Ca chiều");
        buoilamField.setOptionsList(list);
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {

            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                tendonviField.setEditable(false);
                tendonviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
//            tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien().getTengiaovien());
                //Xoá
                tengiaovienField.clear();
                ngaylamField.clear();
                buoilamField.clear();
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                    tendonviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
                    tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                    tengiaovienField.setEditable(false);
                }
            } else {
                tendonviField.setEditable(true);
                //lấy dữ liệu string cho lookup
                donvisDl.load();
                List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                        .map(Donvi::getTendonvi)
                        .collect(Collectors.toList());
                tendonviField.setOptionsList(sessionTypeNames);

                //Xoá
                tengiaovienField.clear();
                ngaylamField.clear();
                buoilamField.clear();
                tendonviField.clear();
            }
        }
    }

    private List<Giaovien> tengiaovien(Object dvgiaovien) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien.tendonvi = :dvgiaovien")
                .parameter("dvgiaovien", dvgiaovien)
                .list();
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() == null){
            try {
                tengiaovienField.setOptionsList(tengiaovien(tendonviField.getValue()));
            } catch (NullPointerException ex) {
            }
        }
    }


    public Component stt(Chamconggv entity) {
        int lineNumber = 1;
        try {
            lineNumber = chamconggvsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }


    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = tendonviField.getValue();
        Object giaovien = tengiaovienField.getValue();
        Date ngaylam = ngaylamField.getValue();
        Object buoilam = buoilamField.getValue();

        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, giaovien, ngaylam, buoilam, params);

        chamconggvsDl.setQuery(query);
        chamconggvsDl.setParameters(params);
        chamconggvsDl.load();
    }

    private String returnQuery(Object donvi, Object giaovien, Date ngaylam, Object buoilam, Map<String, Object> params) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = " where 1=1 ";

        //đơn vị
        if (donvi != null) {
            where += "and e.donvigv.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //giáo viên
        if (giaovien != null) {
            where += "and e.hotengv = :giaovien ";
            params.put("giaovien", giaovien);
        }
        //ngày làm
        if (ngaylam != null) {
            where += "and e.ngaylam = :ngaylam ";
            params.put("ngaylam", ngaylam);
        }
        //ngày làm
        if (buoilam != null) {
            where += "and e.buoilam = :buoilam ";
            params.put("buoilam", buoilam);
        }
        query = query + where;
        return query;
    }
}