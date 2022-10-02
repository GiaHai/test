alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_TENLOP foreign key (TENLOP_ID) references TRUONGHOC_TENLOP(ID);
alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_DONVI foreign key (DONVI_ID) references TRUONGHOC_DONVI(ID);
alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_GIAOVIENCN foreign key (GIAOVIENCN_ID) references TRUONGHOC_GIAOVIEN(ID);
create index IDX_TRUONGHOC_LOPHOC_ON_TENLOP on TRUONGHOC_LOPHOC (TENLOP_ID);
create index IDX_TRUONGHOC_LOPHOC_ON_DONVI on TRUONGHOC_LOPHOC (DONVI_ID);
create index IDX_TRUONGHOC_LOPHOC_ON_GIAOVIENCN on TRUONGHOC_LOPHOC (GIAOVIENCN_ID);
