package pr3.ini;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Настройки программы-конвертера
 * Created by Ermakov Dmitry on 11/10/16.
 */
@XmlRootElement(name = "Pr3IniValues")
public class IniValues {

	/**
	 * Порт по-умолчанию для подключения к Firebird
	 */
//	public static final int DEFAULT_FB_PORT = 3050;

	/**
	 * Порт по-умолчанию для подключения к PostgreSQL
	 */
//	public static final int DEFAULT_PG_PORT = 5432;

	/**
	 * Количество записей по-умолчанию, добавляемых в таблицу за один раз
	 */
//	public static final int DEFAULT_INS_BATCH_SIZE = 1000;

	/**
	 * Количество выводов сведений об обработанном количестве записей в одну строку лога по-умолчанию
	 */
//	public static final int DEFAULT_LOG_BATCH_COUNT_IN_LINE = 31 * DEFAULT_INS_BATCH_SIZE;

	/**
	 * Каталог с исходными xls-файлами
	 */
//	private String srcDirName;

	/**
	 * Настройки доступа к входному каталогу с xls-файлами
	 */
	private	IniValuesSrc iniValuesSrc;

	/**
	 * Настройки формирования выходного xls-файла
	 */
	private IniValuesOutXls iniValuesOutXls;

	/**
	 * Настройки доступа к выходной базе
	 */
	private IniValuesOutDb iniValuesOutDb;


//	public String getSrcDirName() {
//		return srcDirName;
//	}
//
//	@XmlAttribute(name="SRC_DIR_NAME", required = true)
//	public void setSrcDirName(String srcDirName) {
//		this.srcDirName = srcDirName;
//	}

	public IniValuesSrc getIniValuesSrc() {
		return iniValuesSrc;
	}

	@XmlElement(name="Src")
	public void setIniValuesSrc(IniValuesSrc iniValuesSrc) {
		this.iniValuesSrc = iniValuesSrc;
	}

	public IniValuesOutXls getIniValuesOutXls() {
		return iniValuesOutXls;
	}

	@XmlElement(name="OutXls")
	public void setIniValuesOutXls(IniValuesOutXls iniValuesOutXls) {
		this.iniValuesOutXls = iniValuesOutXls;
	}

	public IniValuesOutDb getIniValuesOutDb() {
		return iniValuesOutDb;
	}

	@XmlElement(name="OutDb")
	public void setIniValuesOutDb(IniValuesOutDb iniValuesOutDb) {
		this.iniValuesOutDb = iniValuesOutDb;
	}

	//	/**
//	 * Хост с БД Firebird
//	 */
//	private String fbHost;
//
//	/**
//	 * Порт sql-сервера БД Firebird
//	 */
//	private Integer fbPort;
//
//	/**
//	 * Полное имя файла базы БД Firebird
//	 */
//	private String fbDbName;
//
//	/**
//	 * Имя пользователя БД Firebird
//	 */
//	private String fbUser;
//
//	/**
//	 * Пароль пользователя БД Firebird
//	 */
//	private String fbPassword;
//
//	/**
//	 * Кодировка БД Firebird
//	 */
//	private String fbEncoding;
//
//	/**
//	 * Хост с БД PostgreSQL
//	 */
//	private String pgHost;
//
//	/**
//	 * Порт sql-сервера БД PostgreSQL
//	 */
//	private Integer pgPort;
//
//	/**
//	 * Название базы PostgreSQL
//	 */
//	private String pgDbName;
//
//	/**
//	 * Название схемы PostgreSQL
//	 */
//	private String pgSchema;
//
//	/**
//	 * Имя пользователя БД PostgreSQL
//	 */
//	private String pgUser;
//
//	/**
//	 * Пароль пользователя БД PostgreSQL
//	 */
//	private String pgPassword;
//
//	/**
//	 * Таблицы из ini-файла, которые подлежат конвертации
//	 */
//	@XmlElement(name="TABLES_TO_CONV")
//	@XmlJavaTypeAdapter(TablesToConvAdapter.class)
//	private Map<String, TableToConv> tablesToConv;
//
//	/**
//	 * Количество записей, добавляемых в таблицу за один раз
//	 */
//	private Integer insBatchSize;
//
//	/**
//	 * Количество выводов сведений об обработанном количестве записей в одну строку лога
//	 */
//	private Integer logBatchCountInLine;


//	public String getFbHost() {
//		return fbHost;
//	}
//
//	@XmlAttribute(name="FB_HOST", required = true)
//	public void setFbHost(String fbHost) {
//		this.fbHost = fbHost;
//	}
//
//	public Integer getFbPort() {
//		if (fbPort == null) {
//			return DEFAULT_FB_PORT;
//		}
//
//		return fbPort;
//	}
//
//	@XmlAttribute(name="FB_PORT", required = false)
//	public void setFbPort(Integer fbPort) {
//		this.fbPort = fbPort;
//	}
//
//	public String getFbDbName() {
//		return fbDbName;
//	}
//
//	@XmlAttribute(name="FB_DBNAME", required = true)
//	public void setFbDbName(String fbDbName) {
//		this.fbDbName = fbDbName;
//	}
//
//	public String getFbUser() {
//		return fbUser;
//	}
//
//	@XmlAttribute(name="FB_USER", required = true)
//	public void setFbUser(String fbUser) {
//		this.fbUser = fbUser;
//	}
//
//	public String getFbPassword() {
//		return fbPassword;
//	}
//
//	@XmlAttribute(name="FB_PASSWORD", required = true)
//	public void setFbPassword(String fbPassword) {
//		this.fbPassword = fbPassword;
//	}
//
//	public String getFbEncoding() {
//		return fbEncoding;
//	}
//
//	@XmlAttribute(name="FB_ENCODING", required = true)
//	public void setFbEncoding(String fbEncoding) {
//		this.fbEncoding = fbEncoding;
//	}
//
//	public String getPgHost() {
//		return pgHost;
//	}
//
//	@XmlAttribute(name="PG_HOST", required = true)
//	public void setPgHost(String pgHost) {
//		this.pgHost = pgHost;
//	}
//
//	public Integer getPgPort() {
//		if (pgPort == null) {
//			return DEFAULT_PG_PORT;
//		}
//
//		return pgPort;
//	}
//
//	@XmlAttribute(name="PG_PORT", required = false)
//	public void setPgPort(Integer pgPort) {
//		this.pgPort = pgPort;
//	}
//
//	public String getPgDbName() {
//		return pgDbName;
//	}
//
//	@XmlAttribute(name="PG_DBNAME", required = true)
//	public void setPgDbName(String pgDbName) {
//		this.pgDbName = pgDbName;
//	}
//
//	public String getPgSchema() {
//		return pgSchema;
//	}
//
//	@XmlAttribute(name="PG_SCHEMA", required = true)
//	public void setPgSchema(String pgSchema) {
//		this.pgSchema = pgSchema;
//	}
//
//	public String getPgUser() {
//		return pgUser;
//	}
//
//	@XmlAttribute(name="PG_USER", required = true)
//	public void setPgUser(String pgUser) {
//		this.pgUser = pgUser;
//	}
//
//	public String getPgPassword() {
//		return pgPassword;
//	}
//
//	@XmlAttribute(name="PG_PASSWORD", required = true)
//	public void setPgPassword(String pgPassword) {
//		this.pgPassword = pgPassword;
//	}
//
//	public void setTablesToConv(String[] tablesToConvAssStrArr) {
//		tablesToConv = new HashMap<>(tablesToConvAssStrArr.length);
//		for (String tableName : tablesToConvAssStrArr) {
//			tableName = tableName.trim();
//			tablesToConv.put(tableName, new TableToConv(tableName, null));
//		}
//	}
//
//	public Map<String, TableToConv> getTablesToConvMap() {
//		return tablesToConv;
//	}
//
//	public List<TableToConv> getTablesToConvList() {
//		return new ArrayList<>(tablesToConv.values());
//	}
//
//	public Integer getInsBatchSize() {
//		return insBatchSize;
//	}
//
//	@XmlAttribute(name="INS_BATCH_SIZE", required = false)
//	public void setInsBatchSize(Integer insBatchSize) {
//		this.insBatchSize = insBatchSize;
//	}
//
//	public Integer getLogBatchCountInLine() {
//		return logBatchCountInLine;
//	}
//
//	@XmlAttribute(name="LOG_BATCH_COUNT_IN_LINE", required = false)
//	public void setLogBatchCountInLine(Integer logBatchCountInLine) {
//		this.logBatchCountInLine = logBatchCountInLine;
//	}
//
//	/**
//	 * URL подключения к БД Firebird
//	 */
//	public String getFbUrl() {
//		// jdbc:firebirdsql://192.168.1.200/c:/project/base.fb/base_norm_35.fdb
//		return "jdbc:firebirdsql://" + fbHost + ":" + Integer.toString(fbPort) + "/" + fbDbName;
//	}
//
//	/**
//	 * URL подключения к БД PostgreSQL
//	 */
//	public String getPgUrl() {
//		// jdbc:postgresql://192.168.1.167:5432/cas2
//		return "jdbc:postgresql://" + pgHost + ":" + Integer.toString(getPgPort()) + "/" + pgDbName;
//	}

	public String asString() {
		return "IniValues {" +
//				"\n\tsrcDirName='" + srcDirName + '\'' +
				"\n\tiniValuesSrc=" + iniValuesSrc.asString() +
				"\n\tiniValuesOutXls=" + iniValuesOutXls.asString() +
				"\n\tiniValuesOutDb=" + iniValuesOutDb.asString() +
				"\n}";
	}

	@Override
	public String toString() {
		return "IniValues{" +
//				"srcDirName='" + srcDirName + '\'' +
				", iniValuesOutXls='" + iniValuesOutXls + '\'' +
				", iniValuesOutDb=" + iniValuesOutDb +
				'}';
	}

}
