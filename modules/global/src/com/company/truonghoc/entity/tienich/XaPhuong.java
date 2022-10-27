package com.company.truonghoc.entity.tienich;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TRUONGHOC_XA_PHUONG")
@Entity(name = "truonghoc_XaPhuong")
@NamePattern("%s|tenXaPhuong")
public class XaPhuong extends StandardEntity {
    private static final long serialVersionUID = -3437196964835966671L;

    @Column(name = "TEN_XA_PHUONG")
    private String tenXaPhuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUAN_HUYEN_ID")
    private QuanHuyen quanHuyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TINH_THANH_ID")
    private TinhThanh tinhThanh;

    @Column(name = "XA_PHUONG")
    private String xa_Phuong;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    public String getXa_Phuong() {
        return xa_Phuong;
    }

    public void setXa_Phuong(String xa_Phuong) {
        this.xa_Phuong = xa_Phuong;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public TinhThanh getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(TinhThanh tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public QuanHuyen getQuanHuyen() {
        return quanHuyen;
    }

    public void setQuanHuyen(QuanHuyen quanHuyen) {
        this.quanHuyen = quanHuyen;
    }

    public String getTenXaPhuong() {
        return tenXaPhuong;
    }

    public void setTenXaPhuong(String tenXaPhuong) {
        this.tenXaPhuong = tenXaPhuong;
    }
}