package Sites;

import java.sql.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import java.sql.DriverManager;

/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Class        CreateSiteDB.java
 * Description  A class that reads information from an external file and creates
 *              and fills the database given the information.
 * Date         10/26/2023     
 * @author      <i>Kirill Grichanichenko</i>
 * @see     	javax.swing.JFrame
 * @see     	java.awt.Toolkit         
 *****************************************************************************/
public class CreateSiteDB implements MySQLConnection {
    private static final String SITE_FILE = "src/Data/sites.txt";
    private static ArrayList<Site> sites = new ArrayList<Site>();
    private static Site mySite = new Site();

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       main()
     * Description  runs the CreateSiteDB class.
     * Date         10/26/2023
     * @param       args are the command line strings
     * @author      <i>Kirill Grichanichenko</i>
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void main(String[] args) {
        createDatabase();
        createTable(SITE_FILE);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       createDatabase()
     * Description  method that creates the sitesDB database in the server
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void createDatabase()
    {
        try {
            String url = DB_URL;
            String user = USER;
            String password = PASS;

            // Obtain connection to the MySQL server
            Connection con = DriverManager.getConnection(url, user, password); 
            Statement stmt = con.createStatement();

            // Check if the database already exists
            ResultSet result = stmt.executeQuery("SHOW DATABASES LIKE 'sitesDB'");
            if (!result.next()) {
                // Create the database if it does not exist
                stmt.executeUpdate("CREATE DATABASE sitesDB");
                System.out.println("Database 'sitesDB' created successfully.");
            } else {
                System.out.println("Database 'sitesDB' already exists.");
            }
            stmt.close();
            con.close();
        } catch (SQLException exp) {
            JOptionPane.showMessageDialog(null, "SQL error", "SQL ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       createTable()
     * Description  method that takes the file creates and fills 
     *              the database given the information.
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023
     * @param       file String
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void createTable(String file)
    {
        try
        {
            // Read from text file and fill ArrayList
            readFromTextFile(file);
            // Obtain connection to constants from interface
            String url = DB_URL;
            String user = USER;
            String password = PASS;
            
            // Make connection to mySQL DB
            Connection con = DriverManager.getConnection(url, user, password);
            
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet table;
            
            // Check to see if the tables already exist
            table = dbm.getTables(null, null, "SitesBook", null);
            if(table.next())
            {
                // If the table exists, clear it so we can re-make it.
                stmt.executeUpdate("DROP TABLE SitesBook");
            }
            // Create table SitesBook
            stmt.executeUpdate("CREATE TABLE SitesBook (siteID"
                    + " SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT, Name"
                    + " VARCHAR(40), Country VARCHAR(40), Population FLOAT,"
                    + " Capital VARCHAR(30), Area FLOAT,"
                    + " PRIMARY KEY (siteID))");
            
           for (int i = 0; i < sites.size(); i++)
           {
               stmt.executeUpdate("INSERT INTO SitesBook VALUES ("
                    + sites.get(i).getID() + ","
                    + "'" + sites.get(i).getName() + "',"
                    + "'" + sites.get(i).getCountry() + "',"
                    + sites.get(i).getPopulation() + ","
                    + "'" + sites.get(i).getCapital() + "',"
                    + sites.get(i).getArea() + ")");
           }
           stmt.close();
           
        }
        catch(SQLException exp)
        {
            JOptionPane.showMessageDialog(null, "SQL error", "SQL ERROR!", JOptionPane.ERROR_MESSAGE);
        }
        
        System.exit(0);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       readFromTextFile()
     * Description  method that reads the information from the
     *              external text file.
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023
     * @param       textFile String
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void readFromTextFile(String textFile)
    {        
        try
        {            
           FileReader fileReader = new FileReader(textFile);
           BufferedReader input = new BufferedReader(fileReader);
           String line = input.readLine(); // Read the first line.
           
           while(line != null)
           {
               Site tempSite = new Site();
               StringTokenizer token = new StringTokenizer(line, ",");
               while(token.hasMoreElements())
               {
                   tempSite.setID(Integer.parseInt(token.nextToken()));
                   tempSite.setName(token.nextToken());
                   tempSite.setCountry(token.nextToken());
                   tempSite.setPopulation(Float.parseFloat(token.nextToken()));
                   tempSite.setCapital(token.nextToken());
                   tempSite.setArea(Float.parseFloat(token.nextToken()));
               }
               sites.add(tempSite); // Add site to the ArrayList.
               line = input.readLine();
           }
           input.close(); // Close the bufferedReader.
        }
        catch(FileNotFoundException fnfexp)
        {
            JOptionPane.showMessageDialog(null, "Input error -- File not found.",
                    "File Not Found Error!", JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException | NumberFormatException exp)
        {
            JOptionPane.showMessageDialog(null, "Input error -- File could not be read.",
                    "File Read Error!", JOptionPane.ERROR_MESSAGE);
        }
    }  
    
}
