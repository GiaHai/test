alter table SEC_USER add constraint FK_SEC_USER_ON_GIAOVIEN foreign key (GIAOVIEN_ID) references TRUONGHOC_GIAOVIEN(ID);
create index IDX_SEC_USER_ON_GIAOVIEN on SEC_USER (GIAOVIEN_ID);
