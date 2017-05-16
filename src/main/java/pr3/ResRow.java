package pr3;

import java.util.ArrayList;

/**
 * Строка результирующего файла
 * Created by dmitry on 27.04.17.
 */
public class ResRow implements Cloneable  {

    private ArrayList<String> parentsArrList = new ArrayList<>();
    private ArrayList<String> articlesArrList = new ArrayList<>();
    private ArrayList<String> numbersArrList = new ArrayList<>();
    private ArrayList<String> namesArrList = new ArrayList<>();
    private ArrayList<String> unitsArrList = new ArrayList<>();
    private ArrayList<Double> pricesArrList = new ArrayList<>();
    private ArrayList<String> descrArrList = new ArrayList<>();
    private String otherVals;

    public ResRow() {
    }

    public ArrayList<String> getParentsArrList() {
        return parentsArrList;
    }

    public ArrayList<String> getArticlesArrList() {
        return articlesArrList;
    }

    public ArrayList<String> getNumbersArrList() {
        return numbersArrList;
    }

    public ArrayList<String> getNamesArrList() {
        return namesArrList;
    }

    public ArrayList<String> getUnitsArrList() {
        return unitsArrList;
    }

    public ArrayList<Double> getPricesArrList() {
        return pricesArrList;
    }

    public ArrayList<String> getDescrArrList() {
        return descrArrList;
    }

    public String getOtherVals() {
        return otherVals;
    }

    public void setOtherVals(String otherVals) {
        this.otherVals = otherVals;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Проверка того, что все цены нулевые или не существуют
     * @return
     */
    public Boolean allPricesNull() {
        Boolean res = true;

        for (Double price : pricesArrList) {
            res = res && ((price == null) || (price == 0));
        }

        return res;
    }

    @Override
    public String toString() {
        return "ResRow{" +
                "\n\t parentsArrList=" + parentsArrList +
                "\n\t articlesArrList=" + articlesArrList +
                "\n\t numbersArrList=" + numbersArrList +
                "\n\t namesArrList=" + namesArrList +
                "\n\t unitsArrList=" + unitsArrList +
                "\n\t pricesArrList=" + pricesArrList +
                "\n\t descrArrList=" + descrArrList +
                "\n\t otherVals='" + otherVals + '\'' +
                "\n\t}";
    }
}
