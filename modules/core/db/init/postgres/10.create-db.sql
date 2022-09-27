-- begin TRUONGHOC_THUTIENHOCPHI
create table TRUONGHOC_THUTIENHOCPHI (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_THUTIENHOCPHI_ID uuid,
    DONVITAO_THUTIENHOCPHI_ID uuid,
    TENKHACHHANG varchar(255),
    DIACHI varchar(255),
    TUNGAY timestamp,
    DENNGAY timestamp,
    TENHOCSINH_ID uuid,
    THANHTIEN bigint,
    HINHTHUCTHANHTOAN varchar(255),
    TINHTRANGTHANHTOAN varchar(255),
    --
    primary key (ID)
)^
-- end TRUONGHOC_THUTIENHOCPHI
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
    DONVITAO_LUONGTHANG_ID uuid,
    HOVATEN_ID uuid not null,
    NGAYNHAN date,
    HANNHANLUONG timestamp,
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
    HINHTHUCTHANHTOAN varchar(255),
    TINHTRANGNHANLUONG varchar(255),
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
    USERTAO_THUCHI_ID uuid,
    DONVITAO_THUCHI_ID uuid,
    KHOANCHI varchar(255),
    NGAYCHI timestamp,
    HANCHI timestamp,
    SOLUONG integer,
    DONGIA bigint,
    THANHTIEN bigint,
    GHICHU varchar(255),
    HINHTHUCTHANHTOAN varchar(255),
    TINHTRANGCHI varchar(255),
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
    DONVITAO_HOCSINH_ID uuid,
    TENHOCSINH varchar(255),
    NGAYSINHHOCSINH date,
    GIOITINHHOCSINH varchar(255),
    QUEQUANHOCSINH varchar(255),
    GHICHU varchar(255),
    --
    primary key (ID)
)^
-- end TRUONGHOC_HOCSINH
-- begin TRUONGHOC_TENLOP
create table TRUONGHOC_TENLOP (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DOVI_ID uuid,
    TENLOP varchar(255),
    GIAOVIENCN_ID uuid,
    THANGHOC varchar(255),
    NAMHOC integer,
    TINHTRANGLOP boolean,
    --
    primary key (ID)
)^
-- end TRUONGHOC_TENLOP
-- begin TRUONGHOC_DIEMDANH
create table TRUONGHOC_DIEMDANH (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NGUOITAODD_ID uuid,
    LOPDD_ID uuid,
    DONVIDD_ID uuid,
    NGAYNGHI date,
    --
    primary key (ID)
)^
-- end TRUONGHOC_DIEMDANH
-- begin TRUONGHOC_CHAMCONGGV
create table TRUONGHOC_CHAMCONGGV (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    HOTENGV_ID uuid,
    DONVIGV_ID uuid,
    NGAYLAM date,
    BUOILAM varchar(255),
    --
    primary key (ID)
)^
-- end TRUONGHOC_CHAMCONGGV
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
    DONVITAO_GIAOVIEN_ID uuid,
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
    USERTAO_HOCPHI_ID uuid,
    DOVITAO_HOCPHI_ID uuid,
    HOVATEN_ID uuid,
    GHICHU varchar(255),
    SOTIENTAMTINH bigint,
    SOTIENTHUTHEOHD bigint,
    NGAYDONG date,
    HANDONG timestamp,
    HINHTHUCTHANHTOAN varchar(255),
    TINHTRANGTHANHTOAN varchar(255),
    --
    primary key (ID)
)^
-- end TRUONGHOC_HOCPHI
-- begin TRUONGHOC_LOPHOC
create table TRUONGHOC_LOPHOC (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TENLOP_ID uuid,
    DONVI_ID uuid,
    GIAOVIENCN_ID uuid,
    --
    primary key (ID)
)^
-- end TRUONGHOC_LOPHOC
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
    UPDATE_ timestamp,
    TENDONVI varchar(255) not null,
    DONVITRUNGTAM boolean,
    --
    primary key (ID)
)^
-- end TRUONGHOC_DONVI
-- begin TRUONGHOC_CHITIETTHU
create table TRUONGHOC_CHITIETTHU (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TENPHI varchar(255),
    SOLUONG bigint,
    DONGIA bigint,
    TONGGIA bigint,
    THUTIENHOCPHI_ID uuid not null,
    --
    primary key (ID)
)^
-- end TRUONGHOC_CHITIETTHU
-- begin SEC_USER
alter table SEC_USER add column LOOCKUP_DONVI_ID uuid ^
alter table SEC_USER add column GIAOVIEN_ID uuid ^
alter table SEC_USER add column DTYPE varchar(31) ^
update SEC_USER set DTYPE = 'truonghoc_UserExt' where DTYPE is null ^
-- end SEC_USER
-- begin TRUONGHOC_DIEMDANH_HOCSINH_LINK
create table TRUONGHOC_DIEMDANH_HOCSINH_LINK (
    DIEMDANH_ID uuid,
    HOCSINH_ID uuid,
    primary key (DIEMDANH_ID, HOCSINH_ID)
)^
-- end TRUONGHOC_DIEMDANH_HOCSINH_LINK
-- begin TRUONGHOC_LOPHOC_HOCSINH_LINK
create table TRUONGHOC_LOPHOC_HOCSINH_LINK (
    LOPHOC_ID uuid,
    HOCSINH_ID uuid,
    primary key (LOPHOC_ID, HOCSINH_ID)
)^
-- end TRUONGHOC_LOPHOC_HOCSINH_LINK
