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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
    protected LookupField<Donvi> donviFiled;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected LookupField<String> thanghocField;

    @Subscribe
    protected void onInit(InitEvent event) {
        donvisDl.load();
        donviFiled.setOptionsList(loaddonvi());

        List<String> thang = Arrays.asList(
                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        );
        thanghocField.setOptionsList(thang);
    }

    private List<Donvi> loaddonvi() {
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .list();
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donviFiled.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donviFiled.setEditable(false);
        }
    }

    @Subscribe("donviFiled")
    protected void onDonviFiledValueChange(HasValue.ValueChangeEvent event) {
        if (donviFiled.getValue() != null) {
            giaoviencnField.setOptionsList(loadgiaovien());
        } else {
            giaoviencnField.clear();
        }
    }

    private List<Giaovien> loadgiaovien() {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien =:donvi")
                .parameter("donvi", donviFiled.getValue())
                .list();
    }
}