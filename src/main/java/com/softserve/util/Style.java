package com.softserve.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;

public class Style {

    private static final BaseColor TITLE_COLOUR = new BaseColor(40, 88, 142);
    private static final BaseColor HEADER_COLOUR = new BaseColor(143, 184, 230);
    private static final BaseColor PERIOD_COLOUR = new BaseColor(170, 199, 230, 125);

    private static final float HEADER_HEIGHT = 18f * 2f;
    private static final float CELL_HEIGHT = HEADER_HEIGHT * 2f;


    /**
     * Method used for formatting title cells of schedule.
     *
     * @param cell for formatting
     */
    public void titleCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(7f);

        // background color
        cell.setBackgroundColor(TITLE_COLOUR);

        // height
        cell.setMinimumHeight(HEADER_HEIGHT);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(2f);
    }

    /**
     * Method used for formatting header cells of schedule.
     *
     * @param cell for formatting
     */
    public void headerCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(0f);

        // background color
        cell.setBackgroundColor(HEADER_COLOUR);

        // height
        cell.setMinimumHeight(HEADER_HEIGHT);

        // border
        cell.setBorderWidthBottom(1f);
    }

    /**
     * Method used for formatting period cells of schedule.
     *
     * @param cell for formatting
     */
    public void periodCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(0f);

        // background color
        cell.setBackgroundColor(PERIOD_COLOUR);

        // height
        cell.setMinimumHeight(CELL_HEIGHT);

        // border
        cell.setBorderWidthBottom(1f);
    }

    /**
     * Method used for formatting value cells of schedule.
     *
     * @param cell for formatting
     */
    public void valueCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(5f);

        // height
        cell.setMinimumHeight(CELL_HEIGHT);

        // border
        cell.setBorderWidthBottom(1f);
    }

    /**
     * Method used for formatting inner value cells of schedule.
     *
     * @param cell for formatting
     */
    public void innerValueCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(5f);

        // height
        cell.setMinimumHeight(CELL_HEIGHT);

        // border
        cell.setBorderWidth(0);
    }
}
