package com.company.truonghoc.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "TRUONGHOC_DIEMDANH")
@Entity(name = "truonghoc_Diemdanh")
public class Diemdanh extends StandardEntity {
    private static final long serialVersionUID = 1305572135769904742L;

    @JoinColumn(name = "NGUOITAODD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Giaovien nguoitaodd;

    @JoinColumn(name = "LOPDD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lophoc lopdd;

    @JoinColumn(name = "DONVIDD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Donvi donvidd;

    @ManyToMany
    @JoinTable(name = "TRUONGHOC_DIEMDANH_HOCSINH_LINK",
            joinColumns = @JoinColumn(name = "DIEMDANH_ID"),
            inverseJoinColumns = @JoinColumn(name = "HOCSINH_ID"))
    private List<Hocsinh> hotenhs;

    @Column(name = "NGAYNGHI")
    @Temporal(TemporalType.DATE)
    private Date ngaynghi;

    public void setHotenhs(List<Hocsinh> hotenhs) {
        this.hotenhs = hotenhs;
    }

    public List<Hocsinh> getHotenhs() {
        return hotenhs;
    }

    public void setDonvidd(Donvi donvidd) {
        this.donvidd = donvidd;
    }

    public Donvi getDonvidd() {
        return donvidd;
    }

    public void setLopdd(Lophoc lopdd) {
        this.lopdd = lopdd;
    }

    public Lophoc getLopdd() {
        return lopdd;
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
