package pr3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public static Settings settings;

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
        FileName iniFileName = new FileName(args[0]);
        // C:\Users\Дмитрий\IdeaProjects\pr3\pr3_ini.xml
        // /home/dmitry/IdeaProjects/pr3/pr3_ini.xml
        addToLog("pr3.jar is running with ini-file (" + iniFileName.getFullNameWithDir() + ") ...");

//        try {

            if (!iniFileName.getExtension().equalsIgnoreCase("xml")) {
                throw new RuntimeException("файл настроек (" + iniFileName.getFullNameWithoutDir() + ") должен иметь расширение xml, а не " + iniFileName.getExtension());
            }

            // Загрузка настроек из xml-файла
            XmlIniFile xmlIniFile = new XmlIniFile();
            settings = xmlIniFile.load(iniFileName.getFullNameWithDir(), Settings.class);
            addToLog(settings.asString());

//        } catch (XmlIniFileException e) {
//            throw new RuntimeException(e.getLocalizedMessage());
//        }

        System.out.println("!!!!!!!:"+ settings.getSettingsXlsOut().asString());
        System.out.println("!!!!!!!:" + settings.getSettingsDbOut().asString());

        DbMySql dbMySql = new DbMySql(settings.getSettingsDbOut());

        String srcDirName = settings.getSrcDirName();
        String xlsOutFileName = settings.getXlsOutFileName();
        addToLog("Выходной xls-файл:" + xlsOutFileName);

        FileName resFileName = new FileName(xlsOutFileName);

//	    try {
        addToLog("Рекурсивный перебор файлов в каталоге " + srcDirName +" ...");

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
            addToLog("Обработка файла: "+path);
            FileName srcFileName = null;
            try {
                srcFileName = new FileName(path.toString());
                XLSParser xlsParser = new XLSParser(srcFileName, resFileName, settings);
                xlsParser.parseFile();
            } catch (Exception e) {
                addToLog("Файл НЕ обработан, т.к. произошло исключение!");
                addToLog(e);
                e.printStackTrace();
            }

            addToLog("---------------------------------------");
            addToLog("Обработка файла: " + path + " закончена");
            addToLog("");
        });

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
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp + " " +mes);
    }

}
