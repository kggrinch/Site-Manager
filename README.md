# Site-Manager
GUI application for managing a database of famous sites stored in an external SQL repository. The application features functionalities to display, add, edit, delete, and search for site entries. Users can view details such as the site's name, country, population, capital, and area. The program includes sorting capabilities by name and population (ascending) and provides a separate form to showcase each site's picture and a detailed summary. 

Set-up Instructions:
1. Download the Site-Manager-main file into a zip. Site-Manager -> Code -> Download zip
2. Extract the zip file into a folder
3. Download and store the provided mysql-connector-java-8.0.24.jar into a folder.
4. Install the MySQL Server from the MySQL official website. Link provided if needed https://dev.mysql.com/downloads/mysql/
5. Set up a MySQL database server using MySQL Workbench or a preferred management tool. (Note: Remember the username and password for the server)
8. In IntelliJ Open Project -> Choose the extracted Site-Manager-main folder
9. Place mysql-connector-java-8.0.24.jar in the projects library. While in IntelliJ click File -> Project Structure -> Libraries -> + -> Java -> Find and click the saved
   mysql-connector-java-8.0.24.jar file -> apply -> OK
10. Update the MySQLConnection class with your server username and password. Site-Manager-main -> src -> Sites -> MySQLConnection -> Fill in USER and PASS constant strings with your 
    correct server username and password that you should have remembered in step 5.
11. If using the IntelliJ community version download the Database plugin. IntelliJ -> Settings -> Plugins -> Marketplace -> Search and install Database Navigator.
12. In IntelliJ open Database Navigator -> Click + -> MySQL ->
       a) Give it any name and a description if you want. (Could leave description blank)
   
       b) Enter the same server username in step 5
   
       c) Enter the same server password in step 5
   
       d) Test connection -> apply - OK
13. Run the CreateSiteDB class to create the database (This class should be run once manually)
14. Run the SitesGUI class to start the program.

User Instructions    
1. Click on the names of the sites to see information, such as:
      a) Name of Site
      b) Country of Site
      c) Population of Site
      d) Capital of Site
      e) Area
2. Button Features
      a) Add - This will allow the user to add a site to the database and application.
      b) Edit - This will allow the user to edit an existing site from the database.
      c) Delete - This will allow the user to delete an existing site from the database and application.
      e) Exit - This will close the application
3. Help -> About - This will open up a separate application that explains Site-Manager
4. Database Management - This allows users to access the add, edit, and delete buttons. This also allows                            users to search for a site in the database, and open the details application                               which gives a picture of the site with more details.
5. Sort - This will organize the sites either by names (A-Z) or by population (greatest to least)
6. Print - This will print the selected site details
7. Print form - This will print the GUI interface
8. New - This allows the user to choose from three different files of Sites. Choosing a new file will ask           the user to restart the program.

For questions or contributions, please contact me at kirillkongrichanichenko@gmail.com
      



