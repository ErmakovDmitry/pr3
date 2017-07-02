package pr3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.core.Appender;
//import org.apache.logging.log4j.core.Layout;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.FileAppender;
//import org.apache.logging.log4j.core.appender.RollingFileAppender;
//import org.apache.logging.log4j.core.config.AppenderRef;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.apache.logging.log4j.core.layout.PatternLayout;


import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.status.StatusLogger;
import pr3.ini.*;
import pr3.utils.FileName;
import pr3.utils.FileNameException;
import pr3.xls.ColumnSemanticType;
import pr3.xls.XLSParser;
import pr3.xls.XLSWorkbookException;

import java.io.Console;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.zip.Deflater;

import static java.lang.System.exit;

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

/*
https://stackoverflow.com/questions/30881990/how-to-configure-log4j-2-x-purely-programmatically
    // set log4j.configurationFactory to be our optimized version
    final String factory = MPLoggingConfiguration.class.getName();
    System.setProperty(ConfigurationFactory.CONFIGURATION_FACTORY_PROPERTY, factory);
    StatusLogger.getLogger().debug("Using log4j configuration factory '{}'", factory);
    LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
    loggerContext.reconfigure();
LogManager.getLogger(MPLoggingConfiguration.PR3_LOGGER_NAME).debug("qwer");
*/

//        initLogFile("/home/dmitry/IdeaProjects/pr3/log/zpr3_%d{yyyy-MM-dd HH:mm:ss}_%i.log", Level.ALL);
//        initLogFile("C:\\Users\\Дмитрий\\IdeaProjects\\pr3\\zpr3_%d{yyyy-MM-dd HH:mm:ss}_%i.log", Level.ALL);
//        initLogFile("C:\\Users\\Дмитрий\\IdeaProjects\\pr3\\log\\zpr3.log", Level.ALL);

//        RollingFileAppender rollingFileAppender = new FileAppender();
//        val console_appender = new ConsoleAppender()
//        val pattern = "%d %p [%c,%C{1}] %m%n"
//        console_appender.setLayout(new PatternLayout(pattern))
//        console_appender.setThreshold(level)
//        console_appender.activateOptions()
//        Logger.getRootLogger().addAppender(console_appender)

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
            if (!iniFileName.getExtension().equalsIgnoreCase("xml")) {
                throw new RuntimeException("файл настроек (" + iniFileName.getFullNameWithoutDir() + ") должен иметь расширение xml, а не " + iniFileName.getExtension());
            }

            // Загрузка настроек из xml-файла
            XmlIniFile xmlIniFile = new XmlIniFile();
            iniValues = xmlIniFile.load(iniFileName.getFullNameWithDir(), IniValues.class);

            // Инициализация логгирования
            configLog(iniValues.getIniValuesLog());

            // Эти вызовы идут после инициализации логгирования, иначе не попадают в лог
            logger.info("////////////////////////");
            logger.info("pr3.jar is running with ini-file (" + iniFileName.getFullNameWithDir() + ") ...");
            logger.info(iniValues.asString());

            // Инициализация элементов enum ключевыми словами для распознавания типов
            ColumnSemanticType.init(iniValues.getIniValuesParser().getIniValuesColumnSemanticTypes());

            FileName xlsOutFileName = new FileName(iniValues.getIniValuesOutXls().getFileName());
            logger.info("Выходной xls-файл:" + xlsOutFileName.getFullNameWithDir());

            Path xlsOutFilePath = Paths.get(xlsOutFileName.getFullNameWithDir());

            if (Files.exists(xlsOutFilePath)) {
                if (Files.isRegularFile(xlsOutFilePath)) {
                    Files.deleteIfExists(xlsOutFilePath);
                    logger.info("Выходной xls-файл:" + xlsOutFileName.getFullNameWithDir() + " удален");
                } else {
                    logger.error("Имя выходного xls-файла занято, видимо, каталогом");
                    exit(1);
                }
            }

            // Каталог с исходными файлами
            String srcDirName = iniValues.getIniValuesSrc().getDirName();

            // Рекурсивный перебор фйайлов в каталоге-источнике
            logger.info("Рекурсивный перебор файлов в каталоге " + srcDirName +" ...");
            Files.find(
                Paths.get(srcDirName),
                Integer.MAX_VALUE,
//                (filePath, fileAttr) -> fileAttr.isRegularFile()
                (path, basicFileAttributes) -> {
                    if (basicFileAttributes.isRegularFile()) {
//                        logger.debug("path:"+path);
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
                }

                logger.info("---------------------------------------");
                logger.info("Обработка файла: " + path + " закончена");
            });
        } catch (XmlIniFileException e) {
            logger.error("Ошибка при обработке XmlIni-файла", e);
        } catch (FileNameException e) {
            logger.error("Ошибка при обработке имени файла", e);
        } catch (IOException e) {
            logger.error("Ошибка ввода/вывода", e);
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

    /**
     * Конфигурирование логгера
     * @param iniValuesLog Параметры логгирования
     */
    public static void configLog(IniValuesLog iniValuesLog) {

//        String dir = System.getProperty("java.io.tmpdir") + "test\\";
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        final PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(config)
                .withPattern(iniValuesLog.getLogPattern())
                .build();
        TriggeringPolicy policy = CompositeTriggeringPolicy.createPolicy(
                SizeBasedTriggeringPolicy.createPolicy("1 M"),
                OnStartupTriggeringPolicy.createPolicy(1)
        );
        final DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(
                "9"
                , "1"
                , "max"
                , Deflater.NO_COMPRESSION + ""
                , null
                , true
                , config
        );

        String logFilePath = iniValuesLog.getDirName();

        Appender fileAppender = RollingFileAppender.newBuilder()
                .withAdvertise(Boolean.parseBoolean(null))
                .withAdvertiseUri(null)
                .withAppend(true)
                .withBufferedIo(true)
                .withBufferSize(8192)
                .setConfiguration(config)
                .withFileName(logFilePath + iniValuesLog.getFileName())
                .withFilePattern(logFilePath + iniValuesLog.getFileNamePattern())
                .withFilter(null)
                .withIgnoreExceptions(true)
                .withImmediateFlush(true)
                .withLayout(layout)
                .withCreateOnDemand(false)
                .withLocking(false)
                .withName("File")
                .withPolicy(policy)
                .withStrategy(strategy)
                .build();
        fileAppender.start();

        Appender consoleAppender = ConsoleAppender.newBuilder()
                .withName("console")
                .build();
        consoleAppender.start();

        config.addAppender(fileAppender);
        config.addAppender(consoleAppender);

        AppenderRef ref = AppenderRef.createAppenderRef("File", Level.ALL, null);
        AppenderRef refConsole = AppenderRef.createAppenderRef("console", Level.ALL, null);
        AppenderRef[] refs = new AppenderRef[] { ref, refConsole };

        LoggerConfig loggerConfig = LoggerConfig.createLogger(
                true
                , Level.ALL
                , LogManager.ROOT_LOGGER_NAME
                , "true"
                ,  refs
                , null
                , config
                , null

        );
        loggerConfig.addAppender(fileAppender, Level.ALL, null);
        loggerConfig.addAppender(consoleAppender, Level.ALL, null);

        config.addLogger(LogManager.ROOT_LOGGER_NAME, loggerConfig);
        ctx.updateLoggers();
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