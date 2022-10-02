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
    protected LookupField<Donvi> donvitao_hocsinhField;
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
    protected TextField<String> sreachHsField;
    @Inject
    protected LookupField sreachGtinhField;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected CollectionContainer<Hocsinh> hocsinhsDc;
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


    /**** tokenlist****/
    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> gioitinh = new ArrayList<>();
        gioitinh.add("Nam");
        gioitinh.add("Nữ");

        sreachGtinhField.setOptionsList(gioitinh);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {
            if (lookupActions.isVisible() == true) {
                donvitao_hocsinhField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
                donvitao_hocsinhField.setEditable(false);
                excuteSearch(true);
            }
            donvitao_hocsinhField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donvitao_hocsinhField.setEditable(false);
            excuteSearch(true);
        }
        if (lookupActions.isVisible() == true) {
            if (dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam() == null) {

            }
        }
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
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        String hocsinh = sreachHsField.getValue();
        String gioitinh = (String) sreachGtinhField.getValue();
        Object donvi =  donvitao_hocsinhField.getValue();
        Map<String, Object> params = new HashMap<>();
        String query = returnQuery(donvi, params, hocsinh, gioitinh);

        hocsinhsDl.setQuery(query);
        hocsinhsDl.setParameters(params);
        hocsinhsDl.load();
    }

    private String returnQuery(Object donvi, Map<String, Object> params, String hocsinh, String gioitinh) {
        String query = "select e from truonghoc_Hocsinh e ";
        String where = " where 1=1 ";

        // Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_hocsinh.tendonvi = :donvi ";
            params.put("donvi", donvitao_hocsinhField.getValue().getTendonvi());
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
//        }

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