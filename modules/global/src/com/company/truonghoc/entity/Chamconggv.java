package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TRUONGHOC_CHAMCONGGV")
@Entity(name = "truonghoc_Chamconggv")
public class Chamconggv extends StandardEntity {
    private static final long serialVersionUID = 822647512299074261L;

    @JoinColumn(name = "HOTENGV_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien hotengv;

    @Column(name = "DONVIGV")
    private String donvigv;

    @Column(name = "NGAYLAM")
    @Temporal(TemporalType.DATE)
    private Date ngaylam;

    @Column(name = "BUOILAM")
    private String buoilam;

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

    public String getBuoilam() {
        return buoilam;
    }

    public void setBuoilam(String buoilam) {
        this.buoilam = buoilam;
    }

    public String getDonvigv() {
        return donvigv;
    }

    public void setDonvigv(String donvigv) {
        this.donvigv = donvigv;
    }

}