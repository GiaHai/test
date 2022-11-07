package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TRUONGHOC_CHAMCONGGV")
@Entity(name = "truonghoc_Chamconggv")
public class Chamconggv extends StandardEntity {
    private static final long serialVersionUID = 822647512299074261L;

    @JoinColumn(name = "HOTENGV_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Giaovien hotengv;

    @JoinColumn(name = "DONVIGV_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Donvi donvigv;

    @Column(name = "NGAYLAM")
    @Temporal(TemporalType.DATE)
    private Date ngaylam;

    @Column(name = "BUOILAM")
    private Integer buoilam;

    @Column(name = "TIEN_BUOI")
    private Integer tienBuoi;

    public void setBuoilam(Integer buoilam) {
        this.buoilam = buoilam;
    }

    public Integer getBuoilam() {
        return buoilam;
    }

    public Integer getTienBuoi() {
        return tienBuoi;
    }

    public void setTienBuoi(Integer tienBuoi) {
        this.tienBuoi = tienBuoi;
    }


    public void setDonvigv(Donvi donvigv) {
        this.donvigv = donvigv;
    }

    public Donvi getDonvigv() {
        return donvigv;
    }

    public void setNgaylam(Date ngaylam) {
        this.ngaylam = ngaylam;
    }

    public Date getNgaylam() {
        return ngaylam;
    }

    public void setHotengv(Giaovien hotengv) {
        this.hotengv = hotengv;
    }

    public Giaovien getHotengv() {
        return hotengv;
    }

}