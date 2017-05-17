package pr3;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/4/14
 * Time: 5:17 PM
 */
public class SrcXLSWorkbook extends XLSWorkbook {

	private String insCompanyName;

	private String[] headerCells = {
		"Экономический  регион"
		,"Марка ТС"
		,"Количество СТОА"
		,"Cредняя стоимость нормочаса, руб."
		,"Количество СТОА"
		,"Cредняя стоимость нормочаса, руб."
		,"Количество СТОА"
		,"Cредняя стоимость нормочаса, руб."
	};

	/**
	 * Локализация настроек программы
	 */
	private Settings settings;

	public SrcXLSWorkbook(FileName fileName, Settings settings) throws Exception {
		super(fileName);

		this.settings = settings;

		definePriceListParamsFromFileName(fileName.getNameWithoutExtension());

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
			throw new Exception("файл " + fileName + " не найден");
		} catch (IOException e) {
			throw new Exception("при чтении файла " + fileName);
		}

		createStyles();
	}

	private void definePriceListParamsFromFileName(String fileNameWithoutExtension) throws Exception {

//		int dashPos = fileNameWithoutExtension.lastIndexOf("-") ;
//		if (dashPos == -1) {
//			throw new Exception("не удалось определить id СК, т.к. название файла (" + fileNameWithoutExtension + " не содержит тире ('-')");
//		}
//		insCompanyName = Integer.parseInt(fileNameWithoutExtension.substring(dashPos + 1));

		System.out.println("Наименование файла: " + fileNameWithoutExtension);

//		PriceListParams priceListParams = new PriceListParams(fileNameWithoutExtension);
//
//		System.out.println("Параметры прайс-листа из наименования файла: " + priceListParams);

	}

	/**
	 * Парсинг xls-файла
	 * @param resXLSWorkbook
	 * @throws Exception
	 */
	public void parseWorkbook(ResXLSWorkbook resXLSWorkbook) throws Exception {

		// Перебор листов xls-книги
		for (int i = 0; i <  workbook.getNumberOfSheets(); i++) {
			parseSheet(workbook.getSheetAt(i), resXLSWorkbook);
		}

	}

	/**
	 * Парсинг одного листа рабочей книги
	 * @param srcSheet
	 * @param resXLSWorkbook
	 * @throws Exception
	 */
	public void parseSheet(Sheet srcSheet, ResXLSWorkbook resXLSWorkbook) throws Exception {
		System.out.print("Лист " + srcSheet.getSheetName());

		// Определяем размер строк с данными в виде количества заполненных ячеек
		Integer modaCellsCount = defineModaCellsCount(srcSheet);

		// Определяем заголовок прайса, идентифицируем его колонки
		PriceListHeader header = priceListStructureDetection(srcSheet, modaCellsCount);

		// Обрабатываем строки данных
		dataRowsProcessing(srcSheet, modaCellsCount, header, resXLSWorkbook);
	}

	/**
	 * Определение моды (самого часто встречающегося) количества заполненных ячеек по строкам на xls-листе
	 * @param srcSheet xls-лист с данными
	 * @return Integer Мода количества заполненных ячеек по строкам
	 */
	private Integer defineModaCellsCount(Sheet srcSheet) {
		System.out.println("defineModaCellsCount //////////////////////////////////////////////////");

		// Мэп количества заполненных ячеек в строке с частотой их встречаемости
		HashMap<Integer, Integer> cellsInRowHashMap = new HashMap<>();

		cellsInRowHashMap.clear();

		Row row;
		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			// Количество заполненных ячеек в строке (длина строки)
			int cellsInRow = countCellsInRow(row);
//			System.out.println(row.getRowNum() + " " + cellsInRow);
			// Создание в мэпе нового элемента для подсчета количества строк с еще не встречавшейся длиной
			cellsInRowHashMap.computeIfAbsent(cellsInRow, (v) -> 0);
			// Подсчет количества строк с разной длиной
			cellsInRowHashMap.computeIfPresent(cellsInRow, (k,v) -> v+1);
		}
//		System.out.println("Длины строк " + cellsInRowHashMap);

		// Количество строк с типичной длиной
		int maxCellsCount = 0;
		// Ключ в мэпе элемента с максимальным значением
		Integer maxCellsCountKey = null;
		// Общее количество строк
		int totalCount = 0;
		// Потск в мэпе элемента с макоимальным значением
		for (Map.Entry<Integer, Integer> entry : cellsInRowHashMap.entrySet()) {
			if (entry.getValue() > maxCellsCount) {
				maxCellsCount = entry.getValue();
				maxCellsCountKey = entry.getKey();
			}
			totalCount += entry.getValue();
		}

		System.out.println("Типичная длина строки " + maxCellsCountKey + " (" + maxCellsCount + " из " + totalCount + ")");
		System.out.println();

		return maxCellsCountKey;
	}

	/**
	 * Выявление структуры прайс-листа
	 * @param srcSheet
	 * @param modaCellsCount
	 * @return
	 */
	private PriceListHeader priceListStructureDetection(Sheet srcSheet, Integer modaCellsCount) {
		System.out.println("priceListStructureDetection ///////////////////////////////////////////////////////");

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

		// Текущая строка
		Row row;

		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			int cellsInRow = countCellsInRow(row);

			if (cellsInRow == modaCellsCount) {

				// Признак непрерывного с начала заполнения ячеек строки
				// Предполагается, что непервые строки заголовка будут заполняться с дырами
				// Непрерывное заполнение - признак группирующих подзаголовков
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
//						System.out.print(cell.getRowIndex() + ":" + cell.getColumnIndex() + " ");

						CellType cellType = cell.getCellTypeEnum();
						try {

							switch (cellType) {
								case STRING:
									String cellStrVal = cell.getStringCellValue().trim();
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
									System.out.println("Нет обработчика для ячейки типа " + cellType);
							}
						} catch (Exception e) {
							System.out.println("Exception при получении значения ячейки");
						}

						if (preCell != null && preCell.getCellTypeEnum() == CellType.BLANK && cellType != CellType.BLANK) {
							rowContiniousFilling = false;
						}

						preCell = cell;
					} else {
						System.out.println("Значение NULL в ячейке");
					}
				}

				// Работа со стеком иерархии строк (outline в терминах Excel)
				int outlineLevel = row.getOutlineLevel();

				// Выяляем строки заголовка, если еще не найдена последняя строка заголовка
				if (header.getEndRowInd() == null) {

					if (header.getStartRowInd() == null && dblCells.size() == 0) {
						header.setStartRowInd(row.getRowNum());
					}

					if (header.getStartRowInd() != null && rowContiniousFilling /*(dblCells.size() > 0 || outlineLevel > 0)*/) {
						// Заголовок всей таблицы закончился
						header.setEndRowInd(row.getRowNum() - 1);

						// Заполняем массив с описанием колонок сведениями про заголовок
						System.out.println("Header consolidation:");
						for (int r = header.getStartRowInd(); r <= header.getEndRowInd(); r++) {
//							System.out.println("getRow(" + r + "):" + srcSheet.getRow(r));
							Row headerRow = srcSheet.getRow(r);
							for (int c = headerRow.getFirstCellNum(); c < headerRow.getLastCellNum(); c++) {
								Cell headerCell = headerRow.getCell(c);
								String headerCellStrVal = headerCell.getStringCellValue();
//								System.out.println("headerCell(" + c + "):" + headerCell.getStringCellValue());
								if (r == header.getStartRowInd()) {
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
				boolean isHeaderLine = (header.getEndRowInd() == null);
				System.out.println("Заголовок:" + String.valueOf(isHeaderLine));

				System.out.print("tmpStrLen:");
				for (int tmpStrLen : tmpStrLenArr) {
					System.out.print(" ;" + tmpStrLen);
				}
				System.out.println();

				System.out.print("s:");
				for (String s : sArr) {
					System.out.print(" ;" + s);
				}
				System.out.println();

				if (!isHeaderLine /* && dblCells.size() != 0 */) {
					// Дополняем массив с описанием колонок сведениями про значения
					for (int i = 0; i < columns.length; i++) {
						if (columns[i].getMaxStrLen() < tmpStrLenArr[i]) {
							columns[i].setMaxStrLen(tmpStrLenArr[i]);
						}
					}
				}

				System.out.println(
						"dataRowInd:"+dataRowInd
								+ " RowNum:"+(row.getRowNum()+1)
								+ " outlineLevel:" + outlineLevel
								+ " continiousFilling:" + rowContiniousFilling
								+ "\n strCells: " + strCells
								+ "\n dblCells:"+dblCells
								+ "\n headerRowStartInd:" + header.getStartRowInd()
								+ "\n headerRowEndInd:" + header.getEndRowInd()
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
//				System.out.println("outlineStack:" + outlineStack);

				System.out.println("--------------");
				System.out.println();

				dataRowInd++;
				//if (dataRowInd > 20) break;
			}
		}

		System.out.print(header.asString());

		System.out.println();

		return header;
	}

	public void dataRowsProcessing(Sheet srcSheet, Integer modaCellsCount, PriceListHeader header, ResXLSWorkbook resXLSWorkbook) {

		System.out.println("dataRowsProcessing ////////////////////////////////////////////////////////////////");

		Stack<String> outlineStack = new Stack<>();

		// Уровень группировки данных предыдущей строки
		int preOutlineLevel = -1;

		// Локализация массива со сведениями о колонках заголовка
		PriceListColumn[] columns = header.getColumns();

		// Индекс первой строки с данными
		Integer dataStartRowInd = header.getEndRowInd() + 1;

		// Текущая строка
		Row row;

		Iterator<Row> rowIterator = srcSheet.iterator();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			// Индекс текущей строки с данными, т.е. строки типичной длины (modaCellsCount)
			int dataCurRowInd = row.getRowNum();

			// Пропуск заголовка
			if (dataCurRowInd < dataStartRowInd) {
				continue;
			}

			int cellsInRow = countCellsInRow(row);

			if (cellsInRow == modaCellsCount) {
				System.out.println("dataCurRowInd:"+dataCurRowInd + " (in Excel:"+(row.getRowNum()+1+")"));

				// Результирующая (выходная) запись
				ResRow resRow = new ResRow();

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
							System.out.println(cell.getRowIndex() + ":" + cellColInd + " semanticType:" + cellSemanticType);

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
//									System.out.println("Нет обработчика для ячейки типа " + cellType);
//							}
							} catch (Exception e) {
								System.out.println("Exception при получении значения ячейки");
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
//						System.out.println("NULL");
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
				System.out.println("outlineStack:" + outlineStack);
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
					System.out.println("resRow:" + resRow);
					// Если создана выходная xls-книга пишем в нее очередную строку
					if (resXLSWorkbook != null) {
						resXLSWorkbook.addRow(resRow);
					}
					System.out.println("--------------");
					System.out.println();
				}

				if (dataCurRowInd > 20) break;
			}
		}

//		System.out.print("header2: ");
//		for (int z = 0; z < columns.length; z++) {
//			System.out.println("; header[" + z + "] " + columns[z]);
//		}

		System.out.println();

//		for (int maxStrLen : maxStrLenArr) {
//			System.out.print(" ;" + maxStrLen);
//		}
//		System.out.println();

	}


	public void parseSheetOld(Sheet srcSheet, ResXLSWorkbook resXLSWorkbook) throws Exception {

		Boolean headerFound = false;
		Boolean cityColMissed = false; // Признак того, что в исходном файле отсутствует колонка "Населенный пункт"

		// Ищем заголовок таблицы с ценами по названиям колонок с учетом их порядка
		System.out.print("1) ищем заголовок ... ");

		Row row;
		Iterator<Row> it = srcSheet.iterator();

		while (it.hasNext()) {
			row = it.next();
			Iterator<Cell> cells = row.iterator();
			int cell_ind = 0;
			Cell cell = null;
			Boolean missCell= false;
			while (cells.hasNext()) {
				if (cell_ind >= headerCells.length) {
					break;
				}

				if (!missCell) {
					cell = cells.next();
					System.out.println("cell " + cell.toString());
				} else {
					missCell= false;
				}

				if (cell != null) {
					if (cell.toString().trim().contains(headerCells[cell_ind].trim())) { //equalsIgnoreCase
						if (cell_ind == headerCells.length-1) {
							headerFound = true;
							break;
						}
//					} else if (headerCells[cell_ind].trim().equalsIgnoreCase("Населенный пункт")) {
//						cityColMissed = true;
//						missCell = true;
					} else {
						break;
					}
				}
				cell_ind++;
			}

			if (headerFound) {
				break;
			}

		}

		if (headerFound) {
			// Заголовок таблицы найден
			System.out.println("найден");

			// Список диапазонов объединенных ячеек
			List<CellRangeAddress> regionsList = new ArrayList<CellRangeAddress>();
			for(int i = 0; i < srcSheet.getNumMergedRegions(); i++) {
				regionsList.add(srcSheet.getMergedRegion(i));
			}

			// Перебираем строки с данными
			System.out.println("2) перебираем строки с данными ...");
			int rowInd = 0;
			RefRow preRefRow = new RefRow();
			while (it.hasNext()) {
				Boolean missRow = false; // Флаг пропуска строки, например, по причине ее несоответсвия формату
				row = it.next();
				System.out.println("стр. " + (row.getRowNum()+1) + ": ");
				Iterator<Cell> cells = row.iterator();
				String cellSrcVals = "";
				int cellIndShift =0;
				int cellInd = 0;
				RefRow refRow = new RefRow();
				Field[] rowFields = RefRow.class.getDeclaredFields();
				while (cells.hasNext() && cellInd<rowFields.length) {
//					System.out.println("!!!:"+rowFields[cellInd].getNamesArrList());
					if (cityColMissed && (rowFields[cellInd].getName().equalsIgnoreCase("city"))) {
						cellIndShift++;
					}

					Cell cell = cells.next();
					cellInd = cell.getColumnIndex() + cellIndShift;

					// Проверяем ячейку на вхождение в объединение
					for(CellRangeAddress region : regionsList) {
						if(region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
							// Берем первую ячейку объединенного диапазона
							int rowNum = region.getFirstRow();
							int colIndex = region.getFirstColumn();
							cell = srcSheet.getRow(rowNum).getCell(colIndex);
						}
					}

					int cellType = cell.getCellType();

					if (cellSrcVals.trim().length() != 0) {
						cellSrcVals = cellSrcVals + "; ";
					}
					cellSrcVals = cellSrcVals + cell.getColumnIndex() + "(" + rowFields[cellInd].getName() + ")=["+ cell.toString() + "]";

					Field field = rowFields[cellInd];
					Type fieldType = field.getType();

//					System.out.println("cell "+cellInd+":'"+cell.toString()+"' cellType:"+cellType+" fieldName:"+field.getNamesArrList()+" fieldType:"+fieldType);

					if (
						((fieldType.equals(Integer.class) || fieldType.equals(Double.class)) && (cellType == Cell.CELL_TYPE_NUMERIC)) ||
						(fieldType.equals(String.class) && ((cellType == Cell.CELL_TYPE_STRING)))
						) {

						if (fieldType.equals(Integer.class)) {
							field.set(refRow, (int) cell.getNumericCellValue());
						} else if (fieldType.equals(Double.class)) {
							field.set(refRow, cell.getNumericCellValue());
						} else if (fieldType.equals(String.class)) {
							field.set(refRow, cell.getStringCellValue());
						} else {
							throw new Exception("для типа " + fieldType + " не определен способ конвертации значения");
						}
					} else if (fieldType.equals(Integer.class) && cellType == Cell.CELL_TYPE_STRING) {
						// убираем из количества "шт", "шт.", " шт", " шт."
						String s = cell.getStringCellValue();
						Integer int_val;
						try {
							int_val = Integer.parseInt(s.replaceAll("шт\\.*", "").replace(",", ".").trim());
						} catch (NumberFormatException e) {
							int_val = new Integer(0);
						}
						field.set(refRow, int_val);
					} else if (fieldType.equals(Double.class) && cellType == Cell.CELL_TYPE_STRING) {
						Double dbl_val;
						String s = cell.getStringCellValue();

						// убираем из суммы "руб", "руб.", " руб", " руб."
						s = s.replaceAll("руб\\.*", "").replace(",", ".").trim();

						// убираем из суммы "от"
						s = s.replaceAll("от", "").trim();

						// Проверяем, не содержит ли ячейка диапазон цен через - или /
						String[] parts = SplitUsingTokenizer(s, "-/");

						if (parts.length == 2) {
							try {
								// Из диапазона делаем среднее
								dbl_val = round((Double.parseDouble(parts[0]) + Double.parseDouble(parts[1])) / 2, 2);
							} catch (NumberFormatException e) {
								dbl_val = new Double(0);
							}
						} else {
							try {
								dbl_val = Double.parseDouble(s);
							} catch (NumberFormatException e) {
								dbl_val = new Double(0);
							}
						}

						field.set(refRow, dbl_val);
					} else if (fieldType.equals(Double.class) && cellType == Cell.CELL_TYPE_BLANK) {
						field.set(refRow, 0d);
					} else if (fieldType.equals(Integer.class) && cellType == Cell.CELL_TYPE_BLANK) {
						field.set(refRow, 0);
					} else if (fieldType.equals(String.class) && cellType == Cell.CELL_TYPE_BLANK) {
						field.set(refRow, "");
					} else {
						missRow = true;
						break;
					}

					cellInd++;
				}
//				System.out.println("cellSrcVals:"+cellSrcVals);

				if (missRow == false && ((refRow.price_leg <= 0) && (refRow.price_gruz <= 0) && (refRow.price_avt <= 0))) {
					missRow = true;
				}

				if (refRow.raion.trim().length() == 0) {
					refRow.raion = preRefRow.raion;
				}
//				System.out.println("preRefRow:"+preRefRow);
				System.out.println("refRow:"+refRow);

				if (!missRow) {
					pcs.firePropertyChange("rowAdd", null, refRow);
					resXLSWorkbook.addRow(insCompanyName, refRow);
				} else {
					pcs.firePropertyChange("rowSkip", null, cellSrcVals);
				}

				preRefRow = (RefRow) refRow.clone();

				System.out.println();

				rowInd++;
			}

		} else {
			System.out.println("не найден");
		}

	}

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
