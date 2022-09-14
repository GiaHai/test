create table TRUONGHOC_GIAOVIEN (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_GIAOVIEN varchar(255),
    DONVITAO_GIAOVIEN varchar(255),
    TENGIAOVIEN varchar(255) not null,
    NGAYSINHGIAOVIEN date,
    QUEQUANGIAOVIEN varchar(255),
    GIOITINHGIAOVIEN varchar(255),
    LUONGCOBAN bigint not null,
    GHICHU varchar(255),
    --
    primary key (ID)
);