package com.company.truonghoc.entity.tienich;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TRUONGHOC_TINH_THANH")
@Entity(name = "truonghoc_TinhThanh")
@NamePattern("%s|tenTinhThanh")
public class TinhThanh extends StandardEntity {
    private static final long serialVersionUID = 6055366641300128355L;

    @Column(name = "TINH_THANHPHO")
    private String tinh_thanhpho;

    @Column(name = "TEN_TINH_THANH")
    private String tenTinhThanh;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    public String getTinh_thanhpho() {
        return tinh_thanhpho;
    }

    public void setTinh_thanhpho(String tinh_thanhpho) {
        this.tinh_thanhpho = tinh_thanhpho;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTenTinhThanh() {
        return tenTinhThanh;
    }

    public void setTenTinhThanh(String tenTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
    }
}