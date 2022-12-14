package com.company.truonghoc.web.screens.tenlop;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Tenlop;
import com.haulmont.cuba.security.global.UserSession;
import com.company.truonghoc.entity.enums.ThangHocEnum;


import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Tenlop.edit")
@UiDescriptor("tenlop-edit.xml")
@EditedEntityContainer("tenlopDc")
@LoadDataBeforeShow
public class TenlopEdit extends StandardEditor<Tenlop> {
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
    protected LookupField<Integer> thanghocField;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected TextField<String> tenlopField;

    @Subscribe
    protected void onInit(InitEvent event) {
        donviFiled.setOptionsList(searchedService.loaddonvi());

//        List<String> thang = Arrays.asList(
//                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
//        );
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Tháng 1", ThangHocEnum.THANG_MOT.getId());
        map.put("Tháng 2", ThangHocEnum.THANG_HAI.getId());
        map.put("Tháng 3", ThangHocEnum.THANG_BA.getId());
        map.put("Tháng 4", ThangHocEnum.THANG_TU.getId());
        map.put("Tháng 5", ThangHocEnum.THANG_NAM.getId());
        map.put("Tháng 6", ThangHocEnum.THANG_SAU.getId());
        map.put("Tháng 7", ThangHocEnum.THANG_BAY.getId());
        map.put("Tháng 8", ThangHocEnum.THANG_TAM.getId());
        map.put("Tháng 9", ThangHocEnum.THANG_CHIN.getId());
        map.put("Tháng 10", ThangHocEnum.THANG_MUOI.getId());
        map.put("Tháng 11", ThangHocEnum.THANG_MUOI_MOT.getId());
        map.put("Tháng 12", ThangHocEnum.THANG_MUOI_HAI.getId());
        
        thanghocField.setOptionsMap(map);
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
            giaoviencnField.setOptionsList(searchedService.loadgiaovien(donviFiled.getValue()));
        } else {
            giaoviencnField.clear();
        }
    }

    public static String toTitleCase(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    @Subscribe("tenlopField")
    protected void onTenlopFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (tenlopField.getValue() != null) {
            String[] splitPhrase = tenlopField.getValue().split(" ");
            String result = "";

            for (String word : splitPhrase) {
                result += toTitleCase(word) + " ";
            }
            tenlopField.setValue(result.trim());
        }
    }

}