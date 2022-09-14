package com.company.truonghoc.service;

import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(ThuchiService.NAME)
public class ThuchiServiceBean implements ThuchiService {
    @Inject
    protected DataManager dataManager;

    @Override
    public Long hocphi(String value) {
        return dataManager.loadValue("select sum(e.sotienthutheohd) from truonghoc_Hocphi e" +
                        " where e.dovitao_hocphi= :tendonvi and e.sotienthutheohd is not null and e.hinhthucthanhtoan is not null"
                ,Long.class)
                .parameter("tendonvi",value)
                .one();
    }

    @Override
    public Long thutienhocphi(String value) {
        return dataManager.loadValue("select sum(e.thanhtien) from truonghoc_Thutienhocphi e" +
                        " where e.donvitao_thutienhocphi= :tendonvi and e.thanhtien is not null and e.hinhthucthanhtoan is not null"
                ,Long.class)
                .parameter("tendonvi",value)
                .one();
    }

    @Override
    public Long luongthang(String value) {
        return dataManager.loadValue("select sum(e.tonglinh) from truonghoc_Luongthang e" +
                        " where e.donvitao_luongthang= :tendonvi and e.tonglinh is not null and e.hinhthucthanhtoan is not null"
                ,Long.class)
                .parameter("tendonvi",value)
                .one();
    }

    @Override
    public Long thuchi(String value) {
        return dataManager.loadValue("select sum(e.thanhtien) from truonghoc_Thuchi e" +
                        " where e.donvitao_thuchi= :tendonvi and e.thanhtien is not null and e.hinhthucthanhtoan is not null"
                ,Long.class)
                .parameter("tendonvi",value)
                .one();
    }
}