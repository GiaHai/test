create table TRUONGHOC_GIAOVIEN (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    DONVITAO_GIAOVIEN_ID uniqueidentifier,
    TENGIAOVIEN nvarchar(255) not null,
    NGAYSINHGIAOVIEN datetime2,
    QUEQUANGIAOVIEN nvarchar(255),
    GIOITINHGIAOVIEN nvarchar(255),
    LUONGCOBAN bigint not null,
    GHICHU nvarchar(255),
    --
    primary key nonclustered (ID)
);