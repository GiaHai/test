package com.company.truonghoc.web.screens.baocao;


import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.service.ThuchiService;
import com.haulmont.charts.gui.amcharts.model.Guide;
import com.haulmont.charts.gui.components.charts.SerialChart;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UiController("truonghoc_BaocaoScreen")
@UiDescriptor("baocao-screen.xml")
@LoadDataBeforeShow

public class BaocaoScreen extends Screen {
    @Inject
    protected CollectionContainer<Donvi> donvisDc;
    @Inject
    protected ThuchiService thuchiService;
    @Inject
    protected DataManager dataManager;
    @Inject
    private SerialChart serialChart;
    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        List<Guide> guides = new ArrayList<>();

        List<Donvi> dataList = donvisDc.getItems();
        Map<String, List<Donvi>> groupedData = groupDataByGuide(dataList);

        for (Map.Entry<String, List<Donvi>> entry : groupedData.entrySet()) {
            List<Donvi> guideData = entry.getValue();
            guides.add(new Guide()
                    .setCategory(guideData.get(0).getTendonvi())
                    .setToCategory(guideData.get(guideData.size() - 1).getTendonvi())
                    .setLabel(entry.getKey())
                    .setExpand(true)
                    .setLabelRotation(0)
                    .setTickLength(80));
        }

        serialChart.getCategoryAxis().addGuides(guides.toArray(new Guide[guides.size()]));
    }

    private Map<String, List<Donvi>> groupDataByGuide(List<Donvi> dataList) {
        Map<String, List<Donvi>> result = new HashMap<>();
        for (Donvi data : dataList) {
            String guideKey = data.getTendonvi();
            List<Donvi> guideData = result.computeIfAbsent(guideKey, k -> new ArrayList<>());
            guideData.add(data);
        }
        return result;
    }


}