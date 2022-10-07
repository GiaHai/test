package com.company.truonghoc.web.screens.lophoc;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Giaovien;
import com.company.truonghoc.entity.Tenlop;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Lophoc;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("truonghoc_Lophoc.browse")
@UiDescriptor("lophoc-browse.xml")
@LookupComponent("lophocsTable")
@LoadDataBeforeShow
public class LophocBrowse extends StandardLookup<Lophoc> {
    @Inject
    protected LookupField<Giaovien> searchGvcnField;
    @Inject
    protected LookupField<Tenlop> searchLopField;
    @Inject
    protected CollectionLoader<Lophoc> lophocsDl;
    @Inject
    protected LookupField searchDvField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected SearchedService searchedService;

    @Subscribe
    protected void onInit(InitEvent event) {
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        searchDvField.setOptionsList(sessionTypeNames);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        try {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null){
                searchDvField.setEditable(false);
                searchDvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
                excuteSearch(true);
                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null){
                    searchGvcnField.setValue((dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien()));
                    searchGvcnField.setEditable(false);
                    excuteSearch(true);
                }
            }
        }catch (NullPointerException ex){

        }
    }

//    @Subscribe("lophocsTable.create")
//    protected void onLophocsTableCreate(Action.ActionPerformedEvent event) {
//        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi() != null) {
//            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() != null) {
//                this.lophocsTableCreate.execute();
//            } else {
//                dialogs.createMessageDialog()
//                        .withCaption("THÔNG BÁO")
//                        .withMessage("Bạn không có quyền")
//                        .withType(Dialogs.MessageType.WARNING)
//                        .show();
//                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
//                    this.lophocsTableCreate.execute();
//                }
//            }
//        }
//    }

    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String donvi = searchDvField.getValue().toString();
        Object tengv = searchGvcnField.getValue();
        Object tenlop = searchLopField.getValue();

        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);
        lophocsDl.setQuery(query);
        lophocsDl.setParameters(params);
        lophocsDl.load();
    }

    private String returnQuery(String donvi, Object tenlop, Object tengv, Map<String, Object> params) {
        String query = "select e from truonghoc_Lophoc e ";
        String where = " where 1=1 ";
        //Đơn vị
        if (donvi != null){
            where += "and e.donvi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        // Tên lớp
        if (tenlop != null){
            where += "and e.tenlop.tenlop = :tenlop ";
            params.put("tenlop", searchLopField.getValue().getTenlop());
        }
        // Tên giáo viên
        //load nếu lớp học bằng true thì giáo viên mới xem được
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null){
            if (tengv != null){
                where += "and e.giaoviencn.tengiaovien like :tengv and e.tenlop.tinhtranglop = true ";
                params.put("tengv", searchGvcnField.getValue().getTengiaovien());
            }
        }else {
            if (tengv != null){
                where += "and e.giaoviencn.tengiaovien like :tengv ";
                params.put("tengv", searchGvcnField.getValue().getTengiaovien());
            }
        }
        query = query + where;
        return query;
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = lophocsDl.getContainer().getItemIndex(entity.getId()) + 1;
        }
        catch (Exception ex)
        {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("searchDvField")
    protected void onSearchDvFieldValueChange(HasValue.ValueChangeEvent event) {
        searchGvcnField.setOptionsList(searchedService.loadgiaovien(searchDvField.getValue()));
    }

    @Subscribe("searchGvcnField")
    protected void onSearchGvcnFieldValueChange(HasValue.ValueChangeEvent event) {
        searchLopField.setOptionsList(searchedService.loadlop(searchDvField.getValue(), searchGvcnField.getValue().getTengiaovien()));
    }

}