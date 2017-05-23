package pr3.ini;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by dmitry on 23.05.17.
 */
@XmlRootElement(name="KeyWords")
public class IniValuesColumnSemanticTypeKeyWords {

    private List<IniValuesColumnSemanticTypeKeyWord> keyWords;

    @XmlElement(name="KeyWord")
    public List<IniValuesColumnSemanticTypeKeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<IniValuesColumnSemanticTypeKeyWord> keyWords) {
        this.keyWords = keyWords;
    }

    public String asString() {
        String res = "IniValuesColumnSemanticTypeKeyWords {";
        res += "\n\t\t\t\t\t\tkeyWords=[";
        for (IniValuesColumnSemanticTypeKeyWord itm : keyWords) {
            res += "\n\t\t\t\t\t\t\t" + itm.asString();
        }
        res += "\n\t\t\t\t\t\t]";
        res += "\n\t\t\t\t\t}";

        return res;
    }

    @Override
    public String toString() {
        return "IniValuesColumnSemanticTypeKeyWords{" +
                "keyWords=" + keyWords +
                '}';
    }
}

