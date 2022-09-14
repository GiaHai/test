update TRUONGHOC_DONVI set TENDONVI = '' where TENDONVI is null ;
alter table TRUONGHOC_DONVI alter column TENDONVI nvarchar(255) not null ;
