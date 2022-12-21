package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TRUONGHOC_HOCPHI")
@Entity(name = "truonghoc_Hocphi")
public class Hocphi extends StandardEntity {
    private static final long serialVersionUID = 6350442847448829593L;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi donvi;

    @JoinColumn(name = "HOVATEN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Hocsinh hovaten;

    @Column(name = "GHICHU")
    private String ghichu;

    @Column(name = "SOTIENTAMTINH")
    private Long sotientamtinh;

    @Column(name = "SOTIENTHUTHEOHD")
    private Long sotienthutheohd;

    @Column(name = "NGAYDONG")
    @Temporal(TemporalType.DATE)
    private Date ngaydong;

    @Column(name = "HANDONG")
    @Temporal(TemporalType.DATE)
    private Date handong;

    @Column(name = "HINHTHUCTHANHTOAN")
    private String hinhthucthanhtoan;

    @Column(name = "TINHTRANGTHANHTOAN")
    private String tinhtrangthanhtoan;

    public void setDonvi(Donvi donvi) {
        this.donvi = donvi;
    }

    public Donvi getDonvi() {
        return donvi;
    }

    public String getTinhtrangthanhtoan() {
        return tinhtrangthanhtoan;
    }

    public void setTinhtrangthanhtoan(String tinhtrangthanhtoan) {
        this.tinhtrangthanhtoan = tinhtrangthanhtoan;
    }

    public void setHandong(Date handong) {
        this.handong = handong;
    }

    public Date getHandong() {
        return handong;
    }

    public void setHinhthucthanhtoan(String hinhthucthanhtoan) {
        this.hinhthucthanhtoan = hinhthucthanhtoan;
    }

    public String getHinhthucthanhtoan() {
        return hinhthucthanhtoan;
    }

    public void setNgaydong(Date ngaydong) {
        this.ngaydong = ngaydong;
    }

    public Date getNgaydong() {
        return ngaydong;
    }

    public void setSotientamtinh(Long sotientamtinh) {
        this.sotientamtinh = sotientamtinh;
    }

    public Long getSotientamtinh() {
        return sotientamtinh;
    }

    public void setSotienthutheohd(Long sotienthutheohd) {
        this.sotienthutheohd = sotienthutheohd;
    }

    public Long getSotienthutheohd() {
        return sotienthutheohd;
    }

    public void setHovaten(Hocsinh hovaten) {
        this.hovaten = hovaten;
    }

    public Hocsinh getHovaten() {
        return hovaten;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

}