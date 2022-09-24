exec sp_rename 'TRUONGHOC_DIEMDANH.DONVIDD', 'DONVIDD__U06010', 'COLUMN' ^
exec sp_rename 'TRUONGHOC_DIEMDANH.LOPDD', 'LOPDD__U04904', 'COLUMN' ^
exec sp_rename 'TRUONGHOC_DIEMDANH.NGUOITAODD', 'NGUOITAODD__U61130', 'COLUMN' ^
alter table TRUONGHOC_DIEMDANH add LOPDD_ID uniqueidentifier ;
alter table TRUONGHOC_DIEMDANH add NGUOITAODD_ID uniqueidentifier ;
alter table TRUONGHOC_DIEMDANH add DONVIDD_ID uniqueidentifier ;
