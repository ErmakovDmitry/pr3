package pr3;

import java.sql.*;

/**
 * Created by Дмитрий on 17.05.2017.
 */
public class DbMySql {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/test";
    private static final String user = "root";
    private static final String password = "root";

    /**
     * Настройки подключения к базе
     */
    private SettingsDbOut settingsDbOut;

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static void main(String args[]) {
        String query = "select count(*) from books";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total number of books in the table : " + count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public DbMySql(SettingsDbOut settingsDbOut) {
        this.settingsDbOut = settingsDbOut;
    }

    public void sel() {
        String query = "select id, name, author from books";

//        rs = stmt.executeQuery(query);
//
//        while (rs.next()) {
//            int id = rs.getInt(1);
//            String name = rs.getString(2);
//            String author = rs.getString(3);
//            System.out.printf("id: %d, name: %s, author: %s %n", id, name, author);
//        }
    }

    public void ins() {
        String query = "INSERT INTO test.books (id, name, author) \n" +
                " VALUES (3, 'Head First Java', 'Kathy Sieara');";

        // executing SELECT query
//        stmt.executeUpdate(query);
    }


}
