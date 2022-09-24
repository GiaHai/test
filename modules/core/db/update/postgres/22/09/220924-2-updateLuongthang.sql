alter table TRUONGHOC_LUONGTHANG rename column donvitao_luongthang to donvitao_luongthang__u58769 ;
alter table TRUONGHOC_LUONGTHANG rename column usertao_luongthang to usertao_luongthang__u12278 ;
alter table TRUONGHOC_LUONGTHANG add column DONVITAO_LUONGTHANG_ID uuid ;
alter table TRUONGHOC_LUONGTHANG add column USERTAO_LUONGTHANG_ID uuid ;
