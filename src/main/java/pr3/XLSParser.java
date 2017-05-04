package pr3;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitry Ermakov <de@ae-inf.ru>
 * Date: 3/3/14
 * Time: 1:32 PM
 */
public class XLSParser {

	private SrcXLSWorkbook srcWb;
	private ResXLSWorkbook resWb;

	public XLSParser(FileName resFileName, FileName srcFileName) throws Exception {
		resWb = new ResXLSWorkbook(resFileName);
		srcWb = new SrcXLSWorkbook(srcFileName);
	}

	public SrcXLSWorkbook getSrcWb() {
		return srcWb;
	}

	public void parseFile() throws Exception {
		srcWb.parseWorkbook(resWb);
		resWb.save();
	}


}
