package pr3.utils;

/**
 * Исключения, произошедшие при работе с именем файла
 * Created by dmitry on 19.05.17.
 */
public class FileNameException extends Exception {

    public FileNameException(Throwable cause) {
        super(cause);
    }

    public FileNameException(String message) {
        super(message);
    }

    public FileNameException(String message, Throwable cause) {
        super(message, cause);
    }

}
