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
    THANGHOC nvarchar(255),
    NAMHOC integer,
    TINHTRANGLOP tinyint,
    --
    primary key nonclustered (ID)
);