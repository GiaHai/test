package com.company.truonghoc.web.screens.luongthang;

import com.company.truonghoc.entity.Chamconggv;
import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
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
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Month;
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
    protected TextField<Long> luongcobanField;
    @Inject
    protected CollectionContainer<Giaovien> giaoviensDc;
    @Inject
    protected TextField<Long> thuclinhField;
    @Inject
    protected TextField<BigDecimal> buoilamField;
    @Inject
    protected TextField<Long> daythemField;
    @Inject
    protected TextField<Long> trocapField;
    @Inject
    protected TextField<Long> trachnhiemField;
    @Inject
    protected TextField<Long> chuyencanField;
    @Inject
    protected TextField<Long> thuongField;
    @Inject
    protected TextField<Long> tonglinhField;
    @Inject
    protected Notifications notifications;
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
    Long a = Long.valueOf(0);
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
    }


    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {

            donViField.setOptionsList(searchedService.loaddonvi());
            trachnhiemField.setValue(a);
            daythemField.setValue(a);
            trocapField.setValue(a);
            chuyencanField.setValue(a);
            thuongField.setValue(a);
            thuclinhField.setValue(a);
            if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
                donViField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                donViField.setEditable(false);
            }
        }
        tinhNgayCong();
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        tinhthuclinh();
    }

    @Subscribe("hovatenField")
    protected void onHovatenFieldValueChange(HasValue.ValueChangeEvent<Giaovien> event) {
        if (hovatenField.getValue() != null) {
            luongcobanField.setValue(hovatenField.getValue().getLuongcoban());

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
        if (cangoaiField.getValue() == null) {
            cangoaiField.setValue(0);
        }
        if (luongcobanField.getValue() != null &&
                buoilamField.getValue() != null) {
            BigDecimal b = new BigDecimal(luongcobanField.getValue() / 26.00000000);
            BigDecimal c = b.setScale(10, BigDecimal.ROUND_HALF_EVEN);
            thuclinhField.setValue((long) (c.doubleValue() * buoilamField.getValue().doubleValue()) + cangoaiField.getValue() + cachunhatField.getValue());

        } else {
            if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
                thuclinhField.setValue(luongcobanField.getValue());
            }
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
                thuongField.getValue() != null
        ) {
            tonglinhField.setValue(thuclinhField.getValue() + daythemField.getValue() + trocapField.getValue() +
                    trachnhiemField.getValue() + chuyencanField.getValue() + thuongField.getValue() - tienBhField.getValue().longValue());
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

    @Subscribe("luongcobanField")
    protected void onLuongcobanFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        thuclinhField.setValue(luongcobanField.getValue());
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
        Date date = tungayField.getValue();
        denngayField.setValue(DateUtils.addMonths(date, +1));
    }


    public void timkiem() {
//        excuteSearch(true);

    }

    @Subscribe("searchBLamBtn")
    protected void onSearchBLamBtnClick(Button.ClickEvent event) {
        tinhNgayCong();
    }

    private void tinhNgayCong(){
        caNgay = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), "Làm cả ngày").size();
        caSang = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), "Ca sáng").size();
        caChieu = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), "Ca chiều").size();
        caChuNhat = searchedService.tinhca(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), "Ca chủ nhật").size();

        buoilamField.setValue(BigDecimal.valueOf(caNgay + caSang * 0.5 + caChieu * 0.5));
        casangField.setValue(caSang);
        cachunhatField.setValue(caChuNhat * 100000);

        List<KeyValueEntity> caChieuDaythem1 = searchedService.caChieudaythem(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), "Ca chiều 5h-6h");
        List<KeyValueEntity> caChieuDaythem2 = searchedService.caChieudaythem(donViField.getValue(), hovatenField.getValue(), tungayField.getValue(), denngayField.getValue(), "Ca chiều 6h-7h");
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
        cangoaiField.setValue(cachieu1.hashCode() + cachieu2.hashCode());
        tinhthuclinh();

        soCaCnFiled.setValue(caChuNhat);
    }

//    private List<Chamconggv> cangay() {
//
//        return dataManager.load(Chamconggv.class)
//                .query("select e from truonghoc_Chamconggv e where" +
//                        " e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = 'Làm cả ngày'" +
//                        " and e.ngaylam >= :tungay and :denngay >= e.ngaylam")
//                .parameter("donvi", donViField.getValue())
//                .parameter("giaovien", hovatenField.getValue())
//                .parameter("tungay", tungayField.getValue())
//                .parameter("denngay", denngayField.getValue())
//                .list();
//    }
//
//    private List<Chamconggv> casang() {
//
//        return dataManager.load(Chamconggv.class)
//                .query("select e from truonghoc_Chamconggv e where" +
//                        " e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = 'Ca sáng'" +
//                        " and e.ngaylam >= :tungay and :denngay >= e.ngaylam")
//                .parameter("donvi", donViField.getValue())
//                .parameter("giaovien", hovatenField.getValue())
//                .parameter("tungay", tungayField.getValue())
//                .parameter("denngay", denngayField.getValue())
//                .list();
//    }
//
//    private List<Chamconggv> cachieu() {
//        return dataManager.load(Chamconggv.class)
//                .query("select e from truonghoc_Chamconggv e where" +
//                        " e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = 'Ca chiều'" +
//                        " and e.ngaylam >= :tungay and :denngay >= e.ngaylam")
//                .parameter("donvi", donViField.getValue())
//                .parameter("giaovien", hovatenField.getValue())
//                .parameter("tungay", tungayField.getValue())
//                .parameter("denngay", denngayField.getValue())
//                .list();
//    }
//
//    private List<Chamconggv> cachunhat() {
//        return dataManager.load(Chamconggv.class)
//                .query("select e from truonghoc_Chamconggv e where" +
//                        " e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = 'Ca chủ nhật'" +
//                        " and e.ngaylam >= :tungay and :denngay >= e.ngaylam")
//                .parameter("donvi", donViField.getValue())
//                .parameter("giaovien", hovatenField.getValue())
//                .parameter("tungay", tungayField.getValue())
//                .parameter("denngay", denngayField.getValue())
//                .list();
//    }

//    private List<KeyValueEntity> cachieu1() {
//        String queryStr = "select sum(e.tienBuoi) tienBuoi from truonghoc_Chamconggv e where" +
//                " e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = 'Ca chiều 5h-6h'" +
//                " and e.ngaylam >= :tungay and :denngay >= e.ngaylam";
//
//        return dataManager.loadValues(queryStr)
//                .properties("tienBuoi")
//                .parameter("donvi", donViField.getValue())
//                .parameter("giaovien", hovatenField.getValue())
//                .parameter("tungay", tungayField.getValue())
//                .parameter("denngay", denngayField.getValue())
//                .list();
//    }
//
//    private List<KeyValueEntity> cachieu2() {
//        String queryStr = "select sum(e.tienBuoi) tienBuoi from truonghoc_Chamconggv e where" +
//                " e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = 'Ca chiều 6h-7h'" +
//                " and e.ngaylam >= :tungay and :denngay >= e.ngaylam";
//
//        return dataManager.loadValues(queryStr)
//                .properties("tienBuoi")
//                .parameter("donvi", donViField.getValue())
//                .parameter("giaovien", hovatenField.getValue())
//                .parameter("tungay", tungayField.getValue())
//                .parameter("denngay", denngayField.getValue())
//                .list();
//    }

}