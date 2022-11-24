package com.company.truonghoc.web.screens.donvi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.utils.StringUtility;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Scanner;


@UiController("truonghoc_Donvi.edit")
@UiDescriptor("donvi-edit.xml")
@EditedEntityContainer("donviDc")
@LoadDataBeforeShow
public class DonviEdit extends StandardEditor<Donvi> {

}