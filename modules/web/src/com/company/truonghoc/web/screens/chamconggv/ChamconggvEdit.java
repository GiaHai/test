package com.company.truonghoc.web.screens.chamconggv;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
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
    protected LookupField<String> buoilamField;
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
        List<String> buoilam = Arrays.asList("Làm cả ngày", "Ca sáng", "Ca chiều", "Ca chủ nhật", "Ca chiều 5h-6h", "Ca chiều 6h-7h");
        buoilamField.setOptionsList(buoilam);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donvigvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donvigvField.setEditable(false);
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                hotenGvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
                hotenGvField.setEditable(false);
            }
        } else {
            donvigvField.setOptionsList(searchedService.loaddonvi());
        }
    }

    @Subscribe("buoilamField")
    protected void onBuoilamFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (buoilamField.getValue() == "Ca chủ nhật") {
            tienbuoiField.setValue(Integer.valueOf("100000"));
        }
        if (buoilamField.getValue() == "Ca chiều 5h-6h" || buoilamField.getValue() == "Ca chiều 6h-7h") {
            List<Integer> tienbuoi = new ArrayList<>();
            tienbuoi.add(50000);
            tienbuoi.add(60000);
            tienbuoi.add(80000);

            tienbuoiField.setOptionsList(tienbuoi);
        }
        if (buoilamField.getValue() == "Làm cả ngày" || buoilamField.getValue() == "Ca sáng" || buoilamField.getValue() == "Ca chiều") {
            tienbuoiField.clear();
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

    public List<Chamconggv> chamCongLoadDkKhongTrung(Donvi donVi, Giaovien giaoVien, Date ngayLam, String buoiLam) {
        String query = "select e from truonghoc_Chamconggv e ";
        String where = "where e.donvigv = :donvi and e.hotengv = :giaovien " +
                "and e.ngaylam = :ngaylam and e.buoilam like :buoilam";

        Map<String, Object> params = new HashMap<>();

        params.put("donvi", donVi);
        params.put("giaovien", giaoVien);
        params.put("ngaylam", ngayLam);
        params.put("buoilam", "%"+ buoiLam +"%");
        return dataManager.load(Chamconggv.class)
                .query(query + where)
                .setParameters(params)
                .list();
    }

    @Subscribe("commitAndCloseBtn")
    protected void onCommitAndCloseBtnClick(Button.ClickEvent event) {
        Integer chamCongLoadDkKhongTrungLap = chamCongLoadDkKhongTrung(donvigvField.getValue(), hotenGvField.getValue(), ngaylamField.getValue(), buoilamField.getValue()).size();
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