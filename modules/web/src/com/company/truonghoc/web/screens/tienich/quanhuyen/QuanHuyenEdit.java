package com.company.truonghoc.web.screens.tienich.quanhuyen;

import com.company.truonghoc.entity.tienich.TinhThanh;
import com.company.truonghoc.service.TienIchService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.QuanHuyen;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("truonghoc_QuanHuyen.edit")
@UiDescriptor("quan-huyen-edit.xml")
@EditedEntityContainer("quanHuyenDc")
@LoadDataBeforeShow
public class QuanHuyenEdit extends StandardEditor<QuanHuyen> {
    @Inject
    protected LookupField<String> quan_huyenField;
    @Inject
    protected LookupField<TinhThanh> tinhThanhField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TienIchService tienIchService;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Thành phố", "Quận", "Thị xã", "Huyện");
        quan_huyenField.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        tinhThanhField.setOptionsList(tienIchService.loadTinhThanh());
    }

}