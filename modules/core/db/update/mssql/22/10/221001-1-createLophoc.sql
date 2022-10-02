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
    TENLOP_ID uniqueidentifier,
    DONVI_ID uniqueidentifier,
    GIAOVIENCN_ID uniqueidentifier,
    --
    primary key nonclustered (ID)
);