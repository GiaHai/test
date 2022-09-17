package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TRUONGHOC_DIEMDANH")
@Entity(name = "truonghoc_Diemdanh")
public class Diemdanh extends StandardEntity {
    private static final long serialVersionUID = 1305572135769904742L;

   @Column(name = "NGUOITAODD")
    private String nguoitaodd;

    @Column(name = "DONVIDD")
    private String donvidd;

    @JoinColumn(name = "HOTENHS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Hocsinh hotenhs;

    @Column(name = "NGAYNGHI")
    @Temporal(TemporalType.DATE)
    private Date ngaynghi;

    public void setNgaynghi(Date ngaynghi) {
        this.ngaynghi = ngaynghi;
    }

    public Date getNgaynghi() {
        return ngaynghi;
    }

    public void setHotenhs(Hocsinh hotenhs) {
        this.hotenhs = hotenhs;
    }

    public Hocsinh getHotenhs() {
        return hotenhs;
    }

    public String getDonvidd() {
        return donvidd;
    }

    public void setDonvidd(String donvidd) {
        this.donvidd = donvidd;
    }

    public String getNguoitaodd() {
        return nguoitaodd;
    }

    public void setNguoitaodd(String nguoitaodd) {
        this.nguoitaodd = nguoitaodd;
    }
}
