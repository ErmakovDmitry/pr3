package pr3.ini;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by dmitry on 03.07.17.
 */
@XmlRootElement(name="OutColumns")
public class IniValuesOutColumns {

    private List<IniValuesOutColumn> iniValuesOutColumns;

    @XmlElement(name="OutColumn")
    public List<IniValuesOutColumn> getIniValuesOutColumns() {
        return iniValuesOutColumns;
    }

    public void setIniValuesOutColumns(List<IniValuesOutColumn> iniValuesOutColumns) {
        this.iniValuesOutColumns = iniValuesOutColumns;
    }

    public String asString() {
        String res = "IniValuesOutColumns {";
        res += "\n\t\t\toutColumn=[";
        for (IniValuesOutColumn itm : iniValuesOutColumns) {
            res += "\n\t\t\t\t" + itm.asString();
        }
        res += "\n\t\t\t]";
        res += "\n\t\t}";

        return res;
    }

    @Override
    public String toString() {
        return "IniValuesOutColumns{" +
                "iniValuesOutColumns=" + iniValuesOutColumns +
                '}';
    }
}
