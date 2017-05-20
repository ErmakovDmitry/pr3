package pr3.xls;

import java.time.LocalDate;

import static pr3.utils.DateUtils.strToLocalDate;

/**
 * Параметры прайс-листа, определяемые из строки (имени файла), содержащей эти параметры в текстовом виде через разделитель
 * Created by dmitry on 25.04.17.
 */
public class PriceListParams {

//    /**
//     * Массив сеттеров для изысканной наркомании
//     */
//    final private List<Consumer<String>> FIELDS = Arrays.asList(this::setPriceListSrc, this::setPriceListCurrency);
//    FIELDS.get(0).accept();

    /**
     * Количество полей (разделенных PARAMS_DELIM), из которых должна состоять строка с параметрами прайс-листа (имя файла)
     */
    final static private int PARAMS_COUNT = 4;

    /**
     * Разделитель параметров прайс-листа в строке
     */
    final static private String PARAMS_DELIM = "_";

    /**
     * Источник прайс-листа
     */
    private String priceListSrc;

    /**
     * Дата прайс-листа
     */
    private LocalDate priceListStartDate;

    /**
     * Валюта прайс-листа
     */
    private String priceListCurrency;

    /**
     * Тип прайс-листа
     */
    private String priceListType;

    public PriceListParams() {

    }

    public PriceListParams(String params) throws PriceListParamsException {
        parse(params);
    }

    public void parse(String params) throws PriceListParamsException {
        String[] strParams = params.split(PARAMS_DELIM);
        if (strParams.length != PARAMS_COUNT) {
            throw new PriceListParamsException("Имя файла должно состоять из " + PARAMS_COUNT + " полей, разделенных '" + PARAMS_DELIM + "'");
        }

        setPriceListSrc(strParams[0]);
        setPriceListStartDate(strParams[1]);
        setPriceListCurrency(strParams[2]);
        setPriceListType(strParams[3]);
    }

    public String getPriceListSrc() {
        return priceListSrc;
    }

    public void setPriceListSrc(String priceListSrc) {
        this.priceListSrc = priceListSrc;
    }

    public LocalDate getPriceListStartDate() {
        return priceListStartDate;
    }

    public void setPriceListStartDate(LocalDate priceListStartDate) {
        this.priceListStartDate = priceListStartDate;
    }

    public void setPriceListStartDate(String priceListStartDateStr) {
        setPriceListStartDate(strToLocalDate(priceListStartDateStr, "yyyyMMdd"));
    }

    public String getPriceListCurrency() {
        return priceListCurrency;
    }

    public void setPriceListCurrency(String priceListCurrency) {
        this.priceListCurrency = priceListCurrency;
    }

    public String getPriceListType() {
        return priceListType;
    }

    public void setPriceListType(String priceListType) {
        this.priceListType = priceListType;
    }

    @Override
    public String toString() {
        return "PriceListParams{" +
                "priceListSrc='" + priceListSrc + '\'' +
                ", priceListStartDate=" + priceListStartDate +
                ", priceListCurrency='" + priceListCurrency + '\'' +
                ", priceListType='" + priceListType + '\'' +
                '}';
    }


}
