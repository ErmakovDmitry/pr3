package pr3;

/**
 * Колонка прайс-листа со сведениями, которые про нее удалось определить
 * Created by dmitry on 03.05.17.
 */
public class PriceListColumn {

    /**
     * Индекс колонки
     */
    private int ind;

    /**
     * Строковое значение ячейки
     */
    private String headerCellStrVal;

    /**
     * Максимальная длина строкового значения в колонке
     */
    private int maxStrLen;

    /**
     * Смысловое значение колонки
     */
    private ColumnSemanticType semanticType;

    public PriceListColumn() {
        this(-1, "", 0, null);
    }

    public PriceListColumn(int ind, String headerCellStrVal, int maxStrLen, ColumnSemanticType semanticType) {
        this.ind = ind;
        this.headerCellStrVal = headerCellStrVal;
        this.maxStrLen = maxStrLen;
        this.semanticType = semanticType;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public String getHeaderCellStrVal() {
        return headerCellStrVal;
    }

    public void setHeaderCellStrVal(String headerCellStrVal) {
        this.headerCellStrVal = headerCellStrVal;
    }

    public int getMaxStrLen() {
        return maxStrLen;
    }

    public void setMaxStrLen(int maxStrLen) {
        this.maxStrLen = maxStrLen;
    }

    public ColumnSemanticType getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(ColumnSemanticType semanticType) {
        this.semanticType = semanticType;
    }

    @Override
    public String toString() {
        return "PriceListColumn{" +
                "ind=" + ind +
                ", headerCellStrVal='" + headerCellStrVal + '\'' +
                ", maxStrLen=" + maxStrLen +
                ", semanticType=" + semanticType +
                '}';
    }
}
