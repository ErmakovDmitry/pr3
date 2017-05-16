package pr3;

import java.io.*;
import java.util.Properties;

/**
 * Файл для хранения настроек в ini-формате
 * Created by Ermakov Dmitry on 7/7/16.
 */
public class IniFile {

	private String iniFileName;
	private Properties props = new Properties();

	public static IniFile load(String iniFileName) throws IniFileException {
		IniFile self = new IniFile();
		self.iniFileName = iniFileName;
		try {
			return self.load(new FileInputStream(new File(iniFileName)));
		} catch (FileNotFoundException e) {
			throw new IniFileException("Error while reading ini-file (" + iniFileName +"): " + e.getLocalizedMessage(), e);
		}
	}

	public IniFile load(InputStream inputStream) throws IniFileException {
		try {
			props.load(inputStream);
		} catch (IOException e) {
			throw new IniFileException("Error while reading ini-file (" + iniFileName +"): " + e.getLocalizedMessage(), e);
		}
		return this;
	}


	public Integer getIntegerProperty(String propertyName, Boolean required) throws IniFileException {
		return getIntegerProperty(propertyName, required, null);
	}

	public Integer getIntegerProperty(String propertyName, Boolean required, Integer defaultValue) throws IniFileException {
		String val = getStringProperty(propertyName, required, null);
		return (val == null) ?
			defaultValue :
			new Integer(val);
	}

	public String getStringProperty(String propertyName, Boolean required) throws IniFileException {
		return getStringProperty(propertyName, required, null);
	}

	public String getStringProperty(String propertyName, Boolean required, String defaultValue) throws IniFileException {

		String val = props.getProperty(propertyName);
		if (val == null) {
			if (required) {
				throw new IniFileException("Error while reading required property (" + propertyName + ") from ini-file (" + iniFileName +")");
			} else {
				val = defaultValue;
			}
		} else {
			val = val.trim();
		}

		return val;
	}

	public String[] getStringArrayProperty(String propertyName, Boolean required) throws IniFileException {
		return 	getStringArrayProperty(propertyName, required, null);
	}

	public String[] getStringArrayProperty(String propertyName, Boolean required, String defaultValue) throws IniFileException {

		String[] res = getStringProperty(propertyName, required, defaultValue).split(";");

		for (int i = 0; i < res.length; i++) {
			res[i] = res[i].trim();
		}

		return res;
	}

}
