package pr3.xls;

/**
 * Исключения, произошедшие при работе с xls-парсера
 * Created by dmitry on 19.05.17.
 */
public class XLSParserException extends Exception {

    public XLSParserException(Throwable cause) {
        super(cause);
    }

    public XLSParserException(String message) {
        super(message);
    }

    public XLSParserException(String message, Throwable cause) {
        super(message, cause);
    }

}
