alter table TRUONGHOC_HOCPHI rename column namsinh to namsinh__u88736 ;
alter table TRUONGHOC_HOCPHI rename column dovitao_hocphi to dovitao_hocphi__u29092 ;
alter table TRUONGHOC_HOCPHI rename column usertao_hocphi to usertao_hocphi__u78003 ;
alter table TRUONGHOC_HOCPHI add column DOVITAO_HOCPHI_ID uuid ;
alter table TRUONGHOC_HOCPHI add column USERTAO_HOCPHI_ID uuid ;
