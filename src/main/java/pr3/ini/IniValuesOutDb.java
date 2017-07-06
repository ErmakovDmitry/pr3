package pr3.ini;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Настройки доступа к базе данных
 * Created by Дмитрий on 17.05.2017.
 */
public class IniValuesOutDb {

    /**
     * Признак необходимости формирования выходного xls-файла
     */
    private Boolean enabled;

    /**
     * Драйвер доступа к базе
     */
    private String driver;

    /**
     * Строка подключения к базе
     */
    private String url;

    /**
     * Пользователь
     */
    private String user;

    /**
     * Пароль
     */
    private String pass;

    /**
     * Идентификатор источника загрузки
     */
    private String srcType;

    public Boolean getEnabled() {
        return enabled;
    }

    @XmlAttribute(name="ENABLED", required = false)
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDriver() {
        return driver;
    }

    @XmlAttribute(name="DRIVER", required = false)
    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    @XmlAttribute(name="URL", required = false)
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    @XmlAttribute(name="USER", required = false)
    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    @XmlAttribute(name="PASS", required = false)
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSrcType() {
        return srcType;
    }

    @XmlAttribute(name="SRC_TYPE", required = false)
    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public String asString() {
        return "IniValuesOutDb {" +
                "\n\t\tenabled=" + enabled +
                "\n\t\tdriver='" + driver + '\'' +
                "\n\t\turl='" + url + '\'' +
                "\n\t\tuser='" + user + '\'' +
                "\n\t\tpass='" + pass + '\'' +
                "\n\t\tsrcType='" + srcType + '\'' +
                "\n\t}";
    }

    @Override
    public String toString() {
        return "IniValuesOutDb{" +
                "enabled=" + enabled +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
