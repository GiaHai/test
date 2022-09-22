package com.company.truonghoc.web.screens.thutienhocphi;

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
import com.company.truonghoc.entity.Thutienhocphi;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_Thutienhocphi.browse")
@UiDescriptor("thutienhocphi-browse.xml")
@LookupComponent("thutienhocphisTable")
@LoadDataBeforeShow
public class ThutienhocphiBrowse extends StandardLookup<Thutienhocphi> {
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected UserSession userSession;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected LookupField donvitao_thutienhocphiField;
    @Inject
    protected LookupField trangthaiField;
    @Inject
    protected TextField<String> tenHsField;
    @Inject
    protected TextField<String> tenKhField;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected TextField<String> nguoithuField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected CollectionLoader<Thutienhocphi> thutienhocphisDl;
    @Inject
    protected Button createBtn;
    @Named("thutienhocphisTable.create")
    protected CreateAction<Thutienhocphi> thutienhocphisTableCreate;
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
        excuteSearch(true);
    }

    @Subscribe("thutienhocphisTable.create")
    protected void onThutienhocphisTableCreate(Action.ActionPerformedEvent event) {
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null) {
            this.thutienhocphisTableCreate.execute();
        } else {
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
        dkphanquyen();
    }

    //Điều kiện login
    private void dkphanquyen() {
        //điều kiện đơn vị trung tâm nếu
        if (dulieuUserService.timbrowerdonvi(userSession.getUser().getLogin()).size() == 0) {
            donvitao_thutienhocphiField.setEditable(false);
            donvitao_thutienhocphiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()); //Chèn đơn vị từ user vào text
            //Xoá
            nguoithuField.clear();
            tenHsField.clear();
            tenKhField.clear();
            tungayField.clear();
            denngayField.clear();
            trangthaiField.clear();
        } else {
            donvitao_thutienhocphiField.setEditable(true);

            donvisDl.load();
            List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                    .map(Donvi::getTendonvi)
                    .collect(Collectors.toList());
            donvitao_thutienhocphiField.setOptionsList(sessionTypeNames);

            //Xoá
            donvitao_thutienhocphiField.clear();
            nguoithuField.clear();
            tenHsField.clear();
            tenKhField.clear();
            tungayField.clear();
            denngayField.clear();
            trangthaiField.clear();
        }
        if (dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv() != null) {
            donvitao_thutienhocphiField.setEditable(false);
            nguoithuField.setEditable(false);
            tenHsField.clear();
            tenKhField.clear();
            tungayField.clear();
            denngayField.clear();
            trangthaiField.clear();
            donvitao_thutienhocphiField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTendonvi()); //Chèn đơn vị từ user vào text
            nguoithuField.setValue(dulieuUserService.timEditdonvi(userSession.getUser().getLogin()).getTextgv());  //chèn tên giáo viên từ user vào text
        }
    }

    public Component stt(Entity entity) {
        int lineNumber = 1;
        try {
            lineNumber = thutienhocphisDl.getContainer().getItemIndex(entity.getId()) + 1;
        } catch (Exception ex) {
            lineNumber = 1;
        }
        Label field = uiComponents.create(Label.NAME);
        field.setValue(lineNumber);
        return field;
    }

    public Component checkhan(Thutienhocphi entity) {
        HtmlBoxLayout htmlBoxLayout = uiComponents.create(HtmlBoxLayout.class);
        htmlBoxLayout.setHtmlSanitizerEnabled(true);

        if (entity.getHinhthucthanhtoan() != null) {
            String body = "<a style=\"background-color: #00FFFF; width: 100%;\">ĐÃ THANH TOÁN</a>\n";
            htmlBoxLayout.setTemplateContents(body);
        } else {
            HtmlBoxLayout htmlBoxLayout1 = uiComponents.create(HtmlBoxLayout.class);
            htmlBoxLayout.setHtmlSanitizerEnabled(true);
            Date now = new Date();
            Date han = entity.getDenngay();
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

    private void excuteSearch(boolean b) {
        Object donvi = donvitao_thutienhocphiField.getValue();
        String ngthu = nguoithuField.getValue();
        String Kh = tenKhField.getValue();
        String tenHs = tenHsField.getValue();
        Date tungay = tungayField.getValue();
        Date denngay = denngayField.getValue();
        Object trangthai = trangthaiField.getValue();

        Map<String, Object> params = new HashMap<>();

        String query = returnQuery(donvi, ngthu, Kh, tenHs, tungay, denngay, trangthai, params);
        thutienhocphisDl.setQuery(query);
        thutienhocphisDl.setParameters(params);
        thutienhocphisDl.load();
    }

    private String returnQuery(Object donvi, String ngthu, String kh, String tenHs, Date tungay, Date denngay, Object trangthai, Map<String, Object> params) {
        String query = "select e from truonghoc_Thutienhocphi e ";
        String where = " where 1=1 ";

        //Đơn vị
        if (donvi != null) {
            where += "and e.donvitao_thutienhocphi = :donvi ";
            params.put("donvi", donvi);
        }
        //người thu
        if (!StringUtils.isEmpty(ngthu)) {
            where += "and e.usertao_thutienhocphi = :ngthu ";
            params.put("ngthu", ngthu);
        }
        //Khách hàng
        if (!StringUtils.isEmpty(kh)) {
            where += "and e.tenkhachhang = :kh ";
            params.put("kh", kh);
        }
        //học sinh
        if (!StringUtils.isEmpty(tenHs)) {
            where += "and e.tenhocsinh.tenhocsinh = :tenHs ";
            params.put("tenHs", tenHs);
        }
        //từ ngày
        if (tungay != null) {
            where += "and e.tungay >= :tungay ";
            params.put("tungay", tungay);
        }
        //đến ngày
        if (denngay != null) {
            where += "and :denngay >= e.denngay ";
            params.put("denngay", denngay);
        }
        //trạng thái
        if (trangthai != null) {
            where += "and e.tinhtrangthanhtoan = :trangthai ";
            params.put("trangthai", trangthai);
        }
        query = query + where;
        return query;
    }


}