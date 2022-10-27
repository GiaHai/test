package com.company.truonghoc.service;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Hocsinh;
import com.company.truonghoc.entity.Tenlop;

import java.util.List;

public interface SearchedService {
    String NAME = "truonghoc_SearchedService";

    List<Donvi> loaddonvi();

    List<Giaovien> loadgiaovien(Object donVi);

    List<Tenlop> loadlopDK(Object donvi, Object giaovien);

    List<Tenlop> loadlop(Object donvi, Object giaovien);

    List<Hocsinh> loadHs(Object donvi);

//    List<Hocsinh> loadHsDk(String tendonvi, String tengiaovien);
}