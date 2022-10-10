package com.company.truonghoc.web.screens.tienich.namsinh;

import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.Namsinh;

@UiController("truonghoc_Namsinh.browse")
@UiDescriptor("namsinh-browse.xml")
@LookupComponent("namsinhsTable")
@LoadDataBeforeShow
public class NamsinhBrowse extends StandardLookup<Namsinh> {
}