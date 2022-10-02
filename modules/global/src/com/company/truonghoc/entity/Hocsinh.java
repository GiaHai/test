package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TRUONGHOC_HOCSINH")
@Entity(name = "truonghoc_Hocsinh")
@NamePattern("%s|tenhocsinh")
public class Hocsinh extends StandardEntity {
    private static final long serialVersionUID = 6396620010673039383L;

    @JoinColumn(name = "DONVITAO_HOCSINH_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi donvitao_hocsinh;

    @Column(name = "TENHOCSINH")
    private String tenhocsinh;

    @Column(name = "NGAYSINHHOCSINH")
    @Temporal(TemporalType.DATE)
    private Date ngaysinhhocsinh;

    @Column(name = "GIOITINHHOCSINH")
    private String gioitinhhocsinh;

    @Column(name = "QUEQUANHOCSINH")
    private String quequanhocsinh;

    @Column(name = "GHICHU")
    private String ghichu;

    public void setDonvitao_hocsinh(Donvi donvitao_hocsinh) {
        this.donvitao_hocsinh = donvitao_hocsinh;
    }

    public Donvi getDonvitao_hocsinh() {
        return donvitao_hocsinh;
    }

    public void setNgaysinhhocsinh(Date ngaysinhhocsinh) {
        this.ngaysinhhocsinh = ngaysinhhocsinh;
    }

    public Date getNgaysinhhocsinh() {
        return ngaysinhhocsinh;
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