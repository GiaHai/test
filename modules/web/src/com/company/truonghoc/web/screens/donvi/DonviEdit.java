package com.company.truonghoc.web.screens.donvi;

import com.company.truonghoc.entity.Donvi;
import com.haulmont.cuba.gui.screen.*;


@UiController("truonghoc_Donvi.edit")
@UiDescriptor("donvi-edit.xml")
@EditedEntityContainer("donviDc")
@LoadDataBeforeShow
public class DonviEdit extends StandardEditor<Donvi> {
}