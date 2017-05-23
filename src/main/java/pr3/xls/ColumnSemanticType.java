package pr3.xls;

import pr3.ini.IniValuesColumnSemanticType;
import pr3.ini.IniValuesColumnSemanticTypeKeyWord;
import pr3.ini.IniValuesColumnSemanticTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Смысловое значение колонки xls-файла для парсера
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

    /**
     * Инициализация элементов enum ключевыми словами для распознавания типов
     * @param iniValuesColumnSemanticTypes Ключевые слова из XmlIni-файла
     */
    public static void init(IniValuesColumnSemanticTypes iniValuesColumnSemanticTypes) {

        for (IniValuesColumnSemanticType itm : iniValuesColumnSemanticTypes.getIniValuesColumnSemanticTypeList()) {
            ColumnSemanticType columnSemanticType = ColumnSemanticType.valueOf(itm.getName());
            if (columnSemanticType != null) {
                for (IniValuesColumnSemanticTypeKeyWord wrd : itm.getIniValuesColumnSemanticTypeKeyWords().getKeyWords()) {
                    columnSemanticType.getKeyWords().add(wrd.getValue());
                }
            }
        }

//        ColumnSemanticType.ARTICLE.setKeyWords(Arrays.asList("артикул","для заказа"));
//        ColumnSemanticType.NUMBER.setKeyWords(Arrays.asList("номер","номенклат"));
//        ColumnSemanticType.NAME.setKeyWords(Arrays.asList("наимен", "назв"));
//        ColumnSemanticType.UNIT.setKeyWords(Arrays.asList("един", "измер"));
//        ColumnSemanticType.PRICE.setKeyWords(Arrays.asList("цена", "стоим"));
//        ColumnSemanticType.DESCRIPTION.setKeyWords(Arrays.asList("полное", "опис"));
    }
}
