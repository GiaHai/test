package com.company.truonghoc.web.screens;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.screen.Subscribe;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class ExtUserEditor extends UserEditor {
    @Inject
    protected CollectionDatasource<Donvi, UUID> donvisDc;
    @Inject
    protected LookupField<Donvi> loockuptendonvi;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Giaovien> tengiaovienField;

    @Subscribe
    protected void onInit(InitEvent event) {
        loockuptendonvi.setRequired(true);
    }
    @Subscribe("loockuptendonvi")
    protected void onLoockuptendonviValueChange(HasValue.ValueChangeEvent<Donvi> event) {

        if (loockuptendonvi.getValue() != null){
            tengiaovienField.setOptionsList(tengiaovien(loockuptendonvi.getValue()));
        }
        if (loockuptendonvi.getValue().getDonvitrungtam() != null){
            tengiaovienField.setVisible(false);
            tengiaovienField.clear();
        }else {
            tengiaovienField.setVisible(true);
        }
    }
    private List<Giaovien> tengiaovien(Object dvgiaovien) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :dvgiaovien")
                .parameter("dvgiaovien", dvgiaovien)
                .list();
    }
}