package com.company.truonghoc.web.screens.thongbao;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UiController("truonghoc_ThongBaoHocSinhChuaDongTien")
@UiDescriptor("thong-bao-hoc-sinh-chua-dong-tien.xml")
public class ThongBaoHocSinhChuaDongTien extends Screen {
    protected Donvi donvi = null;
    @Inject
    private DulieuUserService dulieuUserService;
    @Inject
    private UserSession userSession;
    @Inject
    private SearchedService searchedService;
    @Inject
    private KeyValueCollectionContainer thongBaoHocSinhChuaDongTiensDc;
    @Inject
    private DateField<Date> tungayField;
    @Inject
    private DateField<Date> denngayField;

    @Subscribe
    public void onInit(InitEvent event) {
        donvi = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {


    }

    @Subscribe("timkiemBtn")
    public void onTimkiemBtnClick(Button.ClickEvent event) {
        List<KeyValueEntity> result = thongBaoHsChuaDongTien();
        thongBaoHocSinhChuaDongTiensDc.setItems(result);
    }

    private List<KeyValueEntity> thongBaoHsChuaDongTien() {
        List<KeyValueEntity> result = new ArrayList<>();
        List<Hocsinh> hocsinhList = searchedService.getthongBaoHsChuaDongTien(tungayField.getValue(), denngayField.getValue());
        int stt = 1;
        for (Hocsinh hocsinh : hocsinhList) {
            KeyValueEntity entity = new KeyValueEntity();
            entity.setValue("stt", stt);
            entity.setValue("hoVaTen", hocsinh.getTenhocsinh());

            result.add(entity);
            stt++;
        }
        return result;
    }

}