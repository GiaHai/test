package com.company.truonghoc.service;

import com.company.truonghoc.entity.tienich.QuanHuyen;
import com.company.truonghoc.entity.tienich.TinhThanh;
import com.company.truonghoc.entity.tienich.XaPhuong;

import java.util.List;
import java.util.UUID;

public interface TienIchService {
    String NAME = "truonghoc_TienIchService";

    XaPhuong findXaPhuongById(UUID xaPhuongId, String viewName, boolean allowCached);

    List<XaPhuong> getFullDmXaPhuong();

    List<TinhThanh> loadTinhThanh();

    List<QuanHuyen> loadQuanHuyen(TinhThanh tinhThanh);
}