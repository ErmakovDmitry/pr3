package pr3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Настройки доступа к xls-файлу
 * Created by dmitry on 17.05.17.
 */
public class SettingsXlsOut {

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
        return "SettingsXlsOut{" +
                "\n\tenabled=" + enabled +
                "\n\tfileName='" + fileName + '\'' +
                "\n}";
    }

    @Override
    public String toString() {
        return "SettingsXlsOut{" +
                "enabled=" + enabled +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
