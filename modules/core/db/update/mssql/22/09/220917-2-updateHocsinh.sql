-- alter table TRUONGHOC_HOCSINH add LOPHOC_ID uniqueidentifier ^
-- update TRUONGHOC_HOCSINH set LOPHOC_ID = <default_value> ;
-- alter table TRUONGHOC_HOCSINH alter column LOPHOC_ID uniqueidentifier not null ;
alter table TRUONGHOC_HOCSINH add LOPHOC_ID uniqueidentifier not null ;
