alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_DONVI foreign key (DONVI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_LOPHOC_ON_DONVI on TRUONGHOC_LOPHOC (DONVI_ID);