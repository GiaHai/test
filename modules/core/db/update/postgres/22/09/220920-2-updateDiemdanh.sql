alter table TRUONGHOC_DIEMDANH rename column ngaynghi to ngaynghi__u72544 ;
alter table TRUONGHOC_DIEMDANH rename column hotenhs_id to hotenhs_id__u29019 ;
alter table TRUONGHOC_DIEMDANH drop constraint FK_TRUONGHOC_DIEMDANH_ON_HOTENHS ;
drop index IDX_TRUONGHOC_DIEMDANH_ON_HOTENHS ;
alter table TRUONGHOC_DIEMDANH add column NGAYNGHI date ;
