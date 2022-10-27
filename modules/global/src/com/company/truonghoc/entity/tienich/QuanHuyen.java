package com.company.truonghoc.entity.tienich;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TRUONGHOC_QUAN_HUYEN")
@Entity(name = "truonghoc_QuanHuyen")
@NamePattern("%s|tenQuanHuyen")
public class QuanHuyen extends StandardEntity {
    private static final long serialVersionUID = -7280496386328963412L;

    @Column(name = "TEN_QUAN_HUYEN")
    private String tenQuanHuyen;

    @Column(name = "QUAN_HUYEN")
    private String quan_Huyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TINH_THANH_ID")
    private TinhThanh tinhThanh;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    public String getQuan_Huyen() {
        return quan_Huyen;
    }

    public void setQuan_Huyen(String quan_Huyen) {
        this.quan_Huyen = quan_Huyen;
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

    public String getTenQuanHuyen() {
        return tenQuanHuyen;
    }

    public void setTenQuanHuyen(String tenQuanHuyen) {
        this.tenQuanHuyen = tenQuanHuyen;
    }
}