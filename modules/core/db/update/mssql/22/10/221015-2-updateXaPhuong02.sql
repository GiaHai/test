alter table TRUONGHOC_XA_PHUONG add constraint FK_TRUONGHOC_XA_PHUONG_ON_TINH_THANH foreign key (TINH_THANH_ID) references TRUONGHOC_TINH_THANH(ID);
create index IDX_TRUONGHOC_XA_PHUONG_ON_TINH_THANH on TRUONGHOC_XA_PHUONG (TINH_THANH_ID);