create table TRUONGHOC_THUCHI (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_THUCHI varchar(255),
    DONVITAO_THUCHI varchar(255),
    KHOANCHI varchar(255),
    SOLUONG integer,
    DONGIA bigint,
    THANHTIEN bigint,
    GHICHU varchar(255),
    --
    primary key (ID)
);