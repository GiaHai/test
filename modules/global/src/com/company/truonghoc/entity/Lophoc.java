package com.company.truonghoc.entity;

import com.company.truonghoc.entity.Hocsinh;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "TRUONGHOC_LOPHOC")
@Entity(name = "truonghoc_Lophoc")
public class Lophoc extends StandardEntity {
    private static final long serialVersionUID = -7972786784091409232L;

    @Column(name = "TENLOP")
    private String tenlop;

    @Column(name = "GIAOVIENCN")
    private String giaoviencn;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "lophoc")
    private List<Hocsinh> dshocsinh;

    public void setGiaoviencn(String giaoviencn) {
        this.giaoviencn = giaoviencn;
    }

    public String getGiaoviencn() {
        return giaoviencn;
    }

    public List<Hocsinh> getDshocsinh() {
        return dshocsinh;
    }

    public void setDshocsinh(List<Hocsinh> dshocsinh) {
        this.dshocsinh = dshocsinh;
    }

    public String getTenlop() {
        return tenlop;
    }

    public void setTenlop(String tenlop) {
        this.tenlop = tenlop;
    }
}