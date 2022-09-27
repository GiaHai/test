package com.company.truonghoc.web.screens.hocsinh;

import com.company.truonghoc.entity.*;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
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
    protected LookupField<Giaovien> sreachgvFiled;
//    @Inject
//    protected LookupField thanghocField;
    @Inject
    protected LookupField<Lophoc> searchLopField;
    @Inject
    protected CollectionLoader<Lophoc> lophocsDl;
    @Inject
    protected CollectionContainer<Lophoc> lophocsDc;
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
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected HBoxLayout lookupActions;
    @Inject
    protected Button diemdanhBtn;


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

//        Search đơn vị
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        donvitao_hocsinhField.setOptionsList(sessionTypeNames);
//        Search giới tính
        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");

        sreachGtinhField.setOptionsList(gioitinh);

        // tháng
        List<String> thang = Arrays.asList(
                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        );
//        thanghocField.setOptionsList(thang);
    }

    @Subscribe("selectionModeField")
    protected void onSelectionModeFieldValueChange(HasValue.ValueChangeEvent<DataGrid.SelectionMode> event) {
        hocsinhsTable.setSelectionMode(event.getValue());
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (lookupActions.isVisible() == true) {
            donvitao_hocsinhField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getTendonvi());
            searchLopField.setEditable(false);
            donvitao_hocsinhField.setEditable(false);
            sreachgvFiled.setEditable(false);
            diemdanhBtn.setVisible(true);
            excuteSearch(true);
        }
    }

    @Subscribe("diemdanhBtn")
    protected void onDiemdanhBtnClick(Button.ClickEvent event) {
        sreachgvFiled.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien());
        searchLopField.setEditable(true);

    }


    //    Điều kiện là giáo viên login vào
    @Subscribe("editBtn")
    protected void onEditBtnClick(Button.ClickEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
            this.hocsinhsTableEdit.execute();
        } else {
            dialogs.createMessageDialog()
                    .withCaption("THÔNG BÁO")
                    .withMessage("Bạn không có quyền")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
        }
    }


    /*** tìm kiếm ***/
    public void timkiemExcute() {
        excuteSearch(true);
        hocsinhsTable.setVisible(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object giaovien = sreachgvFiled.getValue();
        Object lophoc = searchLopField.getValue();
        String hocsinh = sreachHsField.getValue();
        String gioitinh = (String) sreachGtinhField.getValue();
        String donvi = (String) donvitao_hocsinhField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, giaovien, params, lophoc, hocsinh, gioitinh);

        hocsinhsDl.setQuery(query);
        hocsinhsDl.setParameters(params);
        hocsinhsDl.load();
    }

    private String returnQuery(String donvi, Object giaovien, Map<String, Object> params, Object lophoc, String hocsinh, String gioitinh) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = " where 1=1 ";

        // Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_hocsinh.tendonvi = :donvi ";
            params.put("donvi", donvi);
        }
//        //Họ và tên Giáo viên

//        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() != null) {
//            // là đơn vị trung tâm
//            if (giaovien != null) {
//                where += "and e.lophoc.giaoviencn.tengiaovien = :giaoVien ";
//                params.put("giaoVien", sreachgvFiled.getValue().getTengiaovien());
//            }
//        } else {
//            // không phải dơn vị trung tâm
//            if (giaovien != null) {
//                where += "and e.lophoc.giaoviencn.tengiaovien = :giaoVien ";
//                params.put("giaoVien", sreachgvFiled.getValue().getTengiaovien());
//                //đăng nhập bằng tài khoản giáo viên và thêm học sinh và lớp theo trường hợp học sinh chưa có lớp học
//                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
//                    if (lookupActions.isVisible() == false) {
//                        where += "and e.lophoc.giaoviencn.tengiaovien = :giaoVien ";
//                        params.put("giaoVien", sreachgvFiled.getValue().getTengiaovien());
//                    }
//                }
//            } else {
//                if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getGiaovien() != null) {
//                    if (lookupActions.isVisible() == true) {
//                        where += "and e.lophoc is null ";
//                    }
//                }
//            }
        if (lookupActions.isVisible() == true) {
//            where += "and e.lophoc is null ";
        } else {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() != null) {
                // là đơn vị trung tâm
                if (giaovien != null) {
                    where += "and e.lophoc.giaoviencn.tengiaovien = :giaoVien ";
                    params.put("giaoVien", sreachgvFiled.getValue().getTengiaovien());
                }
            } else {
                where += "and e.lophoc.giaoviencn.tengiaovien = :giaoVien ";
                params.put("giaoVien", sreachgvFiled.getValue().getTengiaovien());
            }
        }
//        }
        //Lớp học
        if (lophoc != null) {
            where += "and e.lophoc.tenlop.tenlop = :lophoc ";
            params.put("lophoc", searchLopField.getValue().getTenlop().getTenlop());
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

    @Subscribe("donvitao_hocsinhField")
    protected void onDonvitao_hocsinhFieldValueChange(HasValue.ValueChangeEvent event) {
        sreachgvFiled.setOptionsList(searchedService.loadgiaovien(donvitao_hocsinhField.getValue()));
    }

    @Subscribe("sreachgvFiled")
    protected void onSreachgvFiledValueChange(HasValue.ValueChangeEvent event) {
        if (sreachgvFiled.getValue() == null) {
            searchLopField.clear();
            searchLopField.setEditable(false);
        } else {
            searchLopField.setOptionsList(loadlop());
            searchLopField.setEditable(true);
        }
    }

    private List<Lophoc> loadlop() {
        return dataManager.load(Lophoc.class)
                .query("select e from truonghoc_Lophoc e where e.donvi.tendonvi = :donvi and e.giaoviencn.tengiaovien = :giaovien")
                .parameter("donvi", donvitao_hocsinhField.getValue())
                .parameter("giaovien", sreachgvFiled.getValue().getTengiaovien())
                .list();
    }

//    private List<Hocsinh> test(){
//        return dataManager.load(Hocsinh.class)
//                .query("select e from truonghoc_Hocsinh e where e.lophocs.tenlop.tenlop is null")
//                .parameter("thanghoc", thanghocField.getValue())
//                .view("hocsinh-view")
//                .list();
//    }

//    @Subscribe("test")
//    protected void onTestClick(Button.ClickEvent event) {
//        System.out.println(test());
//    }

}