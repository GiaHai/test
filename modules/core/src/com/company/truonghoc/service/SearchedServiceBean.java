package com.company.truonghoc.service;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.Tenlop;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(SearchedService.NAME)
public class SearchedServiceBean implements SearchedService {

    @Inject
    protected DataManager dataManager;

    @Override
    public List<Giaovien> loadgiaovien(Object donVi) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvitao_giaovien = :donvi")
                .parameter("donvi", donVi)
                .view("giaovien-view")
                .list();
    }

    @Override
    public List<Tenlop> loadlopDK(Object donvi, Object giaovien) {
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn = :giaovien and e.tinhtranglop = true")
                .parameter("donvi", donvi)
                .parameter("giaovien", giaovien)
                .list();
    }

    @Override
    public List<Tenlop> loadlop(Object donvi, Object giaovien) {
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi = :donvi and e.giaoviencn = :giaovien")
                .parameter("donvi", donvi)
                .parameter("giaovien", giaovien)
                .list();
    }

    @Override
    public List<Hocsinh> loadHs(Object donvi) {
        return dataManager.load(Hocsinh.class)
                .query("select e from truonghoc_Hocsinh e where e.donvitao_hocsinh.tendonvi = :tendonvi")
                .parameter("tendonvi", donvi)
                .list();
    }

    @Override
    public List<Donvi> loaddonvi() {
        return dataManager.load(Donvi.class)
                .query("select e from truonghoc_Donvi e")
                .view("donvi-view")
                .list();
    }
}