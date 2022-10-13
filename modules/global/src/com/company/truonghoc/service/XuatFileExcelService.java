package com.company.truonghoc.service;

import com.company.truonghoc.entity.*;

import java.util.List;
public interface XuatFileExcelService {
    String NAME = "truonghoc_XuatFileExcelService";

    List<Giaovien> layDanhSachGiaovien();

    List<Hocsinh> layDanhSachHocsinh();

    List<Lophoc> layDanhSachLophoc();

    List<Tenlop> layDanhSachTenlop();

    List<Hocphi> layDanhSachHocphi();

    List<Luongthang> layDanhSachLuongthang();

    List<Thuchi> layDanhSachThuchi();

    List<Thutienhocphi> layDanhSachThutienHocphi();

    List<Diemdanh> layDanhSachDiemdanh();

    List<Chamconggv> layDanhSachChamconggv();
}