package pr3.ini;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dmitry on 23.05.17.
 */
@XmlRootElement(name="KeyWord")
@XmlAccessorType(XmlAccessType.FIELD)
public class IniValuesColumnSemanticTypeKeyWord {

    @XmlAttribute(name="VALUE")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String asString() {
        return "IniValuesColumnSemanticTypeKeyWord{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "IniValuesColumnSemanticTypeKeyWord{" +
                "value='" + value + '\'' +
                '}';
    }
}
