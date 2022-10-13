package com.company.truonghoc.service;

import com.company.truonghoc.entity.*;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(XuatFileExcelService.NAME)
public class XuatFileExcelServiceBean implements XuatFileExcelService {
    @Inject
    protected DataManager dataManager;

    @Override
    public List<Giaovien> layDanhSachGiaovien() {
        List<Giaovien> giaoviens = dataManager.load(Giaovien.class).view("giaovien-view")
                .query("select e from truonghoc_Giaovien e").list();
        return giaoviens;
    }

    @Override
    public List<Hocsinh> layDanhSachHocsinh() {
        List<Hocsinh> hocsinhs = dataManager.load(Hocsinh.class).view("hocsinh-view")
                .query("select e from truonghoc_Hocsinh e").list();
        return hocsinhs;
    }

    @Override
    public List<Lophoc> layDanhSachLophoc() {
        List<Lophoc> lophocs = dataManager.load(Lophoc.class).view("lophoc-view")
                .query("select e from truonghoc_Lophoc e").list();
        return lophocs;
    }

    @Override
    public List<Tenlop> layDanhSachTenlop() {
        List<Tenlop> tenlops = dataManager.load(Tenlop.class).view("tenlop-view")
                .query("select e from truonghoc_Tenlop e").list();
        return tenlops;
    }

    @Override
    public List<Hocphi> layDanhSachHocphi() {
        List<Hocphi> hocphis = dataManager.load(Hocphi.class).view("hocphi-view")
                .query("select e from truonghoc_Hocphi e").list();
        return hocphis;
    }

    @Override
    public List<Luongthang> layDanhSachLuongthang() {
        List<Luongthang> luongthangs = dataManager.load(Luongthang.class).view("luongthang-view")
                .query("select e from truonghoc_Luongthang e").list();
        return luongthangs;
    }

    @Override
    public List<Thuchi> layDanhSachThuchi() {
        List<Thuchi> thuchis = dataManager.load(Thuchi.class).view("thuchi-view")
                .query("select e from truonghoc_Thuchi e").list();
        return thuchis;
    }

    @Override
    public List<Thutienhocphi> layDanhSachThutienHocphi() {
        List<Thutienhocphi> thutienhocphis = dataManager.load(Thutienhocphi.class).view("thutienhocphi-view")
                .query("select e from truonghoc_Thutienhocphi e").list();
        return thutienhocphis;
    }

    @Override
    public List<Diemdanh> layDanhSachDiemdanh() {
        List<Diemdanh> diemdanhs = dataManager.load(Diemdanh.class).view("diemdanh-view")
                .query("select e from truonghoc_Diemdanh e").list();
        return diemdanhs;
    }

    @Override
    public List<Chamconggv> layDanhSachChamconggv() {
        List<Chamconggv> chamconggvs = dataManager.load(Chamconggv.class).view("chamconggv-view")
                .query("select e from truonghoc_Chamconggv e").list();
        return chamconggvs;
    }
}