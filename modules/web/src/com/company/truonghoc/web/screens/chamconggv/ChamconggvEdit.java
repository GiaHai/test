package com.company.truonghoc.web.screens.chamconggv;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;

@UiController("truonghoc_Chamconggv.edit")
@UiDescriptor("chamconggv-edit.xml")
@EditedEntityContainer("chamconggvDc")
@LoadDataBeforeShow
public class ChamconggvEdit extends StandardEditor<Chamconggv> {
}