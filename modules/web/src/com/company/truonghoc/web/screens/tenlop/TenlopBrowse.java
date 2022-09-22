package com.company.truonghoc.web.screens.tenlop;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.entity.Entity;
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
    protected TextField<String> searchGvcnField;
    @Inject
    protected LookupField searchDvField;
    @Inject
    protected TextField<String> searchLopField;
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

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        searchLopField.setEditable(false);
        searchGvcnField.setEditable(false);
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        searchDvField.setOptionsList(sessionTypeNames);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0){
            searchDvField.setEditable(false);
            searchDvField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());

            excuteSearch(true);
        }
    }

    @Subscribe("searchDvField")
    protected void onSearchDvFieldValueChange(HasValue.ValueChangeEvent event) {
        if (searchDvField.getValue() == null){
            searchLopField.setEditable(false);
            searchGvcnField.setEditable(false);
        }else {
            searchLopField.setEditable(true);
            searchGvcnField.setEditable(true);
        }
    }
    public void timkiemExcute() {
        try {
            excuteSearch(true);
        }catch (NullPointerException ex){
            notifications.create()
                    .withCaption("Bạn chưa nhập thông tin cần tìm kiếm")
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }

    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String donvi = searchDvField.getValue().toString();
        String tenlop = searchLopField.getValue();
        String tengv = searchGvcnField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);

        tenlopsDl.setQuery(query);
        tenlopsDl.setParameters(params);
        tenlopsDl.load();
    }

    private String returnQuery(String donvi, String tenlop, String tengv, Map<String, Object> params) {

        String query = "select e from truonghoc_Tenlop e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null){
            where += "and e.dovi = :donvi ";
            params.put("donvi", donvi);
        }
        //Tên lớp
        if (!StringUtils.isEmpty(tenlop)){
            where += "and e.tenlop like :tenlop ";
            params.put("tenlop", tenlop);
        }
        //Tên giáo viên
        if (!StringUtils.isEmpty(tengv)){
            where += "and e.giaoviencn.tengiaovien like :giaovien ";
            params.put("giaovien", tengv);
        }

        query = query + where;
        return query;
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = tenlopsDl.getContainer().getItemIndex(entity.getId()) + 1;
        }
        catch (Exception ex)
        {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}