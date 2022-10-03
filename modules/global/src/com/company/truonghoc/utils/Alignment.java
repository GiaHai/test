package com.company.truonghoc.utils;

public enum Alignment {
    LEFT("-1"),
    CENTER("0"),
    RIGHT("1");

    private String id;

    Alignment(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    public static Alignment fromId(String id) {
        for (Alignment at : Alignment.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
