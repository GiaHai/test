alter table TRUONGHOC_THUTIENHOCPHI add constraint FK_TRUONGHOC_THUTIENHOCPHI_ON_DONVITAO_THUTIENHOCPHI foreign key (DONVITAO_THUTIENHOCPHI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_THUTIENHOCPHI_ON_DONVITAO_THUTIENHOCPHI on TRUONGHOC_THUTIENHOCPHI (DONVITAO_THUTIENHOCPHI_ID);
