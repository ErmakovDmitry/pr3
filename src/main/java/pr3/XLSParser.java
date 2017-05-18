package pr3;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/3/14
 * Time: 1:32 PM
 */
public class XLSParser {

	/**
	 * Xls-книга - источник данных
	 */
	private SrcXLSWorkbook srcWb;

	/**
	 * Выходная xls-книга
	 */
	private ResXLSWorkbook resWb = null;

	/**
	 * Выходная база
	 */
	private DbMySql resDb = null;

	/**
	 * Локализация настроек программы
	 */
	private Settings settings;


	public XLSParser(FileName srcFileName, FileName resFileName, Settings settings) throws Exception {
		this.settings = settings;

		srcWb = new SrcXLSWorkbook(srcFileName, settings);

		if (settings.getXlsOutEnabled()) {
			resWb = new ResXLSWorkbook(resFileName);
		}

		SettingsDbOut settingsDbOut = settings.getSettingsDbOut();
		if (settingsDbOut.getEnabled()) {
			resDb = new DbMySql(settingsDbOut);
			resDb.connect();
			resDb.sel();
		}
	}

	public SrcXLSWorkbook getSrcWb() {
		return srcWb;
	}

	public void parseFile() throws Exception {

		srcWb.parseWorkbook(resWb, resDb);

		if (resWb != null) {
			resWb.save();
		}

		if (resDb != null) {
			resDb.disconnect();
			resDb = null;
		}
	}


}
