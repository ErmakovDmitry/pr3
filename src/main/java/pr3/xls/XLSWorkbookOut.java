package pr3.xls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pr3.bak.RefRow;
import pr3.utils.FileName;
import pr3.xls.ResRow;
import pr3.xls.XLSWorkbook;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/4/14
 * Time: 4:17 PM
 */
public class XLSWorkbookOut extends XLSWorkbook {

	final static Logger logger = LogManager.getLogger(XLSWorkbookOut.class.getName());

	private Sheet sheet;

	public XLSWorkbookOut(FileName fileName) throws XLSWorkbookException {
		super(fileName);

		try {

			File file = new File(fileName.getFullNameWithDir());

			if(file.exists()){

				InputStream in = new FileInputStream(file);

				if (fileName.getExtension().equalsIgnoreCase("xls")) {
					workbook = new HSSFWorkbook(in);
				} else {
					workbook = new XSSFWorkbook(in);
				}

				in.close();

				sheet = workbook.getSheet("result");

			} else {

				if (fileName.getExtension().equalsIgnoreCase("xls")) {
					workbook = new HSSFWorkbook();
				} else {
					workbook = new XSSFWorkbook();
				}

				sheet = workbook.createSheet("result");

			}


		} catch(FileNotFoundException e) {
			throw new XLSWorkbookException("файл " + fileName + " не найден");
		} catch (IOException e) {
			throw new XLSWorkbookException("при чтении файла " + fileName);
		}

		createStyles();

	}

	@Deprecated // Сделать метод универсальным
	public void addRow(String insCompanyName, RefRow refRow) {
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		addCell(row, 0, insCompanyName);
		addCell(row, 1, refRow.raion);
		addCell(row, 2, refRow.marka);
		addCell(row, 3, refRow.amount_leg);
		addCell(row, 4, refRow.price_leg);
		addCell(row, 5, refRow.amount_gruz);
		addCell(row, 6, refRow.price_gruz);
		addCell(row, 7, refRow.amount_avt);
		addCell(row, 8, refRow.price_avt);
	}

	public void addRow(ResRow resRow) {

		Row row = sheet.createRow(sheet.getLastRowNum()+1);

		logger.debug("resRow:"+(row.getRowNum()+1)+ " getParents:"+resRow.getParentsArrList()+" getNamesArrList: "+resRow.getNamesArrList());

		addCell(row, 0, resRow.getParentsArrList().get(0));
		addCell(row, 1, resRow.getNamesArrList().get(0));
//		addCell(row, 2, resRow.getPrices());
	}

	public void addCell(Row row, Integer index, String value) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(commonCellStyle);
	}

	public void addCell(Row row, Integer index, Double value) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(commonCellStyle);
	}

	public void addCell(Row row, Integer index, Integer value) {
		Cell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(commonCellStyle);
	}

	/**
	 * Write the output to a file
	 * @throws IOException
	 */
	public void save() throws IOException {
		FileOutputStream fileOutStrem = new FileOutputStream(fileName.getFullNameWithDir());
		workbook.write(fileOutStrem);
		fileOutStrem.close();
	}
}
