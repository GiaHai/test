-- begin TRUONGHOC_LUONGTHANG
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
)^
-- end TRUONGHOC_LUONGTHANG
-- begin TRUONGHOC_THUCHI
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
)^
-- end TRUONGHOC_THUCHI
-- begin TRUONGHOC_HOCSINH
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
)^
-- end TRUONGHOC_HOCSINH
-- begin TRUONGHOC_GIAOVIEN
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
)^
-- end TRUONGHOC_GIAOVIEN
-- begin TRUONGHOC_HOCPHI
create table TRUONGHOC_HOCPHI (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_HOCPHI varchar(255),
    DOVITAO_HOCPHI varchar(255),
    HOVATEN_ID uuid,
    NAMSINH date,
    GHICHU varchar(255),
    SOTIENTAMTINH bigint,
    SOTIENTHUTHEOHD bigint,
    NGAYDONG date,
    HINHTHUCTHANHTOAN varchar(255),
    --
    primary key (ID)
)^
-- end TRUONGHOC_HOCPHI
-- begin TRUONGHOC_DONVI
create table TRUONGHOC_DONVI (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TENDONVI varchar(255) not null,
    DONVITRUNGTAM boolean,
    --
    primary key (ID)
)^
-- end TRUONGHOC_DONVI
-- begin SEC_USER
alter table SEC_USER add column LOOCKUP_DONVI_ID uuid ^
alter table SEC_USER add column TENDONVI varchar(255) ^
alter table SEC_USER add column DONVITRUNGTAM boolean ^
alter table SEC_USER add column DTYPE varchar(31) ^
update SEC_USER set DTYPE = 'truonghoc_UserExt' where DTYPE is null ^
-- end SEC_USER
