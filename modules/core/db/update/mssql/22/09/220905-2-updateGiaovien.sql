update TRUONGHOC_GIAOVIEN set TENGIAOVIEN = '' where TENGIAOVIEN is null ;
alter table TRUONGHOC_GIAOVIEN alter column TENGIAOVIEN nvarchar(255) not null ;
update TRUONGHOC_GIAOVIEN set LUONGCOBAN = 0 where LUONGCOBAN is null ;
alter table TRUONGHOC_GIAOVIEN alter column LUONGCOBAN bigint not null ;
