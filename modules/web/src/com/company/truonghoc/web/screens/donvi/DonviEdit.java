package com.company.truonghoc.web.screens.donvi;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.ThuchiService;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("truonghoc_Donvi.edit")
@UiDescriptor("donvi-edit.xml")
@EditedEntityContainer("donviDc")
@LoadDataBeforeShow
public class DonviEdit extends StandardEditor<Donvi> {
    @Inject
    protected TextField<String> tendonviField;
//    @Inject
//    protected TextField<Long> thuField;
//    @Inject
//    protected ThuchiService thuchiService;
//    @Inject
//    protected TextField<Long> chiField;
//
//
//    Long hocphiFiled;
//    Long thutienhocphiField;
//    Long luongthangField;
//    Long thuchiField;

//    @Subscribe
//    protected void onInit(InitEvent event) {
//        thuField.setVisible(false);
//        chiField.setVisible(false);
//    }


//    @Subscribe
//    protected void onAfterShow(AfterShowEvent event) {
//
//        try {
//            hocphiFiled = thuchiService.hocphi(tendonviField.getValue());
//            thutienhocphiField = thuchiService.thutienhocphi(tendonviField.getValue());
//            luongthangField = thuchiService.luongthang(tendonviField.getValue());
//            thuchiField = thuchiService.thuchi(tendonviField.getValue());
////            System.out.println(thutienhocphiField+ hocphiFiled);
//
////            System.out.println("Thu tiền học phí " + thutienhocphiField);
////            thuField.setValue(thutienhocphiField + hocphiFiled);
////            chiField.setValue(luongthangField + thuchiField);
//        }catch (NullPointerException ex){
//
////            if (hocphiFiled != null){
////                thuField.setValue(hocphiFiled);
////            }
////            if (thutienhocphiField != null){
////                thuField.setValue(thutienhocphiField);
////            }
////
////            if (luongthangField != null){
////                chiField.setValue(luongthangField);
////            }
////
////            if (thuchiField != null){
////                chiField.setValue(thuchiField);
////            }
//        }
//    }


}