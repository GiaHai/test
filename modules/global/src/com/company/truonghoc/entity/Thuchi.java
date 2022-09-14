package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TRUONGHOC_THUCHI")
@Entity(name = "truonghoc_Thuchi")
public class Thuchi extends StandardEntity {
    private static final long serialVersionUID = 90251412791812808L;

    @Column(name = "USERTAO_THUCHI")
    private String usertao_thuchi;

    @Column(name = "DONVITAO_THUCHI")
    private String donvitao_thuchi;

    @Column(name = "KHOANCHI")
    @NotNull(message = "{msg://truonghoc_Thuchi.khoanchi.validation.NotNull}")
    private String khoanchi;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NGAYCHI")
    private Date ngaychi;

    @Column(name = "HANCHI")
    @Temporal(TemporalType.TIMESTAMP)
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

    public String getDonvitao_thuchi() {
        return donvitao_thuchi;
    }

    public void setDonvitao_thuchi(String donvitao_thuchi) {
        this.donvitao_thuchi = donvitao_thuchi;
    }

    public String getUsertao_thuchi() {
        return usertao_thuchi;
    }

    public void setUsertao_thuchi(String usertao_thuchi) {
        this.usertao_thuchi = usertao_thuchi;
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