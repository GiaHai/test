exec sp_rename 'TRUONGHOC_LOPHOC.THANG_ID', 'THANG_ID__U05163', 'COLUMN' ^
alter table TRUONGHOC_LOPHOC drop constraint FK_TRUONGHOC_LOPHOC_ON_THANG ;
drop index IDX_TRUONGHOC_LOPHOC_ON_THANG on TRUONGHOC_LOPHOC ;
exec sp_rename 'TRUONGHOC_LOPHOC.NAM_ID', 'NAM_ID__U75269', 'COLUMN' ^
alter table TRUONGHOC_LOPHOC drop constraint FK_TRUONGHOC_LOPHOC_ON_NAM ;
drop index IDX_TRUONGHOC_LOPHOC_ON_NAM on TRUONGHOC_LOPHOC ;
