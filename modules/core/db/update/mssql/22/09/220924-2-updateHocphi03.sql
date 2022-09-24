exec sp_rename 'TRUONGHOC_HOCPHI.NAMSINH', 'NAMSINH__U84657', 'COLUMN' ^
alter table TRUONGHOC_HOCPHI add NAMSINH_ID uniqueidentifier ;
