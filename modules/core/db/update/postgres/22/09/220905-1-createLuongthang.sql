create table TRUONGHOC_LUONGTHANG (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_LUONGTHANG varchar(255),
    DONVITAO_LUONGTHANG varchar(255),
    HOVATEN_ID uuid not null,
    NGAYNHAN date not null,
    LUONGCOBAN bigint,
    BUOILAM decimal(19, 2),
    CANGOAI integer,
    CASANG integer,
    THUCLINH bigint,
    DAYTHEM bigint,
    TROCAP bigint,
    TRACHNHIEM bigint,
    CHUYENCAN bigint,
    THUONG bigint,
    TONGLINH bigint,
    --
    primary key (ID)
);