package com.company.truonghoc.web.screens.thongbao;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.LookupField;
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
    @Inject
    private GroupTable thongBaoHsChuaDongTienTable;
    @Inject
    private LookupField<Donvi> donviField;
    @Inject
    private Notifications notifications;

    @Subscribe
    public void onInit(InitEvent event) {
        donvi = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        donviField.setOptionsList(searchedService.loaddonvi());
        if (!donvi.getDonvitrungtam()) {
            donviField.setValue(donvi);
            donviField.setEditable(false);
        }
    }

    @Subscribe("timkiemBtn")
    public void onTimkiemBtnClick(Button.ClickEvent event) {
        if (donviField.getValue() != null) {
            thongBaoHsChuaDongTienTable.setVisible(true);
            List<KeyValueEntity> result = thongBaoHsChuaDongTien();
            thongBaoHocSinhChuaDongTiensDc.setItems(result);
        } else {
            notifications.create()
                    .withCaption("Bạn chưa chọn Đơn vị")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
        }
    }

    @Subscribe("xoaBtn")
    public void onXoaBtnClick(Button.ClickEvent event) {
        if (donvi.getDonvitrungtam()){
            donviField.clear();
            thongBaoHsChuaDongTienTable.setVisible(false);
        }else {
            List<KeyValueEntity> result = thongBaoHsChuaDongTien();
            thongBaoHocSinhChuaDongTiensDc.setItems(result);
            thongBaoHsChuaDongTienTable.setVisible(true);
        }
        tungayField.clear();
        denngayField.clear();
    }

    private List<KeyValueEntity> thongBaoHsChuaDongTien() {
        List<KeyValueEntity> result = new ArrayList<>();
        List<Hocsinh> hocsinhList = searchedService.getthongBaoHsChuaDongTien(tungayField.getValue(), denngayField.getValue(), donviField.getValue());
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