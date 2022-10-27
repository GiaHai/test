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
    TEN_TINH_THANH nvarchar(255),
    GHI_CHU nvarchar(255),
    --
    primary key nonclustered (ID)
);