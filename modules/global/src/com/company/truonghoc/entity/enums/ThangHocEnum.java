package com.company.truonghoc.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum ThangHocEnum implements EnumClass<Integer> {
    THANG_MOT(1),
    THANG_HAI(2),
    THANG_BA(3),
    THANG_TU(4),
    THANG_NAM(5),
    THANG_SAU(6),
    THANG_BAY(7),
    THANG_TAM(8),
    THANG_CHIN(9),
    THANG_MUOI(10),
    THANG_MUOI_MOT(11),
    THANG_MUOI_HAI(12);

    private Integer id;

    ThangHocEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ThangHocEnum fromId(Integer id) {
        for (ThangHocEnum at : ThangHocEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
