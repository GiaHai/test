alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_GIAOVIENDD foreign key (GIAOVIENDD_ID) references TRUONGHOC_GIAOVIEN(ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_GIAOVIENDD on TRUONGHOC_DIEMDANH (GIAOVIENDD_ID);
