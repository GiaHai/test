exec sp_rename 'TRUONGHOC_XA_PHUONG.TINH_THANH_ID', 'TINH_THANH_ID__U82162', 'COLUMN' ^
exec sp_rename 'TRUONGHOC_XA_PHUONG.QUAN_HUYEN_ID', 'QUAN_HUYEN_ID__U94899', 'COLUMN' ^
alter table TRUONGHOC_XA_PHUONG add XA_PHUONG nvarchar(255) ;
alter table TRUONGHOC_XA_PHUONG add QUAN_HUYEN_ID uniqueidentifier ;
alter table TRUONGHOC_XA_PHUONG add TINH_THANH_ID uniqueidentifier ;
