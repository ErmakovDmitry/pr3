package pr3;

/**
 * Created by Ermakov Dmitry on 7/7/16.
 */
public class IniFileException extends Exception {

	public IniFileException(Throwable cause) {
		super(cause);
	}

	public IniFileException(String message) {
		super(message);
	}

	public IniFileException(String message, Throwable cause) {
		super(message, cause);
	}
}
