package com.company.truonghoc.web.screens.giaovien;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Giaovien;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("truonghoc_Giaovien.browse")
@UiDescriptor("giaovien-browse.xml")
@LookupComponent("giaoviensTable")
@LoadDataBeforeShow
public class GiaovienBrowse extends StandardLookup<Giaovien> {
    @Inject
    protected UserSession userSession;
    @Inject
    protected CollectionLoader<Giaovien> giaoviensDl;
    @Inject
    protected LookupField donvitao_giaovienField;
    @Inject
    protected GroupTable<Giaovien> giaoviensTable;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected TextField<String> searchTenGvField;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0) {
            donvitao_giaovienField.setEditable(false);
            donvitao_giaovienField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
        } else {
            donvitao_giaovienField.setEditable(true);
            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            donvitao_giaovienField.setOptionsList(sessionTypeNames);
        }

    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = giaoviensDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    public void timkiem() {
        try {
            excuteSearch(true);
        }catch (NullPointerException ex){
        }
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String donvi = donvitao_giaovienField.getValue().toString();
        String giaovien = searchTenGvField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, params);
        giaoviensDl.setQuery(query);
        giaoviensDl.setParameters(params);
        giaoviensDl.load();
    }

    private String returnQuery(String donvi, String giaovien, Map<String, Object> params) {
        String query = "select e from truonghoc_Giaovien e ";
        String where = " where 1=1 ";

        if (donvi != null) {
            where += "and e.donvitao_giaovien = :donvi ";
            params.put("donvi", donvi);
        }
        if (!StringUtils.isEmpty(giaovien)){
            where += "and e.tengiaovien like :tengv ";
            params.put("tengv", "%" + giaovien + "%");
        }

        query = query + where;

        return query;
    }
}