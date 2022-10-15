package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.tienich.QuanHuyen;
import com.company.truonghoc.entity.tienich.TinhThanh;
import com.company.truonghoc.entity.tienich.XaPhuong;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.TienIchService;
import com.company.truonghoc.utils.StringUtility;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocsinh;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiController("truonghoc_Hocsinh.edit")
@UiDescriptor("hocsinh-edit.xml")
@EditedEntityContainer("hocsinhDc")
@LoadDataBeforeShow
public class HocsinhEdit extends StandardEditor<Hocsinh> {
    @Inject
    protected LookupField<String> gioitinhhocsinhField;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected InstanceContainer<Hocsinh> hocsinhDc;
    @Inject
    protected LookupField<Donvi> donvitao_hocsinhField;
    @Inject
    protected TextField<String> tenhocsinhField;

    @Inject
    protected LookupField<XaPhuong> noiSinh_XaPhuongField;
    @Inject
    protected LookupField<TinhThanh> noiSinh_TinhThanhField;
    @Inject
    protected LookupField<QuanHuyen> noiSinh_QuanHuyenField;
    @Inject
    protected TienIchService tienIchService;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Nam", "Nữ");
        gioitinhhocsinhField.setOptionsList(list);
        donvitao_hocsinhField.setRequired(true);
        donvitao_hocsinhField.setRequiredMessage("Nhập đơn vị");
        tenhocsinhField.setRequired(true);
        tenhocsinhField.setRequiredMessage("Nhập tên học sinh");


        xaPhuongList = tienIchService.getFullDmXaPhuong();
        initTinhThanhQuanHuyenXaPhuong();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donvitao_hocsinhField.setEditable(false);
            donvitao_hocsinhField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
        }
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        initEventOnChangeXaHuyenTinh(noiSinh_XaPhuongField, noiSinh_QuanHuyenField, noiSinh_TinhThanhField);
    }

    protected List<XaPhuong> xaPhuongList = new ArrayList<>();

    private void initTinhThanhQuanHuyenXaPhuong() {
        noiSinh_XaPhuongField.setOptionsList(xaPhuongList);
    }
    private void initEventOnChangeXaHuyenTinh(LookupField<XaPhuong> xaPhuongField, LookupField<QuanHuyen> quanHuyenField, LookupField<TinhThanh> tinhThanhField) {
        xaPhuongField.addValueChangeListener( xaPhuongValueChangeEvent -> {
            if (xaPhuongValueChangeEvent.getValue() != null){
                XaPhuong dbXaPhuong  = tienIchService.findXaPhuongById(xaPhuongValueChangeEvent.getValue().getId(), "xaPhuong-view", false);
                if (dbXaPhuong == null)
                    return;
                String tinhId = dbXaPhuong.getTinhThanh().getId().toString();
                String huyenId = dbXaPhuong.getQuanHuyen().getId().toString();

                if (quanHuyenField.getValue() != null){
                    String quanHuyenId = quanHuyenField.getValue().getId().toString();
                    if (!quanHuyenId.equals(huyenId))
                        quanHuyenField.setValue(dbXaPhuong.getQuanHuyen());
                }else {
                    quanHuyenField.setValue(dbXaPhuong.getQuanHuyen());
                }
                if (tinhThanhField.getValue() != null) {
                    String tinhThanhId = tinhThanhField.getValue().getId().toString();
                    if (!tinhThanhId.equals(tinhId))
                        tinhThanhField.setValue(dbXaPhuong.getTinhThanh());
                } else {
                    tinhThanhField.setValue(dbXaPhuong.getTinhThanh());
                }
            }
        });
    }
    protected Boolean afterShow = false;

    @Subscribe("quequanhocsinhField")
    protected void onQuequanhocsinhFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (!org.apache.commons.lang3.StringUtils.isEmpty(event.getValue()) && afterShow)
            getEditedEntity().setQuequanhocsinh(StringUtility.capitalizeString(event.getValue()));

    }

}