exec sp_rename 'TRUONGHOC_LUONGTHANG.TIEN_BH_ID', 'TIEN_BH_ID__U27782', 'COLUMN' ^
alter table TRUONGHOC_LUONGTHANG drop constraint FK_TRUONGHOC_LUONGTHANG_ON_TIEN_BH ;
drop index IDX_TRUONGHOC_LUONGTHANG_ON_TIEN_BH on TRUONGHOC_LUONGTHANG ;
alter table TRUONGHOC_LUONGTHANG add TIEN_BH integer ;