exec sp_rename 'TRUONGHOC_TENLOP.THANGHOC', 'THANGHOC__U86114', 'COLUMN' ^
alter table TRUONGHOC_TENLOP add NAMHOC integer ;
alter table TRUONGHOC_TENLOP add THANGHOC nvarchar(255) ;
