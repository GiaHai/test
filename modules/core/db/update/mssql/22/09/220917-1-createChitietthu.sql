create table TRUONGHOC_CHITIETTHU (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    TENPHI nvarchar(255),
    SOLUONG bigint,
    DONGIA bigint,
    TONGGIA bigint,
    THUTIENHOCPHI_ID uniqueidentifier not null,
    --
    primary key nonclustered (ID)
);