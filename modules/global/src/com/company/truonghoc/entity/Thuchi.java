package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TRUONGHOC_THUCHI")
@Entity(name = "truonghoc_Thuchi")
public class Thuchi extends StandardEntity {
    private static final long serialVersionUID = 90251412791812808L;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi donvi;

    @Column(name = "KHOANCHI")
    @NotNull(message = "{msg://truonghoc_Thuchi.khoanchi.validation.NotNull}")
    private String khoanchi;

    @Temporal(TemporalType.DATE)
    @Column(name = "NGAYCHI")
    private Date ngaychi;

    @Column(name = "HANCHI")
    @Temporal(TemporalType.DATE)
    private Date hanchi;

    @Column(name = "SOLUONG")
    private Integer soluong;

    @Column(name = "DONGIA")
    private Long dongia;

    @Column(name = "THANHTIEN")
    private Long thanhtien;

    @Column(name = "GHICHU")
    private String ghichu;

    @Column(name = "HINHTHUCTHANHTOAN")
    private String hinhthucthanhtoan;

    @Column(name = "TINHTRANGCHI")
    private String tinhtrangchi;

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

    public String getTinhtrangchi() {
        return tinhtrangchi;
    }

    public void setTinhtrangchi(String tinhtrangchi) {
        this.tinhtrangchi = tinhtrangchi;
    }

    public void setHanchi(Date hanchi) {
        this.hanchi = hanchi;
    }

    public Date getHanchi() {
        return hanchi;
    }

    public Date getNgaychi() {
        return ngaychi;
    }

    public void setNgaychi(Date ngaychi) {
        this.ngaychi = ngaychi;
    }

    public void setDongia(Long dongia) {
        this.dongia = dongia;
    }

    public Long getDongia() {
        return dongia;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public Long getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Long thanhtien) {
        this.thanhtien = thanhtien;
    }

    public Integer getSoluong() {
        return soluong;
    }

    public void setSoluong(Integer soluong) {
        this.soluong = soluong;
    }

    public String getKhoanchi() {
        return khoanchi;
    }

    public void setKhoanchi(String khoanchi) {
        this.khoanchi = khoanchi;
    }

}