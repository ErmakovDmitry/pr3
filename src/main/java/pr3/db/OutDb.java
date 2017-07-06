package pr3.db;

import com.mysql.jdbc.*;
import com.mysql.jdbc.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pr3.ini.IniValuesOutDb;
import pr3.xls.ResRow;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;


/**
 * Выходная база данных
 * Created by Дмитрий on 17.05.2017.
 */
public class OutDb {

    private static Logger logger = LogManager.getLogger(OutDb.class.getName());

    /**
     * Настройки подключения к базе
     */
    private IniValuesOutDb iniValuesOutDb;

    // JDBC variables for opening and managing connection
    private Connection connection = null;

    public OutDb(IniValuesOutDb iniValuesOutDb) {
        this.iniValuesOutDb = iniValuesOutDb;
    }

    public Connection connect() throws ClassNotFoundException, SQLException {
        logger.info("Подключение к выходной базе с параметрами:\n" +iniValuesOutDb.asString());
        if (connection == null) {
//            Driver d = new Driver();
//            java.util.Properties info = new java.util.Properties();
//            info.put("user", iniValuesOutDb.getUser());
//            info.put("password", iniValuesOutDb.getPass());
//            connection  = d.connect(iniValuesOutDb.getUrl(), info);
            Class.forName(iniValuesOutDb.getDriver());
            connection = DriverManager.getConnection(
                    iniValuesOutDb.getUrl()
                    , iniValuesOutDb.getUser()
                    , iniValuesOutDb.getPass()
            );
        }

        return connection;
    }

    public void disconnect() throws SQLException {

        if (connection != null) {
            connection.close();
            connection = null;
        }

    }

    public void sel() throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = connection.createStatement();

            String query = "SELECT count(*) FROM DB_GP.plna_items";

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
        //            String name = rs.getString(2);
        //            String author = rs.getString(3);
        //            logger.debug("id: %d, name: %s, author: %s %n", id, name, author);
                logger.debug("sel(): %d\n", id);
            }

        } catch (SQLException ex){
            // handle any errors
            throw new SQLException(ex);
//            logger.debug("SQLException: " + ex.getMessage());
//            logger.debug("SQLState: " + ex.getSQLState());
//            logger.debug("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }
    }

    /**
     * Добавление в базу записи об импортируемом прайс-листе
     * @param source_type Идентификатор источника прайс-листа
     * @param source_name Наименование импортируемого файла
     * @return
     * @throws SQLException
     */
    public Integer insPriceListAndGetId(String source_type, String source_name) throws SQLException {
        Integer autoId = null;
        /*
        SELECT * FROM DB_GP.plna_sources;

INSERT INTO DB_GP.plna_sources
SET
	plnas_source_type = "gpermakov_test"
	,plnas_source_name = "file_test";


SELECT * FROM DB_GP.plna_sources
WHERE plnas_source_type = "gpermakov_test";

DELETE FROM DB_GP.plna_sources
WHERE plnas_source_type = "gpermakov_test";

SELECT * FROM DB_GP.plna_sources
WHERE plnas_id = 86;

DELETE FROM DB_GP.plna_sources
WHERE plnas_id = 87;
         */

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql = "INSERT INTO DB_GP.plna_sources (plnas_source_type, plnas_source_name) VALUES (?, ?)";

            stmt = connection. prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, source_type);
            stmt.setString(2, source_name);

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                autoId = rs.getInt(1);
            }

        } catch (SQLException ex){
            throw new SQLException(ex);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }

        return autoId;
    }

    public void insPriceListItem(Integer priceListRecId, ResRow resRow, OutDbRow outDbRow) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql =
                "INSERT INTO DB_GP.plna_items (" +
                "plna_source_id" +
                ",plna_row_num" +
                ",plna_item_text" +
                ",plna_item_text_extra" +
                ",plna_item_cat0" +
                ",plna_item_cat1" +
                ",plna_item_cat2" +
                ",plna_item_cat3" +
                ",plna_item_cat4" +
                ",plna_units" +
                ",plna_price_1" +
                ",plna_article_code" +
                ",plna_item_code" +
                ",plna_part_num" +
                ",plna_extra_data" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            stmt = connection. prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // plna_source_id  int(11)
            stmt.setInt(1, priceListRecId);
            // plna_row_num  int(11)
            stmt.setInt(2, resRow.getSrcRowNum());
            // plna_item_text  text
            stmt.setString(3, (resRow.getNamesArrList().size() > 0)? resRow.getNamesArrList().get(0) : null);
            //	plna_item_text_extra  text
            stmt.setString(4, (resRow.getNamesArrList().size() > 1)? resRow.getNamesArrList().get(1) : null);

            // plna_item_cat0  text
            // plna_item_cat1  text
            // plna_item_cat2  text
            // plna_item_cat3  text
            // plna_item_cat4  text
            for (int i = 5; i <= 9; i++) {
                stmt.setString(i, (resRow.getParentsArrList().size() > i)? resRow.getParentsArrList().get(i) : null);
            }

            // plna_units  varchar(50)
            stmt.setString(10, (resRow.getUnitsArrList().size() > 0)? limitStrLen(resRow.getUnitsArrList().get(0), 50) : null);

            // plna_price_1  decimal(15,2)
            if (resRow.getPricesArrList().size() > 0) {
                stmt.setDouble(11, resRow.getPricesArrList().get(0));
            } else {
                stmt.setObject(11, null);
            }

            // plna_article_code varchar(50)
            stmt.setString(12, (resRow.getArticlesArrList().size() > 0)? limitStrLen(resRow.getArticlesArrList().get(0), 50) : null);

            // plna_item_code  varchar(50)
            stmt.setString(13, (resRow.getNumbersArrList().size() > 0)? limitStrLen(resRow.getNumbersArrList().get(0), 50) : null);

            // plna_part_num varchar(50)
            stmt.setString(14, (resRow.getNumbersArrList().size() > 1)? limitStrLen(resRow.getNumbersArrList().get(1), 50) : null);

            // plna_extra_data text
            stmt.setString(15, (resRow.getOtherVals() != null)? resRow.getOtherVals() : null);

            stmt.executeUpdate();

        } catch (SQLException ex){
            throw new SQLException(ex);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }
    }


    /**
     * Ограничение длины строки для сохранения в поле базы
     * @param inStr
     * @param maxLen
     * @return
     */
    private String limitStrLen(String inStr, int maxLen) {
        String outStr = null;

        if (inStr != null) {
            outStr = inStr.trim();

            if (outStr.length() > 0) {
                outStr = outStr.substring(0, Math.min(outStr.length(), maxLen) - 1);
            }
        }

        return outStr;
    }

}

/*
		try (

			Connection fbConnection = fbDriver.connect(fbUrl, new Properties(){{
				put("user_name", ini.getFbUser());
				put("password", ini.getFbPassword());
				put("encoding", ini.getFbEncoding());
			}});

			Connection pgConnection = pgDriver.connect(pgUrl, new Properties(){{
				put("user", ini.getPgUser());
				put("password", ini.getPgPassword());
			}});

		){

			// Метка времени для лога
			String timeStamp;

			// Исходная база
			DbFb srcDb  = new DbFb(fbConnection);
			// Целевая база
			DbPg destDb = new DbPg(pgConnection);

			// Конвертер
			Fb2PgConverter fb2PgConverter = new Fb2PgConverter();
			fb2PgConverter.setInsBatchSize(ini.getInsBatchSize());
			fb2PgConverter.setLogBatchCountInLine(ini.getLogBatchCountInLine());

			if (!destDb.schemaExists(pgSchema)) {
				throw new Fb2PgConverterException("Schema '" + pgSchema + "' does NOT exist in source base");
			}

			int success_count = 0;
			int unsuccess_count = 0;

			List<TableToConv> tablesToConvList = ini.getTablesToConvList();
			for(TableToConv tableToConv : tablesToConvList) {

//				String tblSql = tableToConv.getSql();
//				logger.debug(tableToConv.getName() + "=> " + tblSql);
//				if (tblSql != null) {
//					logger.debug("SQL");
//				} else {
//					logger.debug("not SQL");
//				}

				// Спец. запрос, которые поставляет исходный набор данных вместо исходной таблицы
				String specSelQuery = tableToConv.getSql();

				String tableName = tableToConv.getName();

				timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				logger.debug("\n" + timeStamp + " table '" + tableName + "':");

				Boolean success = false;

				int srcTableRecCount = 0;
				int destTableRecCount = 0;

				// Проверка существования исходной таблицы в Firebird, если исходные данные не поставляются запросом
				logger.debug("\tchecking existance in Firebird base ... ");
				if ((specSelQuery !=null) || srcDb.tableExists(tableName)) {
					logger.debug("OK");

					// Проверка существования исходной таблицы в PostgreSQL
					logger.debug("\tchecking existance in PostgreSQL base ... ");
					if (destDb.tableExists(pgSchema, tableName)) {
						logger.debug("OK");

						DbTable srcTable = srcDb.getTable(tableName, specSelQuery);
						DbTable destTable = destDb.getTable(pgSchema, tableName);

						logger.debug("\tchecking field set compatibility ... ");

						// log-сравнения полей
						StringBuilder colSetLog = new StringBuilder();

						// Проверка совместимости наборов полей в исходной и целевой таблицах
						if (fb2PgConverter.columnSetsCompatible(srcTable, destTable, colSetLog)) {

//							logger.debug();
//							logger.debug(colSetLog);
							logger.debug("OK");

							// Очистка целевой таблицы
							destTable.empty();

							srcTableRecCount = srcTable.getRecordCount();
							logger.debug("\tdata converting (" + srcTableRecCount + " records) ... ");

							// Конвертация данных
							fb2PgConverter.convTableData(srcTable, destTable);

							// Наивная проверка результатов конвертации
							destTableRecCount = destTable.getRecordCount();
							if (destTableRecCount == srcTableRecCount) {
								logger.debug("OK");
								success = true;
							} else {
								logger.debug("\trecords count in PostgreSQL (" + destTableRecCount + ") NOT EQUAL records count in Firebird (" + srcTableRecCount + ")");
							}

						} else {
							logger.debug("FAIL");
							logger.debug(colSetLog);
						}

					} else {
						logger.debug("FAIL");
					}
				} else {
					logger.debug("FAIL");
				}

				if (success) {
					timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
					logger.debug(timeStamp + " SUCCESS (" + destTableRecCount + " records converted)");
					success_count++;
				} else {
					logger.debug("ERROR");
					unsuccess_count++;
				}
			}

			logger.debug("\nResult: " + success_count + " tables successfully converted; " + unsuccess_count + " tables NOT converted");

		} catch (SQLException e) {
			logger.debug("Error while connection to database: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (DbException e) {
			logger.debug("Error while operating on database structure: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Fb2PgConverterException e) {
			logger.debug("Error while data converting: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
  */
