create table TRUONGHOC_HOCPHI (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USERTAO_HOCPHI varchar(255),
    DOVITAO_HOCPHI varchar(255),
    HOVATEN_ID uuid,
    NAMSINH date,
    GHICHU varchar(255),
    SOTIENTAMTINH bigint,
    SOTIENTHUTHEOHD bigint,
    NGAYDONG date,
    HINHTHUCTHANHTOAN varchar(255),
    --
    primary key (ID)
);