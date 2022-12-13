package com.company.truonghoc.web.screens.luongthang;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.enums.BuoiLamEnum;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Luongthang;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

@UiController("truonghoc_Luongthang.edit")
@UiDescriptor("luongthang-edit.xml")
@EditedEntityContainer("luongthangDc")
@LoadDataBeforeShow
public class LuongthangEdit extends StandardEditor<Luongthang> {

    @Inject
    protected CollectionLoader<Giaovien> giaoviensDl;
    @Inject
    protected TextField<Double> luongcobanField;
    @Inject
    protected CollectionContainer<Giaovien> giaoviensDc;
    @Inject
    protected TextField<Double> thuclinhField;
    @Inject
    protected TextField<Double> buoilamField;
    @Inject
    protected TextField<Double> daythemField;
    @Inject
    protected TextField<Double> trocapField;
    @Inject
    protected TextField<Double> trachnhiemField;
    @Inject
    protected TextField<Double> chuyencanField;
    @Inject
    protected TextField<Double> thuongField;
    @Inject
    protected TextField<Double> tonglinhField;
    @Inject
    protected LookupField<Donvi> donViField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected DateField<Date> ngaynhanField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Giaovien> hovatenField;
    @Inject
    protected DateField<Date> hannhanluongField;
    @Inject
    protected TextField<String> tinhtrangnhanluongField;
    @Inject
    protected InstanceContainer<Luongthang> luongthangDc;
    @Inject
    protected TextField<Integer> cangoaiField;
    @Inject
    protected TextField<Integer> casangField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected Button searchBLamBtn;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected TextField<Integer> cachunhatField;
    @Inject
    protected TextField<Integer> soCaCnFiled;
    Double a = 0.00;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField<String> hinhthucthanhtoanField;
    @Inject
    protected LookupField<Integer> tienBhField;
    Integer caNgay;
    Integer caSang;
    Integer caChieu;
    Integer caChuNhat;
    Integer caNgoai1;
    Integer caNgoai2;
    protected Donvi donvi = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Tiền mặt", "Chuyển khoản");
        hinhthucthanhtoanField.setOptionsList(list);

        thuclinhField.setEditable(false);
        tonglinhField.setEditable(false);
        luongcobanField.setEditable(false);
        ngaynhanField.setValue(new Date());
        tinhtrangnhanluongField.setVisible(false);
        hinhthucthanhtoanField.setVisible(false);

        buoilamField.setEditable(false);
        casangField.setEditable(false);
        cangoaiField.setEnabled(false);
        cachunhatField.setEnabled(false);

        donvi = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }


    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (donvi != null) {

            donViField.setOptionsList(searchedService.loaddonvi());
            trachnhiemField.setValue(a);
            daythemField.setValue(a);
            trocapField.setValue(a);
            chuyencanField.setValue(a);
            thuongField.setValue(a);
            thuclinhField.setValue(a);
            if (!donvi.getDonvitrungtam()) {
                donViField.setValue(donvi);
                donViField.setEditable(false);
            }
        }
    }

    @Subscribe("hovatenField")
    protected void onHovatenFieldValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        if (hovatenField.getValue() != null) {
            luongcobanField.setValue(hovatenField.getValue().getLuongcoban().doubleValue());

            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("450.000 đ", 450000);
            map.put("1.350.000 đ", 1350000);

            tienBhField.setOptionsMap(map);
        } else {
            luongcobanField.clear();
        }
    }

    @Subscribe("buoilamField")
    protected void onBuoilamFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    private void tinhthuclinh() {
        if (luongcobanField.getValue() != null &&
                buoilamField.getValue() != 0) {
            BigDecimal luongMotNgayAbc = new BigDecimal(luongcobanField.getValue() / 26);
            BigDecimal luongMotNgay = BigDecimal.valueOf(luongMotNgayAbc.doubleValue() * buoilamField.getValue().doubleValue());
            thuclinhField.setValue((double) Math.round(luongMotNgay.doubleValue() * 1));
        }
        if (luongcobanField.getValue() != null && buoilamField.getValue() == 0 && cangoaiField.getValue() == 0 & casangField.getValue() == 0) {
            thuclinhField.setValue(luongcobanField.getValue());
        }
    }

    private void tinhtonglinh() {
        if (daythemField.getValue() == null) {
            daythemField.setValue(a);
        }
        if (trachnhiemField.getValue() == null) {
            trachnhiemField.setValue(a);
        }
        if (trocapField.getValue() == null) {
            trocapField.setValue(a);
        }
        if (chuyencanField.getValue() == null) {
            chuyencanField.setValue(a);
        }
        if (thuongField.getValue() == null) {
            thuongField.setValue(a);
        }
        if (tienBhField.getValue() == null) {
            tienBhField.setValue(a.hashCode());
        }
        if (thuclinhField.getValue() != null &&
                trachnhiemField.getValue() != null &&
                daythemField.getValue() != null &&
                trocapField.getValue() != null &&
                chuyencanField.getValue() != null &&
                thuongField.getValue() != null && cachunhatField.getValue() != null
        ) {
            tonglinhField.setValue((double) (Math.round(thuclinhField.getValue() + trocapField.getValue() + daythemField.getValue() + cachunhatField.getValue() +
                    trachnhiemField.getValue() + chuyencanField.getValue() + thuongField.getValue() - tienBhField.getValue()) * 1));
        }

    }

    @Subscribe("daythemField")
    protected void onDaythemFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    @Subscribe("trocapField")
    protected void onTrocapFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    @Subscribe("trachnhiemField")
    protected void onTrachnhiemFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    @Subscribe("chuyencanField")
    protected void onChuyencanFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    @Subscribe("tienBhField")
    protected void onTienBhFieldValueChange(HasValue.ValueChangeEvent<Integer> event) {
        tinhtonglinh();
    }

    @Subscribe("thuongField")
    protected void onThuongFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    @Subscribe("thuclinhField")
    protected void onThuclinhFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtonglinh();
    }

    @Subscribe("donViField")
    protected void onDonViFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (donViField.getValue() != null) {
            hovatenField.setOptionsList(searchedService.loadgiaovien(donViField.getValue()));
        } else {
            hovatenField.clear();
            luongcobanField.clear();
        }
    }

    @Subscribe("ngaynhanField")
    protected void onNgaynhanFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (ngaynhanField.getValue() == null) {
            hannhanluongField.setRequired(true);
            hannhanluongField.setVisible(true);
            tinhtrangnhanluongField.setValue("Chưa nhận lương");
            hinhthucthanhtoanField.setRequired(false);
            hinhthucthanhtoanField.setVisible(false);
        } else {
            hannhanluongField.setVisible(false);
            hannhanluongField.clear();
            tinhtrangnhanluongField.setValue("Đã nhận lương");
            hinhthucthanhtoanField.setRequired(true);
            hinhthucthanhtoanField.setVisible(true);
        }
    }

    @Subscribe("hannhanluongField")
    protected void onHannhanluongFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        hinhthucthanhtoanField.setVisible(false);
    }

    @Subscribe("tungayField")
    protected void onTungayFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (tungayField.getValue() != null) {
            Calendar denngaytru1 = Calendar.getInstance();
            denngaytru1.setTime(tungayField.getValue());
            denngaytru1.add(Calendar.MONTH, +1);
            denngaytru1.add(Calendar.DAY_OF_MONTH, -1);

            denngayField.setValue(denngaytru1.getTime());
        } else {
            denngayField.clear();
        }

    }

    @Subscribe("searchBLamBtn")
    protected void onSearchBLamBtnClick(Button.ClickEvent event) {
        tinhNgayCong();
        Calendar denngaytru1 = Calendar.getInstance();
        denngaytru1.setTime(denngayField.getValue());
        denngaytru1.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    private void tinhNgayCong() {
        caNgay = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.LAM_CA_NGAY.getId()).size();
        caSang = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_SANG.getId()).size();
        caChieu = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_CHIEU.getId()).size();
        caChuNhat = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_CHU_NHAT.getId()).size();
        caNgoai1 = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_CHIEU_5H_6H.getId()).size();
        caNgoai2 = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_CHIEU_6H_7H.getId()).size();

        buoilamField.setValue(caNgay + caSang * 0.5 + caChieu * 0.5);
        casangField.setValue(caSang);
        cachunhatField.setValue(caChuNhat * 100000);

        if (buoilamField.getValue() != null) {
            trocapField.setValue((double) (Math.round(300000 / 26.00000000 * buoilamField.getValue().doubleValue()) * 1));
            trachnhiemField.setValue((double) (Math.round(200000 / 26.00000000 * buoilamField.getValue().doubleValue()) * 1));
        }

        List<KeyValueEntity> caChieuDaythem1 = searchedService.caChieudaythem(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_CHIEU_5H_6H.getId());
        List<KeyValueEntity> caChieuDaythem2 = searchedService.caChieudaythem(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), BuoiLamEnum.CA_CHIEU_6H_7H.getId());
        Object cachieu2 = 0;
        Object cachieu1 = 0;
        for (KeyValueEntity item : caChieuDaythem2) {
            cachieu2 = item.getValue("tienBuoi");
        }
        for (KeyValueEntity item : caChieuDaythem1) {
            cachieu1 = item.getValue("tienBuoi");
        }
        if (cachieu1 == null) {
            cachieu1 = 0;
        }
        if (cachieu2 == null) {
            cachieu2 = 0;
        }
        daythemField.setValue((double) (cachieu1.hashCode() + cachieu2.hashCode()));
        cangoaiField.setValue(caNgoai1 + caNgoai2);
        tinhthuclinh();

        soCaCnFiled.setValue(caChuNhat);
    }


}