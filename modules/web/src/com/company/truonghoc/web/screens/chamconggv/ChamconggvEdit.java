package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;

@UiController("truonghoc_Chamconggv.edit")
@UiDescriptor("chamconggv-edit.xml")
@EditedEntityContainer("chamconggvDc")
@LoadDataBeforeShow
public class ChamconggvEdit extends StandardEditor<Chamconggv> {
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Integer> tienbuoiField;
    @Inject
    protected LookupField<Giaovien> hotenGvField;
    @Inject
    protected LookupField<Donvi> donvigvField;
    @Inject
    protected LookupField<String> buoilamField;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected SearchedService searchedService;

    @Subscribe
    protected void onInit(InitEvent event) {
//        donvigvField.setEditable(false);
        hotenGvField.setRequired(true);
        ngaylamField.setRequired(true);
//        buoilamField.setRequired(true);
        List<String> buoilam = Arrays.asList("Làm cả ngày", "Ca sáng", "Ca chiều", "Ca chủ nhật", "Ca chiều 5h-6h", "Ca chiều 6h-7h");
        buoilamField.setOptionsList(buoilam);


    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donvigvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donvigvField.setEditable(false);
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                hotenGvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                hotenGvField.setEditable(false);
            }
        } else {
            donvigvField.setOptionsList(searchedService.loaddonvi());
        }
    }

    @Subscribe("buoilamField")
    protected void onBuoilamFieldValueChange(HasValue.ValueChangeEvent<String> event) {
//        tienbuoiField.setOptionsList(loadtienluongtheoca());


        if (buoilamField.getValue() == "Ca chủ nhật") {
            tienbuoiField.setValue(Integer.valueOf("100000"));
        }
        if (buoilamField.getValue() == "Ca chiều 5h-6h" || buoilamField.getValue() == "Ca chiều 6h-7h") {
            List<Integer> tienbuoi = new ArrayList<>();
            tienbuoi.add(50000);
            tienbuoi.add(60000);
            tienbuoi.add(80000);

            tienbuoiField.setOptionsList(tienbuoi);
        }
        if (buoilamField.getValue() == "Làm cả ngày" || buoilamField.getValue() == "Ca sáng" || buoilamField.getValue() == "Ca chiều") {
            tienbuoiField.clear();
        }

    }

//    private List<TienluongtheoCa> loadtienluongtheoca(){
//        return dataManager.load(TienluongtheoCa.class)
//                .query("select e from truonghoc_TienluongtheoCa e where e.caLam = :calam")
//                .parameter("calam", buoilamField.getValue())
//                .list();
//    }

    @Subscribe("donvigvField")
    protected void onDonvigvFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (donvigvField.getValue() != null) {
            hotenGvField.setOptionsList(timtengiaovien(donvigvField.getValue()));
        } else {
            hotenGvField.clear();
        }
    }

    public List<Giaovien> timtengiaovien(Donvi donvitao) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :donvitao")
                .parameter("donvitao", donvitao)
                .list();
    }

}