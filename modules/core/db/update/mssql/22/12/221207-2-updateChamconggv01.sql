alter table TRUONGHOC_CHAMCONGGV add constraint FK_TRUONGHOC_CHAMCONGGV_ON_DONVIGV foreign key (DONVIGV_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_CHAMCONGGV_ON_DONVIGV on TRUONGHOC_CHAMCONGGV (DONVIGV_ID);
