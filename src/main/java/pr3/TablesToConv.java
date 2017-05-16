package pr3;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Коллекция таблиц для конвертации данных между СУБД
 * Created by Ermakov Dmitry on 11/11/16.
 */
@XmlRootElement(name="TABLES_TO_CONV")
public class TablesToConv {

	private List<TableToConv> tablesToConv;

	@XmlElement(name="TABLE_TO_CONV")
	public List<TableToConv> getTablesToConv() {
		return tablesToConv;
	}

	public void setTablesToConv(List<TableToConv> tablesToConv) {
		this.tablesToConv = tablesToConv;
	}

}
