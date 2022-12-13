package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "TRUONGHOC_TENLOP")
@Entity(name = "truonghoc_Tenlop")
@NamePattern("%s - Tháng %s - Năm %s|tenlop,thanghoc,namhoc")
public class Tenlop extends StandardEntity {
    private static final long serialVersionUID = 4124330793891358741L;

    @JoinColumn(name = "DOVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi dovi;

    @Column(name = "TENLOP")
    private String tenlop;

    @JoinColumn(name = "GIAOVIENCN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Giaovien giaoviencn;

    @Column(name = "THANGHOC")
    private Integer thanghoc;

    @Column(name = "NAMHOC")
    private String namhoc;

    @Column(name = "TINHTRANGLOP")
    private Boolean tinhtranglop = false;

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
    }

    public String getNamhoc() {
        return namhoc;
    }

    public void setThanghoc(Integer thanghoc) {
        this.thanghoc = thanghoc;
    }

    public Integer getThanghoc() {
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