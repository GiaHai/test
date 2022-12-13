package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "TRUONGHOC_LUONGTHANG")
@Entity(name = "truonghoc_Luongthang")
public class Luongthang extends StandardEntity {
    private static final long serialVersionUID = 4883554460814975042L;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi donvi;

    @JoinColumn(name = "HOVATEN_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "{msg://truonghoc_Luongthang.hovaten.validation.NotNull}")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Giaovien hovaten;

    @Temporal(TemporalType.DATE)
    @Column(name = "NGAYNHAN")
    private Date ngaynhan;

    @Column(name = "HANNHANLUONG")
    @Temporal(TemporalType.DATE)
    private Date hannhanluong;

    @Column(name = "LUONGCOBAN")
    private Double luongcoban;

    @Column(name = "TUNGAY")
    @Temporal(TemporalType.DATE)
    private Date tungay;

    @Column(name = "DENNGAY")
    @Temporal(TemporalType.DATE)
    private Date denngay;

    @Column(name = "BUOILAM")
    private Double buoilam;

    @Column(name = "CANGOAI")
    private Integer cangoai;

    @Column(name = "SO_CA_CN")
    private Integer soCaCn;

    @Column(name = "CACHUNHAT")
    private Integer cachunhat;

    @Column(name = "CASANG")
    private Integer casang;

    @Column(name = "THUCLINH")
    private Double thuclinh;

    @Column(name = "DAYTHEM")
    private Double daythem;

    @Column(name = "TROCAP")
    private Double trocap;

    @Column(name = "TRACHNHIEM")
    private Double trachnhiem;

    @Column(name = "CHUYENCAN")
    private Double chuyencan;

    @Column(name = "THUONG")
    private Double thuong;

    @Column(name = "TIEN_BH")
    private Integer tienBh;

    @Column(name = "TONGLINH")
    private Double tonglinh;

    @Column(name = "HINHTHUCTHANHTOAN")
    private String hinhthucthanhtoan;

    @Column(name = "TINHTRANGNHANLUONG")
    private String tinhtrangnhanluong;

    @Column(name = "GHICHU")
    private String ghichu;

    public void setSoCaCn(Integer soCaCn) {
        this.soCaCn = soCaCn;
    }

    public Integer getSoCaCn() {
        return soCaCn;
    }

    public void setTienBh(Integer tienBh) {
        this.tienBh = tienBh;
    }

    public Integer getTienBh() {
        return tienBh;
    }

    public void setTungay(Date tungay) {
        this.tungay = tungay;
    }

    public Date getTungay() {
        return tungay;
    }

    public void setDenngay(Date denngay) {
        this.denngay = denngay;
    }

    public Date getDenngay() {
        return denngay;
    }

    public void setCachunhat(Integer cachunhat) {
        this.cachunhat = cachunhat;
    }

    public Integer getCachunhat() {
        return cachunhat;
    }

    public void setDonvi(Donvi donvi) {
        this.donvi = donvi;
    }

    public Donvi getDonvi() {
        return donvi;
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

    public void setBuoilam(Double buoilam) {
        this.buoilam = buoilam;
    }

    public Double getBuoilam() {
        return buoilam;
    }

    public void setLuongcoban(Double luongcoban) {
        this.luongcoban = luongcoban;
    }

    public Double getLuongcoban() {
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

    public void setThuclinh(Double thuclinh) {
        this.thuclinh = thuclinh;
    }

    public Double getThuclinh() {
        return thuclinh;
    }

    public void setDaythem(Double daythem) {
        this.daythem = daythem;
    }

    public Double getDaythem() {
        return daythem;
    }

    public void setTrocap(Double trocap) {
        this.trocap = trocap;
    }

    public Double getTrocap() {
        return trocap;
    }

    public void setTrachnhiem(Double trachnhiem) {
        this.trachnhiem = trachnhiem;
    }

    public Double getTrachnhiem() {
        return trachnhiem;
    }

    public void setChuyencan(Double chuyencan) {
        this.chuyencan = chuyencan;
    }

    public Double getChuyencan() {
        return chuyencan;
    }

    public void setThuong(Double thuong) {
        this.thuong = thuong;
    }

    public Double getThuong() {
        return thuong;
    }

    public void setTonglinh(Double tonglinh) {
        this.tonglinh = tonglinh;
    }

    public Double getTonglinh() {
        return tonglinh;
    }

    public void setHovaten(Giaovien hovaten) {
        this.hovaten = hovaten;
    }

    public Giaovien getHovaten() {
        return hovaten;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }
}