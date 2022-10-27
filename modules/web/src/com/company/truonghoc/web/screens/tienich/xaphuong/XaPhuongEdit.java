package com.company.truonghoc.web.screens.tienich.xaphuong;

import com.company.truonghoc.entity.tienich.QuanHuyen;
import com.company.truonghoc.entity.tienich.TinhThanh;
import com.company.truonghoc.service.TienIchService;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.XaPhuong;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("truonghoc_XaPhuong.edit")
@UiDescriptor("xa-phuong-edit.xml")
@EditedEntityContainer("xaPhuongDc")
@LoadDataBeforeShow
public class XaPhuongEdit extends StandardEditor<XaPhuong> {
    @Inject
    protected LookupField<String> xaphuongLookup;
    @Inject
    protected LookupField<TinhThanh> tinhThanhField;
    @Inject
    protected TienIchService tienIchService;
    @Inject
    protected LookupField<QuanHuyen> quanHuyenField;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Phường", "Thị trấn", "Xã");
        xaphuongLookup.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        tinhThanhField.setOptionsList(tienIchService.loadTinhThanh());
    }

    @Subscribe("tinhThanhField")
    protected void onTinhThanhFieldValueChange(HasValue.ValueChangeEvent<TinhThanh> event) {
        quanHuyenField.setOptionsList(tienIchService.loadQuanHuyen(tinhThanhField.getValue()));
    }

    
}