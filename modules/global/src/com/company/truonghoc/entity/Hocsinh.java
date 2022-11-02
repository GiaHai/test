package com.company.truonghoc.entity;

import com.company.truonghoc.entity.tienich.Namsinh;
import com.company.truonghoc.entity.tienich.QuanHuyen;
import com.company.truonghoc.entity.tienich.TinhThanh;
import com.company.truonghoc.entity.tienich.XaPhuong;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "TRUONGHOC_HOCSINH")
@Entity(name = "truonghoc_Hocsinh")
@NamePattern("%s|tenhocsinh")
public class Hocsinh extends StandardEntity {
    private static final long serialVersionUID = 6396620010673039383L;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi donvi;

    @Column(name = "TENHOCSINH")
    private String tenhocsinh;

    @JoinColumn(name = "NGAYSINHHOCSINH_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Namsinh ngaysinhhocsinh;

    @Column(name = "GIOITINHHOCSINH")
    private String gioitinhhocsinh;

    @Column(name = "QUEQUANHOCSINH")
    private String quequanhocsinh;

    @JoinColumn(name = "NOI_SINH_XA_PHUONG_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private XaPhuong noiSinh_XaPhuong;

    @JoinColumn(name = "NOI_SINH_QUAN_HUYEN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private QuanHuyen noiSinh_QuanHuyen;

    @JoinColumn(name = "NOI_SINH_TINH_THANH_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TinhThanh noiSinh_TinhThanh;

    @Column(name = "GHICHU")
    private String ghichu;

    public void setNoiSinh_TinhThanh(TinhThanh noiSinh_TinhThanh) {
        this.noiSinh_TinhThanh = noiSinh_TinhThanh;
    }

    public TinhThanh getNoiSinh_TinhThanh() {
        return noiSinh_TinhThanh;
    }

    public void setNoiSinh_QuanHuyen(QuanHuyen noiSinh_QuanHuyen) {
        this.noiSinh_QuanHuyen = noiSinh_QuanHuyen;
    }

    public QuanHuyen getNoiSinh_QuanHuyen() {
        return noiSinh_QuanHuyen;
    }

    public void setNoiSinh_XaPhuong(XaPhuong noiSinh_XaPhuong) {
        this.noiSinh_XaPhuong = noiSinh_XaPhuong;
    }

    public XaPhuong getNoiSinh_XaPhuong() {
        return noiSinh_XaPhuong;
    }

    public void setNgaysinhhocsinh(Namsinh ngaysinhhocsinh) {
        this.ngaysinhhocsinh = ngaysinhhocsinh;
    }

    public Namsinh getNgaysinhhocsinh() {
        return ngaysinhhocsinh;
    }

    public void setDonvi(Donvi donvi) {
        this.donvi = donvi;
    }

    public Donvi getDonvi() {
        return donvi;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getQuequanhocsinh() {
        return quequanhocsinh;
    }

    public void setQuequanhocsinh(String quequanhocsinh) {
        this.quequanhocsinh = quequanhocsinh;
    }

    public String getGioitinhhocsinh() {
        return gioitinhhocsinh;
    }

    public void setGioitinhhocsinh(String gioitinhhocsinh) {
        this.gioitinhhocsinh = gioitinhhocsinh;
    }

    public String getTenhocsinh() {
        return tenhocsinh;
    }

    public void setTenhocsinh(String tenhocsinh) {
        this.tenhocsinh = tenhocsinh;
    }
}