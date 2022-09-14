alter table TRUONGHOC_CHITIETTHU rename column ten_thutienhocphi_id to ten_thutienhocphi_id__u57686 ;
alter table TRUONGHOC_CHITIETTHU alter column ten_thutienhocphi_id__u57686 drop not null ;
-- alter table TRUONGHOC_CHITIETTHU add column THUTIENHOCPHI_ID uuid ^
-- update TRUONGHOC_CHITIETTHU set THUTIENHOCPHI_ID = <default_value> ;
-- alter table TRUONGHOC_CHITIETTHU alter column THUTIENHOCPHI_ID set not null ;
alter table TRUONGHOC_CHITIETTHU add column THUTIENHOCPHI_ID uuid not null ;
