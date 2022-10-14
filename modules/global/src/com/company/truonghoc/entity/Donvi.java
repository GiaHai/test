package com.company.truonghoc.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TRUONGHOC_DONVI")
@Entity(name = "truonghoc_Donvi")
@NamePattern("%s|tendonvi")
public class Donvi extends StandardEntity {
    private static final long serialVersionUID = 293957502587549671L;

    @Column(name = "UPDATE_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date update;

    @Column(name = "TENDONVI", nullable = false)
    @NotNull(message = "{msg://truonghoc_Donvi.tendonvi.validation.NotNull}")
    private String tendonvi;

    @Column(name = "DONVITRUNGTAM")
    private Boolean donvitrungtam = false;

    public void setDonvitrungtam(Boolean donvitrungtam) {
        this.donvitrungtam = donvitrungtam;
    }

    public Boolean getDonvitrungtam() {
        return donvitrungtam;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public Date getUpdate() {
        return update;
    }

    public String getTendonvi() {
        return tendonvi;
    }

    public void setTendonvi(String tendonvi) {
        this.tendonvi = tendonvi;
    }
}