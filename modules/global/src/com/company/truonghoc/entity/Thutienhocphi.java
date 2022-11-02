package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "TRUONGHOC_THUTIENHOCPHI")
@Entity(name = "truonghoc_Thutienhocphi")
@NamePattern("%s|tenkhachhang")
public class Thutienhocphi extends StandardEntity {
    private static final long serialVersionUID = 3866939199245638055L;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi donvi;

    @Column(name = "TENKHACHHANG")
    private String tenkhachhang;

    @Column(name = "DIACHI")
    private String diachi;

    @Column(name = "TUNGAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tungay;

    @Column(name = "DENNGAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date denngay;

    @Column(name = "NGAYTHANHTOAN")
    @Temporal(TemporalType.DATE)
    private Date ngaythanhtoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENHOCSINH_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Hocsinh tenhocsinh;

    @Column(name = "THANHTIEN")
    private Long thanhtien;

    @Column(name = "HINHTHUCTHANHTOAN")
    private String hinhthucthanhtoan;

    @Column(name = "TINHTRANGTHANHTOAN")
    private String tinhtrangthanhtoan;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "thutienhocphi")
    private List<Chitietthu> lkchitieuthu;

    public void setNgaythanhtoan(Date ngaythanhtoan) {
        this.ngaythanhtoan = ngaythanhtoan;
    }

    public Date getNgaythanhtoan() {
        return ngaythanhtoan;
    }

    public void setDonvi(Donvi donvi) {
        this.donvi = donvi;
    }

    public Donvi getDonvi() {
        return donvi;
    }

    public List<Chitietthu> getLkchitieuthu() {
        return lkchitieuthu;
    }

    public void setLkchitieuthu(List<Chitietthu> lkchitieuthu) {
        this.lkchitieuthu = lkchitieuthu;
    }

    public String getTinhtrangthanhtoan() {
        return tinhtrangthanhtoan;
    }

    public void setTinhtrangthanhtoan(String tinhtrangthanhtoan) {
        this.tinhtrangthanhtoan = tinhtrangthanhtoan;
    }

    public String getHinhthucthanhtoan() {
        return hinhthucthanhtoan;
    }

    public void setHinhthucthanhtoan(String hinhthucthanhtoan) {
        this.hinhthucthanhtoan = hinhthucthanhtoan;
    }

    public void setTenhocsinh(Hocsinh tenhocsinh) {
        this.tenhocsinh = tenhocsinh;
    }

    public Hocsinh getTenhocsinh() {
        return tenhocsinh;
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

    public void setThanhtien(Long thanhtien) {
        this.thanhtien = thanhtien;
    }

    public Long getThanhtien() {
        return thanhtien;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTenkhachhang() {
        return tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        this.tenkhachhang = tenkhachhang;
    }
}