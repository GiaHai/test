create table TRUONGHOC_DIEMDANH (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    NGUOITAODD_ID uniqueidentifier,
    LOPDD_ID uniqueidentifier,
    DONVIDD_ID uniqueidentifier,
    NGAYNGHI datetime2,
    --
    primary key nonclustered (ID)
);