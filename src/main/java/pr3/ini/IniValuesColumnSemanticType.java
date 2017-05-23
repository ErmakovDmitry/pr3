package pr3.ini;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dmitry on 23.05.17.
 */
@XmlRootElement(name="ColumnSemanticType")
public class IniValuesColumnSemanticType {

    private IniValuesColumnSemanticTypeKeyWords iniValuesColumnSemanticTypeKeyWords;

    @XmlElement(name="KeyWords")
    public IniValuesColumnSemanticTypeKeyWords getIniValuesColumnSemanticTypeKeyWords() {
        return iniValuesColumnSemanticTypeKeyWords;
    }

    public void setIniValuesColumnSemanticTypeKeyWords(IniValuesColumnSemanticTypeKeyWords iniValuesColumnSemanticTypeKeyWords) {
        this.iniValuesColumnSemanticTypeKeyWords = iniValuesColumnSemanticTypeKeyWords;
    }

    @Override
    public String toString() {
        return "IniValuesColumnSemanticType{" +
                "iniValuesColumnSemanticTypeKeyWords=" + iniValuesColumnSemanticTypeKeyWords +
                '}';
    }
}
