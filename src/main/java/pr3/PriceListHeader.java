package pr3;

/**
 * Заголовок прайс-листа
 * Created by dmitry on 05.05.17.
 */
public class PriceListHeader {

    /**
     * Количество колонок в прайс-листе
     */
    private int modaCellsCount;

    /**
     * Индекс первой строки заголовка
     */
    private Integer startRowInd;

    /**
     * Индекс последней строки заголовка
     */
    private Integer endRowInd;

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

    public Integer getStartRowInd() {
        return startRowInd;
    }

    public void setStartRowInd(Integer startRowInd) {
        this.startRowInd = startRowInd;
    }

    public Integer getEndRowInd() {
        return endRowInd;
    }

    public void setEndRowInd(Integer endRowInd) {
        this.endRowInd = endRowInd;
    }

    public PriceListColumn[] getColumns() {
        return columns;
    }

    /**
     * Определение семантических типов колонок
     */
    public void defineColumnsSemanticTypes() {
        for (int i = 0; i < columns.length; i++) {
            columns[i].defineColumnSemanticType();
        }
    }

    public String asString() {
        String res = "";

        for (int i = 0; i < columns.length; i++) {
            res = res + "; column[" + i + "] " + columns[i] +"\n";
        }

        res = "Заголовок:" +
                "\nmodaCellsCount=" + modaCellsCount +
                "\nstartRowInd=" + startRowInd +
                "\nendRowInd=" + endRowInd +
                "\n" + res;

        return res;
    }

    @Override
    public String toString() {
        return "Заголовок {" +
                "modaCellsCount=" + modaCellsCount +
                '}';
    }
}
