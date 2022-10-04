alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_NGUOITAODD foreign key (NGUOITAODD_ID) references TRUONGHOC_GIAOVIEN(ID);
alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_LOPDD foreign key (LOPDD_ID) references TRUONGHOC_LOPHOC(ID);
alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_DONVIDD foreign key (DONVIDD_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_NGUOITAODD on TRUONGHOC_DIEMDANH (NGUOITAODD_ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_LOPDD on TRUONGHOC_DIEMDANH (LOPDD_ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_DONVIDD on TRUONGHOC_DIEMDANH (DONVIDD_ID);