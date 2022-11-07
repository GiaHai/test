package com.company.truonghoc.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum BuoiLamEnum implements EnumClass<Integer> {


    LAM_CA_NGAY(1),
    CA_SANG(2),
    CA_CHIEU(3),
    CA_CHU_NHAT(4),
    CA_CHIEU_5H_6H(5),
    CA_CHIEU_6H_7H(6);

    private Integer id;

    BuoiLamEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static BuoiLamEnum fromId(Integer id) {
        for (BuoiLamEnum at : BuoiLamEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

}
