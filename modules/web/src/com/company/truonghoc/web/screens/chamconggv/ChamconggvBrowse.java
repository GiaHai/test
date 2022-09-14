package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@UiController("truonghoc_Chamconggv.browse")
@UiDescriptor("chamconggv-browse.xml")
@LookupComponent("chamconggvsTable")
@LoadDataBeforeShow
public class ChamconggvBrowse extends StandardLookup<Chamconggv> {
    @Inject
    protected CollectionLoader<Chamconggv> chamcongGvsDl;
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
    protected Button timkiem;

    @Subscribe
    protected void onInit(InitEvent event) {
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getDonvitrungtam() == false) {
            tendonviField.setEditable(false);
            tengiaovienField.setEditable(false);
            ngaylamField.setEditable(false);
        }
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        tendonviField.setOptionsList(sessionTypeNames);

    }

    private List<Giaovien> tengiaovien(String dvgiaovien) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :dvgiaovien")
                .parameter("dvgiaovien", dvgiaovien)
                .list();
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        try {
            tengiaovienField.setOptionsList(tengiaovien(tendonviField.getValue().toString()));
        } catch (NullPointerException ex) {
        }
//        System.out.println(tengiaovien(tendonviField.getValue().toString()));
    }

    @Subscribe("timkiem")
    protected void onTimkiemClick(Button.ClickEvent event) {

        if (tengiaovienField.getValue() == null) {
            String loaddonvi = "select e from truonghoc_ChamcongGv e where e.donvigv = :donvi ";
            chamcongGvsDl.setQuery(loaddonvi);
        } else {
            String loadhoten = "select e from truonghoc_ChamcongGv e where e.hotenGv = :hoten and e.donvigv = :donvi";
            chamcongGvsDl.setQuery(loadhoten);
        }
        if (ngaylamField.getValue() != null){
            String loadngay = "select e from truonghoc_ChamcongGv e where e.ngaylam = :ngaylam";
            chamcongGvsDl.setQuery(loadngay);
        }

        chamcongGvsDl.setParameter("ngaylam", ngaylamField.getValue());
        chamcongGvsDl.setParameter("donvi", tendonviField.getValue());
        chamcongGvsDl.setParameter("hoten", tengiaovienField.getValue());
        chamcongGvsDl.load();
    }

    public Component stt(Chamconggv entity) {
        int lineNumber = 1;
        try {
            lineNumber = chamcongGvsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        tendonviField.clear();
        tengiaovienField.clear();
        ngaylamField.clear();
    }
}