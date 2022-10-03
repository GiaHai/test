-- begin TRUONGHOC_THUTIENHOCPHI
create table TRUONGHOC_THUTIENHOCPHI (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVITAO_THUTIENHOCPHI_ID uniqueidentifier,
    TENKHACHHANG nvarchar(255),
    DIACHI nvarchar(255),
    TUNGAY datetime2,
    DENNGAY datetime2,
    NGAYTHANHTOAN datetime2,
    TENHOCSINH_ID uniqueidentifier,
    THANHTIEN bigint,
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGTHANHTOAN nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_THUTIENHOCPHI
-- begin TRUONGHOC_LUONGTHANG
create table TRUONGHOC_LUONGTHANG (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVITAO_LUONGTHANG_ID uniqueidentifier,
    HOVATEN_ID uniqueidentifier not null,
    NGAYNHAN datetime2,
    HANNHANLUONG datetime2,
    LUONGCOBAN bigint,
    TUNGAY datetime2,
    DENNGAY datetime2,
    BUOILAM decimal(19, 2),
    CANGOAI integer,
    CACHUNHAT integer,
    CASANG integer,
    THUCLINH bigint,
    DAYTHEM bigint,
    TROCAP bigint,
    TRACHNHIEM bigint,
    CHUYENCAN bigint,
    THUONG bigint,
    TIEN_BH integer,
    TONGLINH bigint,
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGNHANLUONG nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_LUONGTHANG
-- begin TRUONGHOC_THUCHI
create table TRUONGHOC_THUCHI (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVITAO_THUCHI_ID uniqueidentifier,
    KHOANCHI nvarchar(255),
    NGAYCHI datetime2,
    HANCHI datetime2,
    SOLUONG integer,
    DONGIA bigint,
    THANHTIEN bigint,
    GHICHU nvarchar(255),
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGCHI nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_THUCHI
-- begin TRUONGHOC_HOCSINH
create table TRUONGHOC_HOCSINH (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVITAO_HOCSINH_ID uniqueidentifier,
    TENHOCSINH nvarchar(255),
    NGAYSINHHOCSINH datetime2,
    GIOITINHHOCSINH nvarchar(255),
    QUEQUANHOCSINH nvarchar(255),
    GHICHU nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_HOCSINH
-- begin TRUONGHOC_TENLOP
create table TRUONGHOC_TENLOP (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DOVI_ID uniqueidentifier,
    TENLOP nvarchar(255),
    GIAOVIENCN_ID uniqueidentifier,
    THANGHOC nvarchar(255),
    NAMHOC nvarchar(255),
    TINHTRANGLOP tinyint,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_TENLOP
-- begin TRUONGHOC_DIEMDANH
create table TRUONGHOC_DIEMDANH (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    NGUOITAODD_ID uniqueidentifier,
    LOPDD_ID uniqueidentifier,
    DONVIDD_ID uniqueidentifier,
    HOTENHS_ID uniqueidentifier,
    NGAYNGHI datetime2,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_DIEMDANH
-- begin TRUONGHOC_CHAMCONGGV
create table TRUONGHOC_CHAMCONGGV (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    HOTENGV_ID uniqueidentifier,
    DONVIGV_ID uniqueidentifier,
    NGAYLAM datetime2,
    BUOILAM nvarchar(255),
    TIEN_BUOI integer,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_CHAMCONGGV
-- begin TRUONGHOC_GIAOVIEN
create table TRUONGHOC_GIAOVIEN (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVITAO_GIAOVIEN_ID uniqueidentifier,
    TENGIAOVIEN nvarchar(255) not null,
    NGAYSINHGIAOVIEN datetime2,
    QUEQUANGIAOVIEN nvarchar(255),
    GIOITINHGIAOVIEN nvarchar(255),
    LUONGCOBAN bigint not null,
    TIEN_BH integer,
    GHICHU nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_GIAOVIEN
-- begin TRUONGHOC_HOCPHI
create table TRUONGHOC_HOCPHI (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    USERTAO_HOCPHI_ID uniqueidentifier,
    DOVITAO_HOCPHI_ID uniqueidentifier,
    HOVATEN_ID uniqueidentifier,
    GHICHU nvarchar(255),
    SOTIENTAMTINH bigint,
    SOTIENTHUTHEOHD bigint,
    NGAYDONG datetime2,
    HANDONG datetime2,
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGTHANHTOAN nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_HOCPHI
-- begin TRUONGHOC_LOPHOC
create table TRUONGHOC_LOPHOC (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVI_ID uniqueidentifier,
    GIAOVIENCN_ID uniqueidentifier,
    TENLOP_ID uniqueidentifier,
    DSHOCSINH_ID uniqueidentifier,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_LOPHOC
-- begin TRUONGHOC_DONVI
create table TRUONGHOC_DONVI (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    UPDATE_ datetime2,
    TENDONVI nvarchar(255) not null,
    DONVITRUNGTAM tinyint,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_DONVI
-- begin TRUONGHOC_CHITIETTHU
create table TRUONGHOC_CHITIETTHU (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    TENPHI nvarchar(255),
    SOLUONG bigint,
    DONGIA bigint,
    TONGGIA bigint,
    THUTIENHOCPHI_ID uniqueidentifier not null,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_CHITIETTHU

-- begin SEC_USER
alter table SEC_USER add LOOCKUP_DONVI_ID uniqueidentifier ^
alter table SEC_USER add GIAOVIEN_ID uniqueidentifier ^
alter table SEC_USER add DTYPE nvarchar(31) ^
update SEC_USER set DTYPE = 'truonghoc_UserExt' where DTYPE is null ^
-- end SEC_USER
