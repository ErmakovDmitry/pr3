package pr3.xls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * Колонка прайс-листа со сведениями, которые про нее удалось определить
 * Created by dmitry on 03.05.17.
 */
public class PriceListColumn {

    /**
     * Логгер класса
     */
    final static Logger logger = LogManager.getLogger(PriceListColumn.class.getName());

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

    public void defineColumnSemanticType() {
        if (semanticType == null) {
            // Перебор семантических типов
            for (ColumnSemanticType curSemanticType : ColumnSemanticType.values()) {
                // Ключевые слова очередного семантического типа
                List<String> keyWords = curSemanticType.getKeyWords();

                if (keyWords != null) {
                    // Перебор ключевых слов текущего семантического типа
                    Iterator<String> keyWordsIterator = keyWords.iterator();
                    while (keyWordsIterator.hasNext()) {
                        String word = keyWordsIterator.next().toUpperCase();
                        String cellVal = headerCellStrVal.toUpperCase();
                        if (cellVal.contains(word)) {
                            // Тип определен
                            semanticType = curSemanticType;
                            break;
                        }
                    }
                    // Если тип определен - к следующему типу не переходим
                    if (semanticType != null) {
                        break;
                    }
                }
            }

        }
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
