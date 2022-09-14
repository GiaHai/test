package com.company.truonghoc.web.screens.donvi;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Donvi;

import javax.inject.Inject;

@UiController("truonghoc_Donvi.browse")
@UiDescriptor("donvi-browse.xml")
@LookupComponent("donvisTable")
@LoadDataBeforeShow
public class DonviBrowse extends StandardLookup<Donvi> {
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = donvisDl.getContainer().getItemIndex(entity.getId()) + 1;
        }
        catch (Exception ex)
        {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }
}