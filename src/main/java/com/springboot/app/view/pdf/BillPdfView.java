package com.springboot.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.app.entities.Bill;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@Component("bill/see")
public class BillPdfView extends AbstractPdfView {

    // seria como la vista, recibimos los datos del controller
    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        MessageSourceAccessor messages = getMessageSourceAccessor();

        Bill bill = (Bill) map.get("bill");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(messages.getMessage("text.bill.see.data.client")));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        table.addCell(cell);

        table.addCell(bill.getClient().getFirstName() + " " + bill.getClient().getLastName());
        table.addCell(bill.getClient().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messages.getMessage("text.bill.see.data.bill")));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        table2.addCell(cell);

        table2.addCell(messages.getMessage("text.client.bill.folio") + ": " + bill.getId());
        table2.addCell(messages.getMessage("text.client.bill.description") + ": " + bill.getDescription());
        table2.addCell(messages.getMessage("text.client.bill.date") + ": " + bill.getCreateAt());

        document.add(table);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float[] {3.5f, 1, 1, 1});
        table3.addCell(messages.getMessage("text.bill.form.item.name"));
        table3.addCell(messages.getMessage("text.bill.form.item.price"));
        table3.addCell(messages.getMessage("text.bill.form.item.quantity"));
        table3.addCell(messages.getMessage("text.bill.form.item.total"));

        bill.getItems().stream().forEach(item -> {
            table3.addCell(item.getProduct().getName());
            table3.addCell(item.getProduct().getPrice().toString());

            PdfPCell cell2 = new PdfPCell(new Phrase(item.getQuantity().toString()));
            cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table3.addCell(cell2);
            table3.addCell(item.calculateAmount().toString());
        });

        cell = new PdfPCell(new Phrase(messages.getMessage("text.bill.form.total") + ": "));
        cell.setColspan(3); // que ocupe tres columnas/espacios
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell);
        table3.addCell(bill.getTotal().toString());

        document.add(table3);
    }
}
