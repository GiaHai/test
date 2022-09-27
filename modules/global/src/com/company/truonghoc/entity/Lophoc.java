package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.List;

@Table(name = "TRUONGHOC_LOPHOC")
@Entity(name = "truonghoc_Lophoc")
@NamePattern("%s|tenlop")
public class Lophoc extends StandardEntity {
    private static final long serialVersionUID = -7972786784091409232L;

    @JoinColumn(name = "TENLOP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenlop tenlop;

    @JoinColumn(name = "DONVI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi donvi;

    @JoinColumn(name = "GIAOVIENCN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien giaoviencn;

    @OnDelete(DeletePolicy.CASCADE)
    @ManyToMany
    @JoinTable(name = "TRUONGHOC_LOPHOC_HOCSINH_LINK",
            joinColumns = @JoinColumn(name = "LOPHOC_ID"),
            inverseJoinColumns = @JoinColumn(name = "HOCSINH_ID"))
    private List<Hocsinh> dshocsinh;

    public void setDshocsinh(List<Hocsinh> dshocsinh) {
        this.dshocsinh = dshocsinh;
    }

    public List<Hocsinh> getDshocsinh() {
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