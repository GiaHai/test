package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.EditAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
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
    protected TextField<String> sreachlopField;
    @Inject
    protected TextField<String> sreachHsField;
    @Inject
    protected LookupField sreachGtinhField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Button timkiemBtn;
    @Named("hocsinhsTable.edit")
    protected EditAction<Hocsinh> hocsinhsTableEdit;
    @Inject
    protected Dialogs dialogs;

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
        Map<String, DataGrid.SelectionMode> truongchon = new LinkedHashMap<>();
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
//        khi mở màn hình với điều kiện là đơn vị không phải đơn vị chính

        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
            donvitao_hocsinhField.setEditable(false);
            donvitao_hocsinhField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());

            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
                sreachgvFiled.setEditable(false);
                sreachgvFiled.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien().getTengiaovien());
//                sreachlopField.setValue(loadlop().getTenlop());
//                sreachlopField.setEditable(false);
//                sreachlopField.setValue(dvvagvtimlop().getTenlop());

            }
        } else {
            donvitao_hocsinhField.setEditable(true);
            // lookupField cho đơn vị
            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            donvitao_hocsinhField.setOptionsList(sessionTypeNames);
            // lookupField cho Lớp học
//            lophocsDl.load();
//            List<String> listLophoc = lophocsDc.getMutableItems().stream()
//                    .map(Lophoc::getTenlop)
//                    .collect(Collectors.toList());
//            sreachlopField.setOptionsList(listLophoc);

        }
        excuteSearch(true);
    }



    //    Điều kiện là giáo viên login vào
    @Subscribe("editBtn")
    protected void onEditBtnClick(Button.ClickEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null){
            this.hocsinhsTableEdit.execute();
        }else {
            dialogs.createMessageDialog()
                    .withCaption("THÔNG BÁO")
                    .withMessage("Bạn không có quyền")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
        }
    }

    // Load lớp
    private List<Tenlop> loadlop(String donvi, String giaoviencn){
        return dataManager.load(Tenlop.class)
                .query("select e from truonghoc_Tenlop e where e.dovi.tendonvi = :donvi and e.giaoviencn.tengiaovien = :giaoviencn")
                .parameter("donvi", donvi)
                .parameter("giaoviencn", giaoviencn)
                .list();
    }
    
    /*** tìm kiếm ***/
    public void timkiemExcute() {
        excuteSearch(true);
        hocsinhsTable.setVisible(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String giaovien = sreachgvFiled.getValue();
        String lophoc =  sreachlopField.getValue();
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
        if (donvi != null) {
            where += "and e.donvitao_hocsinh.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
        //Họ và tên Giáo viên
        if (giaovien != null) {
            where += "and e.usertao_hocsinh.tengiaovien like :giaoVien ";
            params.put("giaoVien", giaovien);
        }
        //Lớp học
        if (lophoc != null) {
            where += "and e.lophoc.tenlop.tenlop = :lophoc ";
            params.put("lophoc", lophoc);
        }
        //Học sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.tenhocsinh like :tenhocsinh ";
            params.put("tenhocsinh", hocsinh);
        }
        if (gioitinh != null) {
            where += "and e.gioitinhhocsinh = :gioitinh ";
            params.put("gioitinh", gioitinh);
        }
        query = query + where;
        return query;
    }

}