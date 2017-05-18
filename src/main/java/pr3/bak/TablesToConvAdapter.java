package pr3.bak;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Конвертер объекта TablesToConv в LinkedHashMap
 * Created by Ermakov Dmitry on 11/11/16.
 */
public class TablesToConvAdapter extends XmlAdapter<TablesToConv, Map<String, TableToConv>> {

	@Override
	public Map<String, TableToConv> unmarshal(TablesToConv tablesToConv) throws Exception {

		Map<String, TableToConv> map = new LinkedHashMap<>();
		for(TableToConv tableToConv: tablesToConv.getTablesToConv()) {
			map.put(tableToConv.getName(), tableToConv);
		}

		return map;
	}

	@Override
	public TablesToConv marshal(Map<String, TableToConv> map) throws Exception {

		TablesToConv tablesToConv = new TablesToConv();
		tablesToConv.setTablesToConv(new LinkedList<>(map.values()));

		return tablesToConv;
	}
}
