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
    --
    primary key nonclustered (ID)
);