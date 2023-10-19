package com.softserve.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softserve.dto.*;
import com.softserve.mapper.TeacherMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class GroupTableBuilder extends BaseTableBuilder {

    /**
     * Constructs a GroupTableBuilder.
     *
     * @throws IOException       if the font file could not be read
     * @throws DocumentException if the font is invalid, or when size of table is wrong
     */
    public GroupTableBuilder() throws DocumentException, IOException {
    }

    /**
     * Method used for creating group schedule table in pdf.
     *
     * @param schedule the schedule of group
     * @param language the selected language
     * @return PdfPTable schedule for group
     * @throws DocumentException if the table could not be created
     */
    public PdfPTable createGroupTable(ScheduleForGroupDTO schedule, Locale language) throws DocumentException {
        log.info("Enter into createGroupTable method with schedule {} and language {}", schedule, language);

        // getting amount of columns, setting columns width when creating main table
        int tableWidth = schedule.getDays().size() + 1;
        PdfPTable table = generateEmptyTable(tableWidth);

        //creating table title cell
        table.addCell(createTitleCell(tableWidth, schedule, language));

        //creating table header cell
        List<DayOfWeek> days = schedule.getDays()
                .stream()
                .map(DaysOfWeekWithClassesForGroupDTO::getDay)
                .collect(Collectors.toList());
        PdfPCell[] cells = createHeaderCells(days, language);
        Arrays.stream(cells).forEach(table::addCell);

        // getting all unique periods from schedule
        TreeSet<PeriodDTO> periods = getAllPeriods(schedule);

        // creating table cells with values
        PdfPCell cell;

        //getting in first loop layer - iterating every period
        for (PeriodDTO period : periods) {
            //first column is period
            cell = new PdfPCell(new Phrase(period.getName() + "\n\n" +
                    period.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    + "-" + period.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")), headFont));
            style.periodCellStyle(cell);
            table.addCell(cell);
            //getting in second loop layer - iterating days
            for (DaysOfWeekWithClassesForGroupDTO day : schedule.getDays()) {
                cell = createInnerCell(day, period, language);
                table.addCell(cell);
            }
        }
        return table;
    }

    /**
     * Method used for getting all periods from schedule using TreeSet to avoid
     * duplicating values, then sorting them by start time.
     *
     * @param schedule the schedule of group
     * @return sorted treeSet of periodDTO
     */
    private TreeSet<PeriodDTO> getAllPeriods(ScheduleForGroupDTO schedule) {
        log.info("Enter into getAllPeriods method with schedule {}", schedule);

        return schedule.getDays().stream()
                .flatMap(s -> s.getClasses().stream())
                .map(ClassesInScheduleForGroupDTO::getPeriod)
                .collect(Collectors.toCollection(
                                () -> new TreeSet<>(
                                        Comparator.comparing(PeriodDTO::getStartTime)
                                )
                        )
                );
    }

    /**
     * Method used for creating title cell for table.
     *
     * @param tableWidth the width of table
     * @param schedule   the schedule of group
     * @param language   the selected language
     * @return PdfPTable schedule for group
     */
    private PdfPCell createTitleCell(int tableWidth, ScheduleForGroupDTO schedule, Locale language) {
        log.info("Enter into createTableTitleCell method with tableWidth {} and schedule {} and language {}", tableWidth, schedule, language);

        Font titleFont = new Font(baseFont, 14, Font.BOLD, BaseColor.WHITE);
        String scheduleTitle = MessageFormat.format("{0} {1} {2}",
                StringUtils.capitalize(translator.getTranslation("schedule for", language)),
                schedule.getGroup().getTitle(),
                translator.getTranslation("group", language));
        PdfPCell cellTitle = new PdfPCell(new Phrase(scheduleTitle, titleFont));
        cellTitle.setColspan(tableWidth);
        style.titleCellStyle(cellTitle);
        return cellTitle;
    }

    /**
     * Method used for creating  main table cells
     * depending on whether they are odd or even.
     *
     * @param day      the day of week and classes for group
     * @param period   the period for group
     * @param language the selected language
     * @return inner PdfPCell for table
     */
    private PdfPCell createInnerCell(DaysOfWeekWithClassesForGroupDTO day, PeriodDTO period, Locale language) {
        log.info("Enter into createInnerCell method with day {} period {} language {}", day, period, language);

        PdfPCell cell = new PdfPCell(new Phrase(EMPTY_CELL, cellFont));
        for (int i = 0; i < day.getClasses().size(); i++) {
            ClassesInScheduleForGroupDTO currentClasses = day.getClasses().get(i);
            // filling in column raws checking needed period
            if (currentClasses.getPeriod().equals(period)) {
                if (isOddAndEvenClassesAreEqual(currentClasses)) {
                    cell = generateCell(currentClasses.getWeeks().getEven(), language);
                } else {
                    // creating new inner table with two cells - odd and even
                    PdfPTable inner = new PdfPTable(1);
                    inner.addCell(createUpperInnerCell(currentClasses.getWeeks().getOdd(), language));
                    inner.addCell(createLowerInnerCell(currentClasses.getWeeks().getEven(), language));
                    cell = new PdfPCell(inner);
                }
            }
        }
        style.valueCellStyle(cell);
        return cell;
    }

    /**
     * Method used for creating upper (odd) cell of table.
     *
     * @param lessons  the lessons for group
     * @param language the selected language
     * @return inner PdfPCell for table
     */
    private PdfPCell createUpperInnerCell(LessonsInScheduleDTO lessons, Locale language) {
        PdfPCell upperInnerCell;
        if (lessons == null) {
            upperInnerCell = new PdfPCell(new Phrase(EMPTY_CELL, cellFont));
        } else {
            upperInnerCell = generateCell(lessons, language);
        }
        style.innerValueCellStyle(upperInnerCell);
        upperInnerCell.setBorderWidthBottom(0.5f);
        return upperInnerCell;
    }

    /**
     * Method used for creating lower (even) cell of table.
     *
     * @param lessons  the lessons for group
     * @param language the selected language
     * @return inner PdfPCell for table
     */
    private PdfPCell createLowerInnerCell(LessonsInScheduleDTO lessons, Locale language) {
        PdfPCell lowerInnerCell;
        if (lessons == null) {
            lowerInnerCell = new PdfPCell(new Phrase(EMPTY_CELL, cellFont));
        } else {
            lowerInnerCell = generateCell(lessons, language);
        }
        style.innerValueCellStyle(lowerInnerCell);
        return lowerInnerCell;
    }

    /**
     * Method used for generating schedule text in table's cell.
     *
     * @param lessons  the lessons of schedule
     * @param language the selected language
     * @return inner PdfPCell for table
     */
    private PdfPCell generateCell(LessonsInScheduleDTO lessons, Locale language) {
        String baseText = getBaseTextFromLessonsInScheduleDTO(lessons);
        String linkText = getLinkTextFromLessonsInScheduleDTO(lessons);
        Phrase phrase = new Phrase(baseText, cellFont);
        if (linkText != null) {
            phrase.add(COMA_SEPARATOR + NEW_LINE_SEPARATOR);
            Chunk chunk = new Chunk(translator.getTranslation("follow the link", language), linkFont);
            chunk.setAnchor(linkText);
            phrase.add(chunk);
        }
        return new PdfPCell(phrase);
    }

    /**
     * Method used for get subjectName, lesson type, teacher and room from lessons.
     *
     * @param lessons the lessons of schedule
     * @return text of subjectName, lesson type, teacher and room from lessons
     */
    private String getBaseTextFromLessonsInScheduleDTO(LessonsInScheduleDTO lessons) {
        StringBuilder stringBuilder = new StringBuilder();
        String subjectName = lessons.getSubjectForSite();
        String lessonType = lessons.getLessonType().toLowerCase();
        String teacher = TeacherMapper.teacherDTOToTeacherForSite(lessons.getTeacher());
        String room = lessons.getRoom().getName();
        stringBuilder.append(subjectName).append(COMA_SEPARATOR).append(NEW_LINE_SEPARATOR)
                .append(lessonType).append(COMA_SEPARATOR).append(NEW_LINE_SEPARATOR)
                .append(teacher).append(COMA_SEPARATOR).append(NEW_LINE_SEPARATOR)
                .append(room);
        return stringBuilder.toString();
    }

    /**
     * Method used for get link from lessons.
     *
     * @param lessons the lessons of schedule
     * @return text of link from lessons if link is not null, else null
     */
    private String getLinkTextFromLessonsInScheduleDTO(LessonsInScheduleDTO lessons) {
        StringBuilder stringBuilder = new StringBuilder();
        String link = lessons.getLinkToMeeting();
        if (link == null) {
            return null;
        }
        stringBuilder.append(link);
        return stringBuilder.toString();
    }

    private boolean isOddAndEvenClassesAreEqual(ClassesInScheduleForGroupDTO classes) {
        LessonsInScheduleDTO even = classes.getWeeks().getEven();
        LessonsInScheduleDTO odd = classes.getWeeks().getOdd();
        return even != null && even.equals(odd);
    }
}


