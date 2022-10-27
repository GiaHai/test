package com.company.truonghoc.web.screens.tienich.tinhthanh;

import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.TinhThanh;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiController("truonghoc_TinhThanh.edit")
@UiDescriptor("tinh-thanh-edit.xml")
@EditedEntityContainer("tinhThanhDc")
@LoadDataBeforeShow
public class TinhThanhEdit extends StandardEditor<TinhThanh> {
    @Inject
    protected LookupField<String> tinh_ThanhPhoFiled;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Thành phố", "Tỉnh");
        tinh_ThanhPhoFiled.setOptionsList(list);
    }
}