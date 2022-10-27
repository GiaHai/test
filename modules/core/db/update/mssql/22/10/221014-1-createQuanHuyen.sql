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
    TINH_THANH_ID uniqueidentifier,
    GHI_CHU nvarchar(255),
    --
    primary key nonclustered (ID)
);