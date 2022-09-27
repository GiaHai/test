package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "TRUONGHOC_TENLOP")
@Entity(name = "truonghoc_Tenlop")
@NamePattern("%s|tenlop")
public class Tenlop extends StandardEntity {
    private static final long serialVersionUID = 4124330793891358741L;

    @JoinColumn(name = "DOVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi dovi;

    @Column(name = "TENLOP")
    private String tenlop;

    @JoinColumn(name = "GIAOVIENCN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien giaoviencn;

    @Column(name = "THANGHOC")
    private LocalDate thanghoc;

    @Column(name = "TINHTRANGLOP")
    private Boolean tinhtranglop;

    public void setThanghoc(LocalDate thanghoc) {
        this.thanghoc = thanghoc;
    }

    public LocalDate getThanghoc() {
        return thanghoc;
    }

    public Boolean getTinhtranglop() {
        return tinhtranglop;
    }

    public void setTinhtranglop(Boolean tinhtranglop) {
        this.tinhtranglop = tinhtranglop;
    }

    public void setDovi(Donvi dovi) {
        this.dovi = dovi;
    }

    public Donvi getDovi() {
        return dovi;
    }

    public void setGiaoviencn(Giaovien giaoviencn) {
        this.giaoviencn = giaoviencn;
    }

    public Giaovien getGiaoviencn() {
        return giaoviencn;
    }

    public String getTenlop() {
        return tenlop;
    }

    public void setTenlop(String tenlop) {
        this.tenlop = tenlop;
    }
}