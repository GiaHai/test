create table TRUONGHOC_TENLOP (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DOVI varchar(255),
    TENLOP varchar(255),
    GIAOVIENCN_ID uuid,
    --
    primary key (ID)
);