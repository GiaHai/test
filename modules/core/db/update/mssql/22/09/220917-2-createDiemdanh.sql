alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_HOTENHS foreign key (HOTENHS_ID) references TRUONGHOC_HOCSINH(ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_HOTENHS on TRUONGHOC_DIEMDANH (HOTENHS_ID);