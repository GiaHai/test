create table TRUONGHOC_HOCSINH (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    USERTAO_HOCSINH nvarchar(255),
    DONVITAO_HOCSINH nvarchar(255),
    TENHOCSINH nvarchar(255),
    NGAYSINHHOCSINH datetime2,
    GIOITINHHOCSINH nvarchar(255),
    QUEQUANHOCSINH nvarchar(255),
    GHICHU nvarchar(255),
    LOPHOC_ID uniqueidentifier not null,
    DIEMDANH_ID uniqueidentifier,
    --
    primary key nonclustered (ID)
);