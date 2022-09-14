package com.company.truonghoc.web.screens.diemdanh;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Diemdanh;

@UiController("truonghoc_Diemdanh.edit")
@UiDescriptor("diemdanh-edit.xml")
@EditedEntityContainer("diemdanhDc")
@LoadDataBeforeShow
public class DiemdanhEdit extends StandardEditor<Diemdanh> {
}