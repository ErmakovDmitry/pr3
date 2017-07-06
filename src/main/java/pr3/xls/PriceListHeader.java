package pr3.xls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Заголовок прайс-листа
 * Created by dmitry on 05.05.17.
 */
public class PriceListHeader {

    /**
     * Логгер класса
     */
    final static Logger logger = LogManager.getLogger(PriceListHeader.class.getName());

    /**
     * Количество колонок в прайс-листе
     */
    private int modaCellsCount;

    /**
     * Индекс первой строки заголовка (0-Based)
     */
    private Integer startRowNum = null;

    /**
     * Индекс последней строки заголовка (0-Based)
     */
    private Integer endRowNum = null;

    /**
     * Массив колонок прайс-листа
     */
    private PriceListColumn[] columns;

    public PriceListHeader(int modaCellsCount) {
        this.modaCellsCount = modaCellsCount;
        this.columns = new PriceListColumn[modaCellsCount];
        for (int i = 0; i < this.columns.length; i++) {
            this.columns[i] = new PriceListColumn();
        }
    }

    public Integer getStartRowNum() {
        return startRowNum;
    }

    public void setStartRowNum(Integer startRowNum) {
        this.startRowNum = startRowNum;
    }

    public Integer getEndRowNum() {
        return endRowNum;
    }

    public void setEndRowNum(Integer endRowNum) {
        this.endRowNum = endRowNum;
    }

    public PriceListColumn[] getColumns() {
        return columns;
    }

    /**
     * Определение семантических типов колонок
     */
    public void defineColumnsSemanticTypes() {
        logger.info("Определение семантических типов колонок ...");
        for (int i = 0; i < columns.length; i++) {
            columns[i].defineColumnSemanticType();
            logger.info(i + " => " + columns[i].toString());
        }
    }

    public String asString() {
        String res = "";

        for (int i = 0; i < columns.length; i++) {
            res = res + "; column[" + i + "] " + columns[i] +"\n";
        }

        res = "Заголовок:" +
                "\nmodaCellsCount=" + modaCellsCount +
                "\nstartRowNum=" + startRowNum +
                "\nendRowNum=" + endRowNum +
                "\n" + res;

        return res;
    }

    @Override
    public String toString() {
        return "Заголовок {" +
                "modaCellsCount=" + modaCellsCount +
                '}';
    }

    public ColumnSemanticType defineSemanticTypeByInd(int ind) {
        ColumnSemanticType res = null;

        if (ind > -1) {
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].getInd() == ind) {
                    res = columns[i].getSemanticType();
                    break;
                }
            }
        }

        return res;
    }
}
