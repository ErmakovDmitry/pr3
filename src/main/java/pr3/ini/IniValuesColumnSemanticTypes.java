package pr3.ini;

/**
 * Created by dmitry on 23.05.17.
 */

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="ColumnSemanticTypes")
public class IniValuesColumnSemanticTypes {

    private List<IniValuesColumnSemanticType> iniValuesColumnSemanticTypeList;

    @XmlElement(name="ColumnSemanticType")
    public List<IniValuesColumnSemanticType> getIniValuesColumnSemanticTypeList() {
        return iniValuesColumnSemanticTypeList;
    }

    public void setIniValuesColumnSemanticTypeList(List<IniValuesColumnSemanticType> iniValuesColumnSemanticTypeList) {
        this.iniValuesColumnSemanticTypeList = iniValuesColumnSemanticTypeList;
    }

    public String asString() {
        String res = "IniValuesColumnSemanticTypes {";
        res += "\n\t\t\tiniValuesColumnSemanticTypeList=[";
        for (IniValuesColumnSemanticType itm : iniValuesColumnSemanticTypeList) {
            res += "\n\t\t\t\t" + itm.asString();
        }
        res += "\n\t\t\t]";
        res += "\n\t\t}";

        return res;
    }

    @Override
    public String toString() {
        return "IniValuesColumnSemanticTypes{" +
                "iniValuesColumnSemanticTypeList=" + iniValuesColumnSemanticTypeList +
                '}';
    }
}
