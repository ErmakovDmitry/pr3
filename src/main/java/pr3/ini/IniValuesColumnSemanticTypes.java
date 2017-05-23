package pr3.ini;

/**
 * Created by dmitry on 23.05.17.
 */

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="ColumnSemanticTypes")
public class IniValuesColumnSemanticTypes {

    private List<IniValuesColumnSemanticType> iniValuesColumnSemanticTypes;

    @XmlElement(name="ColumnSemanticType")
    public List<IniValuesColumnSemanticType> getIniValuesColumnSemanticTypes() {
        return iniValuesColumnSemanticTypes;
    }

    public void setIniValuesColumnSemanticTypes(List<IniValuesColumnSemanticType> iniValuesColumnSemanticTypes) {
        this.iniValuesColumnSemanticTypes = iniValuesColumnSemanticTypes;
    }

    public String asString() {
        return "IniValuesColumnSemanticTypes {" +
                "\n\t\tiniValuesColumnSemanticTypes='" + iniValuesColumnSemanticTypes + '\'' +
                "\n\t}";
    }

    @Override
    public String toString() {
        return "IniValuesColumnSemanticTypes{" +
                "iniValuesColumnSemanticTypes=" + iniValuesColumnSemanticTypes +
                '}';
    }
}
