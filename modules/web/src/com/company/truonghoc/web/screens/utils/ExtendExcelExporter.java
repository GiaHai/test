package com.company.truonghoc.web.screens.utils;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.AggregationInfo;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.components.DataGrid.ColumnGenerator;
import com.haulmont.cuba.gui.components.DataGrid.ColumnGeneratorEvent;
import com.haulmont.cuba.gui.components.GroupTable.GroupCellContext;
import com.haulmont.cuba.gui.components.GroupTable.GroupCellValueFormatter;
import com.haulmont.cuba.gui.components.Table.AggregationStyle;
import com.haulmont.cuba.gui.components.Table.Column;
import com.haulmont.cuba.gui.components.Table.Printable;
import com.haulmont.cuba.gui.components.data.GroupTableItems;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.gui.components.data.TreeDataGridItems;
import com.haulmont.cuba.gui.components.data.TreeTableItems;
import com.haulmont.cuba.gui.components.data.meta.EntityDataGridItems;
import com.haulmont.cuba.gui.components.data.meta.EntityTableItems;
import com.haulmont.cuba.gui.data.GroupInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import com.haulmont.cuba.gui.export.*;
import com.haulmont.cuba.security.entity.RoleType;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.LocaleUtil;
import org.dom4j.Element;

public class ExtendExcelExporter {

    protected static final int COL_WIDTH_MAGIC = 48;
    private static final int SPACE_COUNT = 10;
    public static final int MAX_ROW_COUNT = 65535;
    protected HSSFWorkbook wb;
    protected HSSFFont boldFont;
    protected HSSFFont stdFont;
    protected HSSFSheet sheet;
    protected HSSFCellStyle timeFormatCellStyle;
    protected HSSFCellStyle dateFormatCellStyle;
    protected HSSFCellStyle dateTimeFormatCellStyle;
    protected HSSFCellStyle integerFormatCellStyle;
    protected HSSFCellStyle doubleFormatCellStyle;
    protected ExcelAutoColumnSizer[] sizers;
    protected String trueStr;
    protected String falseStr;
    protected String title;
    protected boolean exportAggregation = true;
    protected final Messages messages = (Messages)AppBeans.get("cuba_Messages");
    protected final UserSessionSource userSessionSource = (UserSessionSource)AppBeans.get("cuba_UserSessionSource");
    protected final MetadataTools metadataTools = (MetadataTools)AppBeans.get("cuba_MetadataTools");
    protected boolean isRowNumberExceeded = false;

    public ExtendExcelExporter() {
        this.trueStr = "Đúng";
        this.falseStr = "Sai";
    }

    public ExtendExcelExporter(String title) {
        this.trueStr = "Mở";
        this.falseStr = "Đóng";
        this.title = title;
    }

    public void exportTable(Table table, ExportDisplay display) {
        this.exportTable(table, table.getColumns(), display);
    }

    public void exportTable(Table table, List<Column> columns, ExportDisplay display, ExcelExporter.ExportMode exportMode) {
        this.exportTable(table, columns, false, display, (List)null, (String)null, exportMode);
    }

    protected void createWorkbookWithSheet() {
        this.wb = new HSSFWorkbook();
        this.sheet = this.wb.createSheet("Export");
    }

    protected void createFonts() {
        this.stdFont = this.wb.createFont();
        this.boldFont = this.wb.createFont();
        this.boldFont.setBold(true);
    }

    protected void createAutoColumnSizers(int count) {
        this.sizers = new ExcelAutoColumnSizer[count];
    }

    public void exportTable(Table table, List<Column> columns, ExportDisplay display) {
        this.exportTable(table, columns, false, display);
    }

    public void exportTable(Table table, List<Column> columns, Boolean exportExpanded, ExportDisplay display) {
        this.exportTable(table, columns, exportExpanded, display, (List)null);
    }

    public void exportTable(Table table, List<Column> columns, Boolean exportExpanded, ExportDisplay display, List<String> filterDescription) {
        this.exportTable(table, columns, exportExpanded, display, filterDescription, (String)null, ExcelExporter.ExportMode.ALL_ROWS);
    }

    public void exportTable(Table<Entity> table, List<Column> columns, Boolean exportExpanded, ExportDisplay display, List<String> filterDescription, String fileName, ExcelExporter.ExportMode exportMode) {
        if (display == null) {
            throw new IllegalArgumentException("ExportDisplay is null");
        } else {
            if (filterDescription == null && this.title != null) {
                filterDescription = new ArrayList<>();
                filterDescription.add(this.title);
            }
            this.createWorkbookWithSheet();
            this.createFonts();
            this.createFormats();
            int r = 0;
            if (filterDescription != null) {
                for(r = 0; r < filterDescription.size(); ++r) {
                    String line = (String)filterDescription.get(r);
                    HSSFRow row = this.sheet.createRow(r);
                    if (r == 0) {
                        HSSFRichTextString richTextFilterName = new HSSFRichTextString(line);
                        richTextFilterName.applyFont(this.boldFont);
                        row.createCell(0).setCellValue(richTextFilterName);
                    } else {
                        row.createCell(0).setCellValue(line);
                    }
                }

                ++r;
            }

            HSSFRow row = this.sheet.createRow(r);
            this.createAutoColumnSizers(columns.size());
            float maxHeight = this.sheet.getDefaultRowHeightInPoints();
            CellStyle headerCellStyle = this.wb.createCellStyle();
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Iterator var12 = columns.iterator();

            Column column;
            String caption;
            while(var12.hasNext()) {
                column = (Column)var12.next();
                caption = column.getCaption();
                int countOfReturnSymbols = StringUtils.countMatches(caption, "\n");
                if (countOfReturnSymbols > 0) {
                    maxHeight = Math.max(maxHeight, (float)(countOfReturnSymbols + 1) * this.sheet.getDefaultRowHeightInPoints());
                    headerCellStyle.setWrapText(true);
                }
            }

            row.setHeightInPoints(maxHeight);

            for(int c = 0; c < columns.size(); ++c) {
                column = (Column)columns.get(c);
                caption = column.getCaption();
                HSSFCell cell = row.createCell(c);
                HSSFRichTextString richTextString = new HSSFRichTextString(caption);
                richTextString.applyFont(this.boldFont);
                cell.setCellValue(richTextString);
                ExcelAutoColumnSizer sizer = new ExcelAutoColumnSizer();
                sizer.notifyCellValue(caption, this.boldFont);
                this.sizers[c] = sizer;
                cell.setCellStyle(headerCellStyle);
            }

            TableItems<Entity> tableItems = table.getItems();
            Iterator var36;
            if (exportMode == ExcelExporter.ExportMode.SELECTED_ROWS && table.getSelected().size() > 0) {
                Set<Entity> selected = table.getSelected();
                Stream var10000 = tableItems.getItemIds().stream();
                tableItems.getClass();
                var10000 = var10000.map(tableItems::getItem);
                selected.getClass();
                List<Entity> ordered = (List)var10000.filter(selected::contains).collect(Collectors.toList());
                var36 = ordered.iterator();

                while(var36.hasNext()) {
                    Entity item = (Entity)var36.next();
                    if (this.checkIsRowNumberExceed(r)) {
                        break;
                    }

                    ++r;
                    this.createRow(table, columns, 0, r, item.getId());
                }
            } else {
                if (table.isAggregatable() && this.exportAggregation && this.hasAggregatableColumn(table) && table.getAggregationStyle() == AggregationStyle.TOP) {
                    ++r;
                    r = this.createAggregatableRow(table, columns, r, 1);
                }

                if (table instanceof TreeTable) {
                    TreeTable treeTable = (TreeTable)table;
                    TreeTableItems treeTableSource = (TreeTableItems)treeTable.getItems();

                    Object itemId;
                    for(var36 = treeTableSource.getRootItemIds().iterator(); var36.hasNext(); r = this.createHierarhicalRow(treeTable, columns, exportExpanded, r, itemId)) {
                        itemId = var36.next();
                        if (this.checkIsRowNumberExceed(r)) {
                            break;
                        }
                    }
                } else if (table instanceof GroupTable && tableItems instanceof GroupTableItems && ((GroupTableItems)tableItems).hasGroups()) {
                    GroupTableItems groupTableSource = (GroupTableItems)tableItems;

                    GroupTable var10001;
                    Object item;
                    for(Iterator var30 = groupTableSource.rootGroups().iterator(); var30.hasNext(); r = this.createGroupRow(var10001, columns, r, (GroupInfo)item, 0)) {
                        item = var30.next();
                        if (this.checkIsRowNumberExceed(r)) {
                            break;
                        }

                        var10001 = (GroupTable)table;
                        ++r;
                    }
                } else {
                    Iterator var24 = tableItems.getItemIds().iterator();

                    while(var24.hasNext()) {
                        Object itemId = var24.next();
                        if (this.checkIsRowNumberExceed(r)) {
                            break;
                        }

                        ++r;
                        this.createRow(table, columns, 0, r, itemId);
                    }
                }

                if (table.isAggregatable() && this.exportAggregation && this.hasAggregatableColumn(table) && table.getAggregationStyle() == AggregationStyle.BOTTOM) {
                    ++r;
                    this.createAggregatableRow(table, columns, r, 1);
                }
            }

            for(int c = 0; c < columns.size(); ++c) {
                this.sheet.setColumnWidth(c, this.sizers[c].getWidth() * 48);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                this.wb.write(out);
            } catch (IOException var18) {
                throw new RuntimeException("Unable to write document", var18);
            }

            if (fileName == null) {
                fileName = this.messages.getTools().getEntityCaption(((EntityTableItems)tableItems).getEntityMetaClass());
            }

            display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
        }
    }

    public void exportDataCollection(List<KeyValueEntity> collection, Map<String, String> columns, Map<Integer, String> properties, ExportDisplay display) {
        String fileName = !StringUtils.isEmpty(this.title) ? this.title.toLowerCase() : "CustomData";
        if (!StringUtils.isEmpty(this.title)) {
            List<String> filterDescription = new ArrayList<>();
            filterDescription.add(this.title);
            this.exportDataCollection(collection, columns, properties, display, filterDescription, fileName);
        } else {
            this.exportDataCollection(collection, columns, properties, display, null, fileName);
        }
    }

    /** Xuất ra file Excel không bị ghi tiêu đề ở dòng đầu **/
    public void exportDataCollectionNoTitleInFile(List<KeyValueEntity> collection, Map<String, String> columns, Map<Integer, String> properties, ExportDisplay display) {
        String fileName = !StringUtils.isEmpty(this.title) ? this.title.toLowerCase() : "CustomData";
        if (!StringUtils.isEmpty(this.title)) {
            this.exportDataCollection(collection, columns, properties, display, null, fileName);
        }
    }

    /** Xuất ra file Excel có tiêu đề**/
    public void exportDataCollectionTitleInFile(List<KeyValueEntity> collection, Map<String, String> columns, Map<Integer,
            String> properties, ExportDisplay display, String title) {
        String fileName = !StringUtils.isEmpty(this.title) ? this.title.toLowerCase() : "CustomData";
        if (!StringUtils.isEmpty(this.title)) {
            this.exportDataCollection(collection, columns, properties, display, null, fileName, title);
        }
    }

    public void exportDataCollection(List<KeyValueEntity> collection, Map<String, String> columns, Map<Integer, String> properties,
                                     ExportDisplay display, List<String> filterDescription, String fileName) {
        if (display == null) {
            throw new IllegalArgumentException("ExportDisplay is null");
        } else {
            this.createWorkbookWithSheet();
            this.createFonts();
            this.createFormats();
            int r = 0;
            if (filterDescription != null) {
                for(r = 0; r < filterDescription.size(); ++r) {
                    String line = (String)filterDescription.get(r);
                    HSSFRow row = this.sheet.createRow(r);
                    if(r == 0){
                        HSSFRichTextString richTextFilterName = new HSSFRichTextString(line);
                        richTextFilterName.applyFont(this.boldFont);
                        row.createCell(0).setCellValue(richTextFilterName);
                    } else{
                        row.createCell(0).setCellValue(line);
                    }
                }
                ++r;
            }


            HSSFRow row = this.sheet.createRow(r);
            this.createAutoColumnSizers(columns.size());
            float maxHeight = this.sheet.getDefaultRowHeightInPoints();
            CellStyle headerCellStyle = this.wb.createCellStyle();
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            String column;
            String caption;
            for (Map.Entry<Integer, String> entry : properties.entrySet()) {
                column = entry.getValue();
                caption = StringUtils.isEmpty(columns.get(column)) ? column : columns.get(column);
                int countOfReturnSymbols = StringUtils.countMatches(caption, "\n");
                if (countOfReturnSymbols > 0) {
                    maxHeight = Math.max(maxHeight, (float)(countOfReturnSymbols + 1) * this.sheet.getDefaultRowHeightInPoints());
                    headerCellStyle.setWrapText(true);
                }
            }

            row.setHeightInPoints(maxHeight);

            for (Map.Entry<Integer, String> entry : properties.entrySet()) {
                int c = entry.getKey();
                column = entry.getValue();
                caption = StringUtils.isEmpty(columns.get(column)) ? column : columns.get(column);
                HSSFCell cell = row.createCell(c);
                HSSFRichTextString richTextString = new HSSFRichTextString(caption);
                richTextString.applyFont(this.boldFont);
                cell.setCellValue(richTextString);
                ExcelAutoColumnSizer sizer = new ExcelAutoColumnSizer();
                sizer.notifyCellValue(caption, this.boldFont);
                this.sizers[c] = sizer;
                cell.setCellStyle(headerCellStyle);
            }

            for (int i = 0; i < collection.size(); i++) {
                if (this.checkIsRowNumberExceed(r)) {
                    break;
                }
                ++r;
                this.createRow(collection, properties, r, i);
            }

            for(int c = 0; c < columns.size(); ++c) {
                this.sheet.setColumnWidth(c, this.sizers[c].getWidth() * 48);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                this.wb.write(out);
            } catch (IOException var18) {
                throw new RuntimeException("Unable to write document", var18);
            }

            if (fileName == null) {
                fileName = "NoName";
            }

            display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
        }
    }


    private CellStyle getCellStyle(Workbook wb, Font font, VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment){
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(verticalAlignment);
        cellStyle.setAlignment(horizontalAlignment);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    private Font getFont(int fontsize, String fontname, boolean isbold, Sheet sheet){
        Font font = sheet.getWorkbook().createFont();
        //"Times New Roman"
        font.setFontName(fontname);
        font.setBold(isbold);
        font.setFontHeightInPoints((short) fontsize); // font size

        return font;
    }

    public void exportDataCollection(List<KeyValueEntity> collection, Map<String, String> columns, Map<Integer, String> properties,
                                     ExportDisplay display, List<String> filterDescription, String fileName, String title) {
        if (display == null) {
            throw new IllegalArgumentException("ExportDisplay is null");
        } else {
            this.createWorkbookWithSheet();
            this.createFonts();
            this.createFormats();
            int r = 1;
            if (filterDescription != null) {
                for(r = 0; r < filterDescription.size(); ++r) {
                    String line = (String)filterDescription.get(r);
                    HSSFRow row = this.sheet.createRow(r);
                    if(r == 0){
                        HSSFRichTextString richTextFilterName = new HSSFRichTextString(line);
                        richTextFilterName.applyFont(this.boldFont);
                        row.createCell(0).setCellValue(richTextFilterName);
                    } else{
                        row.createCell(0).setCellValue(line);
                    }
                }
                ++r;
            }

            //Tạo tiêu đề cho mẫu excel
            HSSFRow rowTitle = this.sheet.createRow(0);
            for(int i=0; i < columns.size(); i++){
                Cell cell = rowTitle.createCell(i);
                if(i==0){
                    Font fontTitle = getFont(14,"Times New Roman" ,true, sheet);
                    CellStyle cellStyleTitle = getCellStyle(wb, fontTitle, VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
                    cell.setCellValue(title);
                    cell.setCellStyle(cellStyleTitle);
                }
            }
            rowTitle.setHeight((short) 800);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size()-1));

            HSSFRow row = this.sheet.createRow(r);
            this.createAutoColumnSizers(columns.size());
            float maxHeight = this.sheet.getDefaultRowHeightInPoints();
            CellStyle headerCellStyle = this.wb.createCellStyle();
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            String column;
            String caption;
            for (Map.Entry<Integer, String> entry : properties.entrySet()) {
                column = entry.getValue();
                caption = StringUtils.isEmpty(columns.get(column)) ? column : columns.get(column);
                int countOfReturnSymbols = StringUtils.countMatches(caption, "\n");
                if (countOfReturnSymbols > 0) {
                    maxHeight = Math.max(maxHeight, (float)(countOfReturnSymbols + 1) * this.sheet.getDefaultRowHeightInPoints());
                    headerCellStyle.setWrapText(true);
                }
            }

            row.setHeightInPoints(maxHeight);

            for (Map.Entry<Integer, String> entry : properties.entrySet()) {
                int c = entry.getKey();
                column = entry.getValue();
                caption = StringUtils.isEmpty(columns.get(column)) ? column : columns.get(column);
                HSSFCell cell = row.createCell(c);
                HSSFRichTextString richTextString = new HSSFRichTextString(caption);
                richTextString.applyFont(this.boldFont);
                cell.setCellValue(richTextString);
                ExcelAutoColumnSizer sizer = new ExcelAutoColumnSizer();
                sizer.notifyCellValue(caption, this.boldFont);
                this.sizers[c] = sizer;
                cell.setCellStyle(headerCellStyle);
            }

            for (int i = 0; i < collection.size(); i++) {
                if (this.checkIsRowNumberExceed(r)) {
                    break;
                }
                ++r;
                this.createRow(collection, properties, r, i);
            }

            for(int c = 0; c < columns.size(); ++c) {
                this.sheet.setColumnWidth(c, this.sizers[c].getWidth() * 48);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                this.wb.write(out);
            } catch (IOException var18) {
                throw new RuntimeException("Unable to write document", var18);
            }

            if (fileName == null) {
                fileName = "NoName";
            }

            display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
        }
    }

    public void exportDataGrid(DataGrid dataGrid, ExportDisplay display) {
        this.exportDataGrid(dataGrid, dataGrid.getColumns(), display);
    }

    public void exportDataGrid(DataGrid dataGrid, List<com.haulmont.cuba.gui.components.DataGrid.Column> columns, ExportDisplay display) {
        this.exportDataGrid(dataGrid, columns, display, (List)null, (String)null, ExcelExporter.ExportMode.ALL_ROWS);
    }

    public void exportDataGrid(DataGrid dataGrid, List<com.haulmont.cuba.gui.components.DataGrid.Column> columns, ExportDisplay display, ExcelExporter.ExportMode exportMode) {
        this.exportDataGrid(dataGrid, columns, display, (List)null, (String)null, exportMode);
    }

    public void exportDataGrid(DataGrid dataGrid, List<com.haulmont.cuba.gui.components.DataGrid.Column> columns, ExportDisplay display, List<String> filterDescription) {
        this.exportDataGrid(dataGrid, columns, display, filterDescription, (String)null, ExcelExporter.ExportMode.ALL_ROWS);
    }

    public void exportDataGrid(DataGrid<Entity> dataGrid, List<com.haulmont.cuba.gui.components.DataGrid.Column> columns, ExportDisplay display, List<String> filterDescription, String fileName, ExcelExporter.ExportMode exportMode) {
        if (display == null) {
            throw new IllegalArgumentException("ExportDisplay is null");
        } else {
            this.createWorkbookWithSheet();
            this.createFonts();
            this.createFormats();
            int r = 0;
            if (filterDescription != null) {
                for(r = 0; r < filterDescription.size(); ++r) {
                    String line = (String)filterDescription.get(r);
                    HSSFRow row = this.sheet.createRow(r);
                    if (r == 0) {
                        HSSFRichTextString richTextFilterName = new HSSFRichTextString(line);
                        richTextFilterName.applyFont(this.boldFont);
                        row.createCell(0).setCellValue(richTextFilterName);
                    } else {
                        row.createCell(0).setCellValue(line);
                    }
                }

                ++r;
            }

            HSSFRow row = this.sheet.createRow(r);
            this.createAutoColumnSizers(columns.size());
            float maxHeight = this.sheet.getDefaultRowHeightInPoints();
            CellStyle headerCellStyle = this.wb.createCellStyle();
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Iterator var11 = columns.iterator();

            com.haulmont.cuba.gui.components.DataGrid.Column column;
            String caption;
            while(var11.hasNext()) {
                column = (com.haulmont.cuba.gui.components.DataGrid.Column)var11.next();
                caption = column.getCaption();
                int countOfReturnSymbols = StringUtils.countMatches(caption, "\n");
                if (countOfReturnSymbols > 0) {
                    maxHeight = Math.max(maxHeight, (float)(countOfReturnSymbols + 1) * this.sheet.getDefaultRowHeightInPoints());
                    headerCellStyle.setWrapText(true);
                }
            }

            row.setHeightInPoints(maxHeight);

            for(int c = 0; c < columns.size(); ++c) {
                column = (com.haulmont.cuba.gui.components.DataGrid.Column)columns.get(c);
                caption = column.getCaption();
                HSSFCell cell = row.createCell(c);
                HSSFRichTextString richTextString = new HSSFRichTextString(caption);
                richTextString.applyFont(this.boldFont);
                cell.setCellValue(richTextString);
                ExcelAutoColumnSizer sizer = new ExcelAutoColumnSizer();
                sizer.notifyCellValue(caption, this.boldFont);
                this.sizers[c] = sizer;
                cell.setCellStyle(headerCellStyle);
            }

            EntityDataGridItems<Entity> dataGridSource = (EntityDataGridItems)dataGrid.getItems();
            if (dataGridSource == null) {
                throw new IllegalStateException("DataGrid is not bound to data");
            } else {
                if (exportMode == ExcelExporter.ExportMode.SELECTED_ROWS && dataGrid.getSelected().size() > 0) {
                    Set<Entity> selected = dataGrid.getSelected();
                    Stream var10000 = dataGridSource.getItems();
                    selected.getClass();
                    List<Entity> ordered = (List)var10000.filter(selected::contains).collect(Collectors.toList());
                    Iterator var33 = ordered.iterator();

                    while(var33.hasNext()) {
                        Entity item = (Entity)var33.next();
                        if (this.checkIsRowNumberExceed(r)) {
                            break;
                        }

                        ++r;
                        this.createDataGridRow(dataGrid, columns, 0, r, item.getId());
                    }
                } else if (dataGrid instanceof TreeDataGrid) {
                    TreeDataGrid treeDataGrid = (TreeDataGrid)dataGrid;
                    TreeDataGridItems<Entity> treeDataGridItems = (TreeDataGridItems)dataGridSource;
                    List<Entity> items = (List)treeDataGridItems.getChildren((Entity) null).collect(Collectors.toList());

                    Entity item;
                    for(Iterator var34 = items.iterator(); var34.hasNext(); r = this.createDataGridHierarchicalRow(treeDataGrid, treeDataGridItems, columns, 0, r, item)) {
                        item = (Entity)var34.next();
                        if (this.checkIsRowNumberExceed(r)) {
                            break;
                        }
                    }
                } else {
                    Iterator var24 = ((List)dataGridSource.getItems().map(Entity::getId).collect(Collectors.toList())).iterator();

                    while(var24.hasNext()) {
                        Object itemId = var24.next();
                        if (this.checkIsRowNumberExceed(r)) {
                            break;
                        }

                        ++r;
                        this.createDataGridRow(dataGrid, columns, 0, r, itemId);
                    }
                }

                for(int c = 0; c < columns.size(); ++c) {
                    this.sheet.setColumnWidth(c, this.sizers[c].getWidth() * 48);
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                try {
                    this.wb.write(out);
                } catch (IOException var17) {
                    throw new RuntimeException("Unable to write document", var17);
                }

                if (fileName == null) {
                    fileName = this.messages.getTools().getEntityCaption(dataGridSource.getEntityMetaClass());
                }

                display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
            }
        }
    }

    protected void createFormats() {
        this.timeFormatCellStyle = this.wb.createCellStyle();
        this.timeFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm"));
        this.dateFormatCellStyle = this.wb.createCellStyle();
        this.dateFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        this.dateTimeFormatCellStyle = this.wb.createCellStyle();
        this.dateTimeFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        this.integerFormatCellStyle = this.wb.createCellStyle();
        this.integerFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        DataFormat format = this.wb.createDataFormat();
        this.doubleFormatCellStyle = this.wb.createCellStyle();
        this.doubleFormatCellStyle.setDataFormat(format.getFormat("#,##0.################"));
    }

    protected int createHierarhicalRow(TreeTable table, List<Column> columns, Boolean exportExpanded, int rowNumber, Object itemId) {
        TreeTableItems treeTableSource = (TreeTableItems)table.getItems();
        ++rowNumber;
        this.createRow(table, columns, 0, rowNumber, itemId);
        if (BooleanUtils.isTrue(exportExpanded) && !table.isExpanded(itemId) && !treeTableSource.getChildren(itemId).isEmpty()) {
            return rowNumber;
        } else {
            Collection children = treeTableSource.getChildren(itemId);
            if (children != null && !children.isEmpty()) {
                Iterator var8 = children.iterator();

                while(true) {
                    while(var8.hasNext()) {
                        Object id = var8.next();
                        if (BooleanUtils.isTrue(exportExpanded) && !table.isExpanded(id) && !treeTableSource.getChildren(id).isEmpty()) {
                            ++rowNumber;
                            this.createRow(table, columns, 0, rowNumber, id);
                        } else {
                            rowNumber = this.createHierarhicalRow(table, columns, exportExpanded, rowNumber, id);
                        }
                    }

                    return rowNumber;
                }
            } else {
                return rowNumber;
            }
        }
    }

    protected int createAggregatableRow(Table table, List<Column> columns, int rowNumber, int aggregatableRow) {
        HSSFRow row = this.sheet.createRow(rowNumber);
        Map<Object, Object> results = table.getAggregationResults();
        int i = 0;

        for(Iterator var8 = columns.iterator(); var8.hasNext(); ++i) {
            Column column = (Column)var8.next();
            AggregationInfo agr = column.getAggregation();
            if (agr != null) {
                Object key = agr.getPropertyPath() != null ? agr.getPropertyPath() : column.getId();
                Object aggregationResult = results.get(key);
                if (aggregationResult != null) {
                    HSSFCell cell = row.createCell(i);
                    this.formatValueCell(cell, aggregationResult, (MetaPropertyPath)null, i, rowNumber, 0, (Integer)null);
                }
            }
        }

        return rowNumber;
    }

    protected int createGroupRow(GroupTable table, List<Column> columns, int rowNumber, GroupInfo groupInfo, int groupNumber) {
        GroupTableItems<Entity> groupTableSource = (GroupTableItems)table.getItems();
        HSSFRow row = this.sheet.createRow(rowNumber);
        Map<Object, Object> aggregations = table.isAggregatable() ? table.getAggregationResults(groupInfo) : Collections.emptyMap();
        int i = 0;
        int initialGroupNumber = groupNumber;

        Object aggregationResult;
        for(Iterator var11 = columns.iterator(); var11.hasNext(); ++i) {
            Column column = (Column)var11.next();
            Object val;
            if (i == initialGroupNumber) {
                HSSFCell cell = row.createCell(i);
                val = groupInfo.getValue();
                if (val == null) {
                    val = this.messages.getMessage(this.getClass(), "excelExporter.empty");
                }

                Collection children = groupTableSource.getGroupItemIds(groupInfo);
                if (children.isEmpty()) {
                    return rowNumber;
                }

                Integer groupChildCount = null;
                if (table.isShowItemsCountForGroup()) {
                    groupChildCount = children.size();
                }

                Object captionValue = val;
                Element xmlDescriptor = column.getXmlDescriptor();
                if (xmlDescriptor != null && StringUtils.isNotEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
                    String captionProperty = xmlDescriptor.attributeValue("captionProperty");
                    Object itemId = children.iterator().next();
                    Instance item = (Instance)groupTableSource.getItemNN(itemId);
                    captionValue = item.getValueEx(captionProperty);
                }

                GroupCellValueFormatter<Entity> groupCellValueFormatter = table.getGroupCellValueFormatter();
                if (groupCellValueFormatter != null) {
                    groupChildCount = null;
                    Stream var10000 = groupTableSource.getGroupItemIds(groupInfo).stream();
                    groupTableSource.getClass();
                    List<Entity> groupItems = (List)var10000.map(groupTableSource::getItem).collect(Collectors.toList());
                    GroupCellContext<Entity> cellContext = new GroupCellContext(groupInfo, captionValue, this.metadataTools.format(captionValue), groupItems);
                    captionValue = groupCellValueFormatter.format(cellContext);
                }

                MetaPropertyPath columnId = (MetaPropertyPath)column.getId();
                this.formatValueCell(cell, captionValue, columnId, groupNumber++, rowNumber, 0, groupChildCount);
            } else {
                AggregationInfo agr = column.getAggregation();
                if (agr != null) {
                    val = agr.getPropertyPath() != null ? agr.getPropertyPath() : column.getId();
                    aggregationResult = aggregations.get(val);
                    if (aggregationResult != null) {
                        HSSFCell cell = row.createCell(i);
                        this.formatValueCell(cell, aggregationResult, (MetaPropertyPath)null, i, rowNumber, 0, (Integer)null);
                    }
                }
            }
        }

        int oldRowNumber = rowNumber;
        List<GroupInfo> children = groupTableSource.getChildren(groupInfo);
        GroupInfo child;
        if (children.size() > 0) {
            for(Iterator var25 = children.iterator(); var25.hasNext(); rowNumber = this.createGroupRow(table, columns, rowNumber, child, groupNumber)) {
                child = (GroupInfo)var25.next();
                ++rowNumber;
            }
        } else {
            Collection<?> itemIds = groupTableSource.getGroupItemIds(groupInfo);
            Iterator var29 = itemIds.iterator();

            while(var29.hasNext()) {
                aggregationResult = var29.next();
                ++rowNumber;
                this.createRow(table, columns, groupNumber, rowNumber, aggregationResult);
            }
        }

        if (this.checkIsRowNumberExceed(rowNumber)) {
            this.sheet.groupRow(oldRowNumber + 1, 65535);
        } else {
            this.sheet.groupRow(oldRowNumber + 1, rowNumber);
        }

        return rowNumber;
    }

    protected void createRow(Table table, List<Column> columns, int startColumn, int rowNumber, Object itemId) {
        if (startColumn < columns.size()) {
            if (rowNumber <= 65535) {
                HSSFRow row = this.sheet.createRow(rowNumber);
                Entity instance = (Entity)table.getItems().getItem(itemId);
                int level = 0;
                if (table instanceof TreeTable) {
                    level = ((TreeTable)table).getLevel(itemId);
                }

                for(int c = startColumn; c < columns.size(); ++c) {
                    HSSFCell cell = row.createCell(c);
                    Column column = (Column)columns.get(c);
                    Object cellValue = null;
                    MetaPropertyPath propertyPath = null;
                    Printable printable;
                    if (column.getId() instanceof MetaPropertyPath) {
                        propertyPath = (MetaPropertyPath)column.getId();
                        printable = table.getPrintable(column);
                        if (printable != null) {
                            cellValue = printable.getValue(instance);
                        } else {
                            Element xmlDescriptor = column.getXmlDescriptor();
                            if (xmlDescriptor != null && StringUtils.isNotEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
                                String captionProperty = xmlDescriptor.attributeValue("captionProperty");
                                cellValue = InstanceUtils.getValueEx(instance, captionProperty);
                            } else {
                                cellValue = InstanceUtils.getValueEx(instance, propertyPath.getPath());
                            }

                            if (column.getFormatter() != null) {
                                cellValue = column.getFormatter().apply(cellValue);
                            }
                        }
                    } else {
                        printable = table.getPrintable(column);
                        if (printable != null) {
                            cellValue = printable.getValue(instance);
                        } else if (column.getValueProvider() != null) {
                            cellValue = column.getValueProvider().apply(instance);
                        }
                    }
                    if (column.getIdString().contains("gioiTinh")) {
                        if (cellValue != null)
                            cell.setCellValue(cellValue.toString().contains("1") ? "Nam" : (cellValue.toString().contains("2") ? "Nữ" : ""));
                        else
                            cell.setCellValue("");
                    } else {
                        this.formatValueCell(cell, cellValue, propertyPath, c, rowNumber, level, (Integer) null);
                    }
                }

            }
        }
    }

    protected void createRow(List<KeyValueEntity> collection, Map<Integer, String> properties, int rowNumber, int itemId) {
        if (rowNumber <= 65535) {
            HSSFRow row = this.sheet.createRow(rowNumber);
            Entity instance = collection.get(itemId);
            int level = 0;
            for (Map.Entry<Integer, String> entryCol : properties.entrySet()) {
                int c = entryCol.getKey();
                HSSFCell cell = row.createCell(c);
                String column = entryCol.getValue();
                Object cellValue = null;
                MetaPropertyPath propertyPath = null;
                try {
                    cellValue = instance.getValue(column);
                } catch (Exception ex){
                }
                if (column.contains("gioiTinh"))
                    cell.setCellValue(cellValue.toString().contains("1") ? "Nam" : (cellValue.toString().contains("2") ? "Nữ" : ""));
                else
                    this.formatValueCell(cell, cellValue, propertyPath, c, rowNumber, level, (Integer)null);
            }

        }
    }

    protected int createDataGridHierarchicalRow(TreeDataGrid dataGrid, TreeDataGridItems<Entity> treeDataGridItems, List<com.haulmont.cuba.gui.components.DataGrid.Column> columns, int startColumn, int rowNumber, Entity item) {
        if (!this.checkIsRowNumberExceed(rowNumber)) {
            ++rowNumber;
            this.createDataGridRow(dataGrid, columns, startColumn, rowNumber, item.getId());
            Collection<Entity> children = (Collection)treeDataGridItems.getChildren(item).collect(Collectors.toList());

            Entity child;
            for(Iterator var8 = children.iterator(); var8.hasNext(); rowNumber = this.createDataGridHierarchicalRow(dataGrid, treeDataGridItems, columns, startColumn, rowNumber, child)) {
                child = (Entity)var8.next();
            }
        }

        return rowNumber;
    }

    protected void createDataGridRow(DataGrid dataGrid, List<com.haulmont.cuba.gui.components.DataGrid.Column> columns, int startColumn, int rowNumber, Object itemId) {
        if (startColumn < columns.size()) {
            HSSFRow row = this.sheet.createRow(rowNumber);
            Entity item = (Entity)dataGrid.getItems().getItem(itemId);
            int level = 0;
            if (dataGrid instanceof TreeDataGrid) {
                level = ((TreeDataGrid)dataGrid).getLevel(item);
            }

            for(int c = startColumn; c < columns.size(); ++c) {
                HSSFCell cell = row.createCell(c);
                com.haulmont.cuba.gui.components.DataGrid.Column column = (com.haulmont.cuba.gui.components.DataGrid.Column)columns.get(c);
                Object cellValue = null;
                MetaPropertyPath propertyPath = null;
                if (column.getPropertyPath() != null) {
                    propertyPath = column.getPropertyPath();
                    cellValue = InstanceUtils.getValueEx(item, propertyPath.getPath());
                    if (column.getFormatter() != null) {
                        cellValue = column.getFormatter().apply(cellValue);
                    }
                } else {
                    ColumnGenerator generator;
                    if ((generator = dataGrid.getColumnGenerator(column.getId())) != null) {
                        ColumnGeneratorEvent event = new ColumnGeneratorEvent(dataGrid, item, column.getId(), o -> {
                            return true;
                        });
                        cellValue = generator.getValue(event);
                        if (cellValue == null && Boolean.class.equals(generator.getType())) {
                            cellValue = false;
                        }
                    }
                }
                if (propertyPath.toPathString().contains("gioiTinh"))
                    cell.setCellValue(cellValue.toString().contains("1") ? "Nam" : (cellValue.toString().contains("2") ? "Nữ" : ""));
                else
                    this.formatValueCell(cell, cellValue, propertyPath, c, rowNumber, level, (Integer)null);
            }

        }
    }

    protected String createSpaceString(int level) {
        if (level == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < level * 10; ++i) {
                sb.append(" ");
            }

            return sb.toString();
        }
    }

    protected void formatValueCell(HSSFCell cell, @Nullable Object cellValue, @Nullable MetaPropertyPath metaPropertyPath, int sizersIndex, int notificationRequired, int level, @Nullable Integer groupChildCount) {
        if (cellValue == null) {
            if (metaPropertyPath == null || !metaPropertyPath.getRange().isDatatype()) {
                return;
            }

            Class javaClass = metaPropertyPath.getRange().asDatatype().getJavaClass();
            if (Boolean.class.equals(javaClass)) {
                cellValue = false;
            }
        }

        String childCountValue = "";
        if (groupChildCount != null) {
            childCountValue = " (" + groupChildCount + ")";
        }

        if (cellValue instanceof IdProxy) {
            cellValue = ((IdProxy)cellValue).get();
        }

        String str;
        if (cellValue instanceof Number) {
            Number n = (Number)cellValue;
            Datatype datatype = null;
            if (metaPropertyPath != null) {
                Range range = metaPropertyPath.getMetaProperty().getRange();
                if (range.isDatatype()) {
                    datatype = range.asDatatype();
                }
            }

            datatype = datatype == null ? Datatypes.getNN(n.getClass()) : datatype;
            if (sizersIndex == 0 && level > 0) {
                str = this.createSpaceString(level) + datatype.format(n);
                cell.setCellValue(str);
            } else {
                try {
                    str = datatype.format(n);
                    Number result = (Number)datatype.parse(str);
                    if (result != null) {
                        if (!(n instanceof Integer) && !(n instanceof Long) && !(n instanceof Byte) && !(n instanceof Short)) {
                            cell.setCellValue(result.doubleValue());
                            cell.setCellStyle(this.doubleFormatCellStyle);
                        } else {
                            cell.setCellValue((double)result.longValue());
                            cell.setCellStyle(this.integerFormatCellStyle);
                        }
                    }
                } catch (ParseException var18) {
                    throw new RuntimeException("Unable to parse numeric value", var18);
                }

                cell.setCellType(CellType.NUMERIC);
            }

            if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
            }
        } else if (cellValue instanceof Date) {
            Class javaClass = null;
            boolean supportTimezones = false;
            TimeZone timeZone = this.userSessionSource.getUserSession().getTimeZone();
            if (metaPropertyPath != null) {
                MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
                if (metaProperty.getRange().isDatatype()) {
                    javaClass = metaProperty.getRange().asDatatype().getJavaClass();
                }

                Boolean ignoreUserTimeZone = (Boolean)this.metadataTools.getMetaAnnotationValue(metaProperty, IgnoreUserTimeZone.class);
                supportTimezones = timeZone != null && Objects.equals(Date.class, javaClass) && !Boolean.TRUE.equals(ignoreUserTimeZone);
            }

            Date date = (Date)cellValue;
            if (supportTimezones) {
                TimeZone currentTimeZone = LocaleUtil.getUserTimeZone();

                try {
                    LocaleUtil.setUserTimeZone(timeZone);
                    cell.setCellValue(date);
                } finally {
                    if (Objects.equals(currentTimeZone, TimeZone.getDefault())) {
                        LocaleUtil.resetUserTimeZone();
                    } else {
                        LocaleUtil.setUserTimeZone(currentTimeZone);
                    }

                }
            } else {
                cell.setCellValue(date);
            }

            if (Objects.equals(Time.class, javaClass)) {
                cell.setCellStyle(this.timeFormatCellStyle);
            } else if (Objects.equals(java.sql.Date.class, javaClass)) {
                cell.setCellStyle(this.dateFormatCellStyle);
            } else {
                cell.setCellStyle(this.dateTimeFormatCellStyle);
            }

            if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
//                String str = Datatypes.getNN(Date.class).format(date);
                str = Datatypes.getNN(Date.class).format(date);
                this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
            }
        } else {
//            String str;
            if (cellValue instanceof Boolean) {
                str = "";
                if (sizersIndex == 0) {
                    str = str + this.createSpaceString(level);
                }

                str = str + ((Boolean)cellValue ? this.trueStr : this.falseStr);
                cell.setCellValue(new HSSFRichTextString(str));
                if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                    this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
                }
            } else {
//                String str;
                if (cellValue instanceof EnumClass) {
                    if (cellValue == RoleType.STANDARD)
                        str = "Tiêu chuẩn";
                    else if (cellValue == RoleType.SUPER)
                        str = "Quản trị";
                    else if (cellValue == RoleType.READONLY)
                        str = "Quyền đọc";
                    else if (cellValue == RoleType.DENYING)
                        str = "Khóa quyền";
                    else
                        str = cellValue.getClass().getSimpleName() + "." + cellValue.toString();
                    str = sizersIndex == 0 ? this.createSpaceString(level) + this.messages.getMessage(cellValue.getClass(), str) : this.messages.getMessage(cellValue.getClass(), str);
                    cell.setCellValue(str + childCountValue);
                    if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                        this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
                    }
                } else if (cellValue instanceof Entity) {
                    Entity entityVal = (Entity)cellValue;
                    str = this.metadataTools.getInstanceName(entityVal);
                    str = sizersIndex == 0 ? this.createSpaceString(level) + str : str;
                    str = str + childCountValue;
                    cell.setCellValue(new HSSFRichTextString(str));
                    if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                        this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
                    }
                } else if (cellValue instanceof Collection) {
                    str = "";
                    cell.setCellValue(new HSSFRichTextString(str));
                    if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                        this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
                    }
                } else {
                    str = cellValue == null ? "" : cellValue.toString();
                    str = sizersIndex == 0 ? this.createSpaceString(level) + str : str;
                    str = str + childCountValue;
                    cell.setCellValue(new HSSFRichTextString(str));
                    if (this.sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                        this.sizers[sizersIndex].notifyCellValue(str, this.stdFont);
                    }
                }
            }
        }

    }

    protected boolean checkIsRowNumberExceed(int r) {
        return this.isRowNumberExceeded = r >= 65535;
    }

    public boolean isXlsMaxRowNumberExceeded() {
        return this.isRowNumberExceeded;
    }

    public void setExportAggregation(boolean exportAggregation) {
        this.exportAggregation = exportAggregation;
    }

    public boolean getExportAggregation() {
        return this.exportAggregation;
    }

    protected boolean hasAggregatableColumn(Table table) {
        List<Column> columns = table.getColumns();
        Iterator var3 = columns.iterator();

        Column column;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            column = (Column)var3.next();
        } while(column.getAggregation() == null);

        return true;
    }

    public static enum ExportMode {
        SELECTED_ROWS,
        ALL_ROWS;

        private ExportMode() {
        }
    }

}
