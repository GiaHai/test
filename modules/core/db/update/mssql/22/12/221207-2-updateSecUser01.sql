alter table SEC_USER add constraint FK_SEC_USER_ON_LOOCKUP_DONVI foreign key (LOOCKUP_DONVI_ID) references TRUONGHOC_DONVI(ID);
create index IDX_SEC_USER_ON_LOOCKUP_DONVI on SEC_USER (LOOCKUP_DONVI_ID);