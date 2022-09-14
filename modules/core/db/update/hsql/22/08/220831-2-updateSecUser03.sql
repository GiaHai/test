alter table SEC_USER add constraint FK_SEC_USER_ON_HAIHAI foreign key (HAIHAI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_SEC_USER_ON_HAIHAI on SEC_USER (HAIHAI_ID);
