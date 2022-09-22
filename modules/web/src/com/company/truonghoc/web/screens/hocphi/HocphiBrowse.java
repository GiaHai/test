package com.company.truonghoc.web.screens.hocphi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Hocphi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Hocphi.browse")
@UiDescriptor("hocphi-browse.xml")
@LookupComponent("hocphisTable")
@LoadDataBeforeShow
public class HocphiBrowse extends StandardLookup<Hocphi> {
    @Inject
    protected CollectionLoader<Hocphi> hocphisDl;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected LookupField dovitao_hocphiField;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected ScreenBuilders screenBuilders;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected LookupField trangthaiField;
    @Inject
    protected TextField<String> hovstenField;
    @Inject
    protected TextField<String> giaovienField;
    @Inject
    protected Notifications notifications;
    @Named("hocphisTable.create")
    protected CreateAction<Hocphi> hocphisTableCreate;
    @Inject
    protected Button createBtn;
    @Inject
    protected Dialogs dialogs;

    @Subscribe
    protected void onInit(InitEvent event) {
        List<String> list = Arrays.asList("Đã thanh toán", "Chưa thanh toán");
        trangthaiField.setOptionsList(list);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        dkphanquyen();
    }

    @Subscribe("hocphisTable.create")
    protected void onHocphisTableCreate(Action.ActionPerformedEvent event) {
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null){
            this.hocphisTableCreate.execute();
        }else {
            dialogs.createMessageDialog()
                    .withCaption("THÔNG BÁO")
                    .withMessage("Bạn không có quyền")
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
            System.out.println("hahah");
        }
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        excuteSearch(true);
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = hocphisDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0) {
            dovitao_hocphiField.setEditable(false);
            dovitao_hocphiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()); //Chèn đơn vị từ user vào text
            //Xoá
            denngayField.clear();
            tungayField.clear();
            trangthaiField.clear();
            giaovienField.clear();
            hovstenField.clear();
        } else {
            dovitao_hocphiField.setEditable(true);
            //lấy dữ liệu string cho lookup
            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            dovitao_hocphiField.setOptionsList(sessionTypeNames);
            //xoá
            giaovienField.clear();
            hovstenField.clear();
            dovitao_hocphiField.clear();
            denngayField.clear();
            tungayField.clear();
            trangthaiField.clear();
        }
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null) {
            dovitao_hocphiField.setEditable(false);
            giaovienField.setEditable(false);
            denngayField.clear();
            tungayField.clear();
            trangthaiField.clear();
            hovstenField.clear();
            dovitao_hocphiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()); //Chèn đơn vị từ user vào text
            giaovienField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv());  //chèn tên giáo viên từ user vào text
        }
    }

    public Component checkhandong(Hocphi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getNgaydong() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ ĐÓNG</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getHandong();
            if (now.compareTo(han) >= 0) {
                String body = "<a style=\"background-color: black; color: red ; width: 100%;\">QUÁ HẠN</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            } else {
                String body = "<a style=\"color: red; width: 100%;\">ĐẾN HẠN</a>\n";
                htmlBoxLayout1.setTemplateContents(body);
            }
            return htmlBoxLayout1;
        }
        return htmlBoxLayout;
    }


    public void timkiemExcute() {
        excuteSearch(true);
    }

    private void excuteSearch(boolean isFromSearchBtn) {
        Object donvi = dovitao_hocphiField.getValue();
        String giaovien = giaovienField.getValue();
        String hocsinh = hovstenField.getValue();
        Object trangthai = trangthaiField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, giaovien, hocsinh, trangthai, tungay, denngay, params);
        hocphisDl.setQuery(query);
        hocphisDl.setParameters(params);
        hocphisDl.load();
    }

    private String returnQuery(Object donvi, String giaovien, String hocsinh, Object trangthai, Date tungay, Date denngay, Map<String, Object> params) {
        String query = "select e from truonghoc_Hocphi e ";
        String where = " where 1=1 ";


        //Giáo viên
        if (!StringUtils.isEmpty(giaovien)) {
            where += "and e.usertao_hocphi like :giaovien ";
            params.put("giaovien", "%" + giaovien + "%");
        }
        //Học sinh
        if (!StringUtils.isEmpty(hocsinh)) {
            where += "and e.hovaten.tenhocsinh like :hocsinh ";
            params.put("hocsinh", "%" + hocsinh + "%");
        }

        //Trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangthai);
        }

        //Đơn vị
        if (donvi != null) {
            where += "and e.dovitao_hocphi = :donvi ";
            params.put("donvi", donvi);
        }
        //Từ ngày
        if (tungay != null) {
            where += "and e.ngaydong >= :tungay ";
            params.put("tungay", tungay);
        }
        //Đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.ngaydong ";
            params.put("denngay", denngay);
        }

        query = query + where;
        return query;
    }


}