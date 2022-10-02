create table TRUONGHOC_DIEMDANH_HOCSINH_LINK (
    DIEMDANH_ID uniqueidentifier,
    HOCSINH_ID uniqueidentifier,
    primary key (DIEMDANH_ID, HOCSINH_ID)
);
