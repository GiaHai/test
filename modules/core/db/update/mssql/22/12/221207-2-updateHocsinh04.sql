alter table TRUONGHOC_HOCSINH add constraint FK_TRUONGHOC_HOCSINH_ON_NOI_SINH_TINH_THANH foreign key (NOI_SINH_TINH_THANH_ID) references TRUONGHOC_TINH_THANH(ID);
create index IDX_TRUONGHOC_HOCSINH_ON_NOI_SINH_TINH_THANH on TRUONGHOC_HOCSINH (NOI_SINH_TINH_THANH_ID);
