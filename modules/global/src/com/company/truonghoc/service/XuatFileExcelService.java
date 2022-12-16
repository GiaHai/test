package com.company.truonghoc.service;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.entity.tienich.Namsinh;

import java.util.Date;
import java.util.List;

public interface XuatFileExcelService {
    String NAME = "truonghoc_XuatFileExcelService";

    List<Giaovien> layDanhSachGiaovien(Donvi donVi, String giaoVien);

    List<Hocsinh> layDanhSachHocsinh(Donvi donVi, String hocsinh, String gioitinh, Namsinh namsinh, Boolean tinhTrang);

    List<Lophoc> layDanhSachLophoc(Donvi donVi, Giaovien giaoVien, Tenlop lopHoc);

    List<Tenlop> layDanhSachTenlop(Donvi donVi, Giaovien giaoVien, Tenlop lopHoc);

    List<Hocphi> layDanhSachHocphi(Donvi donVi, String hoVaTen, Object trangThai, Date tuNgay, Date denNgay);

    List<Luongthang> layDanhSachLuongthang(Donvi donVi, Giaovien giaoVien, Object trangThai, Date tuNgay, Date denNgay);

    List<Thuchi> layDanhSachThuchi(Donvi donVi, String khoanChi, Object trangThai, Date tuNgay, Date denNgay);

    List<Thutienhocphi> layDanhSachThutienHocphi(Donvi donVi, String tenKhachhang, String tenHocSinh, Date tuNgay, Date denNgay, Object trangThai);

    List<Diemdanh> layDanhSachDiemdanh(Donvi donVi, Giaovien giaoVien, Date ngayNghi, Tenlop lopHoc);

    List<Chamconggv> layDanhSachChamconggv(Donvi donVi, Giaovien giaoVien, Date ngayLam, Integer buoiLam);
}