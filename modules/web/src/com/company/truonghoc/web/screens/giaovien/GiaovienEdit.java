package com.company.truonghoc.web.screens.giaovien;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Giaovien;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;

@UiController("truonghoc_Giaovien.edit")
@UiDescriptor("giaovien-edit.xml")
@EditedEntityContainer("giaovienDc")
@LoadDataBeforeShow
public class GiaovienEdit extends StandardEditor<Giaovien> {
    @Inject
    protected LookupField<Donvi> donvitao_giaovienField;
    @Inject
    protected LookupField<String> gioitinhgiaovienField;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TextArea<String> ghichuField;
    @Inject
    protected TextField<Long> luongcobanField;
    @Inject
    protected DateField<Date> ngaysinhgiaovienField;
    @Inject
    protected TextField<String> quequangiaovienField;
    @Inject
    protected TextField<String> tengiaovienField;
    @Inject
    protected Button closeBtn;
    @Inject
    protected Actions actions;
    @Inject
    protected LookupField<Integer> tienBhField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Nam", "Nữ");
        gioitinhgiaovienField.setOptionsList(list);
        tienBhField.setEditable(false);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (getEditedEntity().getCreatedBy() == null){
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
                    donvitao_giaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                    donvitao_giaovienField.setEditable(false);
                } else {
                    donvitao_giaovienField.setOptionsList(test());
                }
            }
        }
    }

    private List<Donvi> test(){
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .list();
    }

    @Subscribe("luongcobanField")
    protected void onLuongcobanFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        if (luongcobanField.getValue() == null){
            tienBhField.clear();
            tienBhField.setEditable(false);
        }else {
            tienBhField.setEditable(true);
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("450000Đ", 450000);
            map.put("100% lương", luongcobanField.getValue().hashCode());

            tienBhField.setOptionsMap(map);
        }
    }
}