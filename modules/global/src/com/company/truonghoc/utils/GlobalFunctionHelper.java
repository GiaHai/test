package com.company.truonghoc.utils;

import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Section;
import com.company.truonghoc.utils.aspose.DocumentPageSplitter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalFunctionHelper {
    private static final Logger logger = LoggerFactory.getLogger(GlobalFunctionHelper.class);

    /*************************************************************************************
     * Create: 27-08-2019
     * Modify: 27-08-2019
     * Description: tạo thư mục theo cấu trúc upload của CUBA
     **************************************************************************************/
    public static boolean createStorageFolder(String rootFolder, String pathSubFolder) {
        String tempFolderPath = rootFolder.replace("file://", "").replace("/", "\\");
        String[] arrDateFolder;
        if (pathSubFolder.contains("/")) {
            arrDateFolder = pathSubFolder.split("/");
        } else {
            arrDateFolder = pathSubFolder.split("\\\\");
        }

        if (arrDateFolder.length > 1) {
            for (String subFolder : arrDateFolder) {
                String lastCharInPath = tempFolderPath.substring(tempFolderPath.length() - 1);
                if (lastCharInPath.equals("\\")) {
                    tempFolderPath += subFolder;
                } else {
                    tempFolderPath += "\\" + subFolder;
                }

                File folder = new File(tempFolderPath);
                if (!folder.exists()) {
                    boolean blCreated = folder.mkdirs();
                    if (!blCreated)
                        return false;
                }
            }
        }
        return true;
    }

    public static String getAndCreateFolder(String rootFolder, String... subFolder) {
        Path p = Paths.get(rootFolder, subFolder);
        if (!Files.isDirectory(p)) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                logger.info("Error getAndCreateFolder ", e);
                return "";
            }
        }
        return p.toAbsolutePath().toString();
    }

    public static void pushDataToTempByParagraph(XWPFDocument document, Map<String, Object> parameter) {
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
            findAndReplaceText(paragraph, parameter);
        }
    }

    private static void findAndReplaceText(XWPFParagraph p, Map<String, Object> data) {
        String pText = p.getText(); // complete paragraph as string
        if (pText.contains("${")) { // if paragraph does not include our pattern, ignore
            TreeMap<Integer, XWPFRun> posRuns = getPosToRuns(p);
            Pattern pat = Pattern.compile("\\$\\{(.+?)\\}");
            Matcher m = pat.matcher(pText);
            while (m.find()) { // for all patterns in the paragraph
                String g = m.group(1);  // extract key start and end pos
                int s = m.start(1);
                int e = m.end(1);
                String key = g;
                if (data.containsKey(key)) {
                    String x = isNull_String(data.get(key));

                    SortedMap<Integer, XWPFRun> range = posRuns.subMap(s - 2, true, e + 1, true); // get runs which contain the pattern
                    boolean found1 = false; // found $
                    boolean found2 = false; // found {
                    boolean found3 = false; // found }
                    XWPFRun prevRun = null; // previous run handled in the loop
                    XWPFRun found2Run = null; // run in which { was found
                    int found2Pos = -1; // pos of { within above run

                    for (XWPFRun r : range.values()) {
                        if (r == prevRun)
                            continue; // this run has already been handled

                        if (found3)
                            break; // done working on current key pattern

                        prevRun = r;
                        for (int k = 0;; k++) { // iterate over texts of run r
                            if (found3)
                                break;

                            String txt = null;
                            try {
                                txt = r.getText(k); // note: should return null, but throws exception if the text does not exist
                            } catch (Exception ignored) {}

                            if (txt == null)
                                break; // no more texts in the run, exit loop

                            if (txt.contains("$") && !found1) {  // found $, replace it with value from data map
                                txt = txt.replaceFirst("\\$", x);
                                found1 = true;
                            }

                            if (txt.contains("{") && !found2 && found1) {
                                found2Run = r; // found { replace it with empty string and remember location
                                found2Pos = txt.indexOf('{');
                                txt = txt.replaceFirst("\\{", "");
                                found2 = true;
                            }

                            if (found1 && found2 && !found3) { // find } and set all chars between { and } to blank

                                if (txt.contains("}")) {
                                    if (r == found2Run) { // complete pattern was within a single run
                                        txt = txt.substring(0, found2Pos)+txt.substring(txt.indexOf('}'));
                                    } else {  // pattern spread across multiple runs
                                        txt = txt.substring(txt.indexOf('}'));
                                    }
                                } else if (r == found2Run) // same run as { but no }, remove all text starting at {
                                    txt = txt.substring(0,  found2Pos);
                                else
                                    txt = ""; // run between { and }, set text to blank
                            }

                            if (txt.contains("}") && !found3) {
                                txt = txt.replaceFirst("\\}", "");
                                found3 = true;
                            }
                            r.setText(txt, k);
                        }
                    }
                }
            }
        }

    }

    private static String isNull_String(Object value) {
        if(value == null || StringUtils.isBlank(String.valueOf(value)))
            return "";

        return String.valueOf(value).trim();
    }

    private static TreeMap<Integer, XWPFRun> getPosToRuns(XWPFParagraph paragraph) {
        int pos = 0;
        TreeMap<Integer, XWPFRun> map = new TreeMap<>();
        for (XWPFRun run : paragraph.getRuns()) {
            String runText = run.text();
            if (runText != null && runText.length() > 0) {
                for (int i = 0; i < runText.length(); i++) {
                    map.put(pos + i, run);
                }
                pos += runText.length();
            }

        }
        return map;
    }


    public static void pushDataToTempByTable(XWPFDocument document, Map<String, Object> parameter) {
        List<XWPFTable> tables = document.getTables();

        AtomicReference<List<Integer>> tableDeletes = new AtomicReference<>(new ArrayList<>());
        AtomicInteger tableIndex = new AtomicInteger(0);

        tables.forEach(xwpfTable -> {
            Map<Integer, XWPFTableRow> rowData = new LinkedHashMap<>();
            Map<Integer, List<CellTable>> cellTables = new LinkedHashMap<>();
            AtomicBoolean isDeleteTable = new AtomicBoolean(false);

            fillDataAndCheckCreateRow(xwpfTable.getRows(), rowData, cellTables, parameter, isDeleteTable);

            if (!rowData.isEmpty() && !cellTables.isEmpty()) {
                // Fill du lieu vao row, neu list > 1 thi se insert them row moi
                // Th: row bang lastRow thi tao moi row cuoi cung

                rowData.forEach((idx, tableRow) -> {
                    List<CellTable> datas = cellTables.get(idx);
                    AtomicInteger rowIdxReal = new AtomicInteger(idx);

                    if (datas != null && !datas.isEmpty()) {
                        AtomicReference<XWPFTableRow> oldRow = new AtomicReference<>(null);
                        AtomicInteger rowPrev = new AtomicInteger(0);
                        AtomicReference<XWPFTableRow> newRow = new AtomicReference<>(null);
                        AtomicReference<CellTable> cellPrev = new AtomicReference<>(null);

                        datas.forEach(cellTable -> {
                            fillAndCreateTableRow(idx, rowPrev, tableRow, cellTable, oldRow, rowIdxReal, newRow, cellPrev, xwpfTable);
                            cellPrev.set(cellTable);
                        });

                        // Add row cuoi cung trong danh sach
                        if (newRow.get() != null) {
                            xwpfTable.addRow(newRow.get(), rowIdxReal.get());
                            rowIdxReal.incrementAndGet();
                        }
                    }

                });
            }

            if (isDeleteTable.get()) {
                tableDeletes.get().add(tableIndex.get());
            }

            tableIndex.getAndIncrement();
//            else {
//                // Đẩy dữ liệu vao table trong trương hợp biến đơn mà k phải là biến tạo row
//                pushDataToValueInTable(xwpfTable, parameter);
//            }
        });

        logger.info("Template printer Table delete {}", tableDeletes.get());

        tableDeletes.get().forEach(index -> {
            boolean bl = deleteOneTable(document, index);
            logger.info("Delete table index {} is success!", bl);
        });
    }

    private static boolean deleteOneTable(XWPFDocument document, Integer tableIndex) {
        try {
            int bodyElement = getBodyElementOfTable( document, tableIndex );
            document.removeBodyElement( bodyElement );

            return true;
        } catch( Exception e ) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }
    private static int getBodyElementOfTable(XWPFDocument document, int tableNumberInDocument) {
        List<XWPFTable> tables = document.getTables();
        XWPFTable theTable = tables.get(tableNumberInDocument);

        return document.getPosOfTable( theTable );
    }

    private static void fillAndCreateTableRow(int idx, AtomicInteger rowPrev,
                                              XWPFTableRow tableRow,
                                              CellTable cellTable,
                                              AtomicReference<XWPFTableRow> oldRow,
                                              AtomicInteger rowIdxReal,
                                              AtomicReference<XWPFTableRow> newRow,
                                              AtomicReference<CellTable> cellPrev, XWPFTable xwpfTable) {
        try {
            // Fill du lieu vao row duoc danh dau
            if (rowPrev.get() == 0) {

                tableRow.getCell(cellTable.getCol() - 1)
                        .getParagraphs()
                        .forEach(p -> {
                            if (!p.getRuns().isEmpty()) {
                                XWPFRun xwpfRun = p.getRuns().get(0);
                                if (xwpfRun != null) {
                                    xwpfRun.setText(cellTable.getValue(), 0);

                                    if (cellTable.getAlignment() != null) {
                                        setAlimentParagraph(p, cellTable.getAlignment());
                                    }
                                }
                            }
                        });

                oldRow.set(tableRow);
                rowPrev.set(cellTable.getRow());
//                rowIdxReal.set(idx + rowIdxReal.get());
            } else {
                // Nhung row sau do se duoc insert moi
                if (oldRow.get() != null) {
                    try {

                        if (cellTable.getRow() != rowPrev.get()) {
                            if (newRow.get() != null) {
                                xwpfTable.addRow(newRow.get(), rowIdxReal.get()); // rowIdxReal.get() + idx
//                                rowIdxReal.incrementAndGet();
                                oldRow.set(newRow.get());
                            }

                            rowIdxReal.incrementAndGet();
                            CTRow ctrow = CTRow.Factory.parse(oldRow.get().getCtRow().newInputStream());
                            newRow.set(new XWPFTableRow((org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow) ctrow, xwpfTable));
                            rowPrev.set(cellTable.getRow());
                            // Reset value khi copy tu row cu
                            for (XWPFTableCell cell : newRow.get().getTableCells()) {
                                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                    if (!paragraph.getRuns().isEmpty()) {
                                        paragraph.getRuns().forEach(xwpfRun -> {
                                            if (xwpfRun != null) {
                                                xwpfRun.setText("", 0);
                                            }
                                        });
//                                        XWPFRun xwpfRun = paragraph.getRuns().get(0);
//
//                                        if (xwpfRun != null) {
//                                            xwpfRun.setText("", 0);
//                                        }
                                    }

                                }
                            }
                        }

                        if (newRow.get() != null) {
//                            int i = 1;
                            XWPFTableCell xwpfTableCell = newRow.get().getCell(cellTable.getCol() - 1);
                            if (xwpfTableCell == null) {
                                xwpfTableCell = newRow.get().createCell();
                            }

                            if (!xwpfTableCell.getParagraphs().isEmpty()) {
                                XWPFParagraph paragraph = xwpfTableCell.getParagraphs().get(0);
                                if (!paragraph.getRuns().isEmpty()) {
                                    XWPFRun xwpfRun = paragraph.getRuns().get(0);

                                    if (xwpfRun != null) {
                                        xwpfRun.setText(cellTable.getValue(), 0);

                                        if (cellTable.getAlignment() != null) {
                                            setAlimentParagraph(paragraph, cellTable.getAlignment());
                                        }
                                    }
                                }
                            }
                        } else {
                            XWPFTableCell xwpfTableCell = oldRow.get().getCell(cellTable.getCol() - 1);
                            if (xwpfTableCell != null) {
                                xwpfTableCell.getParagraphs().forEach(p -> {
                                    if (p.getRuns().isEmpty()) {
                                        // ++ Clone lai paragraph truoc do de co cung run
                                        XWPFTableCell xwpfTableCellPrev = oldRow.get().getCell(cellPrev.get().getCol() - 1);
                                        if (xwpfTableCellPrev != null && !xwpfTableCellPrev.getParagraphs().isEmpty()) {
                                            cloneParagraph(p, xwpfTableCellPrev.getParagraphs().get(0));
                                        }
                                    }

                                    // ++ Neu van k clone duoc thi tao moi run
                                    if (p.getRuns().isEmpty()) {
                                        setRun(p.createRun() , cellTable.getFont(), cellTable.getFontSize(),cellTable.isBold());
                                    }

                                    if (!p.getRuns().isEmpty()) {
                                        XWPFRun xwpfRun = p.getRuns().get(0);
                                        if (xwpfRun != null) {
                                            xwpfRun.setText(cellTable.getValue(), 0);

                                            if (cellTable.getAlignment() != null) {
                                                setAlimentParagraph(p, cellTable.getAlignment());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } catch (XmlException | IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static void setRun(XWPFRun run, String text, int fontSize, boolean bold) {
        run.setFontFamily("Times New Roman");
        run.setFontSize(fontSize);
        run.setText(text);
        run.setBold(bold);
    }

    private static void cloneParagraph(XWPFParagraph clone, XWPFParagraph source) {
        CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
        pPr.set(source.getCTP().getPPr());
        for (XWPFRun r : source.getRuns()) {
            XWPFRun nr = clone.createRun();
            cloneRun(nr, r);
        }
    }

    public static void cloneRun(XWPFRun clone, XWPFRun source) {
        CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
        rPr.set(source.getCTR().getRPr());
        clone.setText(source.getText(0));
    }

    private static void setAlimentParagraph(XWPFParagraph paragraph, Alignment alignment) {
        if (alignment == null) return;

        switch (alignment) {
            case LEFT: paragraph.setAlignment(ParagraphAlignment.LEFT); break;
            case RIGHT: paragraph.setAlignment(ParagraphAlignment.RIGHT); break;
            case CENTER: paragraph.setAlignment(ParagraphAlignment.CENTER); break;
        }
    }

    private static void fillDataAndCheckCreateRow(List<XWPFTableRow> rows,
                                                  Map<Integer, XWPFTableRow> rowData,
                                                  Map<Integer, List<CellTable>> cellTables,
                                                  Map<String, Object> parameter,
                                                  AtomicBoolean isDeleteTable) {
        AtomicInteger rowIdx = new AtomicInteger(0);
        int numberRowOfTable = rows.size();
        int numberRowEmpty = 0;

        for (XWPFTableRow row : rows) {
            int numberColOfRow;

            if (row.getTableCells() != null) {
                numberColOfRow = row.getTableCells().size();

                AtomicInteger numberColEmpty = new AtomicInteger(0);
                row.getTableCells().forEach(cell -> {

                    if (cell != null) {
                        String text = isNull_String(cell.getText()).trim();

                        if (text.startsWith("${") && text.endsWith("}")) {
                            text = StringUtils.replace(text, "${", "");
                            text = StringUtils.replace(text, "}", "");

                            if (parameter.containsKey(text)) {
                                if (parameter.get(text) instanceof List) {
                                    List<CellTable> cells = (List<CellTable>) parameter.get(text);

                                    if (cells == null || cells.isEmpty()) {
                                        numberColEmpty.getAndIncrement();
                                    }

                                    rowData.put(rowIdx.get(), row);
                                    cellTables.put(rowIdx.get(), cells);

//                                cell.getParagraphs().forEach(p -> p.getRuns().forEach(xwpfRun -> xwpfRun.setText("", 0)));
                                    String keyReset = text;
                                    cell.getParagraphs().forEach(p -> {
                                        Map<String, Object> resetValue = new HashMap<>();
                                        resetValue.put(keyReset, "");
                                        findAndReplaceText(p, resetValue);
                                    });

                                } else {
                                    if (StringUtils.isBlank(isNull_String(parameter.get(text)))) {
                                        numberColEmpty.getAndIncrement();
                                    }

                                    List<XWPFParagraph> paragraphList = cell.getParagraphs();
                                    paragraphList.forEach(p -> findAndReplaceText(p, parameter));
                                }
                            }
//                            else if (cell.getParagraphs().size() > 1){
//                                List<XWPFParagraph> paragraphList = cell.getParagraphs();
//                                paragraphList.forEach(p -> findAndReplaceText(p, parameter));
//                            }
                        } else {
                            List<XWPFParagraph> paragraphList = cell.getParagraphs();
                            paragraphList.forEach(p -> findAndReplaceText(p, parameter));
                        }
                    }
                });

                if (numberColEmpty.get() == numberColOfRow) {
                    ++numberRowEmpty;
                }
            }

            rowIdx.incrementAndGet();

        }

        if (numberRowOfTable == numberRowEmpty) {
            isDeleteTable.set(true);
        }
    }
    public static boolean writeFile(XWPFDocument document, String output) throws IOException {
        if (StringUtils.isBlank(output) || document == null) return false;

        try {
            File fileOutput = new File(output);
            // Nếu tồn tại file trước đó thì xóa, tạo file mới
            if (fileOutput.exists()) {
                logger.info("Delete file output {} is success {}", output, fileOutput.delete());
            }

            FileOutputStream out = new FileOutputStream(fileOutput);
            document.write(out);
            out.close();

            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean checkIsPageBreakTableSigninFooter(String pathFile, Map<String, Object> parameter) {
        if (StringUtils.isBlank(pathFile)) return false;

        boolean result = false;
        try {
            com.aspose.words.Document srcDoc = new com.aspose.words.Document(pathFile);
            DocumentPageSplitter splitter = new DocumentPageSplitter(srcDoc);

            if (srcDoc.getPageCount() > 1) {
//                srcDoc.save(new File(pathFile).getParent() + "/" + UUID.randomUUID().toString() + ".docx");

                int count = 0;
                for (int page = srcDoc.getPageCount() - 1; page <= srcDoc.getPageCount(); page++) {
                    com.aspose.words.Document pageDoc = splitter.getDocumentOfPage(page);

                    boolean isBreak = false;
                    AtomicBoolean findRoomsSuccess = new AtomicBoolean(false);

                    for (Section section : pageDoc.getSections()) {
                        if (isBreak) {
                            break;
                        }

                        Paragraph paragraph;
                        int index = 0;
                        do {
                            paragraph = (Paragraph) section.getChild(NodeType.PARAGRAPH, index, true);
                            if (paragraph != null) {
                                String valuePara = isNull_String(paragraph.getText()).trim();
                                boolean bl = checkContainValueTable(valuePara, parameter, findRoomsSuccess);
                                if (bl) {
                                    isBreak = true;
                                    ++count;
                                }
                            }
                            ++index;
                        } while (paragraph != null && !isBreak);
                    }
                }

                if (count == 2) {
                    result = true;
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    private static boolean checkContainValueTable(String value, Map<String, Object> parameter, AtomicBoolean findRoomsSuccess) {
        if (StringUtils.isBlank(value) || parameter == null || parameter.isEmpty()) return false;

        try {
            if (value.equalsIgnoreCase(GlobalConstants.MAU_IN_TITLE_NGUOI_LAP_PHIEU)) {
                return true;
            }

            if (parameter.get(GlobalConstants.MAU_IN_TITLE_ROOMS) instanceof List) {
                List<CellTable> cells = (List<CellTable>) parameter.get(GlobalConstants.MAU_IN_TITLE_ROOMS);
                for (CellTable cell : cells) {
                    if (value.equals(cell.getValue().trim())) {
                        findRoomsSuccess.set(true);
                        return true;
                    }
                }
            }

            if (findRoomsSuccess.get()) {
                String nguoiLapPhieu = isNull_String(parameter.get(GlobalConstants.MAU_IN_FLAT_NGUOI_LAP_PHIEU));
                String nguoiKy = isNull_String(parameter.get(GlobalConstants.MAU_IN_FLAT_SIGNIN));
                if (value.equals(nguoiLapPhieu) || value.equals(nguoiKy)) {
                    return true;
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    public static void findAndBreakPage(XWPFDocument document, boolean isBreak) {
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList) {
            String flatBreak = isNull_String(paragraph.getText()).trim();
            if (!StringUtils.isBlank(flatBreak) && flatBreak.equalsIgnoreCase(GlobalConstants.MAU_IN_FLAT_BREAK)) {
                Map<String, Object> parameter = new HashMap<>();
                parameter.put(GlobalConstants.MAU_IN_FLAT_KEY_BREAK, "");
                findAndReplaceText(paragraph, parameter);
                if (isBreak) {
                    paragraph.setPageBreak(true);
                }

                break;
            }
        }
    }

    public static void deleteFile(String path) {
        if (StringUtils.isBlank(path)) return;

        File file = new File(path);
        if (!file.exists()) return;

        try {
            logger.info("Delete file {} is success {}", path, file.delete());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
