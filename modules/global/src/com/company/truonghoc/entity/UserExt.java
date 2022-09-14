package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.global.DbView;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;

@DbView
@Entity(name = "truonghoc_UserExt")
@Extends(User.class)
public class UserExt extends User {
    private static final long serialVersionUID = -1565750359856009244L;


    @JoinColumn(name = "LOOCKUP_DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi loockup_donvi;

    @Column(name = "TENDONVI")
    private String tendonvi;

    @Column(name = "DONVITRUNGTAM")
    private Boolean donvitrungtam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GIAOVIEN_ID")
    private Giaovien giaovien;

    @Column(name = "TEXTGV")
    private String textgv;

    public String getTextgv() {
        return textgv;
    }

    public void setTextgv(String textgv) {
        this.textgv = textgv;
    }

    public Giaovien getGiaovien() {
        return giaovien;
    }

    public void setGiaovien(Giaovien giaovien) {
        this.giaovien = giaovien;
    }

    public void setLoockup_donvi(Donvi loockup_donvi) {
        this.loockup_donvi = loockup_donvi;
    }

    public Donvi getLoockup_donvi() {
        return loockup_donvi;
    }

    public String getTendonvi() {
        return tendonvi;
    }

    public void setTendonvi(String tendonvi) {
        this.tendonvi = tendonvi;
    }

    public void setDonvitrungtam(Boolean donvitrungtam) {
        this.donvitrungtam = donvitrungtam;
    }

    public Boolean getDonvitrungtam() {
        return donvitrungtam;
    }

}