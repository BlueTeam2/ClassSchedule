package com.softserve.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.softserve.dto.ScheduleForGroupDTO;
import com.softserve.dto.ScheduleForTeacherDTO;
import com.softserve.exception.FileDownloadException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;


@Slf4j
public class PdfReportGenerator {

    /**
     * Method used for generating byte array, which we convert into pdf in controller.
     *
     * @param schedule for selected teacher and semester
     * @param language for selected language
     * @return ByteArrayOutputStream teacher schedule in byte array format
     */
    public ByteArrayOutputStream teacherScheduleReport(ScheduleForTeacherDTO schedule, Locale language) {
        log.info("Enter into teacherScheduleReport method with schedule {}", schedule);

        ByteArrayOutputStream bys;
        try {
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            document.setPageSize(PageSize.LETTER.rotate());
            bys = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, bys);
            document.open();
            PdfPTable table = new TeacherTableBuilder().createTeacherTable(schedule, language);
            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage(), e);
            throw new FileDownloadException("Failed to download file");
        }
        return bys;
    }

    /**
     * Method used for generating byte array, which we convert into pdf in controller.
     *
     * @param schedule for selected group and semester
     * @param language for selected language
     * @return ByteArrayOutputStream group schedule in byte array format
     */
    public ByteArrayOutputStream groupScheduleReport(ScheduleForGroupDTO schedule, Locale language) {
        log.info("Enter into groupScheduleReport method with schedule {}", schedule);

        ByteArrayOutputStream bys;
        try {
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            document.setPageSize(PageSize.LETTER.rotate());
            bys = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, bys);
            document.open();
            PdfPTable table = new GroupTableBuilder().createGroupTable(schedule, language);
            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage(), e);
            throw new FileDownloadException("Failed to download file");
        }
        return bys;
    }
}

