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
    GHI_CHU nvarchar(255),
    --
    primary key nonclustered (ID)
);