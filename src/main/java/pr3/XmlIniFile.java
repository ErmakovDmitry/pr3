package pr3;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Файл для хранения настроек в xml-формате
 * Created by Ermakov Dmitry on 11/10/16.
 */
public class XmlIniFile {

	private String xmlIniFileName;

	public <T> T load(String xmlIniFileName, Class<T> c) throws XmlIniFileException {

		this.xmlIniFileName = xmlIniFileName;

		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Object object = unmarshaller.unmarshal(new FileInputStream(new File(xmlIniFileName)));

			return (T) object;
		} catch (JAXBException e) {
			throw new XmlIniFileException("Error while parsing xml-file (" + xmlIniFileName +"): " + e.getLocalizedMessage(), e);
		} catch (FileNotFoundException e) {
			throw new XmlIniFileException("Xml-file not found (" + xmlIniFileName +"): " + e.getLocalizedMessage(), e);
		}

	}

}
