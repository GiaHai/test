package com.company.truonghoc.web.screens.tienich.quanhuyen;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.QuanHuyen;

import javax.inject.Inject;

@UiController("truonghoc_QuanHuyen.browse")
@UiDescriptor("quan-huyen-browse.xml")
@LookupComponent("quanHuyensTable")
@LoadDataBeforeShow
public class QuanHuyenBrowse extends StandardLookup<QuanHuyen> {
    @Inject
    protected CollectionLoader<QuanHuyen> quanHuyensDl;
    @Inject
    protected UiComponents uiComponents;

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = quanHuyensDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}