package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.enums.BuoiLamEnum;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chamconggv;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;

@UiController("truonghoc_Chamconggv.edit")
@UiDescriptor("chamconggv-edit.xml")
@EditedEntityContainer("chamconggvDc")
@LoadDataBeforeShow
public class ChamconggvEdit extends StandardEditor<Chamconggv> {
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField<Integer> tienbuoiField;
    @Inject
    protected LookupField<Giaovien> hotenGvField;
    @Inject
    protected LookupField<Donvi> donvigvField;
    @Inject
    protected LookupField<Integer> buoilamField;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected Button commitAndCloseBtn;
    @Inject
    protected Notifications notifications;

    @Subscribe
    protected void onInit(InitEvent event) {
        hotenGvField.setRequired(true);
        ngaylamField.setRequired(true);


        Map<String, Integer> buoiLam = new LinkedHashMap<>();
        buoiLam.put("Làm cả ngày", BuoiLamEnum.LAM_CA_NGAY.getId());
        buoiLam.put("Ca sáng", BuoiLamEnum.CA_SANG.getId());
        buoiLam.put("Ca chiều", BuoiLamEnum.CA_CHIEU.getId());
        buoiLam.put("Ca chủ nhật", BuoiLamEnum.CA_CHU_NHAT.getId());
        buoiLam.put("Ca chiều 5h-6h", BuoiLamEnum.CA_CHIEU_5H_6H.getId());
        buoiLam.put("Ca chiều 6h-7h", BuoiLamEnum.CA_CHIEU_6H_7H.getId());
        buoilamField.setOptionsMap(buoiLam);


//        List<String> buoilam = Arrays.asList("Làm cả ngày", "Ca sáng", "Ca chiều", "Ca chủ nhật", "Ca chiều 5h-6h", "Ca chiều 6h-7h");

    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donvigvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donvigvField.setEditable(false);
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                hotenGvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                hotenGvField.setEditable(false);
                ngaylamField.setValue(new Date());
//                ngaylamField.setEditable(false);
            }
        } else {
            donvigvField.setOptionsList(searchedService.loaddonvi());
        }

    }

    @Subscribe("buoilamField")
    protected void onBuoilamFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (buoilamField.getValue() == BuoiLamEnum.CA_CHU_NHAT.getId()) {
            tienbuoiField.clear();

            tienbuoiField.setValue(Integer.valueOf("100000"));
            tienbuoiField.setEditable(false);
        }
        if (buoilamField.getValue() == BuoiLamEnum.CA_CHIEU_5H_6H.getId() || buoilamField.getValue() == BuoiLamEnum.CA_CHIEU_6H_7H.getId()) {
            tienbuoiField.clear();

            List<Integer> tienbuoi = new ArrayList<>();
            tienbuoi.add(50000);
            tienbuoi.add(60000);
            tienbuoi.add(80000);
            tienbuoiField.setRequired(true);
            tienbuoiField.setEditable(true);

            tienbuoiField.setOptionsList(tienbuoi);
        }
        if (buoilamField.getValue() == BuoiLamEnum.LAM_CA_NGAY.getId() || buoilamField.getValue() == BuoiLamEnum.CA_SANG.getId() || buoilamField.getValue() == BuoiLamEnum.CA_CHIEU.getId()) {
            tienbuoiField.clear();
            tienbuoiField.setRequired(false);

        }
    }

    @Subscribe("donvigvField")
    protected void onDonvigvFieldValueChange(HasValue.ValueChangeEvent<Donvi> event) {
        if (donvigvField.getValue() != null) {
            hotenGvField.setOptionsList(timtengiaovien(donvigvField.getValue()));
        } else {
            hotenGvField.clear();
        }
    }

    public List<Giaovien> timtengiaovien(Donvi donvitao) {
        return dataManager.load(Giaovien.class)
                .query("select e from truonghoc_Giaovien e where e.donvi = :donvitao")
                .parameter("donvitao", donvitao)
                .list();
    }

    public List<Chamconggv> chamCongLoadDkKhongTrung(Donvi donVi, Giaovien giaoVien, Date ngayLam, Integer buoiLam) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = "where e.donvigv = :donvi and e.hotengv = :giaovien " +
                "and e.ngaylam = :ngaylam and e.buoilam = :buoilam";

        Map<String, Object> params = new HashMap<>();

        params.put("donvi", donVi);
        params.put("giaovien", giaoVien);
        params.put("ngaylam", ngayLam);
        params.put("buoilam", buoiLam);
        return dataManager.load(Chamconggv.class)
                .query(query + where)
                .setParameters(params)
                .list();
    }
    public List<Chamconggv> chamCongLoadDkKhongTrungVoiCaNgay(Donvi donVi, Giaovien giaoVien, Date ngayLam) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = "where e.donvigv = :donvi and e.hotengv = :giaovien and e.ngaylam = :ngaylam" +
                " and e.buoilam in ("+ BuoiLamEnum.CA_CHIEU.getId()+ "," + BuoiLamEnum.CA_SANG.getId() + "," + BuoiLamEnum.LAM_CA_NGAY.getId() +")";

        Map<String, Object> params = new HashMap<>();

        params.put("donvi", donVi);
        params.put("giaovien", giaoVien);
        params.put("ngaylam", ngayLam);
        return dataManager.load(Chamconggv.class)
                .query(query + where)
                .setParameters(params)
                .list();
    }

    @Subscribe("commitAndCloseBtn")
    protected void onCommitAndCloseBtnClick(Button.ClickEvent event) {
        Integer chamCongLoadDkKhongTrungVoiCaNgay = chamCongLoadDkKhongTrungVoiCaNgay(donvigvField.getValue(), hotenGvField.getValue(), ngaylamField.getValue()).size();
        Integer chamCongLoadDkKhongTrungLap = chamCongLoadDkKhongTrung(donvigvField.getValue(), hotenGvField.getValue(), ngaylamField.getValue(), buoilamField.getValue()).size();

        if (buoilamField.getValue() == BuoiLamEnum.CA_CHIEU.getId() || buoilamField.getValue() == BuoiLamEnum.CA_SANG.getId() || buoilamField.getValue() == BuoiLamEnum.LAM_CA_NGAY.getId())
        {
            if (chamCongLoadDkKhongTrungVoiCaNgay == 0){
                closeWithCommit();
            }else {
                notifications.create()
                        .withCaption("Bạn đã điểm danh cho buổi này rồi !")
                        .withPosition(Notifications.Position.BOTTOM_RIGHT)
                        .withType(Notifications.NotificationType.HUMANIZED)
                        .show();
                buoilamField.clear();
                tienbuoiField.clear();
            }
        }else {
            if (chamCongLoadDkKhongTrungLap == 0){
                closeWithCommit();
            }else {
                notifications.create()
                        .withCaption("Bạn đã điểm danh cho buổi này rồi !")
                        .withPosition(Notifications.Position.BOTTOM_RIGHT)
                        .withType(Notifications.NotificationType.HUMANIZED)
                        .show();
                buoilamField.clear();
            }
        }
    }


}