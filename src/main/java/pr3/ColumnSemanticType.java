package pr3;

import java.util.ArrayList;
import java.util.List;

/**
 * Смысловое значение колонки
 * Created by dmitry on 03.05.17.
 */
public enum ColumnSemanticType {

    NUMBER, ARTICUL, NAME, UNIT, PRICE, DESCRIPTION;

    private List<String> keyWords = new ArrayList<>();

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

}
