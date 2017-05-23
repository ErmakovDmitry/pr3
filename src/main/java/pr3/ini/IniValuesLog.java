package pr3.ini;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Настройки логгирования
 * Created by dmitry on 23.05.17.
 */
public class IniValuesLog {

    /**
     * Каталог с log-файлами
     */
    private String dirName;

    /**
     * Имя log-файла, ex. "pr3.log"
     */
    private String fileName;

    /**
     * Шаблон для ротации log-файлов, "pr3_%d{yyyy-MM-dd}_%i.log"
     */
    private String fileNamePattern;

    /**
     * Шаблон строки ввода
     */
    private String logPattern;

    public String getDirName() {
        return dirName;
    }

    @XmlAttribute(name="DIR_NAME", required = true)
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getFileName() {
        return fileName;
    }

    @XmlAttribute(name="FILE_NAME", required = true)
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    @XmlAttribute(name="FILE_NAME_PATTERN", required = true)
    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public String getLogPattern() {
        return logPattern;
    }

    @XmlAttribute(name="LOG_PATTERN", required = true)
    public void setLogPattern(String logPattern) {
        this.logPattern = logPattern;
    }

    public String asString() {
        return "IniValuesLog {" +
                "\n\t\tdirName='" + dirName + '\'' +
                "\n\t\tfileName='" + fileName + '\'' +
                "\n\t\tfileNamePattern='" + fileNamePattern + '\'' +
                "\n\t\tlogPattern='" + logPattern + '\'' +
                "\n\t}";
    }

    @Override
    public String toString() {
        return "IniValuesLog{" +
                "dirName='" + dirName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileNamePattern='" + fileNamePattern + '\'' +
                ", logPattern='" + logPattern + '\'' +
                '}';
    }
}
