package com.company.truonghoc.web.screens;

import com.company.truonghoc.entity.Donvi;
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
    protected CheckBox donvitrungtam;
    @Inject
    protected LookupField<Donvi> loockuptendonvi;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TextField<String> tendonviField;

    @Subscribe("loockuptendonvi")
    protected void onLoockuptendonviValueChange(HasValue.ValueChangeEvent<Donvi> event) {
//        newEntitiesDl.load();
        tendonviField.setValue(donvisDc.getItem().getTendonvi());
        donvitrungtam.setValue(donvisDc.getItem().getDonvitrungtam());
    }


    @Subscribe
    protected void onInit(InitEvent event) {
        tendonviField.setVisible(false);
        donvitrungtam.setEditable(false);
    }

}