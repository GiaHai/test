alter table TRUONGHOC_HOCPHI add constraint FK_TRUONGHOC_HOCPHI_ON_USERTAO_HOCPHI foreign key (USERTAO_HOCPHI_ID) references TRUONGHOC_GIAOVIEN(ID);
create index IDX_TRUONGHOC_HOCPHI_ON_USERTAO_HOCPHI on TRUONGHOC_HOCPHI (USERTAO_HOCPHI_ID);
