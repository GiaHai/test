alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_LOPDD foreign key (LOPDD_ID) references TRUONGHOC_LOPHOC(ID);
create index IDX_TRUONGHOC_DIEMDANH_ON_LOPDD on TRUONGHOC_DIEMDANH (LOPDD_ID);