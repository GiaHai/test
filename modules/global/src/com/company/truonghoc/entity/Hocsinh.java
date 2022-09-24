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

    @JoinColumn(name = "USERTAO_HOCSINH_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien usertao_hocsinh;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LOPHOC_ID")
    private Lophoc lophoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIEMDANH_ID")
    private Diemdanh diemdanh;

    public void setDonvitao_hocsinh(Donvi donvitao_hocsinh) {
        this.donvitao_hocsinh = donvitao_hocsinh;
    }

    public Donvi getDonvitao_hocsinh() {
        return donvitao_hocsinh;
    }

    public void setUsertao_hocsinh(Giaovien usertao_hocsinh) {
        this.usertao_hocsinh = usertao_hocsinh;
    }

    public Giaovien getUsertao_hocsinh() {
        return usertao_hocsinh;
    }

    public Diemdanh getDiemdanh() {
        return diemdanh;
    }

    public void setDiemdanh(Diemdanh diemdanh) {
        this.diemdanh = diemdanh;
    }

    public Lophoc getLophoc() {
        return lophoc;
    }

    public void setLophoc(Lophoc lophoc) {
        this.lophoc = lophoc;
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