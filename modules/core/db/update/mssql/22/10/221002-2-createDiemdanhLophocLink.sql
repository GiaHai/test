alter table TRUONGHOC_DIEMDANH_LOPHOC_LINK add constraint FK_DIELOP_ON_DIEMDANH foreign key (DIEMDANH_ID) references TRUONGHOC_DIEMDANH(ID);
alter table TRUONGHOC_DIEMDANH_LOPHOC_LINK add constraint FK_DIELOP_ON_LOPHOC foreign key (LOPHOC_ID) references TRUONGHOC_LOPHOC(ID);