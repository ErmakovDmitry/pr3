package pr3.ini;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Настройки парсера
 * Created by dmitry on 23.05.17.
 */
@XmlRootElement(name="Parser")
public class IniValuesParser {

    private IniValuesColumnSemanticTypes iniValuesColumnSemanticTypes;
    private IniValuesOutColumns iniValuesOutColumns;

    @XmlElement(name="ColumnSemanticTypes")
    public IniValuesColumnSemanticTypes getIniValuesColumnSemanticTypes() {
        return iniValuesColumnSemanticTypes;
    }

    public void setIniValuesColumnSemanticTypes(IniValuesColumnSemanticTypes iniValuesColumnSemanticTypes) {
        this.iniValuesColumnSemanticTypes = iniValuesColumnSemanticTypes;
    }

    @XmlElement(name="OutColumns")
    public IniValuesOutColumns getIniValuesOutColumns() {
        return iniValuesOutColumns;
    }

    public void setIniValuesOutColumns(IniValuesOutColumns iniValuesOutColumns) {
        this.iniValuesOutColumns = iniValuesOutColumns;
    }

    public String asString() {
        return "IniValuesParser {" +
                "\n\t\tiniValuesColumnSemanticTypes=" + iniValuesColumnSemanticTypes.asString() +
                "\n\t\tiniValuesOutColumns=" + iniValuesOutColumns.asString() +
                "\n\t}";
    }

    @Override
    public String toString() {
        return "IniValuesParser{" +
                "iniValuesColumnSemanticTypes=" + iniValuesColumnSemanticTypes +
                ", iniValuesOutColumns=" + iniValuesOutColumns +
                '}';
    }
}
