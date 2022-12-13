package com.company.truonghoc.web.screens.baocao;


import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.UserExt;
import com.company.truonghoc.service.DulieuUserService;
import com.haulmont.charts.gui.components.charts.PieChart;
import com.haulmont.charts.gui.components.charts.SerialChart;
import com.haulmont.charts.gui.data.ListDataProvider;
import com.haulmont.charts.gui.data.MapDataItem;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@UiController("truonghoc_BaocaoScreen")
@UiDescriptor("baocao-screen.xml")
//@LoadDataBeforeShow
public class BaocaoScreen extends Screen {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected DateField<Date> tungayField;
    @Inject
    protected DateField<Date> denngayField;
    @Inject
    protected SerialChart hocphi;
    @Inject
    protected SerialChart luongthang;
    @Inject
    protected SerialChart thuchi;
//    @Inject
//    protected SerialChart thutienhocphi;
    @Inject
    protected CollectionLoader<Donvi> donvisDl;
    @Inject
    protected DateField<Date> denngaytab2Field;
    @Inject
    protected LookupField timkiemdonviField;
    @Inject
    protected Button timkiemtab2;
    @Inject
    protected DateField<Date> tungaytab2Field;
    @Inject
    protected Notifications notifications;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected Button timkiemBtn;
    @Inject
    protected Button clearBtn;
    @Inject
    protected VBoxLayout tab1;
    @Inject
    protected VBoxLayout tab2;
    private Donvi donVi = null;

    @Subscribe
    protected void onInit(InitEvent event) {
        timkiemdonviField.setRequired(true);
        donVi = dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        donvisDl.load();
        List<String> sessionTypeNames = donvisDc.getMutableItems().stream()
                .map(Donvi::getTendonvi)
                .collect(Collectors.toList());
        timkiemdonviField.setOptionsList(sessionTypeNames);

        if (donVi.getDonvitrungtam() == false){
            tab1.setStyleName("hide-tabs");
        }
    }

//    @Subscribe
//    protected void onAfterShow(AfterShowEvent event) {
//        if (!donVi.getDonvitrungtam()) {
//            timkiemBtn.setVisible(false);
//            clearBtn.setVisible(false);
//            timkiemdonviField.setValue(donVi.getTendonvi());
//            timkiemdonviField.setEditable(false);
//        }else {
//            List<MapDataItem> hocphis = new ArrayList<>();
//            for (KeyValueEntity item : Loadhocphi1()) {
//                hocphis.add(MapDataItem.of(
//                        "title", item.getValue("donvi"),
//                        "value", item.getValue("sotien")));
//            }
//
//            List<MapDataItem> luongthangs = new ArrayList<>();
//            for (KeyValueEntity item : Loadluongthang1()) {
//                luongthangs.add(MapDataItem.of(
//                        "title", item.getValue("donvi"),
//                        "value", item.getValue("sotien")));
//
//            }
////            List<MapDataItem> thutienhocphis = new ArrayList<>();
////            for (KeyValueEntity item : Loadthutienhocphi1()) {
////                thutienhocphis.add(MapDataItem.of(
////                        "title", item.getValue("donvi"),
////                        "value", item.getValue("sotien")));
////
////            }
//            List<MapDataItem> thuchis = new ArrayList<>();
//            for (KeyValueEntity item : Loadthuchi1()) {
//                thuchis.add(MapDataItem.of(
//                        "title", item.getValue("donvi"),
//                        "value", item.getValue("sotien")));
//
//            }
//            hocphi.setDataProvider(new ListDataProvider(hocphis));
////            thutienhocphi.setDataProvider(new ListDataProvider(thutienhocphis));
//            luongthang.setDataProvider(new ListDataProvider(luongthangs));
//            thuchi.setDataProvider(new ListDataProvider(thuchis));
//        }
//    }
    @Subscribe("timkiemBtn")
    protected void onTimkiemBtnClick(Button.ClickEvent event) {

        List<MapDataItem> hocphis = new ArrayList<>();
        for (KeyValueEntity item : Loadhocphi(tungayField.getValue(), denngayField.getValue())) {
            hocphis.add(MapDataItem.of(
                    "title", item.getValue("donvi"),
                    "value", item.getValue("sotien")));
        }

        List<MapDataItem> luongthangs = new ArrayList<>();
        for (KeyValueEntity item : Loadluongthang(tungayField.getValue(), denngayField.getValue())) {
            luongthangs.add(MapDataItem.of(
                    "title", item.getValue("donvi"),
                    "value", item.getValue("sotien")));

        }
//        List<MapDataItem> thutienhocphis = new ArrayList<>();
//        for (KeyValueEntity item : Loadthutienhocphi(tungayField.getValue(), denngayField.getValue())) {
//            thutienhocphis.add(MapDataItem.of(
//                    "title", item.getValue("donvi"),
//                    "value", item.getValue("sotien")));
//
//        }
        List<MapDataItem> thuchis = new ArrayList<>();
        for (KeyValueEntity item : Loadthuchi(tungayField.getValue(), denngayField.getValue())) {
            thuchis.add(MapDataItem.of(
                    "title", item.getValue("donvi"),
                    "value", item.getValue("sotien")));

        }
        hocphi.setDataProvider(new ListDataProvider(hocphis));
//        thutienhocphi.setDataProvider(new ListDataProvider(thutienhocphis));
        luongthang.setDataProvider(new ListDataProvider(luongthangs));
        thuchi.setDataProvider(new ListDataProvider(thuchis));
    }

    @Subscribe("cleartab2Btn")
    protected void onCleartab2BtnClick(Button.ClickEvent event) {
        tungayField.clear();
        denngayField.clear();
        timkiemdonviField.clear();
    }


    private List<KeyValueEntity> Loadhocphi(Date tungay, Date dengay) {
        String queryStr = "select sum(e.sotienthutheohd) sotien, e.donvi.tendonvi donvi from truonghoc_Hocphi e" +
                " where e.sotienthutheohd is not null and e.hinhthucthanhtoan is not null and e.ngaydong >= :tungay and :denngay >= e.ngaydong";
        queryStr += " group by e.donvi ";

        return dataManager.loadValues(queryStr)
                .properties("sotien", "donvi")
                .parameter("tungay", tungay)
                .parameter("denngay", dengay)
                .list();

    }

    private List<KeyValueEntity> Loadluongthang(Date tungay, Date dengay) {
        String queryStr = "select sum(e.tonglinh) sotien, e.donvi.tendonvi donvi from truonghoc_Luongthang e" +
                " where e.tonglinh is not null and e.hinhthucthanhtoan is not null and e.ngaynhan >= :tungay and :denngay >= e.ngaynhan";
        queryStr += " group by e.donvi ";

        return dataManager.loadValues(queryStr)
                .properties("sotien", "donvi")
                .parameter("tungay", tungay)
                .parameter("denngay", dengay)
                .list();

    }

    private List<KeyValueEntity> Loadthuchi(Date tungay, Date dengay) {
        String queryStr = "select sum(e.thanhtien) sotien, e.donvi.tendonvi donvi from truonghoc_Thuchi e" +
                " where e.thanhtien is not null and e.hinhthucthanhtoan is not null and e.ngaychi >= :tungay and :denngay >= e.ngaychi";
        queryStr += " group by e.donvi ";

        return dataManager.loadValues(queryStr)
                .properties("sotien", "donvi")
                .parameter("tungay", tungay)
                .parameter("denngay", dengay)
                .list();

    }

//    private List<KeyValueEntity> Loadthutienhocphi(Date tungay, Date dengay) {
//        String queryStr = "select sum(e.thanhtien) sotien, e.donvi.tendonvi donvi from truonghoc_Thutienhocphi e" +
//                " where e.thanhtien is not null and e.hinhthucthanhtoan is not null and e.ngaythanhtoan >= :tungay and :denngay >= e.ngaythanhtoan";
//        queryStr += " group by e.donvi ";
//
//        return dataManager.loadValues(queryStr)
//                .properties("sotien", "donvi")
//                .parameter("tungay", tungay)
//                .parameter("denngay", dengay)
//                .list();
//
//    }




    private List<KeyValueEntity> Loadhocphi1() {
        String queryStr = "select sum(e.sotienthutheohd) sotien, e.donvi.tendonvi donvi from truonghoc_Hocphi e" +
                " where e.sotienthutheohd is not null and e.hinhthucthanhtoan is not null";
        queryStr += " group by e.donvi ";

        return dataManager.loadValues(queryStr)
                .properties("sotien", "donvi")
                .list();

    }

    private List<KeyValueEntity> Loadluongthang1() {
        String queryStr = "select sum(e.tonglinh) sotien, e.donvi.tendonvi donvi from truonghoc_Luongthang e" +
                " where e.tonglinh is not null and e.hinhthucthanhtoan is not null";
        queryStr += " group by e.donvi ";

        return dataManager.loadValues(queryStr)
                .properties("sotien", "donvi")
                .list();

    }

    private List<KeyValueEntity> Loadthuchi1() {
        String queryStr = "select sum(e.thanhtien) sotien, e.donvi.tendonvi donvi from truonghoc_Thuchi e" +
                " where e.thanhtien is not null and e.hinhthucthanhtoan is not null";
        queryStr += " group by e.donvi ";

        return dataManager.loadValues(queryStr)
                .properties("sotien", "donvi")
                .list();

    }

//    private List<KeyValueEntity> Loadthutienhocphi1() {
//        String queryStr = "select sum(e.thanhtien) sotien, e.donvi.tendonvi donvi from truonghoc_Thutienhocphi e" +
//                " where e.thanhtien is not null and e.hinhthucthanhtoan is not null";
//        queryStr += " group by e.donvi ";
//
//        return dataManager.loadValues(queryStr)
//                .properties("sotien", "donvi")
//                .list();
//
//    }

    @Subscribe("clearBtn")
    protected void onClearBtnClick(Button.ClickEvent event) {
        tungayField.clear();
        denngayField.clear();

        List<MapDataItem> hocphis = new ArrayList<>();
        for (KeyValueEntity item : Loadhocphi1()) {
            hocphis.add(MapDataItem.of(
                    "title", item.getValue("donvi"),
                    "value", item.getValue("sotien")));
        }

        List<MapDataItem> luongthangs = new ArrayList<>();
        for (KeyValueEntity item : Loadluongthang1()) {
            luongthangs.add(MapDataItem.of(
                    "title", item.getValue("donvi"),
                    "value", item.getValue("sotien")));

        }
//        List<MapDataItem> thutienhocphis = new ArrayList<>();
//        for (KeyValueEntity item : Loadthutienhocphi1()) {
//            thutienhocphis.add(MapDataItem.of(
//                    "title", item.getValue("donvi"),
//                    "value", item.getValue("sotien")));
//
//        }
        List<MapDataItem> thuchis = new ArrayList<>();
        for (KeyValueEntity item : Loadthuchi1()) {
            thuchis.add(MapDataItem.of(
                    "title", item.getValue("donvi"),
                    "value", item.getValue("sotien")));

        }
        hocphi.setDataProvider(new ListDataProvider(hocphis));
//        thutienhocphi.setDataProvider(new ListDataProvider(thutienhocphis));
        luongthang.setDataProvider(new ListDataProvider(luongthangs));
        thuchi.setDataProvider(new ListDataProvider(thuchis));
    }


//            --------------------------------TAB2---------------------------------------

    public Long LoadhocphiTab2(Object donvi, Date tungay, Date dengay) {

        return dataManager.loadValue("select sum(e.sotienthutheohd) from truonghoc_Hocphi e" +
                        " where e.donvi.tendonvi = :donvi and e.sotienthutheohd is not null and e.hinhthucthanhtoan is not null and e.ngaydong >= :tungay and :denngay >= e.ngaydong"
                , Long.class)
                .parameter("donvi",   donvi )
                .parameter("tungay", tungay)
                .parameter("denngay", dengay)
                .one();
    }

//    public Long LoadthutienhocphiTab2(String donvi, Date tungay, Date dengay) {
//        return dataManager.loadValue("select sum(e.thanhtien) from truonghoc_Thutienhocphi e" +
//                        " where e.donvi.tendonvi = :donvi and e.thanhtien is not null and e.hinhthucthanhtoan is not null and e.ngaythanhtoan >= :tungay and :denngay >= e.ngaythanhtoan"
//                , Long.class)
//                .parameter("donvi", donvi)
//                .parameter("tungay", tungay)
//                .parameter("denngay", dengay)
//                .one();
//    }

    public Long LoadluongthangTab2(String donvi, Date tungay, Date dengay) {
        return dataManager.loadValue("select sum(e.tonglinh) from truonghoc_Luongthang e" +
                        " where e.donvi.tendonvi = :donvi and e.tonglinh is not null and e.hinhthucthanhtoan is not null and e.ngaynhan >= :tungay and :denngay >= e.ngaynhan"
                , Long.class)
                .parameter("donvi", donvi)
                .parameter("tungay", tungay)
                .parameter("denngay", dengay)
                .one();
    }

    public Long LoadthuchiTab2(String donvi, Date tungay, Date dengay) {
        return dataManager.loadValue("select sum(e.thanhtien) from truonghoc_Thuchi e" +
                        " where e.donvi.tendonvi = :donvi and e.thanhtien is not null and e.hinhthucthanhtoan is not null and e.ngaychi >= :tungay and :denngay >= e.ngaychi"
                , Long.class)
                .parameter("donvi", donvi)
                .parameter("tungay", tungay)
                .parameter("denngay", dengay)
                .one();
    }

    @Subscribe("timkiemtab2")
    protected void onTimkiemtab2Click(Button.ClickEvent event) {
        if (timkiemdonviField.getValue() == null &&
                tungaytab2Field.getValue() == null &&
                denngaytab2Field.getValue() == null) {
            notifications.create()
                    .withCaption("Nhập thiếu trường")
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .show();
        } else {
            try {
                ListDataProvider dataProvider = new ListDataProvider();
                Long hocphi = LoadhocphiTab2(timkiemdonviField.getValue(), tungaytab2Field.getValue(), denngaytab2Field.getValue());
//                Long thutienhocphi = LoadthutienhocphiTab2(timkiemdonviField.getValue().toString(), tungaytab2Field.getValue(), denngaytab2Field.getValue());

                Long luongthang = LoadluongthangTab2(timkiemdonviField.getValue().toString(), tungaytab2Field.getValue(), denngaytab2Field.getValue());
                Long thuchi = LoadthuchiTab2(timkiemdonviField.getValue().toString(), tungaytab2Field.getValue(), denngaytab2Field.getValue());

//                dataProvider.addItem(new MapDataItem().add("title", "Thu: ").add("value", hocphi + thutienhocphi));
                dataProvider.addItem(new MapDataItem().add("title", "Thu: ").add("value", hocphi));
                dataProvider.addItem(new MapDataItem().add("title", "Chi: ").add("value", luongthang + thuchi));
                System.out.println("chuẩn");
                donutChart.setDataProvider(dataProvider);
            } catch (NullPointerException ex) {

                Long hocphi = LoadhocphiTab2(timkiemdonviField.getValue(), tungaytab2Field.getValue(), denngaytab2Field.getValue());
//                Long thutienhocphi = LoadthutienhocphiTab2(timkiemdonviField.getValue().toString(), tungaytab2Field.getValue(), denngaytab2Field.getValue());

                Long luongthang = LoadluongthangTab2(timkiemdonviField.getValue().toString(), tungaytab2Field.getValue(), denngaytab2Field.getValue());
                Long thuchi = LoadthuchiTab2(timkiemdonviField.getValue().toString(), tungaytab2Field.getValue(), denngaytab2Field.getValue());

                ListDataProvider dataProvider = new ListDataProvider();

                Long hp = Long.valueOf(0);
//                Long tthp = Long.valueOf(0);
                Long lt = Long.valueOf(0);
                Long tt = Long.valueOf(0);
                if (hocphi != null) {
                    hp = hocphi;
                }
//                if (thutienhocphi != null) {
//                    tthp = thutienhocphi;
//                }
                if (luongthang != null) {
                    lt = luongthang;
                }
                if (thuchi != null) {
                    tt = thuchi;
                }

//                Long thu = hp + tthp;
                Long thu = hp;
                Long chi = lt + tt;
                dataProvider.addItem(new MapDataItem().add("title", "Thu: ").add("value", thu));
                dataProvider.addItem(new MapDataItem().add("title", "Chi: ").add("value", chi));
                donutChart.setDataProvider(dataProvider);

            }
        }
    }

    @Inject
    private PieChart donutChart;


}