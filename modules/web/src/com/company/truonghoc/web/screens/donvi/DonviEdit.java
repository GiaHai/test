package com.company.truonghoc.web.screens.donvi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.ThuchiService;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import javax.validation.constraints.Null;

@UiController("truonghoc_Donvi.edit")
@UiDescriptor("donvi-edit.xml")
@EditedEntityContainer("donviDc")
@LoadDataBeforeShow
public class DonviEdit extends StandardEditor<Donvi> {
//    @Inject
//    protected CheckBox donvitrungtamField;
//    @Subscribe
//    protected void onBeforeShow(BeforeShowEvent event) {
//        donvitrungtamField.setValue(Boolean.valueOf("0"));
//    }
//
//    @Subscribe("donvitrungtamField")
//    protected void onDonvitrungtamFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
//
//    }
    
}