alter table TRUONGHOC_CHAMCONGGV add constraint FK_TRUONGHOC_CHAMCONGGV_ON_HOTENGV foreign key (HOTENGV_ID) references TRUONGHOC_GIAOVIEN(ID);
create index IDX_TRUONGHOC_CHAMCONGGV_ON_HOTENGV on TRUONGHOC_CHAMCONGGV (HOTENGV_ID);