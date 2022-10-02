package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TRUONGHOC_DIEMDANH")
@Entity(name = "truonghoc_Diemdanh")
public class Diemdanh extends StandardEntity {
    private static final long serialVersionUID = 1305572135769904742L;

    @JoinColumn(name = "NGUOITAODD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien nguoitaodd;

    @JoinColumn(name = "LOPDD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenlop lopdd;

    @JoinColumn(name = "DONVIDD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi donvidd;

    @JoinColumn(name = "HOTENHS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lophoc hotenhs;

    @Column(name = "NGAYNGHI")
    @Temporal(TemporalType.DATE)
    private Date ngaynghi;

    public void setLopdd(Tenlop lopdd) {
        this.lopdd = lopdd;
    }

    public Tenlop getLopdd() {
        return lopdd;
    }

    public void setHotenhs(Lophoc hotenhs) {
        this.hotenhs = hotenhs;
    }

    public Lophoc getHotenhs() {
        return hotenhs;
    }

    public void setDonvidd(Donvi donvidd) {
        this.donvidd = donvidd;
    }

    public Donvi getDonvidd() {
        return donvidd;
    }

    public void setNguoitaodd(Giaovien nguoitaodd) {
        this.nguoitaodd = nguoitaodd;
    }

    public Giaovien getNguoitaodd() {
        return nguoitaodd;
    }

    public void setNgaynghi(Date ngaynghi) {
        this.ngaynghi = ngaynghi;
    }

    public Date getNgaynghi() {
        return ngaynghi;
    }

}
