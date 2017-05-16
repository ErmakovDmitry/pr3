package pr3;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by dmitry on 25.04.17.
 */
public class App {

    static class Stats {
        int fileCount;
        int rowSkip;
        int rowAdd;

        Stats() {
            reset();
        }

        public void reset() {
            fileCount = 0;
            rowSkip = 0;
            rowAdd = 0;
        }

        @Override
        public String toString() {
            int rowTotal = rowSkip + rowAdd;
            return "Обработано: " +
                    " файлов " + fileCount +
                    ", строк всего " + rowTotal +
                    ", добавлено " + rowAdd +
                    ", пропущено " + rowSkip;
        }

    }

    /**
     * Настройки программы
     */
    public static Pr3Settings pr3Settings;

    public static void main(String[] args) throws Exception {
        ColumnSemanticType.init();

//        String jarName = new java.io.File(FileName.class.getProtectionDomain()
//                .getCodeSource()
//                .getLocation()
//                .getPath())
//                .getName();
//        System.out.println(jarName);

        if (args.length != 1) {
            throw new RuntimeException("Usage: pr3.jar pr3_ini.xml");
        }
        System.out.println(args[0]);
        FileName iniFileName = new FileName(args[0]); //"/home/me/IdeaProjects/cas/tool/fb2pg/fb2pg.ini";
        System.out.println("pr3.jar is running with ini-file (" + iniFileName + ") ...");

//        try {

            if (!iniFileName.getExtension().equalsIgnoreCase("xml")) {
                throw new RuntimeException("файл настроек (" + iniFileName.getFullNameWithoutDir() + ") должен иметь расширение xml, а не " + iniFileName.getExtension());
            }

            // Загрузка настроек из xml-файла
            XmlIniFile xmlIniFile = new XmlIniFile();
            pr3Settings = xmlIniFile.load(iniFileName.getFullNameWithDir(), Pr3Settings.class);

//        } catch (XmlIniFileException e) {
//            throw new RuntimeException(e.getLocalizedMessage());
//        }


//        // URL подключения к БД Firebird
//        String fbUrl = pr3Settings.getFbUrl();
//        System.out.println("\tFirebird base url '" + fbUrl + "'");



//        final String srcDirName = "/home/hddraid/TMP/5/";
//        final String srcDirName = "/Users/gbaum/Desktop/TMP/5/";
//        final String srcDirName = "C:\\Users\\Дмитрий\\TMP\\5\\";
        String srcDirName = pr3Settings.getSrcDirName();
        System.out.println("!!!:"+srcDirName);
        final String resFN = "res.xlsx";

        FileName resFileName = new FileName(srcDirName + resFN);

//	    try {
        addToLog("Старт ...");

        // Рекурсивный перебор фйайлов в каталоге-источнике
        Files.find(
            Paths.get(srcDirName),
            Integer.MAX_VALUE,
//            (filePath, fileAttr) -> fileAttr.isRegularFile()
            (path, basicFileAttributes) -> {
                if (basicFileAttributes.isRegularFile()) {
//                        System.out.println("path:"+path);
                    try {
                        FileName srcFileName = new FileName(path.toString());
                        String nameWithoutExtension = srcFileName.getNameWithoutExtension();
                        String fileExtension = srcFileName.getExtension();

                        if (
                            // Отбираем только файлы Excel
                            (fileExtension.equalsIgnoreCase("xls") || fileExtension.equalsIgnoreCase("xlsx"))
                            // Отсеиваем временные файлы LibreOffice
                            && (! ".".equals(nameWithoutExtension.substring(0,1)))
                            // Проверка, что не пытаемся обрабатывать выходной файл
                            && (!srcFileName.getFullNameWithoutDir().equalsIgnoreCase(resFileName.getFullNameWithoutDir()))
                            ) {

                            return true;
                        }
                    } catch (Exception e) {
                        addToLog(e);
                    }
                }
                return false;
            }
        ).forEach(path -> {
            System.out.println("Обработка файла: "+path);
            FileName srcFileName = null;
            try {
                srcFileName = new FileName(path.toString());
                XLSParser xlsParser = new XLSParser(resFileName, srcFileName);
                xlsParser.parseFile();
            } catch (Exception e) {
                addToLog(e);
                e.printStackTrace();
                System.out.println("Файл НЕ обработан");
            }
            System.out.println("---------------------");
            System.out.println("");
        });
//                .forEach(System.out::println);

/*
        FileName resFileName = new FileName(srcDirName + resFN);

        File fileOut = new File(resFileName.getFullNameWithDir());
        fileOut.delete();

        final Stats stat = new Stats();
        HashMap<String, Integer> stat2 = new HashMap<String, Integer>();
        stat2.clear();

        File srcDir = new File(srcDirName);
        File[] srcFiles = srcDir.listFiles();

        for (File srcFile : srcFiles) {
            FileName srcFileName = new FileName(srcDirName + srcFile.getNamesArrList());

            if (!srcFile.isDirectory() &&
                    !srcFileName.getFullNameWithoutDir().equalsIgnoreCase(resFileName.getFullNameWithoutDir()) &&
                    (srcFileName.getExtension().equalsIgnoreCase("xls") || srcFileName.getExtension().equalsIgnoreCase("xlsx"))
                    ) {

                addToLog("Файл:" + srcFileName.getFullNameWithoutDir());

                stat.fileCount++;

                XLSParser xlsParser = new XLSParser(resFileName, srcFileName);
                // Прослушка событий для сбора статистики
                PropertyChangeSupport pcs = xlsParser.getSrcWb().getPropertyChangeSupport();
                pcs.addPropertyChangeListener("rowSkip", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        stat.rowSkip++;
                        addToLog("- пропуск {" + evt.getNewValue() + "}");
                    }
                });
                pcs.addPropertyChangeListener("rowAdd", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        stat.rowAdd++;
                        addToLog("+ запись  " + evt.getNewValue());
                    }
                });
                xlsParser.parseFile();

                stat2.put(srcFile.getNamesArrList(), stat.rowAdd);
            }
//if (i==0) {
//	break;
//}
        }

        addToLog(stat.toString());

        addToLog("Стат. по компаниям: ");
        int j = 0;
        for (Map.Entry entry : stat2.entrySet()) {
            j++;
            addToLog(j + "; " + entry.getKey() + "; " + entry.getValue());
        }
        addToLog("Финиш.");
//	    } catch (Exception e) {
//		    addToLog("Ошибка: " + e);
//	    }
*/
    }

    private static void addToLog(Exception e) {
        addToLog(e.getLocalizedMessage());
//        e.printStackTrace();
    }

    private static void addToLog(String mes) {
        System.out.println(mes);
    }

/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

Connection conn = null;
...
try {
    conn =
       DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                                   "user=minty&password=greatsqldb");

    // Do something with the Connection

   ...
} catch (SQLException ex) {
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

// assume that conn is an already created JDBC connection (see previous examples)

Statement stmt = null;
ResultSet rs = null;

try {
    stmt = conn.createStatement();
    rs = stmt.executeQuery("SELECT foo FROM bar");

    // or alternatively, if you don't know ahead of time that
    // the query will be a SELECT...

    if (stmt.execute("SELECT foo FROM bar")) {
        rs = stmt.getResultSet();
    }

    // Now do something with the ResultSet ....
}
catch (SQLException ex){
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
}
finally {
    // it is a good idea to release
    // resources in a finally{} block
    // in reverse-order of their creation
    // if they are no-longer needed

    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore

        rs = null;
    }

    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException sqlEx) { } // ignore

        stmt = null;
    }
}

 */

/*
		try (

			Connection fbConnection = fbDriver.connect(fbUrl, new Properties(){{
				put("user_name", pr3Settings.getFbUser());
				put("password", pr3Settings.getFbPassword());
				put("encoding", pr3Settings.getFbEncoding());
			}});

			Connection pgConnection = pgDriver.connect(pgUrl, new Properties(){{
				put("user", pr3Settings.getPgUser());
				put("password", pr3Settings.getPgPassword());
			}});

		){

			// Метка времени для лога
			String timeStamp;

			// Исходная база
			DbFb srcDb  = new DbFb(fbConnection);
			// Целевая база
			DbPg destDb = new DbPg(pgConnection);

			// Конвертер
			Fb2PgConverter fb2PgConverter = new Fb2PgConverter();
			fb2PgConverter.setInsBatchSize(pr3Settings.getInsBatchSize());
			fb2PgConverter.setLogBatchCountInLine(pr3Settings.getLogBatchCountInLine());

			if (!destDb.schemaExists(pgSchema)) {
				throw new Fb2PgConverterException("Schema '" + pgSchema + "' does NOT exist in source base");
			}

			int success_count = 0;
			int unsuccess_count = 0;

			List<TableToConv> tablesToConvList = pr3Settings.getTablesToConvList();
			for(TableToConv tableToConv : tablesToConvList) {

//				String tblSql = tableToConv.getSql();
//				System.out.println(tableToConv.getName() + "=> " + tblSql);
//				if (tblSql != null) {
//					System.out.println("SQL");
//				} else {
//					System.out.println("not SQL");
//				}

				// Спец. запрос, которые поставляет исходный набор данных вместо исходной таблицы
				String specSelQuery = tableToConv.getSql();

				String tableName = tableToConv.getName();

				timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				System.out.println("\n" + timeStamp + " table '" + tableName + "':");

				Boolean success = false;

				int srcTableRecCount = 0;
				int destTableRecCount = 0;

				// Проверка существования исходной таблицы в Firebird, если исходные данные не поставляются запросом
				System.out.print("\tchecking existance in Firebird base ... ");
				if ((specSelQuery !=null) || srcDb.tableExists(tableName)) {
					System.out.println("OK");

					// Проверка существования исходной таблицы в PostgreSQL
					System.out.print("\tchecking existance in PostgreSQL base ... ");
					if (destDb.tableExists(pgSchema, tableName)) {
						System.out.println("OK");

						DbTable srcTable = srcDb.getTable(tableName, specSelQuery);
						DbTable destTable = destDb.getTable(pgSchema, tableName);

						System.out.print("\tchecking field set compatibility ... ");

						// log-сравнения полей
						StringBuilder colSetLog = new StringBuilder();

						// Проверка совместимости наборов полей в исходной и целевой таблицах
						if (fb2PgConverter.columnSetsCompatible(srcTable, destTable, colSetLog)) {

//							System.out.println();
//							System.out.print(colSetLog);
							System.out.println("OK");

							// Очистка целевой таблицы
							destTable.empty();

							srcTableRecCount = srcTable.getRecordCount();
							System.out.print("\tdata converting (" + srcTableRecCount + " records) ... ");

							// Конвертация данных
							fb2PgConverter.convTableData(srcTable, destTable);

							// Наивная проверка результатов конвертации
							destTableRecCount = destTable.getRecordCount();
							if (destTableRecCount == srcTableRecCount) {
								System.out.println("OK");
								success = true;
							} else {
								System.out.println("\trecords count in PostgreSQL (" + destTableRecCount + ") NOT EQUAL records count in Firebird (" + srcTableRecCount + ")");
							}

						} else {
							System.out.println("FAIL");
							System.out.print(colSetLog);
						}

					} else {
						System.out.println("FAIL");
					}
				} else {
					System.out.println("FAIL");
				}

				if (success) {
					timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
					System.out.println(timeStamp + " SUCCESS (" + destTableRecCount + " records converted)");
					success_count++;
				} else {
					System.out.println("ERROR");
					unsuccess_count++;
				}
			}

			System.out.println("\nResult: " + success_count + " tables successfully converted; " + unsuccess_count + " tables NOT converted");

		} catch (SQLException e) {
			System.out.println("Error while connection to database: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (DbException e) {
			System.out.println("Error while operating on database structure: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Fb2PgConverterException e) {
			System.out.println("Error while data converting: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
  */

}
