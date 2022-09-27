package com.company.truonghoc.web.screens.tenlop;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Tenlop;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("truonghoc_Tenlop.browse")
@UiDescriptor("tenlop-browse.xml")
@LookupComponent("tenlopsTable")
@LoadDataBeforeShow
public class TenlopBrowse extends StandardLookup<Tenlop> {
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected LookupField searchDvField;
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

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        searchDvField.setOptionsList(sessionTypeNames);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        try {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                searchDvField.setEditable(false);
                searchDvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
                excuteSearch(true);
            }
        }catch (NullPointerException ex){

        }
    }

    public void timkiemExcute() {
        try {
            excuteSearch(true);
        } catch (NullPointerException ex) {
            notifications.create()
                    .withCaption("Bạn chưa nhập thông tin cần tìm kiếm")
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }

    }

    private void excuteSearch(boolean isFromSearchBtn) {

        String donvi = searchDvField.getValue().toString();
        Object tenlop = searchLopField.getValue();
        Object tengv = searchGvcnField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);

        tenlopsDl.setQuery(query);
        tenlopsDl.setParameters(params);
        tenlopsDl.load();
    }

    private String returnQuery(String donvi, Object tenlop, Object tengv, Map<String, Object> params) {

        String query = "select e from truonghoc_Tenlop e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.dovi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //Tên lớp
        if (tenlop != null) {
            where += "and e.tenlop = :tenlop ";
            params.put("tenlop", searchLopField.getValue().getTenlop());
        }
        //Tên giáo viên
        if (tengv != null) {
            where += "and e.giaoviencn.tengiaovien like :giaovien ";
            params.put("giaovien", searchGvcnField.getValue().getTengiaovien());
        }

        query = query + where;
        return query;
    }

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
    public Component tinhtranglop(Tenlop entity) {
        Label label = uiComponents.create(Label.class);
        if (entity.getTinhtranglop() != null){
            if (entity.getTinhtranglop() == true){
                label.setValue("Mở");
            }else {
                label.setValue("Đóng");
            }
        }else {
            label.setValue("Đóng");
        }
        return label;
    }
    @Subscribe("searchDvField")
    protected void onSearchDvFieldValueChange(HasValue.ValueChangeEvent event) {
        searchGvcnField.setOptionsList(searchedService.loadgiaovien(searchDvField.getValue()));
    }

    @Subscribe("searchGvcnField")
    protected void onSearchGvcnFieldValueChange(HasValue.ValueChangeEvent event) {
        searchLopField.setOptionsList(loadlop(searchDvField.getValue(), searchGvcnField.getValue().getTengiaovien()));
    }

    private List<Tenlop> loadlop(Object donvi, Object giaovien) {
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi.tendonvi = :donvi and e.giaoviencn.tengiaovien = :giaovien")
                .parameter("donvi", donvi)
                .parameter("giaovien", giaovien)
                .list();
    }


}