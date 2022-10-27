package com.company.truonghoc.service;

import com.company.truonghoc.entity.tienich.QuanHuyen;
import com.company.truonghoc.entity.tienich.TinhThanh;
import com.company.truonghoc.entity.tienich.XaPhuong;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service(TienIchService.NAME)
public class TienIchServiceBean implements TienIchService {
    @Inject
    protected DataManager dataManager;

    @Override
    public XaPhuong findXaPhuongById(UUID xaPhuongId, String viewName, boolean allowCached) {
        String queryString = "select e from truonghoc_XaPhuong e where e.id = :xaPhuongId";
        Optional<XaPhuong> optionalXaPhuong = dataManager.load(XaPhuong.class)
                .query(queryString)
                .parameter("xaPhuongId", xaPhuongId)
                .view(viewName != null ? viewName : "_local")
                .cacheable(allowCached)
                .optional();
        return optionalXaPhuong.isPresent() ? optionalXaPhuong.get() : null;
    }

    @Override
    public List<XaPhuong> getFullDmXaPhuong() {
        List<XaPhuong> result = new ArrayList<>();
        String sqlQuery = "select e from truonghoc_XaPhuong e";
        result = dataManager.loadList(LoadContext.create(XaPhuong.class).setQuery(
                LoadContext.createQuery(sqlQuery)).setView("xaPhuong-view"));
        for (XaPhuong xaPhuong : result) {
            String fullName = xaPhuong.getXa_Phuong() + " " +xaPhuong.getTenXaPhuong()
                    + (xaPhuong.getQuanHuyen() != null ? ", " + xaPhuong.getQuanHuyen().getQuan_Huyen() + " " + xaPhuong.getQuanHuyen().getTenQuanHuyen() : "")
                    + (xaPhuong.getTinhThanh() != null ? ", " + xaPhuong.getTinhThanh().getTinh_thanhpho() + " " + xaPhuong.getTinhThanh().getTenTinhThanh() : "");
            xaPhuong.setTenXaPhuong(fullName);
        }
        return result;
    }
    /*** Load tỉnh thành ***/
    @Override
    public List<TinhThanh> loadTinhThanh() {
        return dataManager.load(TinhThanh.class)
                .query("select e from truonghoc_TinhThanh e")
                .list();
    }
    /*** Loád quận huyện ***/

    @Override
    public List<QuanHuyen> loadQuanHuyen(TinhThanh tinhThanh) {
        return dataManager.load(QuanHuyen.class)
                .query("select e from truonghoc_QuanHuyen e where e.tinhThanh = :tinhthanh")
                .parameter("tinhthanh", tinhThanh)
                .list();
    }
}