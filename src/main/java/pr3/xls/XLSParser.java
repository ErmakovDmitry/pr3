package pr3.xls;

import pr3.db.OutDb;
import pr3.ini.IniValues;
import pr3.ini.IniValuesOutDb;
import pr3.ini.IniValuesOutXls;
import pr3.utils.FileName;

import java.io.IOException;
import java.sql.SQLException;

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
	private XLSWorkbookSrc srcWb;

	/**
	 * Выходная xls-книга
	 */
	private XLSWorkbookOut resWb = null;

	/**
	 * Выходная база
	 */
	private OutDb resDb = null;

	/**
	 * Локализация настроек программы
	 */
	private IniValues iniValues;


	public XLSParser(FileName srcFileName, FileName outFileName, IniValues iniValues) throws XLSWorkbookException, SQLException, ClassNotFoundException {
		this.iniValues = iniValues;

		srcWb = new XLSWorkbookSrc(srcFileName, iniValues);

		IniValuesOutXls iniValuesOutXls = iniValues.getIniValuesOutXls();
		if (iniValuesOutXls.getEnabled()) {
			resWb = new XLSWorkbookOut(outFileName);
		}

		IniValuesOutDb iniValuesOutDb = iniValues.getIniValuesOutDb();
		if (iniValuesOutDb.getEnabled()) {
			resDb = new OutDb(iniValuesOutDb);
			resDb.connect();
			resDb.sel();
		}
	}

	public XLSWorkbookSrc getSrcWb() {
		return srcWb;
	}

	public void parseFile() throws XLSWorkbookException, IOException, SQLException {

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
