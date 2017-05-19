package pr3.db;

import pr3.ini.IniValuesOutDb;

import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Выходная база данных
 * Created by Дмитрий on 17.05.2017.
 */
public class OutDb {

    private static Logger log = Logger.getLogger(OutDb.class.getName());

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
        log.info("Подключение к выходной базе с параметрами:\n" +iniValuesOutDb.asString());
        if (connection == null) {

//            try {
                Class.forName(iniValuesOutDb.getDriver());
                connection = DriverManager.getConnection(
                        iniValuesOutDb.getUrl()
                        , iniValuesOutDb.getUser()
                        , iniValuesOutDb.getPass()
                );
//            } catch (ClassNotFoundException | SQLException e) {
//                e.printStackTrace();
//            }
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
        //            System.out.printf("id: %d, name: %s, author: %s %n", id, name, author);
                System.out.printf("sel(): %d\n", id);
            }

        } catch (SQLException ex){
            // handle any errors
            throw new SQLException(ex);
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
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

    public void ins(OutDbRow outDbRow) {
        String query = "INSERT INTO test.books (id, name, author) \n" +
                " VALUES (3, '" + outDbRow.getPlna_item_text() + "', 'Kathy Sieara');";

        System.out.println(query);
        // executing SELECT query
//        stmt.executeUpdate(query);
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
//				System.out.println(tableToConv.getName() + "=> " + tblSql);
//				if (tblSql != null) {
//					System.out.println("SQL");
//				} else {
//					System.out.println("not SQL");
//				}

				// Спец. запрос, которые поставляет исходный набор данных вместо исходной таблицы
				String specSelQuery = tableToConv.getSql();

				String tableName = tableToConv.getName();

				timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				System.out.println("\n" + timeStamp + " table '" + tableName + "':");

				Boolean success = false;

				int srcTableRecCount = 0;
				int destTableRecCount = 0;

				// Проверка существования исходной таблицы в Firebird, если исходные данные не поставляются запросом
				System.out.print("\tchecking existance in Firebird base ... ");
				if ((specSelQuery !=null) || srcDb.tableExists(tableName)) {
					System.out.println("OK");

					// Проверка существования исходной таблицы в PostgreSQL
					System.out.print("\tchecking existance in PostgreSQL base ... ");
					if (destDb.tableExists(pgSchema, tableName)) {
						System.out.println("OK");

						DbTable srcTable = srcDb.getTable(tableName, specSelQuery);
						DbTable destTable = destDb.getTable(pgSchema, tableName);

						System.out.print("\tchecking field set compatibility ... ");

						// log-сравнения полей
						StringBuilder colSetLog = new StringBuilder();

						// Проверка совместимости наборов полей в исходной и целевой таблицах
						if (fb2PgConverter.columnSetsCompatible(srcTable, destTable, colSetLog)) {

//							System.out.println();
//							System.out.print(colSetLog);
							System.out.println("OK");

							// Очистка целевой таблицы
							destTable.empty();

							srcTableRecCount = srcTable.getRecordCount();
							System.out.print("\tdata converting (" + srcTableRecCount + " records) ... ");

							// Конвертация данных
							fb2PgConverter.convTableData(srcTable, destTable);

							// Наивная проверка результатов конвертации
							destTableRecCount = destTable.getRecordCount();
							if (destTableRecCount == srcTableRecCount) {
								System.out.println("OK");
								success = true;
							} else {
								System.out.println("\trecords count in PostgreSQL (" + destTableRecCount + ") NOT EQUAL records count in Firebird (" + srcTableRecCount + ")");
							}

						} else {
							System.out.println("FAIL");
							System.out.print(colSetLog);
						}

					} else {
						System.out.println("FAIL");
					}
				} else {
					System.out.println("FAIL");
				}

				if (success) {
					timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
					System.out.println(timeStamp + " SUCCESS (" + destTableRecCount + " records converted)");
					success_count++;
				} else {
					System.out.println("ERROR");
					unsuccess_count++;
				}
			}

			System.out.println("\nResult: " + success_count + " tables successfully converted; " + unsuccess_count + " tables NOT converted");

		} catch (SQLException e) {
			System.out.println("Error while connection to database: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (DbException e) {
			System.out.println("Error while operating on database structure: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Fb2PgConverterException e) {
			System.out.println("Error while data converting: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
  */
