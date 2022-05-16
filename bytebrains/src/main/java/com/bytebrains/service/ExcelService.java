package com.bytebrains.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

	private static final String EXCEL_FILE = "Hackathon_Data_MinorityWomenOwned_2022_v1.xlsx";

	@Autowired
	private WebscarpperService webscarpperService;

	public void read() throws IOException {
		File resource = new ClassPathResource(EXCEL_FILE).getFile();

		try (FileInputStream fis = new FileInputStream(resource)) {
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
			parse(myWorkBook);
		}
	}

	private void parse(XSSFWorkbook myWorkBook) throws IOException {
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
		XSSFSheet outputSheet = outputWorkbook.createSheet(mySheet.getSheetName());
		Iterator<Row> rowIterator = mySheet.iterator();
		int outputRows = 0;
		int leadershipCellIndexNumber = 0;
		while (rowIterator.hasNext()) {
			Row outputRow = outputSheet.createRow(outputRows++);
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			int outputCells = 0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				Cell outputCell = outputRow.createCell(outputCells++);
				CellStyle outputCellStyle = outputWorkbook.createCellStyle();
				outputCellStyle.cloneStyleFrom(cell.getCellStyle());
				outputCell.setCellStyle(outputCellStyle);
				switch (cell.getCellType()) {
				case STRING:
					outputCell.setCellValue(cell.getStringCellValue());
					break;
				case NUMERIC:
					outputCell.setCellValue(cell.getNumericCellValue());
					break;
				case BOOLEAN:
					outputCell.setCellValue(cell.getBooleanCellValue());
					break;
				default:
					outputCell.setCellValue(cell.getStringCellValue());
					break;
				}

			}

			if (row.getCell(1) == null || row.getCell(1).getStringCellValue().trim().length() <= 2) {
				continue;
			}

			if (outputRows == 1) {
				leadershipCellIndexNumber = outputCells;
			}

			Cell outputCell = outputRow.createCell(leadershipCellIndexNumber);
			if (row.getCell(leadershipCellIndexNumber - 1) != null) {
				CellStyle outputCellStyle = outputWorkbook.createCellStyle();
				outputCellStyle.cloneStyleFrom(row.getCell(leadershipCellIndexNumber - 1).getCellStyle());
				outputCell.setCellStyle(outputCellStyle);
			}

			if (outputRows > 1) {
				final String COMPANY_NAME = row.getCell(1).getStringCellValue();
				outputCell.setCellValue(webscarpperService.webscrap(COMPANY_NAME));
			} else {
				outputCell.setCellValue("Company Leadership Link");
			}

		}
		write(outputWorkbook);
	}

	private void write(XSSFWorkbook outputWorkbook) throws IOException {
		String TIMESTAMP_FILE_PATH = Long.toString(System.currentTimeMillis()) + "_" + EXCEL_FILE;
		File resource = new ClassPathResource(EXCEL_FILE).getFile();
		String path = resource.getAbsolutePath();
		path = path.replace(EXCEL_FILE, "output" + File.separator + TIMESTAMP_FILE_PATH);
		System.out.println("Output Path ==>" + path);
		File file = new File(path);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		outputWorkbook.write(fileOutputStream);
		outputWorkbook.close();
		fileOutputStream.close();
	}
}
