alter table TRUONGHOC_LOPHOC_HOCSINH_LINK add constraint FK_LOPHOC_ON_LOPHOC foreign key (LOPHOC_ID) references TRUONGHOC_LOPHOC(ID);
alter table TRUONGHOC_LOPHOC_HOCSINH_LINK add constraint FK_LOPHOC_ON_HOCSINH foreign key (HOCSINH_ID) references TRUONGHOC_HOCSINH(ID);