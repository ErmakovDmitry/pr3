package pr3;

/**
 * Строка результирующего файла
 * Created by dmitry on 27.04.17.
 */
public class ResRow implements Cloneable  {

    private String name;
    private String parents;
    private String prices;

    public ResRow(String parents, String name, String prices) {
        this.name = name;
        this.parents = parents;
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
