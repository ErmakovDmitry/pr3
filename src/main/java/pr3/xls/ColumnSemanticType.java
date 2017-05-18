package pr3.xls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Смысловое значение колонки
 * Created by dmitry on 03.05.17.
 */
public enum ColumnSemanticType {

    NUMBER, ARTICLE, NAME, UNIT, PRICE, DESCRIPTION;

    /**
     * Ключевые слова, содержащиеся в заголовке, по которым можно определить смысловое значение колонки
     */
    private List<String> keyWords = new ArrayList<>();

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public static void init() {
        ColumnSemanticType.ARTICLE.setKeyWords(Arrays.asList("артикул","для заказа"));
        ColumnSemanticType.NUMBER.setKeyWords(Arrays.asList("номер","номенклат"));
        ColumnSemanticType.NAME.setKeyWords(Arrays.asList("наимен", "назв"));
        ColumnSemanticType.UNIT.setKeyWords(Arrays.asList("един", "измер"));
        ColumnSemanticType.PRICE.setKeyWords(Arrays.asList("цена", "стоим"));
        ColumnSemanticType.DESCRIPTION.setKeyWords(Arrays.asList("полное", "опис"));
    }
}
