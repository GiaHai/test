alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_DONVIDD foreign key (DONVIDD_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_DONVIDD on TRUONGHOC_DIEMDANH (DONVIDD_ID);
