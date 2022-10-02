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
    USERTAO_THUCHI_ID uniqueidentifier,
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
);