package pr3.ini;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Настройки доступа к входному каталогу с xls-файлами
 * Created by Дмитрий on 18.05.2017.
 */
public class IniValuesSrc {

    /**
     * Выходной каталог с xls-файлами
     */
    private String dirName;

    public String getDirName() {
        return dirName;
    }

    @XmlAttribute(name="DIR_NAME", required = true)
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String asString() {
        return "IniValuesSrc {" +
                "\n\t\tdirName='" + dirName + '\'' +
                "\n\t}";
    }

    @Override
    public String toString() {
        return "IniValuesSrc{" +
                "dirName='" + dirName + '\'' +
                '}';
    }
}
