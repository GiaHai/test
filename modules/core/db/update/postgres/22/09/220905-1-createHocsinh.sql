create table TRUONGHOC_HOCSINH (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_HOCSINH varchar(255),
    DONVITAO_HOCSINH varchar(255),
    TENHOCSINH varchar(255),
    NGAYSINHHOCSINH date,
    GIOITINHHOCSINH varchar(255),
    QUEQUANHOCSINH varchar(255),
    GHICHU varchar(255),
    --
    primary key (ID)
);