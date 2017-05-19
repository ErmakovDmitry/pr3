package pr3.xls;

/**
 * Исключения, произошедшие при работе с xls-книгой
 * Created by dmitry on 19.05.17.
 */
public class XLSWorkbookException extends Exception {

    public XLSWorkbookException(Throwable cause) {
        super(cause);
    }

    public XLSWorkbookException(String message) {
        super(message);
    }

    public XLSWorkbookException(String message, Throwable cause) {
        super(message, cause);
    }

}
