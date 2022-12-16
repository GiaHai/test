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
    DONVI_ID uniqueidentifier,
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
    DONVI_ID uniqueidentifier,
    HOVATEN_ID uniqueidentifier not null,
    NGAYNHAN datetime2,
    HANNHANLUONG datetime2,
    LUONGCOBAN double precision,
    TUNGAY datetime2,
    DENNGAY datetime2,
    BUOILAM double precision,
    CANGOAI integer,
    CACHIEU integer,
    SO_CA_CN integer,
    CACHUNHAT integer,
    CASANG integer,
    THUCLINH double precision,
    DAYTHEM double precision,
    TROCAP double precision,
    TRACHNHIEM double precision,
    CHUYENCAN double precision,
    THUONG double precision,
    TIEN_BH integer,
    TONGLINH double precision,
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGNHANLUONG nvarchar(255),
    GHICHU nvarchar(255),
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
    DONVI_ID uniqueidentifier,
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
    DONVI_ID uniqueidentifier,
    TENHOCSINH nvarchar(255),
    NGAYSINHHOCSINH_ID uniqueidentifier,
    GIOITINHHOCSINH nvarchar(255),
    QUEQUANHOCSINH nvarchar(255),
    NOI_SINH_XA_PHUONG_ID uniqueidentifier,
    NOI_SINH_QUAN_HUYEN_ID uniqueidentifier,
    NOI_SINH_TINH_THANH_ID uniqueidentifier,
    TINHTRANGHOCSINH tinyint,
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
    THANGHOC integer,
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
    GIAOVIENDD_ID uniqueidentifier,
    LOPDD_ID uniqueidentifier,
    DONVIDD_ID uniqueidentifier,
    HOTENHS_ID uniqueidentifier,
    NGAYNGHI datetime2,
    NGAY_HOCBU datetime2,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_DIEMDANH
-- begin TRUONGHOC_NAMSINH
create table TRUONGHOC_NAMSINH (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    NAM_SINH nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_NAMSINH
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
    BUOILAM integer,
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
    DONVI_ID uniqueidentifier,
    TENGIAOVIEN nvarchar(255) not null,
    NGAYSINHGIAOVIEN datetime2,
    QUEQUANGIAOVIEN nvarchar(255),
    GIOITINHGIAOVIEN nvarchar(255),
    LUONGCOBAN bigint not null,
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
    DONVI_ID uniqueidentifier,
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
-- begin TRUONGHOC_XA_PHUONG
create table TRUONGHOC_XA_PHUONG (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    TEN_XA_PHUONG nvarchar(255),
    QUAN_HUYEN_ID uniqueidentifier,
    TINH_THANH_ID uniqueidentifier,
    XA_PHUONG nvarchar(255),
    GHI_CHU nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_XA_PHUONG
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
-- begin TRUONGHOC_TINH_THANH
create table TRUONGHOC_TINH_THANH (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    TINH_THANHPHO nvarchar(255),
    TEN_TINH_THANH nvarchar(255),
    GHI_CHU nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_TINH_THANH
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
    SOTIENTHOAI nvarchar(255),
    DONVITRUNGTAM tinyint,
    TENQUANLY nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_DONVI
-- begin TRUONGHOC_QUAN_HUYEN
create table TRUONGHOC_QUAN_HUYEN (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    TEN_QUAN_HUYEN nvarchar(255),
    QUAN_HUYEN nvarchar(255),
    TINH_THANH_ID uniqueidentifier,
    GHI_CHU nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_QUAN_HUYEN
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
