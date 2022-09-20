alter table TRUONGHOC_HOCSINH rename column usertao_hocsinh to usertao_hocsinh__u41311 ;
alter table TRUONGHOC_HOCSINH add column DIEMDANH_ID uuid ;
-- alter table TRUONGHOC_HOCSINH add column LOPHOC_ID uuid ^
-- update TRUONGHOC_HOCSINH set LOPHOC_ID = <default_value> ;
-- alter table TRUONGHOC_HOCSINH alter column LOPHOC_ID set not null ;
alter table TRUONGHOC_HOCSINH add column LOPHOC_ID uuid not null ;
alter table TRUONGHOC_HOCSINH add column USERTAO_HOCSINH timestamp ;
