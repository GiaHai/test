alter table TRUONGHOC_THUCHI rename column donvitao_thuchi to donvitao_thuchi__u53266 ;
alter table TRUONGHOC_THUCHI rename column usertao_thuchi to usertao_thuchi__u23768 ;
alter table TRUONGHOC_THUCHI add column USERTAO_THUCHI_ID uuid ;
alter table TRUONGHOC_THUCHI add column DONVITAO_THUCHI_ID uuid ;
