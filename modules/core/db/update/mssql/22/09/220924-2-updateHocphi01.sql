alter table TRUONGHOC_HOCPHI add constraint FK_TRUONGHOC_HOCPHI_ON_DOVITAO_HOCPHI foreign key (DOVITAO_HOCPHI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_HOCPHI_ON_DOVITAO_HOCPHI on TRUONGHOC_HOCPHI (DOVITAO_HOCPHI_ID);
