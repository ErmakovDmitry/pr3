package pr3.xls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pr3.db.OutDb;
import pr3.db.OutDbRow;
import pr3.ini.IniValues;
import pr3.utils.FileName;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/4/14
 * Time: 5:17 PM
 */
public class XLSWorkbookSrc extends XLSWorkbook {

	/**
	 * Логгер класса
	 */
	final static Logger logger = LogManager.getLogger(XLSWorkbookSrc.class.getName());

	/**
	 * Количество строк исходного файла, обрабатываемого парсером, или null, чтобы парсить всё
	 */
	final static Integer MAX_ROWS_TO_PARSE = null;

	/**
	 * Локализация настроек программы
	 */
	private IniValues iniValues;

	public XLSWorkbookSrc(FileName fileName, IniValues iniValues) throws XLSWorkbookException {
		super(fileName);

		this.iniValues = iniValues;

//		definePriceListParamsFromFileName(fileName.getNameWithoutExtension());

		try {

			File file = new File(fileName.getFullNameWithDir());

			InputStream in = new FileInputStream(file);

			if (fileName.getExtension().equalsIgnoreCase("xls")) {
				workbook = new HSSFWorkbook(in); // HSSFWorkbook wb = new HSSFWorkbook(in);
			} else {
				workbook = new XSSFWorkbook(in);
			}

			in.close();

		} catch(FileNotFoundException e) {
			throw new XLSWorkbookException("файл " + fileName + " не найден");
		} catch (IOException e) {
			throw new XLSWorkbookException("при чтении файла " + fileName);
		}

		createStyles();
	}

	private void definePriceListParamsFromFileName(String fileNameWithoutExtension) throws XLSWorkbookException {

//		int dashPos = fileNameWithoutExtension.lastIndexOf("-") ;
//		if (dashPos == -1) {
//			throw new Exception("не удалось определить id СК, т.к. название файла (" + fileNameWithoutExtension + " не содержит тире ('-')");
//		}
//		insCompanyName = Integer.parseInt(fileNameWithoutExtension.substring(dashPos + 1));

		logger.info("Наименование файла: " + fileNameWithoutExtension);

//		PriceListParams priceListParams = new PriceListParams(fileNameWithoutExtension);
//
//		logger.info("Параметры прайс-листа из наименования файла: " + priceListParams);

	}

	/**
	 * Парсинг xls-файла
	 * @param XLSWorkbookOut
	 * @param resDb
	 * @throws Exception
	 */
	public void parseWorkbook(XLSWorkbookOut XLSWorkbookOut, OutDb resDb) throws XLSWorkbookException {

		// Перебор листов xls-книги
		for (int i = 0; i <  workbook.getNumberOfSheets(); i++) {
			parseSheet(workbook.getSheetAt(i), XLSWorkbookOut, resDb);
		}

	}

	/**
	 * Парсинг одного листа рабочей книги
	 * @param srcSheet
	 * @param xlsWorkbookOut
	 * @param resDb
	 * @throws Exception
	 */
	public void parseSheet(Sheet srcSheet, XLSWorkbookOut xlsWorkbookOut, OutDb resDb) {
		logger.info("Лист '" + srcSheet.getSheetName() + "' начало обработки ...");

		if (srcSheet.getLastRowNum() > 0) {

			Integer modaCellsCount = defineModaCellsCount(srcSheet);
			Integer modaLastCellNum = defineModaLastCellNum(srcSheet);

			if (modaCellsCount != null) {

				// Определяем заголовок прайса, идентифицируем его колонки
				PriceListHeader header = priceListStructureDetection(srcSheet, modaCellsCount, modaLastCellNum);

				// Если конец заголовка найден,
				if (header.getEndRowNum() != null) {
					// обрабатываем строки данных
					dataRowsProcessing(srcSheet, modaCellsCount, modaLastCellNum, header, xlsWorkbookOut, resDb);
				} else {
					logger.info("Заголовок на листе '" + srcSheet.getSheetName()+ "'не найден");
				}

			} else {
				logger.info("На листе '" + srcSheet.getSheetName()+ "' не определена типичная длина строки");
			}
		} else {
			logger.info("Лист '" + srcSheet.getSheetName()+ "' пуст");
		}
	}

	/**
	 * Определение моды (самого часто встречающегося) количества заполненных ячеек по строкам на xls-листе
	 * @param srcSheet xls-лист с данными
	 * @return Integer Мода количества заполненных ячеек по строкам
	 */
	private Integer defineModaCellsCount(Sheet srcSheet) {
		logger.info("Определение моды количества заполненных ячеек по строкам");

		// Мэп количества заполненных ячеек в строке с частотой их встречаемости
		HashMap<Integer, Integer> cellsInRowHashMap = new HashMap<>();

		cellsInRowHashMap.clear();

		Row row;
		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			// Количество заполненных ячеек в строке (длина строки)
			int cellsInRow = countCellsInRow(row);
//			logger.debug(row.getRowNum() + " " + cellsInRow);
			// Создание в мэпе нового элемента для подсчета количества строк с еще не встречавшейся длиной
			cellsInRowHashMap.computeIfAbsent(cellsInRow, (v) -> 0);
			// Подсчет количества строк с разной длиной
			cellsInRowHashMap.computeIfPresent(cellsInRow, (k,v) -> v+1);
		}
//		logger.debug("Длины строк " + cellsInRowHashMap);

		// Количество строк с типичной длиной
		int maxCellsCount = 0;
		// Ключ в мэпе элемента с максимальным значением
		Integer maxCellsCountKey = null;
		// Общее количество строк
		int totalCount = 0;
		// Поиск в мэпе элемента с максимальным значением
		for (Map.Entry<Integer, Integer> entry : cellsInRowHashMap.entrySet()) {
			if (entry.getValue() > maxCellsCount) {
				maxCellsCount = entry.getValue();
				maxCellsCountKey = entry.getKey();
			}
			totalCount += entry.getValue();
		}

		logger.info("Типичная длина строки " + maxCellsCountKey + " (" + maxCellsCount + " строк из " + totalCount + ")");

		return maxCellsCountKey;
	}


	/**
	 * Определение моды (самого часто встречающегося) номера последней заполненной ячейки в строке
	 * @param srcSheet xls-лист с данными
	 * @return Integer Мода количества заполненных ячеек по строкам
	 */
	private Integer defineModaLastCellNum(Sheet srcSheet) {
		logger.info("Определение моды индекса колонки для последней заполненной в строке ячейки");

		// Мэп количества заполненных ячеек в строке с частотой их встречаемости
		HashMap<Integer, Integer> lastCellNumHashMap = new HashMap<>();

		lastCellNumHashMap.clear();

		Row row;
		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			// Количество заполненных ячеек в строке (длина строки)
			int lastCellNum = row.getLastCellNum();

			// Создание в мэпе нового элемента для подсчета количества строк с еще не встречавшейся длиной
			lastCellNumHashMap.computeIfAbsent(lastCellNum, (v) -> 0);
			// Подсчет количества строк с разной длиной
			lastCellNumHashMap.computeIfPresent(lastCellNum, (k,v) -> v+1);
		}

		// Количество строк с типичной длиной
		int maxCellNumCount = 0;
		// Ключ в мэпе элемента с максимальным значением - искомое значение
		Integer maxCellNumKey = null;
		// Общее количество строк
		int totalCount = 0;
		// Поиск в мэпе элемента с максимальным значением
		for (Map.Entry<Integer, Integer> entry : lastCellNumHashMap.entrySet()) {
			if (entry.getValue() > maxCellNumCount) {
				maxCellNumCount = entry.getValue();
				maxCellNumKey = entry.getKey();
			}
			totalCount += entry.getValue();
		}

		logger.info("Типичный номер последней ячейки " + maxCellNumKey + " (" + maxCellNumCount + " строк из " + totalCount + ")");

		return maxCellNumKey;
	}

	/**
	 * Выявление структуры прайс-листа
	 * @param srcSheet
	 * @param modaCellsCount
	 * @return
	 */
	private PriceListHeader priceListStructureDetection(Sheet srcSheet, Integer modaCellsCount, Integer modaLastCellNum) {
		logger.info("Выявление структуры прайс-листа ...");

		PriceListHeader header = new PriceListHeader(modaCellsCount);

		Stack<String> outlineStack = new Stack<>();

		ArrayList<String> strCells = new ArrayList<>();
		ArrayList<Double> dblCells = new ArrayList<>();

		// Временный массив для заполнения maxStrLenArr
		int[] tmpStrLenArr = new int[modaCellsCount];
		String[] sArr = new String[modaCellsCount];

		// Уровень группировки данных предыдущей строки
		int preOutlineLevel = -1;
		// Индекс текущей строки с данными, т.е. строки типичной длины (modaCellsCount)
		int dataRowInd = 0;

		// Локализация массива со сведениями о колонках заголовка
		PriceListColumn[] columns = header.getColumns();

		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			// Текущая строка
			Row row = rowIterator.next();

//			int cellsInRow = countCellsInRow(row);
			int lastCellNum = row.getLastCellNum();

//			if (cellsInRow == modaCellsCount || cellsInRow == 12) {
			if (lastCellNum == modaLastCellNum) {
				//logger.debug(row.getRowNum());
				// Признак непрерывного (с начала) заполнения ячеек строки
				// Предполагается, что непервые строки заголовка будут заполняться с дырами
				// Непрерывное заполнение - признак первой строки заголовка или группирующих подзаголовков в строках с данными
				boolean rowContiniousFilling = true;

				strCells.clear();
				dblCells.clear();

				for (int j = 0; j < tmpStrLenArr.length; j++) {
					tmpStrLenArr[j] = 0;
					sArr[j] = null;
				}

				// Текущая ячейка в итераторе
				Cell cell;
				// Предыдущая ячейка в итераторе
				Cell preCell = null;

				Iterator<Cell> cellIterator = row.iterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					if (cell != null) {
//						logger.debug(cell.getRowIndex() + ":" + cell.getColumnIndex());

						CellType cellType = cell.getCellTypeEnum();
						try {

							switch (cellType) {
								case STRING:
									String cellStrVal = cell.getStringCellValue().trim();
//									logger.debug(cellStrVal);
									strCells.add(cellStrVal);
									int cellStrValLen = cellStrVal.length();
									tmpStrLenArr[cell.getColumnIndex()] = cellStrValLen;
									sArr[cell.getColumnIndex()] = cellStrVal;
									break;
								case NUMERIC:
									dblCells.add(cell.getNumericCellValue());
									break;
								case FORMULA:
									break;
								case BLANK:
								case BOOLEAN:
								case ERROR:
									break;
								default:
									logger.error("Нет обработчика для ячейки типа " + cellType);
							}
						} catch (Exception e) {
							logger.error("Exception при получении значения ячейки", e);
						}

						if (preCell != null && preCell.getCellTypeEnum() == CellType.BLANK && cellType != CellType.BLANK) {
							rowContiniousFilling = false;
						}

						preCell = cell;
					} else {
						logger.info("Значение NULL в ячейке");
					}
				}

				// Работа со стеком иерархии строк (outline в терминах Excel)
				int outlineLevel = row.getOutlineLevel();

				// Выяляем строки заголовка, если еще не найдена последняя строка заголовка
				if (header.getEndRowNum() == null) {
					if (header.getStartRowNum() == null && dblCells.size() == 0) {
						header.setStartRowNum(row.getRowNum());
					}
					if (header.getStartRowNum() != null && rowContiniousFilling /*(dblCells.size() > 0 || outlineLevel > 0)*/) {
						// Заголовок всей таблицы закончился
						header.setEndRowNum(row.getRowNum() - 1);
						// Корректировка последней строки заголовка для однострочных заголовков (она получает меньше первой из-за -1 в предыдущей строке)
						if (header.getEndRowNum() < header.getStartRowNum()) {
							header.setEndRowNum(header.getStartRowNum());
						}
						// Заполняем массив с описанием колонок сведениями про заголовок
						logger.info("Сборка строк заголовка ...");
						for (int r = header.getStartRowNum(); r <= header.getEndRowNum(); r++) {
//							logger.debug("getRow(" + r + "):" + srcSheet.getRow(r));
							Row headerRow = srcSheet.getRow(r);
							for (int c = headerRow.getFirstCellNum(); c < headerRow.getLastCellNum(); c++) {
								Cell headerCell = headerRow.getCell(c);
								String headerCellStrVal = headerCell.getStringCellValue();
//								logger.debug("headerCell(" + c + "):" + headerCell.getStringCellValue());
								if (r == header.getStartRowNum()) {
									columns[c].setHeaderCellStrVal(headerCellStrVal);
								} else {
									if (headerCellStrVal.length() > 0) {
										columns[c].setHeaderCellStrVal(columns[c].getHeaderCellStrVal() + " " + headerCellStrVal);
									}
								}
							}
						}

						// Определение семантических типов колонок
						header.defineColumnsSemanticTypes();
					}
				}

				// Признак принадлежности строки заголовку
				boolean isHeaderLine = (
					(header.getEndRowNum() == null)
					||
					(
						(header.getEndRowNum() != null)
						&&
						(header.getEndRowNum() == header.getStartRowNum())
						&&
						(row.getRowNum() <= header.getEndRowNum())
					)
				);
				logger.debug("Строка принадлжит заголовоку: " + isHeaderLine);

				// Дербан ячеек строки
//				logger.debug("tmpStrLen:");
//				for (int tmpStrLen : tmpStrLenArr) {
//					logger.debug(" ;" + tmpStrLen);
//				}

//				logger.debug("s:");
//				for (String s : sArr) {
//					logger.debug(" ;" + s);
//				}

				if (!isHeaderLine /* && dblCells.size() != 0 */) {
					// Дополняем массив с описанием колонок сведениями про значения
					for (int i = 0; i < columns.length; i++) {
						if (columns[i].getMaxStrLen() < tmpStrLenArr[i]) {
							columns[i].setMaxStrLen(tmpStrLenArr[i]);
						}
					}
				}

				logger.debug(
				"dataRowInd:"+dataRowInd
					+ " RowNum:"+(row.getRowNum())
					+ " outlineLevel:" + outlineLevel
					+ " continiousFilling:" + rowContiniousFilling
					+ "\n strCells: " + strCells
					+ "\n dblCells:"+dblCells
					+ "\n headerRowStartInd:" + header.getStartRowNum()
					+ "\n headerRowEndInd:" + header.getEndRowNum()
				);

//				String outlineLevelName = strCells.toString();
//
//				if (outlineLevel > preOutlineLevel) {
//					// Переход к следующему уровню
//					outlineStack.push(outlineLevelName);
//					preOutlineLevel = outlineLevel;
//				} else if (outlineLevel == preOutlineLevel) {
//					// Обновление значения текущего уровня иерархии
//					outlineStack.set(outlineLevel, outlineLevelName);
//				} else {
//					// Здесь возможно всплытие на несколько уровней, поэтому цикл
//					for (int i = 0; i < (preOutlineLevel - outlineLevel); i++) {
//						outlineStack.pop();
//					}
//					outlineStack.set(outlineLevel, outlineLevelName);
//					preOutlineLevel = outlineLevel;
//				}
//				logger.debug("outlineStack:" + outlineStack);

				logger.info("--------------");

				dataRowInd++;
				//if (dataRowInd > 20) break;
			}
		}

		logger.info("Заголовок", header.asString());

		return header;
	}

	public void dataRowsProcessing(Sheet srcSheet, Integer modaCellsCount, Integer modaLastCellNum, PriceListHeader header, XLSWorkbookOut XLSWorkbookOut, OutDb resDb) {

		// Если существует объект для выходной xls-книга, пишем в нее строку-заголовок с именами полей из базы данных
		if (XLSWorkbookOut != null) {
			XLSWorkbookOut.addHeaderRow();
		}

		logger.info("Обработка строк с данными ...");

		Stack<String> outlineStack = new Stack<>();

		// Уровень группировки данных предыдущей строки
		int preOutlineLevel = -1;

		// Локализация массива со сведениями о колонках заголовка
		PriceListColumn[] columns = header.getColumns();

		// Индекс первой строки с данными
		Integer dataStartRowInd = header.getEndRowNum() + 1;

		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			// Текущая строка
			Row row = rowIterator.next();

			// Индекс текущей строки с данными, т.е. строки типичной длины (modaCellsCount)
			int dataCurRowInd = row.getRowNum();

			// Пропуск заголовка
			if (dataCurRowInd < dataStartRowInd) {
				continue;
			}

//			int cellsInRow = countCellsInRow(row);
			int lastCellNum = row.getLastCellNum();

//			if (cellsInRow == modaCellsCount) {
			if (lastCellNum == modaLastCellNum) {
				logger.debug("dataCurRowInd:"+dataCurRowInd + " (in Excel:"+(row.getRowNum()+1+")"));

				// Результирующая (выходная) запись
				ResRow resRow = new ResRow();
				resRow.setSrcFileName(fileName.getFullNameWithoutDir());
				resRow.setSrcRowNum(dataCurRowInd + 1);
//				// Признак непрерывного с начала заполнения ячеек строки
//				// Предполагается, что непервые строки заголовка будут заполняться с дырами
//				// Непрерывное заполнение - признак группирующих подзаголовков
//				boolean rowContiniousFilling = true;

				// Текущая ячейка в итераторе
				Cell cell;
				// Предыдущая ячейка в итераторе
//				Cell preCell = null;

				String firstStrValInRow = null;

				Iterator<Cell> cellIterator = row.iterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					if (cell != null) {
						int cellColInd = cell.getColumnIndex();
						ColumnSemanticType cellSemanticType = columns[cellColInd].getSemanticType();
						String cellStrVal = null;
						if (cellSemanticType != null) {
//							CellType cellType = cell.getCellTypeEnum();
							logger.debug(cell.getRowIndex() + ":" + cellColInd + " semanticType:" + cellSemanticType);

							try {
								switch (cellSemanticType) {
									case ARTICLE:
										cellStrVal = cell.getStringCellValue().trim();
										resRow.getArticlesArrList().add(cellStrVal);
										break;
									case NUMBER:
										cellStrVal = cell.getStringCellValue().trim();
										resRow.getNumbersArrList().add(cellStrVal);
										break;
									case NAME:
										cellStrVal = cell.getStringCellValue().trim();
										resRow.getNamesArrList().add(cellStrVal);
										break;
									case UNIT:
										cellStrVal = cell.getStringCellValue().trim();
										resRow.getUnitsArrList().add(cellStrVal);
										break;
									case PRICE:
										double cellDblVal = cell.getNumericCellValue();
										resRow.getPricesArrList().add(cellDblVal);
										break;
									case DESCRIPTION:
										cellStrVal = cell.getStringCellValue().trim();
										resRow.getDescrArrList().add(cellStrVal);
										break;
								}
//							switch (cellType) {
//								case STRING:
//									String cellStrVal = cell.getStringCellValue().trim();
//									break;
//								case NUMERIC:
//									double cellDblVal = cell.getNumericCellValue();
//									break;
//								case FORMULA:
//									break;
//								case BLANK:
//								case BOOLEAN:
//								case ERROR:
//									break;
//								default:
//									logger.debug("Нет обработчика для ячейки типа " + cellType);
//							}
							} catch (Exception e) {
								logger.error("Exception при получении значения ячейки");
							}

						}

						if (firstStrValInRow == null && cellStrVal != null) {
							firstStrValInRow = cellStrVal;
						}
//
//						if (preCell != null && preCell.getCellTypeEnum() == CellType.BLANK && cellType != CellType.BLANK) {
//							rowContiniousFilling = false;
//						}
//
//						preCell = cell;
//					} else {
//						logger.debug("NULL");
					}

				}

				// Работа со стеком иерархии строк (outline в терминах Excel)
				int outlineLevel = row.getOutlineLevel();

				String outlineLevelName = firstStrValInRow;

				if (outlineLevel > preOutlineLevel) {
					// Переход к следующему уровню
					outlineStack.push(outlineLevelName);
					preOutlineLevel = outlineLevel;
				} else if (outlineLevel == preOutlineLevel) {
					// Обновление значения текущего уровня иерархии
					outlineStack.set(outlineLevel, outlineLevelName);
				} else {
					// Здесь возможно всплытие на несколько уровней, поэтому цикл
					for (int i = 0; i < (preOutlineLevel - outlineLevel); i++) {
						outlineStack.pop();
					}
					outlineStack.set(outlineLevel, outlineLevelName);
					preOutlineLevel = outlineLevel;
				}
				logger.debug("outlineStack:" + outlineStack);
				// Сборка в строку всех элементов стека кроме последнего
//				String r = "";
//				Iterator<String> iter = outlineStack.iterator();
//				while (iter.hasNext()) {
//					String el = iter.next();
//					if (iter.hasNext()) {
//						if (!r.isEmpty()) {
//							r += "; ";
//						}
//						r += el;
//					}
//				}
				// Все элементы стека, кроме последнего, являются родительскими
				ArrayList<String> parentsArrList = resRow.getParentsArrList();
				Iterator<String> iter = outlineStack.iterator();
				while (iter.hasNext()) {
					String el = iter.next();
					if (iter.hasNext()) {
						parentsArrList.add(el);
					}
				}

				if (!resRow.allPricesNull()) {
					logger.debug("resRow:" + resRow);
					// Если существует объект для выходной xls-книга, пишем в нее очередную строку
					if (XLSWorkbookOut != null) {
						XLSWorkbookOut.addRow(resRow);
					}

					// Если существует объект для выходной базы, пишем в нее очередную строку
					if (resDb != null) {
						// Готовим запись для базы
						OutDbRow outDbRow = new OutDbRow();
						outDbRow.setPlna_item_text(resRow.getNamesArrList().get(0));
						// Сохраняем запись
						resDb.ins(outDbRow);
					}
					logger.info("--------------");
				}

				if (MAX_ROWS_TO_PARSE != null && dataCurRowInd > MAX_ROWS_TO_PARSE) break;
			}
		}

//		logger.debug("header2: ");
//		for (int z = 0; z < columns.length; z++) {
//			logger.debug("; header[" + z + "] " + columns[z]);
//		}

//		for (int maxStrLen : maxStrLenArr) {
//			logger.debug(" ;" + maxStrLen);
//		}

	}


//	public void parseSheetOld(Sheet srcSheet, XLSWorkbookOut XLSWorkbookOut) throws Exception {
//
//		Boolean headerFound = false;
//		Boolean cityColMissed = false; // Признак того, что в исходном файле отсутствует колонка "Населенный пункт"
//
//		// Ищем заголовок таблицы с ценами по названиям колонок с учетом их порядка
//		logger.debug("1) ищем заголовок ... ");
//
//		Row row;
//		Iterator<Row> it = srcSheet.iterator();
//
//		while (it.hasNext()) {
//			row = it.next();
//			Iterator<Cell> cells = row.iterator();
//			int cell_ind = 0;
//			Cell cell = null;
//			Boolean missCell= false;
//			while (cells.hasNext()) {
//				if (cell_ind >= headerCells.length) {
//					break;
//				}
//
//				if (!missCell) {
//					cell = cells.next();
//					logger.debug("cell " + cell.toString());
//				} else {
//					missCell= false;
//				}
//
//				if (cell != null) {
//					if (cell.toString().trim().contains(headerCells[cell_ind].trim())) { //equalsIgnoreCase
//						if (cell_ind == headerCells.length-1) {
//							headerFound = true;
//							break;
//						}
////					} else if (headerCells[cell_ind].trim().equalsIgnoreCase("Населенный пункт")) {
////						cityColMissed = true;
////						missCell = true;
//					} else {
//						break;
//					}
//				}
//				cell_ind++;
//			}
//
//			if (headerFound) {
//				break;
//			}
//
//		}
//
//		if (headerFound) {
//			// Заголовок таблицы найден
//			logger.debug("найден");
//
//			// Список диапазонов объединенных ячеек
//			List<CellRangeAddress> regionsList = new ArrayList<CellRangeAddress>();
//			for(int i = 0; i < srcSheet.getNumMergedRegions(); i++) {
//				regionsList.add(srcSheet.getMergedRegion(i));
//			}
//
//			// Перебираем строки с данными
//			logger.debug("2) перебираем строки с данными ...");
//			int rowInd = 0;
//			RefRow preRefRow = new RefRow();
//			while (it.hasNext()) {
//				Boolean missRow = false; // Флаг пропуска строки, например, по причине ее несоответсвия формату
//				row = it.next();
//				logger.debug("стр. " + (row.getRowNum()+1) + ": ");
//				Iterator<Cell> cells = row.iterator();
//				String cellSrcVals = "";
//				int cellIndShift =0;
//				int cellInd = 0;
//				RefRow refRow = new RefRow();
//				Field[] rowFields = RefRow.class.getDeclaredFields();
//				while (cells.hasNext() && cellInd<rowFields.length) {
////					logger.debug("!!!:"+rowFields[cellInd].getNamesArrList());
//					if (cityColMissed && (rowFields[cellInd].getName().equalsIgnoreCase("city"))) {
//						cellIndShift++;
//					}
//
//					Cell cell = cells.next();
//					cellInd = cell.getColumnIndex() + cellIndShift;
//
//					// Проверяем ячейку на вхождение в объединение
//					for(CellRangeAddress region : regionsList) {
//						if(region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
//							// Берем первую ячейку объединенного диапазона
//							int rowNum = region.getFirstRow();
//							int colIndex = region.getFirstColumn();
//							cell = srcSheet.getRow(rowNum).getCell(colIndex);
//						}
//					}
//
//					int cellType = cell.getCellType();
//
//					if (cellSrcVals.trim().length() != 0) {
//						cellSrcVals = cellSrcVals + "; ";
//					}
//					cellSrcVals = cellSrcVals + cell.getColumnIndex() + "(" + rowFields[cellInd].getName() + ")=["+ cell.toString() + "]";
//
//					Field field = rowFields[cellInd];
//					Type fieldType = field.getType();
//
////					logger.debug("cell "+cellInd+":'"+cell.toString()+"' cellType:"+cellType+" fieldName:"+field.getNamesArrList()+" fieldType:"+fieldType);
//
//					if (
//						((fieldType.equals(Integer.class) || fieldType.equals(Double.class)) && (cellType == Cell.CELL_TYPE_NUMERIC)) ||
//						(fieldType.equals(String.class) && ((cellType == Cell.CELL_TYPE_STRING)))
//						) {
//
//						if (fieldType.equals(Integer.class)) {
//							field.set(refRow, (int) cell.getNumericCellValue());
//						} else if (fieldType.equals(Double.class)) {
//							field.set(refRow, cell.getNumericCellValue());
//						} else if (fieldType.equals(String.class)) {
//							field.set(refRow, cell.getStringCellValue());
//						} else {
//							throw new Exception("для типа " + fieldType + " не определен способ конвертации значения");
//						}
//					} else if (fieldType.equals(Integer.class) && cellType == Cell.CELL_TYPE_STRING) {
//						// убираем из количества "шт", "шт.", " шт", " шт."
//						String s = cell.getStringCellValue();
//						Integer int_val;
//						try {
//							int_val = Integer.parseInt(s.replaceAll("шт\\.*", "").replace(",", ".").trim());
//						} catch (NumberFormatException e) {
//							int_val = new Integer(0);
//						}
//						field.set(refRow, int_val);
//					} else if (fieldType.equals(Double.class) && cellType == Cell.CELL_TYPE_STRING) {
//						Double dbl_val;
//						String s = cell.getStringCellValue();
//
//						// убираем из суммы "руб", "руб.", " руб", " руб."
//						s = s.replaceAll("руб\\.*", "").replace(",", ".").trim();
//
//						// убираем из суммы "от"
//						s = s.replaceAll("от", "").trim();
//
//						// Проверяем, не содержит ли ячейка диапазон цен через - или /
//						String[] parts = SplitUsingTokenizer(s, "-/");
//
//						if (parts.length == 2) {
//							try {
//								// Из диапазона делаем среднее
//								dbl_val = round((Double.parseDouble(parts[0]) + Double.parseDouble(parts[1])) / 2, 2);
//							} catch (NumberFormatException e) {
//								dbl_val = new Double(0);
//							}
//						} else {
//							try {
//								dbl_val = Double.parseDouble(s);
//							} catch (NumberFormatException e) {
//								dbl_val = new Double(0);
//							}
//						}
//
//						field.set(refRow, dbl_val);
//					} else if (fieldType.equals(Double.class) && cellType == Cell.CELL_TYPE_BLANK) {
//						field.set(refRow, 0d);
//					} else if (fieldType.equals(Integer.class) && cellType == Cell.CELL_TYPE_BLANK) {
//						field.set(refRow, 0);
//					} else if (fieldType.equals(String.class) && cellType == Cell.CELL_TYPE_BLANK) {
//						field.set(refRow, "");
//					} else {
//						missRow = true;
//						break;
//					}
//
//					cellInd++;
//				}
////				logger.debug("cellSrcVals:"+cellSrcVals);
//
//				if (missRow == false && ((refRow.price_leg <= 0) && (refRow.price_gruz <= 0) && (refRow.price_avt <= 0))) {
//					missRow = true;
//				}
//
//				if (refRow.raion.trim().length() == 0) {
//					refRow.raion = preRefRow.raion;
//				}
////				logger.debug("preRefRow:"+preRefRow);
//				logger.debug("refRow:"+refRow);
//
//				if (!missRow) {
//					pcs.firePropertyChange("rowAdd", null, refRow);
//					XLSWorkbookOut.addRow(insCompanyName, refRow);
//				} else {
//					pcs.firePropertyChange("rowSkip", null, cellSrcVals);
//				}
//
//				preRefRow = (RefRow) refRow.clone();
//
//				logger.debug();
//
//				rowInd++;
//			}
//
//		} else {
//			logger.debug("не найден");
//		}
//
//	}

	/**
	 * Подсчет количества ячеек в строке с использованием итератора
	 * @param row
	 * @return int Количество заполненных ячеек
	 */
	private int countCellsInRow(Row row) {
		int i = 0;

		Iterator<Cell> cellIterator = row.iterator();
		while(cellIterator.hasNext()) {
			i++;
			cellIterator.next();
		}

		return i;
	}


	/**
	 * Деление строки по разделителям
	 * @param subject исходная строка
	 * @param delimiters строка разделителей
	 * @return String[]
	 */
	public static String[] SplitUsingTokenizer(String subject, String delimiters) {
		StringTokenizer strTkn = new StringTokenizer(subject, delimiters);
		ArrayList<String> arrLis = new ArrayList<>(subject.length());

		while(strTkn.hasMoreTokens())
			arrLis.add(strTkn.nextToken());

		return arrLis.toArray(new String[0]);
	}

	/**
	 * Округление по правилам обычной арифметики
	 * @param value
	 * @param places
	 * @return
	 */
	private static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
