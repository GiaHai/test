package com.company.truonghoc.web.screens.tenlop;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Tenlop;

@UiController("truonghoc_Tenlop.browse")
@UiDescriptor("tenlop-browse.xml")
@LookupComponent("tenlopsTable")
@LoadDataBeforeShow
public class TenlopBrowse extends StandardLookup<Tenlop> {
}