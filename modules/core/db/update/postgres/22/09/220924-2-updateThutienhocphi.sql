alter table TRUONGHOC_THUTIENHOCPHI rename column donvitao_thutienhocphi to donvitao_thutienhocphi__u56858 ;
alter table TRUONGHOC_THUTIENHOCPHI rename column usertao_thutienhocphi to usertao_thutienhocphi__u75889 ;
alter table TRUONGHOC_THUTIENHOCPHI add column USERTAO_THUTIENHOCPHI_ID uuid ;
alter table TRUONGHOC_THUTIENHOCPHI add column DONVITAO_THUTIENHOCPHI_ID uuid ;
