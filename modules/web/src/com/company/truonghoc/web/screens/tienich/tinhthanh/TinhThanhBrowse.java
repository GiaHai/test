package com.company.truonghoc.web.screens.tienich.tinhthanh;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.TinhThanh;

import javax.inject.Inject;

@UiController("truonghoc_TinhThanh.browse")
@UiDescriptor("tinh-thanh-browse.xml")
@LookupComponent("tinhThanhsTable")
@LoadDataBeforeShow
public class TinhThanhBrowse extends StandardLookup<TinhThanh> {
    @Inject
    protected CollectionLoader<TinhThanh> tinhThanhsDl;
    @Inject
    protected UiComponents uiComponents;

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = tinhThanhsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}