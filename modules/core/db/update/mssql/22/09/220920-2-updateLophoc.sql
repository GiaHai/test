exec sp_rename 'TRUONGHOC_LOPHOC.GIAOVIENCN_ID', 'GIAOVIENCN_ID__U01304', 'COLUMN' ^
alter table TRUONGHOC_LOPHOC drop constraint FK_TRUONGHOC_LOPHOC_ON_GIAOVIENCN ;
drop index IDX_TRUONGHOC_LOPHOC_ON_GIAOVIENCN on TRUONGHOC_LOPHOC ;
alter table TRUONGHOC_LOPHOC add GIAOVIENCN_ID uniqueidentifier ;