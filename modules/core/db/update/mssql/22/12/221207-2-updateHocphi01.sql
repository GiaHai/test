alter table TRUONGHOC_HOCPHI add constraint FK_TRUONGHOC_HOCPHI_ON_DONVI foreign key (DONVI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_HOCPHI_ON_DONVI on TRUONGHOC_HOCPHI (DONVI_ID);