package pr3.ini;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dmitry on 23.05.17.
 */
@XmlRootElement(name="ColumnSemanticType")
public class IniValuesColumnSemanticType {

    private String name;
    private IniValuesColumnSemanticTypeKeyWords iniValuesColumnSemanticTypeKeyWords;

    public String getName() {
        return name;
    }

    @XmlAttribute(name="NAME", required = true)
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="KeyWords")
    public IniValuesColumnSemanticTypeKeyWords getIniValuesColumnSemanticTypeKeyWords() {
        return iniValuesColumnSemanticTypeKeyWords;
    }

    public void setIniValuesColumnSemanticTypeKeyWords(IniValuesColumnSemanticTypeKeyWords iniValuesColumnSemanticTypeKeyWords) {
        this.iniValuesColumnSemanticTypeKeyWords = iniValuesColumnSemanticTypeKeyWords;
    }

    public String asString() {
        return
                "IniValuesColumnSemanticType{" +
                "\n\t\t\t\t\tname='" + name + '\'' +
                "\n\t\t\t\t\tiniValuesColumnSemanticTypeKeyWords=" + iniValuesColumnSemanticTypeKeyWords.asString() +
                "\n\t\t\t\t}";
    }

    @Override
    public String toString() {
        return "IniValuesColumnSemanticType{" +
                "name='" + name + '\'' +
                ", iniValuesColumnSemanticTypeKeyWords=" + iniValuesColumnSemanticTypeKeyWords +
                '}';
    }
}
