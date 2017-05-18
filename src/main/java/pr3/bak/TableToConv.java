package pr3.bak;

import javax.xml.bind.annotation.*;

/**
 * Таблица для конвертации данных между СУБД
 * Created by Ermakov Dmitry on 11/11/16.
 */
@XmlRootElement(name="TABLE_TO_CONV")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableToConv {

	@XmlAttribute(name="name")
	private String name;
	@XmlElement(name="SQL")
	private String sql;

	public TableToConv() {
	}

	public TableToConv(String name, String sql) {
		this.name = name;
		this.sql = sql;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		if(sql==null){
			return name;
		} else {
			return "\n\tTableToConv{\n" +
				"\t\tname='" + name + "'\n" +
				"\t\t, sql:\n" + sql + "\n" +
			"\t}\n";
		}
	}
}
