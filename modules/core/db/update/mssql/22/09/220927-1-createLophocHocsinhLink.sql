create table TRUONGHOC_LOPHOC_HOCSINH_LINK (
    LOPHOC_ID uniqueidentifier,
    HOCSINH_ID uniqueidentifier,
    primary key (LOPHOC_ID, HOCSINH_ID)
);
