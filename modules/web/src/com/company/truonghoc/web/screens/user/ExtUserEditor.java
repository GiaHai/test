package com.company.truonghoc.web.screens.user;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.screen.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;
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
    @Inject
    protected DulieuUserService dulieuUserService;
    @Named("fieldGroupLeft.name")
    protected TextField<String> nameField;
    @Named("fieldGroupLeft.lastName")
    protected TextField<String> lastNameField;
    @Named("fieldGroupLeft.firstName")
    protected TextField<String> firstNameField;
    @Named("fieldGroupLeft.middleName")
    protected TextField<String> middleNameField;
    @Named("fieldGroupLeft.login")
    protected TextField<String> loginField;


    @Subscribe
    protected void onInit(InitEvent event) {
//        loockuptendonvi.setRequired(true);
        nameField.setRequired(true);
        lastNameField.setVisible(false);
        firstNameField.setVisible(false);
        middleNameField.setVisible(false);
        loginField.setCaption("Tài khoản người dùng");
        nameField.setCaption("Họ và tên người dùng");
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            loockuptendonvi.setEditable(false);
            loockuptendonvi.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
        }
    }

    @Subscribe("loockuptendonvi")
    protected void onLoockuptendonviValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (loockuptendonvi.getValue() != null) {
            tengiaovienField.setOptionsList(tengiaovien(loockuptendonvi.getValue()));
            if (!loockuptendonvi.getValue().getDonvitrungtam()) {
                tengiaovienField.setVisible(true);
            } else {
                tengiaovienField.setVisible(false);
                tengiaovienField.clear();
            }
        } else {
            tengiaovienField.clear();
        }
    }

    @Subscribe("tengiaovienField")
    protected void onTengiaovienValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        if (tengiaovienField.getValue() != null) {
            nameField.setValue(tengiaovienField.getValue().getTengiaovien());
        } else {
            nameField.clear();
        }
    }

    private List<Giaovien> tengiaovien(Object dvgiaovien) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :dvgiaovien")
                .parameter("dvgiaovien", dvgiaovien)
                .list();
    }
}