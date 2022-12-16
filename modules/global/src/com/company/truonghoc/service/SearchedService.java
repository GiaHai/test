package com.company.truonghoc.service;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.entity.tienich.Namsinh;
import com.haulmont.cuba.core.entity.KeyValueEntity;

import java.util.Date;
import java.util.List;

public interface SearchedService {
    String NAME = "truonghoc_SearchedService";

    List<Donvi> loaddonvi();

    List<Giaovien> loadgiaovien(Object donVi);

    List<Tenlop> loadlopDK(Object donvi, Object giaovien);

    List<Tenlop> loadlop(Object donvi, Object giaovien);

    List<Hocsinh> loadHs(Object donvi);


    //    List<Hocsinh> loadHsDk(String tendonvi, String tengiaovien);
    //    ---------------------- Tính ngày lương -----------------
    List<Chamconggv> tinhca(Donvi donVi, Giaovien giaoVien, Date tuNgay, Date denNgay, Integer ngayLam);

    List<KeyValueEntity> caChieudaythem(Donvi donVi, Giaovien giaoVien, Date tuNgay, Date denNgay, Integer ngayLam);

    List<Namsinh> loadNamSinh();

    List<Hocsinh> getthongBaoHsChuaDongTien(Date tuNgay, Date denNgay, Object donvi);
}