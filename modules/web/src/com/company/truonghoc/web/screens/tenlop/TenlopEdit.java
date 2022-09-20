package com.company.truonghoc.web.screens.tenlop;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Tenlop;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@UiController("truonghoc_Tenlop.edit")
@UiDescriptor("tenlop-edit.xml")
@EditedEntityContainer("tenlopDc")
@LoadDataBeforeShow
public class TenlopEdit extends StandardEditor<Tenlop> {
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Giaovien> giaoviencnField;
    @Inject
    protected LookupField<String> donviFiled;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;

    @Subscribe
    protected void onInit(InitEvent event) {
        donvisDl.load();

        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        donviFiled.setOptionsList(sessionTypeNames);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getDonvitrungtam() == null) {
            donviFiled.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
            donviFiled.setEditable(false);
        }
    }

    @Subscribe("donviFiled")
    protected void onDonviFiledValueChange(HasValue.ValueChangeEvent event) {
        giaoviencnField.setOptionsList(loadgiaovien());
    }

    private List<Giaovien> loadgiaovien() {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien =:donvi")
                .parameter("donvi", donviFiled.getValue())
                .list();
    }
}