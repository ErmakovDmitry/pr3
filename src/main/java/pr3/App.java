package pr3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static void main(String[] args) throws Exception {

        ColumnSemanticType.NUMBER.setKeyWords(Arrays.asList("цена", "стоим"));

        final String srcDirName = "/home/hddraid/TMP/5/";
//        final String srcDirName = "/Users/gbaum/Desktop/TMP/5/";
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
            FileName srcFileName = new FileName(srcDirName + srcFile.getName());

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

                stat2.put(srcFile.getName(), stat.rowAdd);
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


}
