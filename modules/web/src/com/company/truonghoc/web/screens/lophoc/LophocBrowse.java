package com.company.truonghoc.web.screens.lophoc;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.entity.Entity;
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
    protected TextField<String> searchGvcnField;
    @Inject
    protected TextField<String> searchLopField;
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
    @Named("lophocsTable.create")
    protected CreateAction<Lophoc> lophocsTableCreate;
    @Inject
    protected Dialogs dialogs;

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
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null){
            searchDvField.setEditable(false);
            searchDvField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
            excuteSearch(true);
        }
    }

    @Subscribe("lophocsTable.create")
    protected void onLophocsTableCreate(Action.ActionPerformedEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null){
            this.lophocsTableCreate.execute();
        }else {
            dialogs.createMessageDialog()
                    .withCaption("THÔNG BÁO")
                    .withMessage("Bạn không có quyền")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
        }
    }

    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String donvi = searchDvField.getValue().toString();
        String tenlop = searchLopField.getValue();
        String tengv = searchGvcnField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, tenlop, tengv, params);

        lophocsDl.setQuery(query);
        lophocsDl.setParameters(params);
        lophocsDl.load();
    }

    private String returnQuery(String donvi, String tenlop, String tengv, Map<String, Object> params) {
        String query = "select e from truonghoc_Lophoc e ";
        String where = " where 1=1 ";
        //Đơn vị
        if (donvi != null){
            where += "and e.donvi.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        // Tên lớp
        if (!StringUtils.isEmpty(tenlop)){
            where += "and e.tenlop like :tenlop ";
            params.put("tenlop", tenlop);
        }
        // Tên giáo viên
        if (!StringUtils.isEmpty(tengv)){
            where += "and e.giaoviencn like :tengv";
            params.put("tengv", tengv);
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
}