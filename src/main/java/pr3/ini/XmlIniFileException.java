package pr3.ini;

/**
 * Created by Ermakov Dmitry on 11/10/16.
 */
public class XmlIniFileException extends Exception {

	public XmlIniFileException(Throwable cause) {
		super(cause);
	}

	public XmlIniFileException(String message) {
		super(message);
	}

	public XmlIniFileException(String message, Throwable cause) {
		super(message, cause);
	}
}
