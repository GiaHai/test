package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TRUONGHOC_TENLOP")
@Entity(name = "truonghoc_Tenlop")
public class Tenlop extends StandardEntity {
    private static final long serialVersionUID = 4124330793891358741L;

    @Column(name = "DOVI")
    private String dovi;

    @Column(name = "TENLOP")
    private String tenlop;

    @JoinColumn(name = "GIAOVIENCN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien giaoviencn;

    public void setDovi(String dovi) {
        this.dovi = dovi;
    }

    public String getDovi() {
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