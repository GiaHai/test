alter table TRUONGHOC_LOPHOC rename column donvi to donvi__u94013 ;
alter table TRUONGHOC_LOPHOC rename column giaoviencn to giaoviencn__u48014 ;
alter table TRUONGHOC_LOPHOC rename column tenlop to tenlop__u14982 ;
alter table TRUONGHOC_LOPHOC add column TENLOP_ID uuid ;
alter table TRUONGHOC_LOPHOC add column DONVI_ID uuid ;
alter table TRUONGHOC_LOPHOC add column GIAOVIENCN_ID uuid ;
