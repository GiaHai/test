package com.company.truonghoc.entity.tienich;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TRUONGHOC_NAMSINH")
@Entity(name = "truonghoc_Namsinh")
public class Namsinh extends StandardEntity {
    private static final long serialVersionUID = -186322768104544471L;

    @Column(name = "NAM_SINH")
    private String namSinh;

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public String getNamSinh() {
        return namSinh;
    }

}