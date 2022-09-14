package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Diemdanh;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@UiController("truonghoc_Diemdanh.browse")
@UiDescriptor("diemdanh-browse.xml")
@LookupComponent("diemdanhsTable")
@LoadDataBeforeShow
public class DiemdanhBrowse extends StandardLookup<Diemdanh> {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField tendonviField;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionLoader<Diemdanh> diemdanhsDl;
    @Inject
    protected TextField<String> tengiaovienField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DateField<Date> ngaylamField;

    @Subscribe
    protected void onInit(InitEvent event) {
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        tendonviField.setOptionsList(sessionTypeNames);
    }

    @Subscribe("timkiem")
    protected void onTimkiemClick(Button.ClickEvent event) {

        if (tengiaovienField.getValue() == null) {
            String loaddonvi = "select e from truonghoc_Diemdanh e where e.donvidd = :donvi";
            diemdanhsDl.setQuery(loaddonvi);
            if (ngaylamField.getValue() != null) {
                String loadngaylam = "select e from truonghoc_Diemdanh e where e.ngaynghi = :ngaynghi and e.donvidd = :donvi";
                diemdanhsDl.setQuery(loadngaylam);
            }
        } else {
            String loadhoten = "select e from truonghoc_Diemdanh e where e.donvidd = :donvi and e.nguoitaodd = :nguoitao";
            diemdanhsDl.setQuery(loadhoten);
            if (ngaylamField.getValue() != null) {
                String loadngaylam = "select e from truonghoc_Diemdanh e where e.ngaynghi = :ngaynghi and e.donvidd = :donvi and e.nguoitaodd = :nguoitao";
                diemdanhsDl.setQuery(loadngaylam);
            }
        }
        diemdanhsDl.setParameter("donvi", tendonviField.getValue());
        diemdanhsDl.setParameter("nguoitao", tengiaovienField.getValue());
        diemdanhsDl.setParameter("ngaynghi", ngaylamField.getValue());
        diemdanhsDl.load();
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        if (tendonviField.getValue() != null) {
            tengiaovienField.setEditable(true);
        }
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        tendonviField.clear();
        tengiaovienField.clear();
        ngaylamField.clear();
    }
}