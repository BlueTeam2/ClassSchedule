package com.softserve.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Base class for the several table builder types supported.
 */
public abstract class BaseTableBuilder {

    protected static final String EMPTY_CELL = "--//--";
    protected static final String NEW_LINE_SEPARATOR = "\n";
    protected static final String COMA_SEPARATOR = ", ";
    protected static final float MAX_COLUMN_WIDTH = 7f;
    protected static final float FIRST_COLUMN_WIDTH_COMPARED_TO_OTHER = 0.5f;

    protected final Translator translator;
    protected final Style style = new Style();
    protected final BaseFont baseFont;
    protected final Font cellFont;
    protected final Font linkFont;
    protected final Font titleFont;
    protected final Font headFont;

    /**
     * Constructs a BaseTableBuilder.
     *
     * @throws IOException       when the font file could not be read
     * @throws DocumentException when the font is invalid
     */
    protected BaseTableBuilder() throws DocumentException, IOException {
        this.baseFont = BaseFont.createFont(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("font/times.ttf")).toString(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        this.cellFont = new Font(baseFont, 11, Font.NORMAL, BaseColor.BLACK);
        this.linkFont = new Font(baseFont, 11, Font.NORMAL, BaseColor.BLUE);
        this.titleFont = new Font(baseFont, 14, Font.BOLD, BaseColor.WHITE);
        this.headFont = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);
        this.translator = Translator.getInstance();
    }

    /**
     * Method used for creating empty table.
     *
     * @param tableWidth the width of table
     * @return empty PdfPTable
     * @throws DocumentException if the size of table is wrong
     */
    protected PdfPTable generateEmptyTable(int tableWidth) throws DocumentException {
        PdfPTable table = new PdfPTable(tableWidth);
        table.setWidthPercentage(100);
        float columnWidthAverage = MAX_COLUMN_WIDTH / tableWidth;
        float firstColumnWidth = columnWidthAverage * FIRST_COLUMN_WIDTH_COMPARED_TO_OTHER;
        float otherColumnWidth = (MAX_COLUMN_WIDTH - firstColumnWidth) / (tableWidth - 1);
        float[] columnsWidth = new float[tableWidth];
        columnsWidth[0] = firstColumnWidth;
        for (int i = 1; i < tableWidth; i++) {
            columnsWidth[i] = otherColumnWidth;
        }
        table.setWidths(columnsWidth);
        return table;
    }

    /**
     * Method used for creating header cells for table.
     *
     * @param days     the days of the week
     * @param language the selected language
     * @return mass of PdfPCell for table header, where the first cell is 'period' and other cells are days of week
     */
    protected PdfPCell[] createHeaderCells(List<DayOfWeek> days, Locale language) {
        PdfPCell[] cells = new PdfPCell[days.size() + 1];
        // putting first empty cell for this row
        PdfPCell header = new PdfPCell();
        style.headerCellStyle(header);
        cells[0] = header;

        // getting all days from schedule, putting it into cells
        int i = 1;
        for (DayOfWeek dayOfWeek : days) {
            String day = translator.getTranslation(dayOfWeek.toString().toLowerCase(), language);
            header = new PdfPCell(new Phrase(StringUtils.capitalize(day), headFont));
            style.headerCellStyle(header);
            cells[i++] = header;
        }
        return cells;
    }
}
