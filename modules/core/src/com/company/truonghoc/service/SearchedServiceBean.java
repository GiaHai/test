package com.company.truonghoc.service;

import com.company.truonghoc.entity.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Service(SearchedService.NAME)
public class SearchedServiceBean implements SearchedService {

    @Inject
    protected DataManager dataManager;
    @Inject
    protected Persistence persistence;

    @Override
    public List<Giaovien> loadgiaovien(Object donVi) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvi = :donvi")
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
                .query("select e from truonghoc_Hocsinh e where e.donvi.tendonvi = :tendonvi")
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

//    ---------------------- Tính ngày lương -----------------


    @Override
    public List<Chamconggv> tinhca(Donvi donVi, Giaovien giaoVien, Date tuNgay, Date denNgay, String ngayLam) {
        List<Chamconggv> resultList;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            String sql = "select e from truonghoc_Chamconggv e ";
            String where = "where e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = :ngayLam " +
                    " and e.ngaylam >= :tungay and :denngay >= e.ngaylam";
            Query query = entityManager.createQuery(sql + where);
            query.setParameter("donvi", donVi);
            query.setParameter("giaovien", giaoVien);
            query.setParameter("tungay", tuNgay);
            query.setParameter("denngay", denNgay);
            query.setParameter("ngayLam", ngayLam);

            resultList = (List<Chamconggv>) query.getResultList();
            // commit transaction
            tx.commit();
        }
        return resultList;
    }

    @Override
    public List<KeyValueEntity> caChieudaythem(Donvi donVi, Giaovien giaoVien, Date tuNgay, Date denNgay, String ngayLam) {
        String sql = "select sum(e.tienBuoi) tienBuoi from truonghoc_Chamconggv e ";
        String where = "where e.donvigv = :donvi and e.hotengv = :giaovien and e.buoilam = :ngayLam " +
                " and e.ngaylam >= :tungay and :denngay >= e.ngaylam";
        return dataManager.loadValues(sql + where)
                .properties("tienBuoi")
                .parameter("donvi", donVi)
                .parameter("giaovien", giaoVien)
                .parameter("tungay", tuNgay)
                .parameter("denngay", denNgay)
                .parameter("ngayLam", ngayLam)
                .list();
    }
}