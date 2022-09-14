-- begin TRUONGHOC_LUONGTHANG
create table TRUONGHOC_LUONGTHANG (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    HOVATEN_ID varchar(36),
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
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
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
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
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
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TENGIAOVIEN varchar(255),
    NGAYSINHGIAOVIEN date,
    QUEQUANGIAOVIEN varchar(255),
    GIOITINHGIAOVIEN varchar(255),
    LUONGCOBAN bigint,
    GHICHU varchar(255),
    --
    primary key (ID)
)^
-- end TRUONGHOC_GIAOVIEN
-- begin TRUONGHOC_HOCPHI
create table TRUONGHOC_HOCPHI (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    HOVATEN_ID varchar(36),
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
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TENDONVI varchar(255),
    DONVITRUNGTAM boolean,
    --
    primary key (ID)
)^
-- end TRUONGHOC_DONVI
-- begin SEC_USER
alter table SEC_USER add column HAIHAI_ID varchar(36) ^
alter table SEC_USER add column DTYPE varchar(31) ^
update SEC_USER set DTYPE = 'truonghoc_UserExt' where DTYPE is null ^
-- end SEC_USER
