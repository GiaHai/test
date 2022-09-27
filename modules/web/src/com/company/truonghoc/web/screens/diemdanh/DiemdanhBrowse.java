package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Tenlop;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Diemdanh;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("truonghoc_Diemdanh.browse")
@UiDescriptor("diemdanh-browse.xml")
@LookupComponent("diemdanhsTable")
@LoadDataBeforeShow
public class DiemdanhBrowse extends StandardLookup<Diemdanh> {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected LookupField tendonviField;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionLoader<Diemdanh> diemdanhsDl;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected LookupField<Tenlop> lopField;
    @Inject
    protected LookupField<Giaovien> tengiaovienField;
    @Inject
    protected Button createBtn;
    @Named("diemdanhsTable.create")
    protected CreateAction<Diemdanh> diemdanhsTableCreate;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected SearchedService searchedService;


    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);
//        tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien().getTengiaovien());
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    @Subscribe("diemdanhsTable.create")
    protected void onDiemdanhsTableCreate(Action.ActionPerformedEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            this.diemdanhsTableCreate.execute();
        } else {
            dialogs.createMessageDialog()
                    .withCaption("THÔNG BÁO")
                    .withMessage("Bạn không có quyền")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
            System.out.println("hahah");
        }
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
            tendonviField.setEditable(false);
            tendonviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            lopField.clear();

            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                tendonviField.setEditable(false);
                tengiaovienField.setEditable(false);
                ngaylamField.clear();
                tendonviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi()); //Chèn đơn vị từ user vào text
                tengiaovienField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());  //chèn tên giáo viên từ user vào text
            }
        } else {
            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            tendonviField.setOptionsList(sessionTypeNames);

            //Xoá
            tendonviField.clear();
            tengiaovienField.clear();
            ngaylamField.clear();
            lopField.clear();
        }

    }

    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = tendonviField.getValue();
        Object giaovien = tengiaovienField.getValue();
        Date ngaylam = ngaylamField.getValue();
        Object lop = lopField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, ngaylam, lop, params);

        diemdanhsDl.setQuery(query);
        diemdanhsDl.setParameters(params);
        diemdanhsDl.load();
    }

    private String returnQuery(Object donvi, Object giaovien, Date ngaylam, Object lop, Map<String, Object> params) {
        String query = "select e from truonghoc_Diemdanh e ";
        String where = " where 1=1 ";


        //Tên đơn vị
        if (donvi != null) {
            where += "and e.donvidd.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //giáo viên
        if (giaovien != null) {
            where += "and e.nguoitaodd.tengiaovien = :giaovien ";
            params.put("giaovien", tengiaovienField.getValue().getTengiaovien());
        }
        //Ngày làm
        if (ngaylam != null) {
            where += "and e.ngaynghi = :ngaylam ";
            params.put("ngaylam", ngaylam);
        }
        //Lớp
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null){
            if (lop == null) {
                where += "and e.lopdd.tenlop.tinhtranglop = true ";
            }else {
                if (lop != null) {
                    where += "and e.lopdd.tenlop.tenlop = :lop ";
                    params.put("lop", lopField.getValue().getTenlop());
                }
            }
        }else {
            if (lop != null) {
                where += "and e.lopdd.tenlop.tenlop = :lop ";
                params.put("lop", lopField.getValue().getTenlop());
            }
        }
        query = query + where;
        return query;
    }

    @Subscribe("tendonviField")
    protected void onTendonviFieldValueChange(HasValue.ValueChangeEvent event) {
        tengiaovienField.setOptionsList(searchedService.loadgiaovien(tendonviField.getValue()));
    }

    @Subscribe("tengiaovienField")
    protected void onTengiaovienFieldValueChange(HasValue.ValueChangeEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            lopField.setOptionsList(searchedService.loadlopDK(tendonviField.getValue(), tengiaovienField.getValue().getTengiaovien()));
        } else {
            lopField.setOptionsList(searchedService.loadlop(tendonviField.getValue(), tengiaovienField.getValue().getTengiaovien()));
        }
    }

}