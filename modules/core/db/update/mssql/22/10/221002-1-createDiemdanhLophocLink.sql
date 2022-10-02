create table TRUONGHOC_DIEMDANH_LOPHOC_LINK (
    DIEMDANH_ID uniqueidentifier,
    LOPHOC_ID uniqueidentifier,
    primary key (DIEMDANH_ID, LOPHOC_ID)
);
