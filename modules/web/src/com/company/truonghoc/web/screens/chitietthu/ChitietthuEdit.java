package com.company.truonghoc.web.screens.chitietthu;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chitietthu;

@UiController("truonghoc_Chitietthu.edit")
@UiDescriptor("chitietthu-edit.xml")
@EditedEntityContainer("chitietthuDc")
@LoadDataBeforeShow
public class ChitietthuEdit extends StandardEditor<Chitietthu> {
}