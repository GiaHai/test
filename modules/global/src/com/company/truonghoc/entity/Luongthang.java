package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "TRUONGHOC_LUONGTHANG")
@Entity(name = "truonghoc_Luongthang")
public class Luongthang extends StandardEntity {
    private static final long serialVersionUID = 4883554460814975042L;

    @JoinColumn(name = "USERTAO_LUONGTHANG_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User usertao_luongthang;

    @JoinColumn(name = "DONVITAO_LUONGTHANG_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi donvitao_luongthang;

    @JoinColumn(name = "HOVATEN_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "{msg://truonghoc_Luongthang.hovaten.validation.NotNull}")
    private Giaovien hovaten;

    @Temporal(TemporalType.DATE)
    @Column(name = "NGAYNHAN")
    private Date ngaynhan;

    @Column(name = "HANNHANLUONG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hannhanluong;

    @Column(name = "LUONGCOBAN")
    private Long luongcoban;

    @Column(name = "BUOILAM")
    private BigDecimal buoilam;

    @Column(name = "CANGOAI")
    private Integer cangoai;

    @Column(name = "CASANG")
    private Integer casang;

    @Column(name = "THUCLINH")
    private Long thuclinh;

    @Column(name = "DAYTHEM")
    private Long daythem;

    @Column(name = "TROCAP")
    private Long trocap;

    @Column(name = "TRACHNHIEM")
    private Long trachnhiem;

    @Column(name = "CHUYENCAN")
    private Long chuyencan;

    @Column(name = "THUONG")
    private Long thuong;

    @Column(name = "TONGLINH")
    private Long tonglinh;

    @Column(name = "HINHTHUCTHANHTOAN")
    private String hinhthucthanhtoan;

    @Column(name = "TINHTRANGNHANLUONG")
    private String tinhtrangnhanluong;

    public void setUsertao_luongthang(User usertao_luongthang) {
        this.usertao_luongthang = usertao_luongthang;
    }

    public User getUsertao_luongthang() {
        return usertao_luongthang;
    }

    public void setDonvitao_luongthang(Donvi donvitao_luongthang) {
        this.donvitao_luongthang = donvitao_luongthang;
    }

    public Donvi getDonvitao_luongthang() {
        return donvitao_luongthang;
    }

    public String getHinhthucthanhtoan() {
        return hinhthucthanhtoan;
    }

    public void setHinhthucthanhtoan(String hinhthucthanhtoan) {
        this.hinhthucthanhtoan = hinhthucthanhtoan;
    }

    public void setHannhanluong(Date hannhanluong) {
        this.hannhanluong = hannhanluong;
    }

    public Date getHannhanluong() {
        return hannhanluong;
    }

    public String getTinhtrangnhanluong() {
        return tinhtrangnhanluong;
    }

    public void setTinhtrangnhanluong(String tinhtrangnhanluong) {
        this.tinhtrangnhanluong = tinhtrangnhanluong;
    }

    public Date getNgaynhan() {
        return ngaynhan;
    }

    public void setNgaynhan(Date ngaynhan) {
        this.ngaynhan = ngaynhan;
    }

    public void setBuoilam(BigDecimal buoilam) {
        this.buoilam = buoilam;
    }

    public BigDecimal getBuoilam() {
        return buoilam;
    }

    public void setLuongcoban(Long luongcoban) {
        this.luongcoban = luongcoban;
    }

    public Long getLuongcoban() {
        return luongcoban;
    }

    public void setCangoai(Integer cangoai) {
        this.cangoai = cangoai;
    }

    public Integer getCangoai() {
        return cangoai;
    }

    public void setCasang(Integer casang) {
        this.casang = casang;
    }

    public Integer getCasang() {
        return casang;
    }

    public void setThuclinh(Long thuclinh) {
        this.thuclinh = thuclinh;
    }

    public Long getThuclinh() {
        return thuclinh;
    }

    public void setDaythem(Long daythem) {
        this.daythem = daythem;
    }

    public Long getDaythem() {
        return daythem;
    }

    public void setTrocap(Long trocap) {
        this.trocap = trocap;
    }

    public Long getTrocap() {
        return trocap;
    }

    public void setTrachnhiem(Long trachnhiem) {
        this.trachnhiem = trachnhiem;
    }

    public Long getTrachnhiem() {
        return trachnhiem;
    }

    public void setChuyencan(Long chuyencan) {
        this.chuyencan = chuyencan;
    }

    public Long getChuyencan() {
        return chuyencan;
    }

    public void setThuong(Long thuong) {
        this.thuong = thuong;
    }

    public Long getThuong() {
        return thuong;
    }

    public void setTonglinh(Long tonglinh) {
        this.tonglinh = tonglinh;
    }

    public Long getTonglinh() {
        return tonglinh;
    }

    public void setHovaten(Giaovien hovaten) {
        this.hovaten = hovaten;
    }

    public Giaovien getHovaten() {
        return hovaten;
    }

}