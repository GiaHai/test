create table TRUONGHOC_DONVI (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    UPDATE_ datetime2,
    TENDONVI nvarchar(255) not null,
    DONVITRUNGTAM tinyint,
    --
    primary key nonclustered (ID)
);