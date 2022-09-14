package com.company.truonghoc.web.screens.diemdanh;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Diemdanh;

@UiController("truonghoc_Diemdanh.browse")
@UiDescriptor("diemdanh-browse.xml")
@LookupComponent("diemdanhsTable")
@LoadDataBeforeShow
public class DiemdanhBrowse extends StandardLookup<Diemdanh> {
}