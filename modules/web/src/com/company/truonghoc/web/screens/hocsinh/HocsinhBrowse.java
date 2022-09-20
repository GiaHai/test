package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Lophoc;
import com.company.truonghoc.entity.UserExt;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocsinh;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Hocsinh.browse")
@UiDescriptor("hocsinh-browse.xml")
@LookupComponent("hocsinhsTable")
@LoadDataBeforeShow
public class HocsinhBrowse extends StandardLookup<Hocsinh> {
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField donvitao_hocsinhField;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected CollectionLoader<Hocsinh> hocsinhsDl;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected LookupField<DataGrid.SelectionMode> selectionModeField;
    @Inject
    protected DataGrid<Hocsinh> hocsinhsTable;
    @Inject
    protected TextField<String> sreachgvFiled;
    @Inject
    protected CollectionLoader<Lophoc> lophocsDl;
    @Inject
    protected CollectionContainer<Lophoc> lophocsDc;
    @Inject
    protected LookupField sreachlopField;
    @Inject
    protected TextField<String> sreachHsField;
    @Inject
    protected LookupField sreachGtinhField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Button timkiemBtn;

    /**** tokenlist****/
    @Subscribe
    protected void onInit(InitEvent event) {
        Map<String, DataGrid.SelectionMode> selectionModeMap = new LinkedHashMap<>();
        for (DataGrid.SelectionMode mode : DataGrid.SelectionMode.values()) {
            selectionModeMap.put(mode.name(), mode);
        }
        selectionModeField.setOptionsMap(selectionModeMap);
        selectionModeField.setValue(hocsinhsTable.getSelectionMode());
//        Chọn trường trong token list Field
        Map<String , DataGrid.SelectionMode> truongchon = new LinkedHashMap<>();
        truongchon.put("Chọn một trường", DataGrid.SelectionMode.SINGLE);
        truongchon.put("Chọn nhiều trường", DataGrid.SelectionMode.MULTI_CHECK);

        selectionModeField.setOptionsMap(truongchon);
//        Search giới tính
        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");

        sreachGtinhField.setOptionsList(gioitinh);
    }

    @Subscribe("selectionModeField")
    protected void onSelectionModeFieldValueChange(HasValue.ValueChangeEvent<DataGrid.SelectionMode> event) {
        hocsinhsTable.setSelectionMode(event.getValue());
    }
    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0){
            donvitao_hocsinhField.setEditable(false);
            donvitao_hocsinhField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi());
        }else {
            donvitao_hocsinhField.setEditable(true);

            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            donvitao_hocsinhField.setOptionsList(sessionTypeNames);

            lophocsDl.load();
            List<String> listLophoc = lophocsDc.getMutableItems().stream()
                    .map(Lophoc::getTenlop)
                    .collect(Collectors.toList());
            sreachlopField.setOptionsList(listLophoc);
        }
        if (tenGv().getTextgv() == null){
            dkdonvi();
        }else {
            donvitao_hocsinhField.setValue(tenGv().getTendonvi());
            sreachgvFiled.setValue(tenGv().getTextgv());

            donvitao_hocsinhField.setEditable(false);
            sreachgvFiled.setEditable(false);
            hocsinhsTable.setVisible(false);
        }
    }
//    Điều kiện là giáo viên login vào
    private UserExt tenGv(){
        return dataManager.load(UserExt.class)
                .query("Select e from truonghoc_UserExt e where e.login = :admin")
                .parameter("admin", userSession.getUser().getLogin())
                .one();
    }
//    public Component stt(Hocsinh entity) {
//        int lineNumber = 1;
//        try {
//            lineNumber = hocsinhsDl.getContainer().getItemIndex(entity.getId()) + 1;
//        }
//        catch (Exception ex)
//        {
//            lineNumber = 1;
//        }
//        Label field = uiComponents.create(Label.NAME);
//        field.setValue(lineNumber);
//        return field;
//    }
    /*** tìm kiếm ***/
    public void timkiemExcute() {
        excuteSearch(true);
        hocsinhsTable.setVisible(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String giaovien = sreachgvFiled.getValue();
        String lophoc = (String) sreachlopField.getValue();
        String hocsinh = sreachHsField.getValue();
        String gioitinh = (String) sreachGtinhField.getValue();
        String donvi = (String) donvitao_hocsinhField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, params, lophoc, hocsinh, gioitinh);

        hocsinhsDl.setQuery(query);
        hocsinhsDl.setParameters(params);
        hocsinhsDl.load();
    }

    private String returnQuery(String donvi, String giaovien, Map<String, Object> params, String lophoc, String hocsinh, String gioitinh) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = " where 1=1 ";

        // Đơn vị
        if (donvi != null){
            where += "and e.donvitao_hocsinh = :donvi ";
            params.put("donvi", donvi);
        }
        //Họ và tên Giáo viên
        if (!StringUtils.isEmpty(giaovien)) {
            where += "and e.usertao_hocsinh like :giaoVien ";
            params.put("giaoVien", "%" + giaovien + "%");
        }
        //Lớp học
        if (lophoc != null){
            where += "and e.lophoc.tenlop = :lophoc ";
            params.put("lophoc", lophoc);
        }
        //Học sinh
        if (!StringUtils.isEmpty(hocsinh)){
            where +="and e.tenhocsinh like :tenhocsinh ";
            params.put("tenhocsinh", hocsinh);
        }
        if (gioitinh != null){
            where += "and e.gioitinhhocsinh = :gioitinh ";
            params.put("gioitinh", gioitinh);
        }
        query = query + where;
        return query;
    }

    /*** các điều kiện **/

    @Subscribe("donvitao_hocsinhField")
    protected void onDonvitao_hocsinhFieldValueChange(HasValue.ValueChangeEvent event) {
        dkdonvi();
    }
    private void dkdonvi(){
        if (donvitao_hocsinhField.getValue() == null){
            sreachHsField.setEditable(false);
            sreachGtinhField.setEditable(false);
            sreachgvFiled.setEditable(false);
            sreachlopField.setEditable(false);
        }else {
            sreachHsField.setEditable(true);
            sreachGtinhField.setEditable(true);
            sreachgvFiled.setEditable(true);
            sreachlopField.setEditable(true);
        }
    }

}