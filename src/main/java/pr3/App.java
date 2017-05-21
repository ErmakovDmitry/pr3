package pr3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import pr3.ini.IniValues;
import pr3.ini.XmlIniFileException;
import pr3.utils.FileName;
import pr3.utils.FileNameException;
import pr3.xls.ColumnSemanticType;
import pr3.xls.XLSParser;
import pr3.ini.XmlIniFile;
import pr3.xls.XLSWorkbookException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Created by dmitry on 25.04.17.
 */
public class App {

//    https://habrahabr.ru/post/130195/
//    http://stackoverflow.com/questions/30881990/how-to-configure-log4j-2-x-purely-programmatically

    /**
     * Логгер
     */
    final static Logger logger = LogManager.getLogger(App.class.getName());

    /**
     * Настройки программы
     */
    public static IniValues iniValues;

    public static void main(String[] args)   {
//        Properties props = System.getProperties();
//        props.setProperty("org.apache.logging.log4j.simplelog.StatusLogger.level", "TRACE");

//        java.util.logging.Formatter="%2$s: %5$s%6$s%n"
//
////        try {
////            file://C:/Users/Дмитрий/IdeaProjects/pr3/src/main/java/pr3/App.java
////            String propertiesFileName = "C:\\Users\\Дмитрий\\IdeaProjects\\pr3\\src\\main\\java\\logging.properties";
//            String propertiesFileName = "/resources/logging.properties";
////            String propertiesFileName = "1";
//            ClassLoader classLoader = App.class.getClassLoader();
//            if (classLoader == null) {
//                logger.debug("null");
//            } else {
//                logger.debug("not null");
//            }
////            URL resource = classLoader.getResource(propertiesFileName);
//            InputStream inputStream = classLoader.getResourceAsStream(propertiesFileName);
//            if (inputStream == null) {
//                logger.debug("null");
//            } else {
//                logger.debug("not null");
//            }
//
//            LogManager.getLogManager().readConfiguration(
////                    App.class.getResourceAsStream(propertiesFileName)
//                    inputStream
//            );
//        } catch (IOException e) {
//
//            System.err.println("Could not setup logger configuration: " + e.toString());
//        }
//        exit(0);

        ColumnSemanticType.init();

//        String jarName = new java.io.File(FileName.class.getProtectionDomain()
//                .getCodeSource()
//                .getLocation()
//                .getPath())
//                .getName();
//        logger.debug(jarName);

        if (args.length != 1) {
            throw new RuntimeException("Usage: pr3.jar pr3_ini.xml");
        }

        try {

            FileName iniFileName = new FileName(args[0]);
            // C:\Users\Дмитрий\IdeaProjects\pr3\pr3_ini.xml
            // /home/dmitry/IdeaProjects/pr3/pr3_ini.xml
            logger.info("pr3.jar is running with ini-file (" + iniFileName.getFullNameWithDir() + ") ...");

            if (!iniFileName.getExtension().equalsIgnoreCase("xml")) {
                throw new RuntimeException("файл настроек (" + iniFileName.getFullNameWithoutDir() + ") должен иметь расширение xml, а не " + iniFileName.getExtension());
            }

            // Загрузка настроек из xml-файла
            XmlIniFile xmlIniFile = new XmlIniFile();
            iniValues = xmlIniFile.load(iniFileName.getFullNameWithDir(), IniValues.class);
            logger.info(iniValues.asString());

//            String srcDirName = iniValues.getSrcDirName();
            String srcDirName = iniValues.getIniValuesSrc().getDirName();

            FileName xlsOutFileName = new FileName(iniValues.getIniValuesOutXls().getFileName());
            logger.info("Выходной xls-файл:" + xlsOutFileName.getFullNameWithDir());


            logger.info("Рекурсивный перебор файлов в каталоге " + srcDirName +" ...");

        // Рекурсивный перебор фйайлов в каталоге-источнике
        Files.find(
            Paths.get(srcDirName),
            Integer.MAX_VALUE,
//            (filePath, fileAttr) -> fileAttr.isRegularFile()
            (path, basicFileAttributes) -> {
                if (basicFileAttributes.isRegularFile()) {
//                    logger.debug("path:"+path);
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
                            && (!srcFileName.getFullNameWithoutDir().equalsIgnoreCase(xlsOutFileName.getFullNameWithoutDir()))
                            ) {

                            return true;
                        }
                    } catch (Exception e) {
                        logger.log(Level.ERROR, "Iterate files", e);
                    }
                }
                return false;
            }
        ).forEach(path -> {
            logger.info("Обработка файла: " + path);
//            FileName srcFileName = null;
            try {
                FileName srcFileName = new FileName(path.toString());
                XLSParser xlsParser = new XLSParser(srcFileName, xlsOutFileName, iniValues);
                xlsParser.parseFile();
            } catch (XLSWorkbookException | SQLException | ClassNotFoundException | FileNameException | IOException e) {
                logger.log(Level.ERROR, "Файл НЕ обработан, т.к. произошло исключение!", e);
//                e.printStackTrace();
            }

            logger.info("---------------------------------------");
            logger.info("Обработка файла: " + path + " закончена");
        });
        } catch (XmlIniFileException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (FileNameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


}

//    static class Stats {
//        int fileCount;
//        int rowSkip;
//        int rowAdd;
//
//        Stats() {
//            reset();
//        }
//
//        public void reset() {
//            fileCount = 0;
//            rowSkip = 0;
//            rowAdd = 0;
//        }
//
//        @Override
//        public String toString() {
//            int rowTotal = rowSkip + rowAdd;
//            return "Обработано: " +
//                    " файлов " + fileCount +
//                    ", строк всего " + rowTotal +
//                    ", добавлено " + rowAdd +
//                    ", пропущено " + rowSkip;
//        }
//
//    }