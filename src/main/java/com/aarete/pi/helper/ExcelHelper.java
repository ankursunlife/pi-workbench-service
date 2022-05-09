package com.aarete.pi.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aarete.pi.bean.CellBean;
import com.aarete.pi.bean.ExcelSheetBean;
import com.aarete.pi.bean.ExportRequest;

public class ExcelHelper {
	private static final  Logger log = LoggerFactory.getLogger(ExcelHelper.class);
	private static final int HEADER_ROW_NUMBER = 0;
	private static final String FILENAME_SUFFIX_FORMAT = "MMddyyyy";
	private static final String FILE_PATH = ".";

	public static <T> void exportExcel(List<T> rawData, ExportRequest exportRequest)
			throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Workbook workbook = new XSSFWorkbook();
		createWorkSheet(rawData, exportRequest, workbook);

		saveWorkbook(workbook, exportRequest.getFileName());

	}

	/**
	 * @param <T>
	 * @param rawData
	 * @param exportRequest
	 * @param workbook
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private static <T> void createWorkSheet(List<T> rawData, ExportRequest exportRequest, Workbook workbook)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (ExcelSheetBean excelSheetBean : exportRequest.getExcelSheetBeans()) {
			Sheet sheet = workbook.createSheet(excelSheetBean.getSheetName());
			writeHeaderRow(sheet, excelSheetBean.getCellBeanList());
			writeDataRow(sheet, excelSheetBean.getCellBeanList(), rawData);
		}
	}

	private static void writeHeaderRow(Sheet sheet, List<CellBean> downloadExcelBeans) {
		Row row = sheet.createRow(HEADER_ROW_NUMBER);
		for (CellBean headerRow : downloadExcelBeans) {
			createCell(row, headerRow.getCellOrder(), headerRow.getCellValue());
		}
	}

	private static void createCell(Row row, int cellOrder, String cellValue) {
		Cell headerCell = row.createCell(cellOrder);
		headerCell.setCellValue(cellValue);
		// headerCell.setCellStyle(setStyle(workbook));
	}

	private static <T> void writeDataRow(Sheet sheet, List<CellBean> cellBeans, List<T> claimLineBeans)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		int rowNumber = 1;
		for (T claimLineBean : claimLineBeans) {
			Row row = sheet.createRow(rowNumber);
			for (CellBean cellBean : cellBeans) {
				createCell(row, cellBean.getCellOrder(),
						BeanUtils.getProperty(claimLineBean, cellBean.getCellMapperField()));

			}
			++rowNumber;
		}
	}

	private static void saveWorkbook(Workbook workbook, String fileName) throws IOException {
		Format f = new SimpleDateFormat(FILENAME_SUFFIX_FORMAT);
		String strDate = f.format(new Date());
		File currDir = new File(FILE_PATH);
		String path = currDir.getAbsolutePath();
		// TODO RK need to update later and have a constant for the same
		String fileLocation = path.substring(0, path.length() - 1) + "/download/" + fileName + "_" + strDate + ".xlsx";

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileLocation);
		} catch (FileNotFoundException e) {
			log.error("Exception Path not found", e);
		}
		try {
			workbook.write(outputStream);
		} catch (IOException e) {
			log.error("Download process File IO exception.", e);
		} finally {
			workbook.close();
		}

		workbook.close();
	}
}
