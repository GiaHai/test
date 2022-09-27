-- begin TRUONGHOC_THUTIENHOCPHI
alter table TRUONGHOC_THUTIENHOCPHI add constraint FK_TRUONGHOC_THUTIENHOCPHI_ON_USERTAO_THUTIENHOCPHI foreign key (USERTAO_THUTIENHOCPHI_ID) references TRUONGHOC_GIAOVIEN(ID)^
alter table TRUONGHOC_THUTIENHOCPHI add constraint FK_TRUONGHOC_THUTIENHOCPHI_ON_DONVITAO_THUTIENHOCPHI foreign key (DONVITAO_THUTIENHOCPHI_ID) references TRUONGHOC_DONVI(ID)^
alter table TRUONGHOC_THUTIENHOCPHI add constraint FK_TRUONGHOC_THUTIENHOCPHI_ON_TENHOCSINH foreign key (TENHOCSINH_ID) references TRUONGHOC_HOCSINH(ID)^
create index IDX_TRUONGHOC_THUTIENHOCPHI_ON_USERTAO_THUTIENHOCPHI on TRUONGHOC_THUTIENHOCPHI (USERTAO_THUTIENHOCPHI_ID)^
create index IDX_TRUONGHOC_THUTIENHOCPHI_ON_DONVITAO_THUTIENHOCPHI on TRUONGHOC_THUTIENHOCPHI (DONVITAO_THUTIENHOCPHI_ID)^
create index IDX_TRUONGHOC_THUTIENHOCPHI_ON_TENHOCSINH on TRUONGHOC_THUTIENHOCPHI (TENHOCSINH_ID)^
-- end TRUONGHOC_THUTIENHOCPHI
-- begin TRUONGHOC_LUONGTHANG
alter table TRUONGHOC_LUONGTHANG add constraint FK_TRUONGHOC_LUONGTHANG_ON_DONVITAO_LUONGTHANG foreign key (DONVITAO_LUONGTHANG_ID) references TRUONGHOC_DONVI(ID)^
alter table TRUONGHOC_LUONGTHANG add constraint FK_TRUONGHOC_LUONGTHANG_ON_HOVATEN foreign key (HOVATEN_ID) references TRUONGHOC_GIAOVIEN(ID)^
create index IDX_TRUONGHOC_LUONGTHANG_ON_DONVITAO_LUONGTHANG on TRUONGHOC_LUONGTHANG (DONVITAO_LUONGTHANG_ID)^
create index IDX_TRUONGHOC_LUONGTHANG_ON_HOVATEN on TRUONGHOC_LUONGTHANG (HOVATEN_ID)^
-- end TRUONGHOC_LUONGTHANG
-- begin TRUONGHOC_THUCHI
alter table TRUONGHOC_THUCHI add constraint FK_TRUONGHOC_THUCHI_ON_USERTAO_THUCHI foreign key (USERTAO_THUCHI_ID) references TRUONGHOC_GIAOVIEN(ID)^
alter table TRUONGHOC_THUCHI add constraint FK_TRUONGHOC_THUCHI_ON_DONVITAO_THUCHI foreign key (DONVITAO_THUCHI_ID) references TRUONGHOC_DONVI(ID)^
create index IDX_TRUONGHOC_THUCHI_ON_USERTAO_THUCHI on TRUONGHOC_THUCHI (USERTAO_THUCHI_ID)^
create index IDX_TRUONGHOC_THUCHI_ON_DONVITAO_THUCHI on TRUONGHOC_THUCHI (DONVITAO_THUCHI_ID)^
-- end TRUONGHOC_THUCHI
-- begin TRUONGHOC_HOCSINH
alter table TRUONGHOC_HOCSINH add constraint FK_TRUONGHOC_HOCSINH_ON_DONVITAO_HOCSINH foreign key (DONVITAO_HOCSINH_ID) references TRUONGHOC_DONVI(ID)^
create index IDX_TRUONGHOC_HOCSINH_ON_DONVITAO_HOCSINH on TRUONGHOC_HOCSINH (DONVITAO_HOCSINH_ID)^
-- end TRUONGHOC_HOCSINH
-- begin TRUONGHOC_TENLOP
alter table TRUONGHOC_TENLOP add constraint FK_TRUONGHOC_TENLOP_ON_DOVI foreign key (DOVI_ID) references TRUONGHOC_DONVI(ID)^
alter table TRUONGHOC_TENLOP add constraint FK_TRUONGHOC_TENLOP_ON_GIAOVIENCN foreign key (GIAOVIENCN_ID) references TRUONGHOC_GIAOVIEN(ID)^
create index IDX_TRUONGHOC_TENLOP_ON_DOVI on TRUONGHOC_TENLOP (DOVI_ID)^
create index IDX_TRUONGHOC_TENLOP_ON_GIAOVIENCN on TRUONGHOC_TENLOP (GIAOVIENCN_ID)^
-- end TRUONGHOC_TENLOP
-- begin TRUONGHOC_DIEMDANH
alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_NGUOITAODD foreign key (NGUOITAODD_ID) references TRUONGHOC_GIAOVIEN(ID)^
alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_LOPDD foreign key (LOPDD_ID) references TRUONGHOC_LOPHOC(ID)^
alter table TRUONGHOC_DIEMDANH add constraint FK_TRUONGHOC_DIEMDANH_ON_DONVIDD foreign key (DONVIDD_ID) references TRUONGHOC_DONVI(ID)^
create index IDX_TRUONGHOC_DIEMDANH_ON_NGUOITAODD on TRUONGHOC_DIEMDANH (NGUOITAODD_ID)^
create index IDX_TRUONGHOC_DIEMDANH_ON_LOPDD on TRUONGHOC_DIEMDANH (LOPDD_ID)^
create index IDX_TRUONGHOC_DIEMDANH_ON_DONVIDD on TRUONGHOC_DIEMDANH (DONVIDD_ID)^
-- end TRUONGHOC_DIEMDANH
-- begin TRUONGHOC_CHAMCONGGV
alter table TRUONGHOC_CHAMCONGGV add constraint FK_TRUONGHOC_CHAMCONGGV_ON_HOTENGV foreign key (HOTENGV_ID) references TRUONGHOC_GIAOVIEN(ID)^
alter table TRUONGHOC_CHAMCONGGV add constraint FK_TRUONGHOC_CHAMCONGGV_ON_DONVIGV foreign key (DONVIGV_ID) references TRUONGHOC_DONVI(ID)^
create index IDX_TRUONGHOC_CHAMCONGGV_ON_HOTENGV on TRUONGHOC_CHAMCONGGV (HOTENGV_ID)^
create index IDX_TRUONGHOC_CHAMCONGGV_ON_DONVIGV on TRUONGHOC_CHAMCONGGV (DONVIGV_ID)^
-- end TRUONGHOC_CHAMCONGGV
-- begin TRUONGHOC_GIAOVIEN
alter table TRUONGHOC_GIAOVIEN add constraint FK_TRUONGHOC_GIAOVIEN_ON_DONVITAO_GIAOVIEN foreign key (DONVITAO_GIAOVIEN_ID) references TRUONGHOC_DONVI(ID)^
create index IDX_TRUONGHOC_GIAOVIEN_ON_DONVITAO_GIAOVIEN on TRUONGHOC_GIAOVIEN (DONVITAO_GIAOVIEN_ID)^
-- end TRUONGHOC_GIAOVIEN
-- begin TRUONGHOC_HOCPHI
alter table TRUONGHOC_HOCPHI add constraint FK_TRUONGHOC_HOCPHI_ON_USERTAO_HOCPHI foreign key (USERTAO_HOCPHI_ID) references TRUONGHOC_GIAOVIEN(ID)^
alter table TRUONGHOC_HOCPHI add constraint FK_TRUONGHOC_HOCPHI_ON_DOVITAO_HOCPHI foreign key (DOVITAO_HOCPHI_ID) references TRUONGHOC_DONVI(ID)^
alter table TRUONGHOC_HOCPHI add constraint FK_TRUONGHOC_HOCPHI_ON_HOVATEN foreign key (HOVATEN_ID) references TRUONGHOC_HOCSINH(ID)^
create index IDX_TRUONGHOC_HOCPHI_ON_USERTAO_HOCPHI on TRUONGHOC_HOCPHI (USERTAO_HOCPHI_ID)^
create index IDX_TRUONGHOC_HOCPHI_ON_DOVITAO_HOCPHI on TRUONGHOC_HOCPHI (DOVITAO_HOCPHI_ID)^
create index IDX_TRUONGHOC_HOCPHI_ON_HOVATEN on TRUONGHOC_HOCPHI (HOVATEN_ID)^
-- end TRUONGHOC_HOCPHI
-- begin TRUONGHOC_LOPHOC
alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_TENLOP foreign key (TENLOP_ID) references TRUONGHOC_TENLOP(ID)^
alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_DONVI foreign key (DONVI_ID) references TRUONGHOC_DONVI(ID)^
alter table TRUONGHOC_LOPHOC add constraint FK_TRUONGHOC_LOPHOC_ON_GIAOVIENCN foreign key (GIAOVIENCN_ID) references TRUONGHOC_GIAOVIEN(ID)^
create index IDX_TRUONGHOC_LOPHOC_ON_TENLOP on TRUONGHOC_LOPHOC (TENLOP_ID)^
create index IDX_TRUONGHOC_LOPHOC_ON_DONVI on TRUONGHOC_LOPHOC (DONVI_ID)^
create index IDX_TRUONGHOC_LOPHOC_ON_GIAOVIENCN on TRUONGHOC_LOPHOC (GIAOVIENCN_ID)^
-- end TRUONGHOC_LOPHOC
-- begin TRUONGHOC_CHITIETTHU
alter table TRUONGHOC_CHITIETTHU add constraint FK_TRUONGHOC_CHITIETTHU_ON_THUTIENHOCPHI foreign key (THUTIENHOCPHI_ID) references TRUONGHOC_THUTIENHOCPHI(ID)^
create index IDX_TRUONGHOC_CHITIETTHU_ON_THUTIENHOCPHI on TRUONGHOC_CHITIETTHU (THUTIENHOCPHI_ID)^
-- end TRUONGHOC_CHITIETTHU
-- begin SEC_USER
alter table SEC_USER add constraint FK_SEC_USER_ON_LOOCKUP_DONVI foreign key (LOOCKUP_DONVI_ID) references TRUONGHOC_DONVI(ID)^
create index IDX_SEC_USER_ON_LOOCKUP_DONVI on SEC_USER (LOOCKUP_DONVI_ID)^
alter table SEC_USER add constraint FK_SEC_USER_ON_GIAOVIEN foreign key (GIAOVIEN_ID) references TRUONGHOC_GIAOVIEN(ID)^
create index IDX_SEC_USER_ON_GIAOVIEN on SEC_USER (GIAOVIEN_ID)^
-- end SEC_USER
-- begin TRUONGHOC_DIEMDANH_HOCSINH_LINK
alter table TRUONGHOC_DIEMDANH_HOCSINH_LINK add constraint FK_DIEHOC_ON_DIEMDANH foreign key (DIEMDANH_ID) references TRUONGHOC_DIEMDANH(ID)^
alter table TRUONGHOC_DIEMDANH_HOCSINH_LINK add constraint FK_DIEHOC_ON_HOCSINH foreign key (HOCSINH_ID) references TRUONGHOC_HOCSINH(ID)^
-- end TRUONGHOC_DIEMDANH_HOCSINH_LINK
-- begin TRUONGHOC_LOPHOC_HOCSINH_LINK
alter table TRUONGHOC_LOPHOC_HOCSINH_LINK add constraint FK_LOPHOC_ON_LOPHOC foreign key (LOPHOC_ID) references TRUONGHOC_LOPHOC(ID)^
alter table TRUONGHOC_LOPHOC_HOCSINH_LINK add constraint FK_LOPHOC_ON_HOCSINH foreign key (HOCSINH_ID) references TRUONGHOC_HOCSINH(ID)^
-- end TRUONGHOC_LOPHOC_HOCSINH_LINK
