exec sp_rename 'TRUONGHOC_LOPHOC.DONVI', 'DONVI__U03069', 'COLUMN' ^
alter table TRUONGHOC_LOPHOC add DONVI_ID uniqueidentifier ;
