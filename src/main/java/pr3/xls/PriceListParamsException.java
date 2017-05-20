package pr3.xls;

/**
 * Исключения, произошедшие при работе с параметрами прайс-листа
 * Created by Дмитрий on 20.05.2017.
 */
public class PriceListParamsException extends Exception {

    public PriceListParamsException(Throwable cause) {
        super(cause);
    }

    public PriceListParamsException(String message) {
        super(message);
    }

    public PriceListParamsException(String message, Throwable cause) {
        super(message, cause);
    }

}
