package com.company.truonghoc.web.screens.baocao;

import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.Hocphi;
import com.haulmont.charts.gui.components.charts.SerialChart;
import com.haulmont.charts.gui.data.ListDataProvider;
import com.haulmont.charts.gui.data.MapDataItem;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("truonghoc_")
@UiDescriptor("new-screen.xml")
@LoadDataBeforeShow
public class NewScreen extends Screen {
    @Inject
    protected DataManager dataManager;
    @Inject
    protected GroupTable<Hocphi> hocphisTable;
//    @Inject
//    private PieChart donutChart;
    @Inject
    protected CollectionContainer<Hocphi> hocphisDc;
    @Inject
    protected SerialChart column3d;
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected TextField<Long> hai;

    @Subscribe
    protected void onInit(InitEvent event) {
//        ListDataProvider dataProvider = new ListDataProvider();
//
//        dataProvider.addItem(new MapDataItem().add("title", "New").add("value", hocphiList()));
//        dataProvider.addItem(new MapDataItem().add("title", "Returning").add("value", 9899));
//
//        donutChart.setDataProvider(dataProvider);
        ListDataProvider dataProvider1 = new ListDataProvider();

//        dataProvider1.addItem(new MapDataItem().add("tendonvi", donvisDc.getMutableItems().listIterator()).add("thu", test()));
//        dataProvider1.addItem(new MapDataItem().add("COLUMN", test()).add("year2015", 9899));

        column3d.setDataProvider(dataProvider1);
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        hai.setValue(hocphiList());
        ListDataProvider dataProvider1 = new ListDataProvider();

        dataProvider1.addItem(new MapDataItem().add("tendonvi", test().getTendonvi()).add("thu", hocphiList()));
//        dataProvider1.addItem(new MapDataItem().add("tendonvi", test().getTendonvi().get).add("thu", 7562));

//        dataProvider1.addItem(new MapDataItem().add("COLUMN", test()).add("year2015", 9899));
//        dataProvider1.addItem(new MapDataItem(ImmutableMap.of("thu", hai, "tendonvi", test())));

        column3d.setDataProvider(dataProvider1);
    }

    public Long hocphiList(){
        return dataManager.loadValue("select sum(e.sotienthutheohd) from truonghoc_Hocphi e" +
                        " where e.sotienthutheohd is not null and e.hinhthucthanhtoan is not null"
                ,Long.class)
                .one();
    }
    public List<Hocphi> tenhocphiList(){
        return dataManager.loadValue("select e from truonghoc_Hocphi e" +
                        " where e.sotienthutheohd is not null and e.hinhthucthanhtoan is not null"
                , Hocphi.class)
                .list();
    }
    public Donvi test(){
        return dataManager.loadValue("select e from truonghoc_Donvi e"
                , Donvi.class)
                .one();
    }
}