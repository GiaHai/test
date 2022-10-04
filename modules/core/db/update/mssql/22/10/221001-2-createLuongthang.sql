alter table TRUONGHOC_LUONGTHANG add constraint FK_TRUONGHOC_LUONGTHANG_ON_DONVITAO_LUONGTHANG foreign key (DONVITAO_LUONGTHANG_ID) references TRUONGHOC_DONVI(ID);
alter table TRUONGHOC_LUONGTHANG add constraint FK_TRUONGHOC_LUONGTHANG_ON_HOVATEN foreign key (HOVATEN_ID) references TRUONGHOC_GIAOVIEN(ID);
create index IDX_TRUONGHOC_LUONGTHANG_ON_DONVITAO_LUONGTHANG on TRUONGHOC_LUONGTHANG (DONVITAO_LUONGTHANG_ID);
create index IDX_TRUONGHOC_LUONGTHANG_ON_HOVATEN on TRUONGHOC_LUONGTHANG (HOVATEN_ID);