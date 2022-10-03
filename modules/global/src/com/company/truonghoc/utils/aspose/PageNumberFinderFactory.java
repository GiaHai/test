package com.company.truonghoc.utils.aspose;

import com.aspose.words.Document;
import com.aspose.words.LayoutCollector;

public class PageNumberFinderFactory {
    /* Simulation of static class by using private constructor */
    private PageNumberFinderFactory() {
    }

    public static PageNumberFinder create(Document document) throws Exception {
        LayoutCollector layoutCollector = new LayoutCollector(document);
        document.updatePageLayout();
        PageNumberFinder pageNumberFinder = new PageNumberFinder(layoutCollector);
        pageNumberFinder.splitNodesAcrossPages();
        return pageNumberFinder;
    }
}
