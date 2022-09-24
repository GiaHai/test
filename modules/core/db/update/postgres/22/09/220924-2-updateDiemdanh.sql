alter table TRUONGHOC_DIEMDANH rename column lopdd to lopdd__u97871 ;
alter table TRUONGHOC_DIEMDANH rename column donvidd to donvidd__u94289 ;
alter table TRUONGHOC_DIEMDANH rename column nguoitaodd to nguoitaodd__u32454 ;
alter table TRUONGHOC_DIEMDANH add column LOPDD_ID uuid ;
alter table TRUONGHOC_DIEMDANH add column NGUOITAODD_ID uuid ;
alter table TRUONGHOC_DIEMDANH add column DONVIDD_ID uuid ;
