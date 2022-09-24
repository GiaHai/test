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
    THANHTIEN bigint,
    HINHTHUCTHANHTOAN nvarchar(255),
    TINHTRANGTHANHTOAN nvarchar(255),
    --
    primary key nonclustered (ID)
);