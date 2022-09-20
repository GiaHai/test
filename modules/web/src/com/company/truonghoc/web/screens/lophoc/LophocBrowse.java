package com.company.truonghoc.web.screens.lophoc;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Lophoc;

@UiController("truonghoc_Lophoc.browse")
@UiDescriptor("lophoc-browse.xml")
@LookupComponent("lophocsTable")
@LoadDataBeforeShow
public class LophocBrowse extends StandardLookup<Lophoc> {
}