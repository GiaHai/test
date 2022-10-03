package com.company.truonghoc.web.screens.thutienhocphi;

import com.company.truonghoc.service.ServerConfigService;
import com.company.truonghoc.utils.GlobalFunctionHelper;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.haulmont.cuba.web.gui.components.JavaScriptComponent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static com.company.truonghoc.utils.GlobalConstants.SUB_FOLDER_PRINTER;


public class WebFunctionHelper {

    private static final Logger logger = LoggerFactory.getLogger(WebFunctionHelper.class);


    public static String modifiedTemplate(String pathTemplate, ServerConfigService serverConfigService, Map<String, Object> parameter) {
        if (StringUtils.isBlank(pathTemplate) || parameter == null) return "";

        File file = new File(pathTemplate);
        if (!file.exists()) {
            logger.warn("Không tồn tại file template: {}", pathTemplate);
            return "";
        }

        try {
            String extension = FilenameUtils.getExtension(file.getName());
            String name = UUID.randomUUID().toString();

            if ("doc".equalsIgnoreCase(extension)) {
                logger.info("Chưa hỗ trợ định dạng file");
            } else if ("docx".equalsIgnoreCase(extension)) {
                String directoryOut =  GlobalFunctionHelper.getAndCreateFolder(serverConfigService.getCubaTempDir(), "template");
                if (StringUtils.isBlank(directoryOut)) {
                    directoryOut = serverConfigService.getCubaTempDir();
                }
                String outputFile = directoryOut + "/" + name +  "." + extension;

                FileInputStream fis = new FileInputStream(pathTemplate);
                XWPFDocument document = new XWPFDocument(OPCPackage.open(fis));

                GlobalFunctionHelper.pushDataToTempByParagraph(document, parameter);
                GlobalFunctionHelper.pushDataToTempByTable(document, parameter);
                boolean bl = GlobalFunctionHelper.writeFile(document, outputFile);

                if (bl) {
                    boolean isBreakTableSinginFooter = GlobalFunctionHelper.checkIsPageBreakTableSigninFooter(outputFile, parameter);
                    GlobalFunctionHelper.findAndBreakPage(document, isBreakTableSinginFooter);

                    bl = GlobalFunctionHelper.writeFile(document, outputFile);
                }

                document.close();

                if (bl) {
                    return outputFile;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return "";
    }

    public static String convertDocToPdf(String pathDoc, String pathSavePdf, boolean allowDelete) {
        if (StringUtils.isBlank(pathDoc)) {
            logger.warn("Đường dẫn file doc rỗng");
            return "";
        }

        File file = new File(pathDoc);
        if (!file.exists()) {
            logger.warn("Không tồn tại file doc: {}", pathDoc);
            return "";
        }

        try {
//            String extension = FilenameUtils.getExtension(file.getName());
            String name =FilenameUtils.removeExtension(file.getName());// FilenameUtils.getName(file.getName());

            String pdfSave = GlobalFunctionHelper.getAndCreateFolder(pathSavePdf, SUB_FOLDER_PRINTER);

            if (StringUtils.isBlank(pdfSave)) {
                logger.warn("Lỗi không tạo được folder để convert pdf");
                return "";
            }

            String fileName = name + ".pdf";
            File outFile = new File(pdfSave + "/" + fileName);

            InputStream docxInputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(outFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            converter.shutDown();
            logger.info("Convert file {} to pdf success ", file.getName());

            return fileName;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            // Cho phep xoa file doc
            if (allowDelete ) {
                logger.info("delete file is success {}", file.delete());
            }
        }

        return "";
    }

    public static void printFiles(JavaScriptComponent printerPdf, List<String> files, Consumer<JavaScriptComponent.JavaScriptCallbackEvent> callbackEvent) {
        if (files == null || files.isEmpty()) {
            logger.warn("Danh sách file in rỗng");
            return;
        }

        if (callbackEvent != null) {
            printerPdf.addFunction("closeCallback", callbackEvent);
        }

        for (String file : files) {
            String urlFile = "farm/" + SUB_FOLDER_PRINTER + "/" + file;
            printerPdf.callFunction("knkxPrinter", urlFile);
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
