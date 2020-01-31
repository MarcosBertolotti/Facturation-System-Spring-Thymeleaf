package com.springboot.app.view.xlsx;

import com.springboot.app.entities.Bill;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component("bill/see.xlsx")
public class BillXlsx extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        MessageSourceAccessor messages = getMessageSourceAccessor();

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"bill_view.xlsx\"");
        Bill bill = (Bill) map.get("bill");
        Sheet sheet = workbook.createSheet("Bill Spring");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(messages.getMessage("text.bill.see.data.client"));

        sheet.createRow(1).createCell(0).setCellValue(bill.getClient().getFirstName() + " " + bill.getClient().getLastName());
        sheet.createRow(2).createCell(0).setCellValue(bill.getClient().getEmail());

        sheet.createRow(4).createCell(0).setCellValue(messages.getMessage("text.bill.see.data.bill"));
        sheet.createRow(5).createCell(0).setCellValue(messages.getMessage("text.client.bill.folio") + ": " + bill.getId());
        sheet.createRow(6).createCell(0).setCellValue(messages.getMessage("text.client.bill.description") + ": " + bill.getDescription());
        sheet.createRow(7).createCell(0).setCellValue(messages.getMessage("text.client.bill.date") + ": " + bill.getCreateAt());

        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(messages.getMessage("text.bill.form.item.name"));
        header.createCell(1).setCellValue(messages.getMessage("text.bill.form.item.price"));
        header.createCell(2).setCellValue(messages.getMessage("text.bill.form.item.quantity"));
        header.createCell(3).setCellValue(messages.getMessage("text.bill.form.item.total"));

        for(int i = 0; i < 4; i ++){
            header.getCell(i).setCellStyle(theaderStyle);
        }
/*
        int rownum = 10;

        for(ItemBill item: bill.getItems()){
            Row row2 = sheet.createRow(rownum++);
            row2.createCell(0).setCellValue(item.getProduct().getName());
        }
*/
        AtomicInteger rownum = new AtomicInteger(10);

        bill.getItems().stream().forEach(item -> {
            Row row2 = sheet.createRow(rownum.getAndIncrement());

            Cell cell2 = row2.createCell(0);
            cell2.setCellValue(item.getProduct().getName());
            cell2.setCellStyle(tbodyStyle);

            cell2 = row2.createCell(1);
            cell2.setCellValue(item.getProduct().getPrice());
            cell2.setCellStyle(tbodyStyle);

            cell2 = row2.createCell(2);
            cell2.setCellValue(item.getQuantity());
            cell2.setCellStyle(tbodyStyle);

            cell2 = row2.createCell(3);
            cell2.setCellValue(item.calculateAmount());
            cell2.setCellStyle(tbodyStyle);
        });

        Row rowTotal = sheet.createRow(rownum.get());
        cell = rowTotal.createCell(2);
        rowTotal.createCell(2).setCellValue(messages.getMessage("text.bill.form.total") + ": ");
        cell.setCellStyle(tbodyStyle);

        cell = rowTotal.createCell(3);
        cell.setCellValue(bill.getTotal());
        cell.setCellStyle(tbodyStyle);

    }
}
