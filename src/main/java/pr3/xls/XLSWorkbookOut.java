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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/4/14
 * Time: 4:17 PM
 */
public class XLSWorkbookOut extends XLSWorkbook {

	final static Logger logger = LogManager.getLogger(XLSWorkbookOut.class.getName());

	/**
	 * Имена полей из таблицы базы данных
	 */
	final static List<String> dbFldNames = Arrays.asList(
			"plna_id",
			"plna_source_type",
			"plna_source_id",
			"plna_row_num",
			"plna_parent_rows_list",
			"plna_diag_statcode",
			"plna_date_from",
			"plna_date_to",
			"plna_item_text",
			"plna_item_text_extra",
			"plna_item_descr",
			"plna_item_cat0",
			"plna_item_cat1",
			"plna_item_cat2",
			"plna_item_cat3",
			"plna_item_cat4",
			"plna_item_currency",
			"plna_currency_rate",
			"plna_vat_rate",
			"plna_units",
			"plna_price_1",
			"plna_price_rub_1",
			"plna_price_many",
			"plna_price_rub_many",
			"plna_price_dealer",
			"plna_price_rub_dealer",
			"plna_price_dealer2",
			"plna_price_rub_dealer2",
			"plna_in_stock_state",
			"plna_discount_proc",
			"plna_brand",
			"plna_seller",
			"plna_article_code",
			"plna_article_type",
			"plna_article_code1",
			"plna_item_code",
			"plna_order_code",
			"plna_minimal_order",
			"plna_minimal_order_sum",
			"plna_part_num",
			"plna_extra_data",
			"plna_sys_row_updated"
	);

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

	public void addHeaderRow() {
		Row row = sheet.createRow(0);

		for (int i = 0; i < dbFldNames.size(); i++) {
			addCell(row, i, dbFldNames.get(i));
		}
	}

	public void addRow(ResRow resRow) {

		Row row = sheet.createRow(sheet.getLastRowNum()+1);

		logger.debug("resRow:"+(row.getRowNum()+1)+ " getParents:"+resRow.getParentsArrList()+" getNamesArrList: "+resRow.getNamesArrList());

//		plna_id bigint(20) UN AI PK
//		plna_source_type  varchar(100)
//		plna_source_id  int(11)

//		plna_row_num  int(11)
		addCell(row, getColIndByDbFldName("plna_row_num"), resRow.getSrcRowNum());

//		plna_parent_rows_list varchar(255)
//		plna_diag_statcode  varchar(255)
//		plna_date_from  date
//		plna_date_to  date

//		plna_item_text  text
		if (resRow.getParentsArrList().size() > 0) {
			addCell(row, getColIndByDbFldName("plna_item_text"), resRow.getNamesArrList().get(0));
		}

//		plna_item_text_extra  text
		if (resRow.getParentsArrList().size() > 1) {
			addCell(row, getColIndByDbFldName("plna_item_text_extra"), resRow.getNamesArrList().get(1));
		}

//		plna_item_descr text

//		plna_item_cat0  text
// 		plna_item_cat1  text
//		plna_item_cat2  text
//		plna_item_cat3  text
//		plna_item_cat4  text
		for (int i = 0; i <= 4; i++) {
			if (resRow.getParentsArrList().size() > i) {
				addCell(row, getColIndByDbFldName("plna_item_cat"+i), resRow.getParentsArrList().get(i));
			} else {
				break;
			}
		}

//		plna_item_currency  varchar(10)
//		plna_currency_rate  decimal(10,4)
//		plna_vat_rate decimal(10,5)
//		plna_units  varchar(50)

//		plna_price_1  decimal(15,2)
		addCell(row, getColIndByDbFldName("plna_price_1"), resRow.getPricesArrList().get(0));

//		plna_price_rub_1  decimal(15,2)
//		plna_price_many decimal(15,2)
//		plna_price_rub_many decimal(15,2)
//		plna_price_dealer decimal(15,2)
//		plna_price_rub_dealer decimal(15,2)
//		plna_price_dealer2  decimal(15,2)
//		plna_price_rub_dealer2  decimal(15,2)
//		plna_in_stock_state varchar(50)
//		plna_discount_proc  decimal(10,2)
//		plna_brand  varchar(50)
//		plna_seller varchar(50)
//		plna_article_code varchar(50)
//		plna_article_type varchar(50)
//		plna_article_code1  varchar(50)
//		plna_item_code  varchar(50)
//		plna_order_code varchar(50)
//		plna_minimal_order  varchar(50)
//		plna_minimal_order_sum  decimal(15,2)
//		plna_part_num varchar(50)
//		plna_extra_data text
//		plna_sys_row_updated  int(11)

	}

	/**
	 * Индекс колонки по имени поля из таблицы базы данных
	 * @return
	 */
	private Integer getColIndByDbFldName(String dbFldName) {

		return dbFldNames.indexOf(dbFldName);
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
