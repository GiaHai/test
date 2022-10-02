exec sp_rename 'TRUONGHOC_TENLOP.NAMHOC', 'NAMHOC__U01400', 'COLUMN' ^
alter table TRUONGHOC_TENLOP add NAMHOC nvarchar(255) ;
