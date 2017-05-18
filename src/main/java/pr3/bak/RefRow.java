package pr3.bak;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/3/14
 * Time: 5:19 PM
 * To change this template use File | IniValues | File Templates.
 */
public class RefRow implements Cloneable {


	public String  raion;        // Экономический район
	public String  marka;        // Марка ТС
	public Integer amount_leg;  // Легковые автомобили (кол-во договоров)
	public Double  price_leg;    // Легковые автомобили (сред. цена)
	public Integer amount_gruz;  // Грузовые автомобили (кол-во договоров)
	public Double  price_gruz;   // Грузовые автомобили (сред. цена)
	public Integer amount_avt;    // Автобусы (кол-во договоров)
	public Double  price_avt;    // Автобусы (сред. цена)


	public RefRow() {
		this.raion = "";
		this.marka = "";
		this.amount_leg  = new Integer(0);
		this.price_leg   = new Double(0);
		this.amount_gruz = new Integer(0);
		this.price_gruz  = new Double(0);
		this.amount_avt  = new Integer(0);
		this.price_avt   = new Double(0);
	}

	@Override
	public String toString() {
		return "RefRow{" +
			"raion='" + raion + '\'' +
			", marka='" + marka + '\'' +
			", amount_leg=" + amount_leg +
			", price_leg=" + price_leg +
			", amount_gruz=" + amount_gruz +
			", price_gruz=" + price_gruz +
			", amount_avt=" + amount_avt +
			", price_avt=" + price_avt +
			'}';
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
