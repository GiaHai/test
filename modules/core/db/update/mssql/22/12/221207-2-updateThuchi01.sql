alter table TRUONGHOC_THUCHI add constraint FK_TRUONGHOC_THUCHI_ON_DONVI foreign key (DONVI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_TRUONGHOC_THUCHI_ON_DONVI on TRUONGHOC_THUCHI (DONVI_ID);
