package com.company.truonghoc.web.screens.chamconggv;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;

@UiController("truonghoc_Chamconggv.browse")
@UiDescriptor("chamconggv-browse.xml")
@LookupComponent("chamconggvsTable")
@LoadDataBeforeShow
public class ChamconggvBrowse extends StandardLookup<Chamconggv> {
}