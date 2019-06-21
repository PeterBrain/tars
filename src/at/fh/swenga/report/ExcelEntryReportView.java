package at.fh.swenga.report;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import at.fh.swenga.model.Entry;

public class ExcelEntryReportView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// change the file name
		response.setHeader("Content-Disposition", "attachment; filename=\"report.xlsx\"");

		List<Entry> entries = (List<Entry>) model.get("entries");

		// ------------------------------------------------------
		// APACHE POI Documenations and examples:
		// https://poi.apache.org/spreadsheet/index.html
		// ------------------------------------------------------

		// create a worksheet
		Sheet sheet = workbook.createSheet("Entry Report");

		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColorPredefined.BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font.setBold(true);
		font.setColor(HSSFColorPredefined.WHITE.getIndex());
		style.setFont(font);

		// create a new row in the worksheet
		Row headerRow = sheet.createRow(0);

		// create a new cell in the row
		Cell cell0 = headerRow.createCell(0);
		cell0.setCellValue("ID");
		cell0.setCellStyle(style);

		// create a new cell in the row
		Cell cell1 = headerRow.createCell(1);
		cell1.setCellValue("Note");
		cell1.setCellStyle(style);

		// create a new cell in the row
		Cell cell2 = headerRow.createCell(2);
		cell2.setCellValue("Activity");
		cell2.setCellStyle(style);

		// create a new cell in the row
		Cell cell3 = headerRow.createCell(3);
		cell3.setCellValue("Editor");
		cell3.setCellStyle(style);

		// create a new cell in the row
		Cell cell4 = headerRow.createCell(4);
		cell4.setCellValue("Timestamp Start");
		cell4.setCellStyle(style);

		// create a new cell in the row
		Cell cell5 = headerRow.createCell(5);
		cell5.setCellValue("Timestamp End");
		cell5.setCellStyle(style);

		// create a new cell in the row
		Cell cell6 = headerRow.createCell(6);
		cell6.setCellValue("Project");
		cell6.setCellStyle(style);

		// create a new cell in the row
		Cell cell7 = headerRow.createCell(7);
		cell7.setCellValue("Category");
		cell7.setCellStyle(style);

		// create multiple rows with data
		int rowNum = 1;
		for (Entry entry : entries) {
			// create the row data
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entry.getEntryId());
			row.createCell(1).setCellValue(entry.getNote());
			row.createCell(2).setCellValue(entry.getActivity());
			row.createCell(3).setCellValue(entry.getEditor().getUserName());
			row.createCell(4).setCellValue(entry.getTimestampStart());
			row.createCell(5).setCellValue(entry.getTimestampEnd());
			row.createCell(6).setCellValue(entry.getProject().getName());
			row.createCell(7).setCellValue(entry.getCategory().getName());
		}

		// adjust column width to fit the content
		sheet.autoSizeColumn((short) 0);
		sheet.autoSizeColumn((short) 1);
		sheet.autoSizeColumn((short) 2);
		sheet.autoSizeColumn((short) 3);
		sheet.autoSizeColumn((short) 4);
		sheet.autoSizeColumn((short) 5);
		sheet.autoSizeColumn((short) 6);
		sheet.autoSizeColumn((short) 7);
	}

}
