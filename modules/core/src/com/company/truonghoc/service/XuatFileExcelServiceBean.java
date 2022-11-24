package com.company.truonghoc.service;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.entity.tienich.Namsinh;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.*;

@Service(XuatFileExcelService.NAME)
public class XuatFileExcelServiceBean implements XuatFileExcelService {
    @Inject
    protected DataManager dataManager;

    @Override
    public List<Giaovien> layDanhSachGiaovien(Donvi donVi, String giaoVien) {
        String query = "select e from truonghoc_Giaovien e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (donVi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donVi);
        }
        if (!StringUtils.isEmpty(giaoVien)) {
            where += "and e.tengiaovien like :tengv ";
            params.put("tengv", "%" + giaoVien + "%");
        }
        List<Giaovien> giaoviens = dataManager.load(Giaovien.class).view("giaovien-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return giaoviens;
    }

    @Override
    public List<Hocsinh> layDanhSachHocsinh(Donvi donVi, String hocsinh, String gioitinh, Namsinh namsinh) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (donVi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donVi);
        }

        //Học sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.tenhocsinh like :tenhocsinh ";
            params.put("tenhocsinh", "%" + hocsinh + "%");
        }
        if (gioitinh != null) {
            where += "and e.gioitinhhocsinh = :gioitinh ";
            params.put("gioitinh", gioitinh);
        }
        if (namsinh != null){
            where += "and e.ngaysinhhocsinh = :namsinh ";
            params.put("namsinh", namsinh);
        }

        if (donVi != null) {
            where += " and e.donvi.id = :donVi";
            params.put("donVi", donVi.getId());
        }
        List<Hocsinh> hocsinhs = dataManager.load(Hocsinh.class).view("hocsinh-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return hocsinhs;
    }

    @Override
    public List<Lophoc> layDanhSachLophoc(Donvi donVi, Giaovien giaoVien, Tenlop lopHoc) {
        String query = "select e from truonghoc_Lophoc e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (donVi != null) {
            where += " and e.donvi = :donVi";
            params.put("donVi", donVi);
        }
        if (giaoVien != null) {
            where += " and e.giaoviencn = :giaoviencn";
            params.put("giaoviencn", giaoVien);
        }
        if (lopHoc != null) {
            where += " and e.tenlop = :tenlop";
            params.put("tenlop", lopHoc);
        }
        List<Lophoc> lophocs = dataManager.load(Lophoc.class).view("lophoc-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return lophocs;
    }

    @Override
    public List<Tenlop> layDanhSachTenlop(Donvi donVi, Giaovien giaoVien, String lopHoc) {

        String query = "select e from truonghoc_Tenlop e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (donVi != null) {
            where += " and e.dovi = :donVi";
            params.put("donVi", donVi);
        }
        if (giaoVien != null) {
            where += " and e.giaoviencn = :giaoviencn";
            params.put("giaoviencn", giaoVien);
        }
        if (lopHoc != null) {
            where += " and e.tenlop like :tenlop";
            params.put("tenlop", "%" + lopHoc + "%");
        }

        List<Tenlop> tenlops = dataManager.load(Tenlop.class).view("tenlop-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return tenlops;
    }

    @Override
    public List<Hocphi> layDanhSachHocphi(Donvi donVi, String hoVaTen, Object trangThai, Date tuNgay, Date denNgay) {
        String query = "select e from truonghoc_Hocphi e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        //Học sinh
        if (!StringUtils.isEmpty(hoVaTen)) {
            where += "and e.hovaten.tenhocsinh like :hocsinh ";
            params.put("hocsinh", "%" + hoVaTen + "%");
        }

        //Trạng thái
        if (trangThai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangThai);
        }

        //Đơn vị
        if (donVi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donVi);
        }
        //Từ ngày
        if (tuNgay != null) {
            where += "and e.ngaydong >= :tungay ";
            params.put("tungay", tuNgay);
        }
        //Đến ngày
        if (denNgay != null) {
            where += "and :denngay >= e.ngaydong ";
            params.put("denngay", denNgay);
        }
        List<Hocphi> hocphis = dataManager.load(Hocphi.class).view("hocphi-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return hocphis;
    }

    @Override
    public List<Luongthang> layDanhSachLuongthang(Donvi donVi, Giaovien giaoVien, Object trangThai, Date tuNgay, Date denNgay) {
        String query = "select e from truonghoc_Luongthang e ";
        String where = " where 1=1 ";
        Map<String, Object> params = new HashMap<>();

        //Đơn vị
        if (donVi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donVi);
        }
        //Giáo viên
        if (giaoVien != null) {
            where += "and e.hovaten = :giaovien ";
            params.put("giaovien", giaoVien);
        }
        //Trạng thái
        if (trangThai != null) {
            where += "and e.tinhtrangnhanluong = :trangthai ";
            params.put("trangthai", trangThai);
        }
        //Từ ngày
        if (tuNgay != null) {
            where += "and e.ngaynhan >= :tungay ";
            params.put("tungay", tuNgay);
        }
        //Đến ngày
        if (denNgay != null) {
            where += "and :denngay >= e.ngaynhan";
            params.put("denngay", denNgay);
        }

        List<Luongthang> luongthangs = dataManager.load(Luongthang.class).view("luongthang-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return luongthangs;
    }

    @Override
    public List<Thuchi> layDanhSachThuchi(Donvi donVi, String khoanChi, Object trangThai, Date tuNgay, Date denNgay) {
        String query = "select e from truonghoc_Thuchi e ";
        String where = " where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        //Đơn vị
        if (donVi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donVi);
        }
        //khoản chi
        if (!StringUtils.isEmpty(khoanChi)) {
            where += "and e.khoanchi like :khoanchi ";
            params.put("khoanchi", "%" + khoanChi + "%");
        }
        //trạng thái
        if (trangThai != null) {
            where += "and e.tinhtrangchi = :trangthai ";
            params.put("trangthai", trangThai);
        }
        //từ ngày
        if (tuNgay != null) {
            where += "and e.ngaychi >= :tungay ";
            params.put("tungay", tuNgay);
        }
        //đến ngày
        if (denNgay != null) {
            where += "and :denngay >= e.ngaychi ";
            params.put("denngay", denNgay);
        }

        List<Thuchi> thuchis = dataManager.load(Thuchi.class).view("thuchi-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return thuchis;
    }

    @Override
    public List<Thutienhocphi> layDanhSachThutienHocphi(Donvi donVi, String tenKhachhang, String tenHocSinh, Date tuNgay, Date denNgay, Object trangThai) {
        String query = "select e from truonghoc_Thutienhocphi e ";
        String where = " where 1=1 ";
        Map<String, Object> params = new HashMap<>();

        //Đơn vị
        if (donVi != null) {
            where += "and e.donvi = :donvi ";
            params.put("donvi", donVi);
        }

        //Khách hàng
        if (!StringUtils.isEmpty(tenKhachhang)) {
            where += "and e.tenkhachhang like :kh ";
            params.put("kh", "%" + tenKhachhang + "%");
        }
        //học sinh
        if (!StringUtils.isEmpty(tenHocSinh)) {
            where += "and e.tenhocsinh.tenhocsinh like :tenHs ";
            params.put("tenHs","%"+ tenHocSinh +"%");
        }
        //từ ngày
        if (tuNgay != null) {
            where += "and e.tungay >= :tungay ";
            params.put("tungay", tuNgay);
        }
        //đến ngày
        if (denNgay != null) {
            where += "and :denngay >= e.denngay ";
            params.put("denngay", denNgay);
        }
        //trạng thái
        if (trangThai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangThai);
        }
        List<Thutienhocphi> thutienhocphis = dataManager.load(Thutienhocphi.class).view("thutienhocphi-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return thutienhocphis;
    }

    @Override
    public List<Diemdanh> layDanhSachDiemdanh(Donvi donVi, Giaovien giaoVien, Date ngayNghi, Tenlop lopHoc) {
        String query = "select e from truonghoc_Diemdanh e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (donVi != null) {
            where += " and e.donvidd = :donVi";
            params.put("donVi", donVi);
        }
        if (giaoVien != null) {
            where += " and e.giaoviendd = :giaoviencn";
            params.put("giaoviencn", giaoVien);
        }
        if (lopHoc != null) {
            where += " and e.hotenhs.tenlop = :tenlop";
            params.put("tenlop", lopHoc);
        }
        if (ngayNghi != null) {
            where += " and e.ngaynghi = :ngaynghi";
            params.put("ngaynghi", ngayNghi);
        }

        List<Diemdanh> diemdanhs = dataManager.load(Diemdanh.class).view("diemdanh-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return diemdanhs;
    }

    @Override
    public List<Chamconggv> layDanhSachChamconggv(Donvi donVi, Giaovien giaoVien, Date ngayLam, Integer buoiLam) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = "where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (donVi != null) {
            where += " and e.donvigv = :donVi";
            params.put("donVi", donVi);
        }
        if (giaoVien != null) {
            where += " and e.hotengv = :giaoviencn";
            params.put("giaoviencn", giaoVien);
        }
        if (buoiLam != null) {
            where += " and e.buoilam = :buoiLam";
            params.put("buoiLam", buoiLam);
        }
        if (ngayLam != null) {
            where += " and e.ngaylam = :ngayLam";
            params.put("ngayLam", ngayLam);
        }

        List<Chamconggv> chamconggvs = dataManager.load(Chamconggv.class).view("chamconggv-view")
                .query(query + where)
                .setParameters(params)
                .list();
        return chamconggvs;
    }
}