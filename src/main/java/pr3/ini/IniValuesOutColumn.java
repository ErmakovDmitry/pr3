package pr3.ini;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dmitry on 03.07.17.
 */
@XmlRootElement(name="OutColumn")
@XmlAccessorType(XmlAccessType.FIELD)
public class IniValuesOutColumn {

    @XmlAttribute(name="NAME", required = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String asString() {
        return "IniValuesOutColumn{" +
               "name='" + name + '\'' +
               "}";
    }

    @Override
    public String toString() {
        return "IniValuesOutColumn{" +
                "name='" + name + '\'' +
                '}';
    }
}
