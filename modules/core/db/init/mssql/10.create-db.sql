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
    USERTAO_LUONGTHANG nvarchar(255),
    DONVITAO_LUONGTHANG nvarchar(255),
    HOVATEN_ID uniqueidentifier not null,
    NGAYNHAN datetime2,
    HANNHANLUONG datetime2,
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
    USERTAO_THUCHI nvarchar(255),
    DONVITAO_THUCHI nvarchar(255),
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
    USERTAO_HOCSINH nvarchar(255),
    DONVITAO_HOCSINH nvarchar(255),
    TENHOCSINH nvarchar(255),
    NGAYSINHHOCSINH datetime2,
    GIOITINHHOCSINH nvarchar(255),
    QUEQUANHOCSINH nvarchar(255),
    GHICHU nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_HOCSINH
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
    USERTAO_GIAOVIEN nvarchar(255),
    DONVITAO_GIAOVIEN nvarchar(255),
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
    USERTAO_HOCPHI nvarchar(255),
    DOVITAO_HOCPHI nvarchar(255),
    HOVATEN_ID uniqueidentifier,
    NAMSINH datetime2,
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
    THU bigint,
    CHI bigint,
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_DONVI
-- begin SEC_USER
alter table SEC_USER add LOOCKUP_DONVI_ID uniqueidentifier ^
alter table SEC_USER add TENDONVI nvarchar(255) ^
alter table SEC_USER add DONVITRUNGTAM tinyint ^
alter table SEC_USER add DTYPE nvarchar(31) ^
update SEC_USER set DTYPE = 'truonghoc_UserExt' where DTYPE is null ^
-- end SEC_USER
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
    USERTAO_THUTIENHOCPHI nvarchar(255),
    DONVITAO_THUTIENHOCPHI nvarchar(255),
    TENKHACHHANG nvarchar(255),
    DIACHI nvarchar(255),
    TUNGAY datetime2,
    DENNGAY datetime2,
    TENHOCSINH_ID uniqueidentifier,
    TENPHI nvarchar(255),
    SOLUONG bigint,
    DONGIA bigint,
    THANHTIEN bigint,
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGTHANHTOAN nvarchar(255),
    --
    primary key nonclustered (ID)
)^
-- end TRUONGHOC_THUTIENHOCPHI
