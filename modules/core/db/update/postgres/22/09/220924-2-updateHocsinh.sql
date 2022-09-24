alter table TRUONGHOC_HOCSINH rename column usertao_hocsinh to usertao_hocsinh__u00443 ;
alter table TRUONGHOC_HOCSINH rename column donvitao_hocsinh to donvitao_hocsinh__u36787 ;
alter table TRUONGHOC_HOCSINH add column DONVITAO_HOCSINH_ID uuid ;
alter table TRUONGHOC_HOCSINH add column USERTAO_HOCSINH_ID uuid ;
