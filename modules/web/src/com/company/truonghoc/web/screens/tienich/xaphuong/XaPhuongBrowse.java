package com.company.truonghoc.web.screens.tienich.xaphuong;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.XaPhuong;

import javax.inject.Inject;

@UiController("truonghoc_XaPhuong.browse")
@UiDescriptor("xa-phuong-browse.xml")
@LookupComponent("xaPhuongsTable")
@LoadDataBeforeShow
public class XaPhuongBrowse extends StandardLookup<XaPhuong> {

    @Inject
    protected CollectionLoader<XaPhuong> xaPhuongsDl;
    @Inject
    protected UiComponents uiComponents;

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = xaPhuongsDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}