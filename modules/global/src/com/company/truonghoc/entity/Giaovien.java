package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TRUONGHOC_GIAOVIEN")
@Entity(name = "truonghoc_Giaovien")
@NamePattern("%s|tengiaovien")
public class Giaovien extends StandardEntity {
    private static final long serialVersionUID = 8016193291854421026L;

    @Column(name = "USERTAO_GIAOVIEN")
    private String usertao_giaovien;

    @Column(name = "DONVITAO_GIAOVIEN")
    private String donvitao_giaovien;

    @Column(name = "TENGIAOVIEN", nullable = false)
    @NotNull(message = "{msg://truonghoc_Giaovien.tengiaovien.validation.NotNull}")
    private String tengiaovien;

    @Column(name = "NGAYSINHGIAOVIEN")
    @Temporal(TemporalType.DATE)
    private Date ngaysinhgiaovien;

    @Column(name = "QUEQUANGIAOVIEN")
    private String quequangiaovien;

    @Column(name = "GIOITINHGIAOVIEN")
    private String gioitinhgiaovien;

    @Column(name = "LUONGCOBAN", nullable = false)
    @NotNull(message = "{msg://truonghoc_Giaovien.luongcoban.validation.NotNull}")
    private Long luongcoban;

    @Column(name = "GHICHU")
    private String ghichu;

    public void setTengiaovien(String tengiaovien) {
        this.tengiaovien = tengiaovien;
    }

    public String getDonvitao_giaovien() {
        return donvitao_giaovien;
    }

    public void setDonvitao_giaovien(String donvitao_giaovien) {
        this.donvitao_giaovien = donvitao_giaovien;
    }

    public void setUsertao_giaovien(String usertao_giaovien) {
        this.usertao_giaovien = usertao_giaovien;
    }

    public String getUsertao_giaovien() {
        return usertao_giaovien;
    }

    public void setLuongcoban(Long luongcoban) {
        this.luongcoban = luongcoban;
    }

    public Long getLuongcoban() {
        return luongcoban;
    }

    public void setGioitinhgiaovien(String gioitinhgiaovien) {
        this.gioitinhgiaovien = gioitinhgiaovien;
    }

    public String getGioitinhgiaovien() {
        return gioitinhgiaovien;
    }

    public void setNgaysinhgiaovien(Date ngaysinhgiaovien) {
        this.ngaysinhgiaovien = ngaysinhgiaovien;
    }

    public Date getNgaysinhgiaovien() {
        return ngaysinhgiaovien;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getQuequangiaovien() {
        return quequangiaovien;
    }

    public void setQuequangiaovien(String quequangiaovien) {
        this.quequangiaovien = quequangiaovien;
    }

    public String getTengiaovien() {
        return tengiaovien;
    }

}