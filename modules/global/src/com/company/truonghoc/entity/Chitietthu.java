package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TRUONGHOC_CHITIETTHU")
@Entity(name = "truonghoc_Chitietthu")
public class Chitietthu extends StandardEntity {
    private static final long serialVersionUID = -591671652700518729L;

    @Column(name = "TENPHI")
    private String tenphi;

    @Column(name = "SOLUONG")
    private Long soluong;

    @Column(name = "DONGIA")
    private Long dongia;

    @Column(name = "TONGGIA")
    private Long tonggia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "THUTIENHOCPHI_ID")
    private Thutienhocphi thutienhocphi;

    public Thutienhocphi getThutienhocphi() {
        return thutienhocphi;
    }

    public void setThutienhocphi(Thutienhocphi thutienhocphi) {
        this.thutienhocphi = thutienhocphi;
    }

    public Long getTonggia() {
        return tonggia;
    }

    public void setTonggia(Long tonggia) {
        this.tonggia = tonggia;
    }

    public Long getDongia() {
        return dongia;
    }

    public void setDongia(Long dongia) {
        this.dongia = dongia;
    }

    public Long getSoluong() {
        return soluong;
    }

    public void setSoluong(Long soluong) {
        this.soluong = soluong;
    }

    public String getTenphi() {
        return tenphi;
    }

    public void setTenphi(String tenphi) {
        this.tenphi = tenphi;
    }
}