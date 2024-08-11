package Sites;

/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Interface    MySQLConnection.java
 * Description  A class representing the SQL Connection database.
 * Date         10/26/2023     
 * @author      <i>Kirill Grichanichenko</i>          
 *****************************************************************************/
public interface MySQLConnection {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/sitesDB";
    public static final String USER = "DB_USER"; // Fill user name from your database server
    public static final String PASS = "DB_PASS"; // Fill password from your database server
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
}
