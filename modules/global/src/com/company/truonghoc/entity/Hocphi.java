package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TRUONGHOC_HOCPHI")
@Entity(name = "truonghoc_Hocphi")
public class Hocphi extends StandardEntity {
    private static final long serialVersionUID = 6350442847448829593L;

    @JoinColumn(name = "USERTAO_HOCPHI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien usertao_hocphi;

    @JoinColumn(name = "DOVITAO_HOCPHI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi dovitao_hocphi;

    @JoinColumn(name = "HOVATEN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "{msg://truonghoc_Hocphi.hovaten.validation.NotNull}")
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date handong;

    @Column(name = "HINHTHUCTHANHTOAN")
    private String hinhthucthanhtoan;

    @Column(name = "TINHTRANGTHANHTOAN")
    private String tinhtrangthanhtoan;

    public void setDovitao_hocphi(Donvi dovitao_hocphi) {
        this.dovitao_hocphi = dovitao_hocphi;
    }

    public Donvi getDovitao_hocphi() {
        return dovitao_hocphi;
    }

    public void setUsertao_hocphi(Giaovien usertao_hocphi) {
        this.usertao_hocphi = usertao_hocphi;
    }

    public Giaovien getUsertao_hocphi() {
        return usertao_hocphi;
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