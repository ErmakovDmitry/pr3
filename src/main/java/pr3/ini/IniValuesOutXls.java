package pr3.ini;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Настройки доступа к выходному xls-файлу
 * Created by dmitry on 17.05.17.
 */
public class IniValuesOutXls {

    /**
     * Признак необходимости формирования выходного xls-файла
     */
    private Boolean enabled;

    /**
     * Выходной xls-файл
     */
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    @XmlAttribute(name="FILE_NAME", required = false)
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    @XmlAttribute(name="ENABLED", required = false)
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String asString() {
        return "IniValuesOutXls {" +
                "\n\t\tenabled=" + enabled +
                "\n\t\tfileName='" + fileName + '\'' +
                "\n\t}";
    }

    @Override
    public String toString() {
        return "IniValuesOutXls{" +
                "enabled=" + enabled +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
