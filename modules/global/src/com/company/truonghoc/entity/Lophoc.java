package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TRUONGHOC_LOPHOC")
@Entity(name = "truonghoc_Lophoc")
@NamePattern("%s|dshocsinh")
public class Lophoc extends StandardEntity {
    private static final long serialVersionUID = -7972786784091409232L;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi donvi;

    @JoinColumn(name = "GIAOVIENCN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien giaoviencn;

    @JoinColumn(name = "TENLOP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenlop tenlop;

    @JoinColumn(name = "DSHOCSINH_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Hocsinh dshocsinh;

    public void setDshocsinh(Hocsinh dshocsinh) {
        this.dshocsinh = dshocsinh;
    }

    public Hocsinh getDshocsinh() {
        return dshocsinh;
    }

    public void setTenlop(Tenlop tenlop) {
        this.tenlop = tenlop;
    }

    public Tenlop getTenlop() {
        return tenlop;
    }

    public void setDonvi(Donvi donvi) {
        this.donvi = donvi;
    }

    public Donvi getDonvi() {
        return donvi;
    }

    public void setGiaoviencn(Giaovien giaoviencn) {
        this.giaoviencn = giaoviencn;
    }

    public Giaovien getGiaoviencn() {
        return giaoviencn;
    }

}