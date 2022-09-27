alter table TRUONGHOC_HOCSINH rename column usertao_hocsinh_id to usertao_hocsinh_id__u02745 ;
alter table TRUONGHOC_HOCSINH drop constraint FK_TRUONGHOC_HOCSINH_ON_USERTAO_HOCSINH ;
drop index IDX_TRUONGHOC_HOCSINH_ON_USERTAO_HOCSINH ;
alter table TRUONGHOC_HOCSINH rename column diemdanh_id to diemdanh_id__u58860 ;
alter table TRUONGHOC_HOCSINH drop constraint FK_TRUONGHOC_HOCSINH_ON_DIEMDANH ;
drop index IDX_TRUONGHOC_HOCSINH_ON_DIEMDANH ;
alter table TRUONGHOC_HOCSINH alter column LOPHOC_ID drop not null ;
