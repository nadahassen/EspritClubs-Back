package com.stage.spring.service;
import com.itextpdf.text.pdf.PdfPTable;

public interface IServicePDFExporter {
    void writeTableHeader(PdfPTable table);

}
