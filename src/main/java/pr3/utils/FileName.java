package pr3.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/5/14
 * Time: 10:00 AM
 * To change this template use File | IniValues | File Templates.
 */
public class FileName {

	/**
	 * Логгер
	 */
	final static Logger logger = LogManager.getLogger(FileName.class.getName());

	private String fullNameWithDir;
	private String fullNameWithoutDir;
	private String nameWithoutExtension;
	private String extension;

	public FileName(String fullNameWithDir) throws FileNameException {

		if ((fullNameWithDir == null) || (fullNameWithDir.isEmpty())) {
			throw new FileNameException("не задано имя файла");
		}

		this.fullNameWithDir = fullNameWithDir.trim();

		fullNameWithoutDir = this.fullNameWithDir;
		int slashPos = this.fullNameWithDir.lastIndexOf("/");
		if (slashPos > -1) {
			fullNameWithoutDir = this.fullNameWithDir.substring(slashPos + 1);
		} else {
			slashPos = this.fullNameWithDir.lastIndexOf("\\");
			if (slashPos > -1) {
				fullNameWithoutDir = this.fullNameWithDir.substring(slashPos + 1);
			}
		}

		int dotPos = fullNameWithoutDir.lastIndexOf(".");
		if (dotPos > -1) {
			nameWithoutExtension = fullNameWithoutDir.substring(0,dotPos);
			extension = fullNameWithoutDir.substring(dotPos + 1);
		} else {
			nameWithoutExtension = fullNameWithoutDir;
			extension = new String("");
		}

//		logger.debug("fullNameWithDir: "+this.fullNameWithDir);
//		logger.debug("fullNameWithoutDir: "+fullNameWithoutDir);
//		logger.debug("nameWithoutExtension: "+nameWithoutExtension);
//		logger.debug("extension: "+extension);
	}

	public String getFullNameWithDir() {
		return fullNameWithDir;
	}

	public String getFullNameWithoutDir() {
		return fullNameWithoutDir;
	}

	public String getNameWithoutExtension() {
		return nameWithoutExtension;
	}

	public String getExtension() {
		return extension;
	}

	/**
	 * Выделение расширения из полного имени файла
	 * @param filename полное имя файла
	 * @return расширение без точки
	 */
	/*
	protected static String getFileExtension(String filename){
		int dotPos = filename.lastIndexOf(".") + 1;
		return filename.substring(dotPos);
	}*/

	@Override
	public String toString() {
		return "FileName{" +
				"fullNameWithDir='" + fullNameWithDir + '\'' +
				", fullNameWithoutDir='" + fullNameWithoutDir + '\'' +
				", nameWithoutExtension='" + nameWithoutExtension + '\'' +
				", extension='" + extension + '\'' +
				'}';
	}
}
