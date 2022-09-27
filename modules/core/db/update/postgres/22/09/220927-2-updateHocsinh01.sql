alter table TRUONGHOC_HOCSINH rename column lophoc_id to lophoc_id__u06527 ;
alter table TRUONGHOC_HOCSINH drop constraint FK_TRUONGHOC_HOCSINH_ON_LOPHOC ;
drop index IDX_TRUONGHOC_HOCSINH_ON_LOPHOC ;
