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
);