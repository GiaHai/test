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

import javax.inject.Inject;
import java.util.List;
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

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0){
            donvitao_giaovienField.setEditable(false);
            donvitao_giaovienField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
        }else {
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