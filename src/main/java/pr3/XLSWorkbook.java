package pr3;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.beans.PropertyChangeSupport;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/4/14
 * Time: 4:46 PM
 */
public class XLSWorkbook {
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public PropertyChangeSupport getPropertyChangeSupport() {
		return pcs;
	}

	public FileName  fileName;
	public Workbook  workbook;
	public CellStyle commonCellStyle;

	public XLSWorkbook(FileName fileName) throws Exception {

		this.fileName = fileName;

		if (!(fileName.getExtension().equalsIgnoreCase("xls") || fileName.getExtension().equalsIgnoreCase("xlsx"))) {
			throw new Exception("файл для парсинга (" + fileName.getFullNameWithoutDir() + ") должен иметь расширение xls или xlsx, а не " + fileName.getExtension());
		}
	}

	protected void createStyles() {

		this.commonCellStyle = workbook.createCellStyle();
		this.commonCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); // ORANGE
		this.commonCellStyle.setFillPattern(HSSFCellStyle.BORDER_HAIR); // SOLID_FOREGROUND

	}


}
