package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DbView;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@DbView
@Entity(name = "truonghoc_UserExt")
@Extends(User.class)
public class UserExt extends User {
    private static final long serialVersionUID = -1565750359856009244L;


    @JoinColumn(name = "LOOCKUP_DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi loockup_donvi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GIAOVIEN_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Giaovien giaovien;

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

}