# Site-Manager
GUI application for managing a database of famous sites stored in an external SQL repository. The application features functionalities to display, add, edit, delete, and search for site entries. Users can view details such as the site's name, country, population, capital, and area. The program includes sorting capabilities by name and population (ascending) and provides a separate form to showcase each site's picture and a detailed summary. 

Set-up Instructions:
1. Download the Site-Manager-main file into a zip. Site-Manager -> Code -> Download zip
2. Extract the zip file into a folder
3. Download and store the provided mysql-connector-java-8.0.24.jar into a folder.
4. Install the MySQL Server from the MySQL official website. Link provided if needed https://dev.mysql.com/downloads/mysql/
5. Set up a MySQL database server using MySQL Workbench or a preferred management tool. (Note: Remember user name and password for the server)
8. Open Project -> Choose the extracted Site-Manager-main folder
9. Place mysql-connector-java-8.0.24.jar in the projects library. While in IntelliJ click File -> Project Structure -> Libraries -> + -> Java -> Find and click the saved
   mysql-connector-java-8.0.24.jar file -> apply -> OK
10. Update MySQLConnection with your server username and password. Site-Manager-main -> src -> Sites -> MySQLConnection -> Fill in USER and PASS constant strings with your correct 
    server username and password that you should have remembered in step 5.
11. Run the CreateSiteDB class to create the database
12. Run the SitesGUI class to start the program.

User Instructions    


