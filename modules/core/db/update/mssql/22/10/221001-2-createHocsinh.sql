alter table TRUONGHOC_HOCSINH add constraint FK_TRUONGHOC_HOCSINH_ON_DONVITAO_HOCSINH foreign key (DONVITAO_HOCSINH_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_HOCSINH_ON_DONVITAO_HOCSINH on TRUONGHOC_HOCSINH (DONVITAO_HOCSINH_ID);