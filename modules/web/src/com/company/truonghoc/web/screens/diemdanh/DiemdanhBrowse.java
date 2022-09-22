package com.company.truonghoc.web.screens.diemdanh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
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
    protected TextField<String> tengiaovienField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DateField<Date> ngaylamField;
    @Inject
    protected TextField<String> lopField;
    @Inject
    protected Button createBtn;
    @Named("diemdanhsTable.create")
    protected CreateAction<Diemdanh> diemdanhsTableCreate;
    @Inject
    protected Dialogs dialogs;


    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
        excuteSearch(true);
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    @Subscribe("diemdanhsTable.create")
    protected void onDiemdanhsTableCreate(Action.ActionPerformedEvent event) {
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null){
            createBtn.setAction(diemdanhsTableCreate);
        }else {
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
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0) {
            tendonviField.setEditable(false);
            tendonviField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()); //Chèn đơn vị từ user vào text
            //Xoá
            tengiaovienField.clear();
            ngaylamField.clear();
            lopField.clear();
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
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null) {
            tendonviField.setEditable(false);
            tengiaovienField.setEditable(false);
            ngaylamField.clear();
            lopField.setVisible(false);
            tendonviField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()); //Chèn đơn vị từ user vào text
            tengiaovienField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv());  //chèn tên giáo viên từ user vào text
        }
    }

    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = tendonviField.getValue();
        String giaovien = tengiaovienField.getValue();
        Date ngaylam = ngaylamField.getValue();
        String lop = lopField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, ngaylam, lop, params);

        diemdanhsDl.setQuery(query);
        diemdanhsDl.setParameters(params);
        diemdanhsDl.load();
    }

    private String returnQuery(Object donvi, String giaovien, Date ngaylam, String lop, Map<String, Object> params) {
        String query = "select e from truonghoc_Diemdanh e ";
        String where = " where 1=1 ";

        //Tên đơn vị
        if (donvi != null) {
            where += "and e.donvidd = :donvi ";
            params.put("donvi", donvi);
        }
        //giáo viên
        if (!StringUtils.isEmpty(giaovien)) {
            where += "and e.nguoitaodd = :giaovien ";
            params.put("giaovien", giaovien);
        }
        //Ngày làm
        if (ngaylam != null) {
            where += "and e.ngaynghi = :ngaylam ";
            params.put("ngaylam", ngaylam);
        }
        //Lớp
        if (!StringUtils.isEmpty(lop)) {
            where += "and e.lopdd = :lop ";
            params.put("lop", lop);
        }
        query = query + where;
        return query;
    }
}