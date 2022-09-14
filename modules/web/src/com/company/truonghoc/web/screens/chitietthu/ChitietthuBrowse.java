package com.company.truonghoc.web.screens.chitietthu;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chitietthu;

@UiController("truonghoc_Chitietthu.browse")
@UiDescriptor("chitietthu-browse.xml")
@LookupComponent("chitietthusTable")
@LoadDataBeforeShow
public class ChitietthuBrowse extends StandardLookup<Chitietthu> {
}