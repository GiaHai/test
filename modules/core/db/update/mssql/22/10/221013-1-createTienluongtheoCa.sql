create table TRUONGHOC_TIENLUONGTHEO_CA (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY nvarchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY nvarchar(50),
    DELETE_TS datetime2,
    DELETED_BY nvarchar(50),
    --
    CA_LAM nvarchar(255),
    SO_TIEN integer,
    --
    primary key nonclustered (ID)
);