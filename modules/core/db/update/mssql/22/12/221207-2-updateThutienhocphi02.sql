alter table TRUONGHOC_THUTIENHOCPHI add constraint FK_TRUONGHOC_THUTIENHOCPHI_ON_TENHOCSINH foreign key (TENHOCSINH_ID) references TRUONGHOC_HOCSINH(ID);
create index IDX_TRUONGHOC_THUTIENHOCPHI_ON_TENHOCSINH on TRUONGHOC_THUTIENHOCPHI (TENHOCSINH_ID);
