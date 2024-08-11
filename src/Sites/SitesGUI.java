package Sites;

import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.*;

/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Class        SitesGUI.java
 * Description  A class representing the GUI used screen used in Vacation Sites Application.
 * Date         10/26/2023     
 * @author      <i>Kirill Grichanichenko</i>
 * @see     	javax.swing.JFrame
 * @see     	java.awt.Toolkit      
 *****************************************************************************/
public class SitesGUI extends javax.swing.JFrame implements MySQLConnection {
    
     // Class instance ArrayList of Site
    private ArrayList<Site> sites = new ArrayList<Site>();
    
    // Class instance of Site
    private Site mySite = new Site();
    // Keep track of currentID and sizeOfDB.
    private int currentID = 1, sizeOfDB;
    
    // External file name for Sites
    private String fileName = "src/Data/sites.txt";

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Constructor  SitesGUI()-Default Constructor
    * Description  Create an instance of GUI form, set the default JButton
    *              to be submitJButton, set icon image, center form, read sites
    *              from external SQL Database.
    * @author      <i>Kirill Grichanichenko</i>
    * Date         10/26/2023             
    *****************************************************************************/
    public SitesGUI() {
        
        try
        {
        initComponents();
        this.getRootPane().setDefaultButton(addJButton); //set addJButton as default
        this.setIconImage(Toolkit.getDefaultToolkit().
                getImage("src/Images/Splash.jpeg"));
        //centers the form at start.
        setLocationRelativeTo(null);
        readSites(fileName);
        
        // Obtain connection to constants from interface
        String url = DB_URL;
        String user = USER;
        String password = PASS;
        
        Connection con = DriverManager.getConnection(url, user, password);
        
        // Keeps the read going forward.
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        // Retrieving the data to get count on how many records are in the DB
        ResultSet rs = stmt.executeQuery("SELECT count(*) FROM SitesBook");
        rs.next();
        
        // Moving the cursor to the last row
        sizeOfDB = rs.getInt("count(*)");
        
        String query = "SELECT * FROM SitesBook";
        rs = stmt.executeQuery(query);
        rs.next(); // Move to first record.
        
        int firstIndex = rs.getInt("siteID");
        Site tempSite = searchSite(firstIndex);
        
        displaySites(tempSite);
        sitesJList.setSelectedIndex(0);
       
        sitesJList.addListSelectionListener(new ListSelectionListener() {
    @Override
    public void valueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            String selectedSiteName = sitesJList.getSelectedValue();
            if (selectedSiteName != null) {
                int siteId = siteID(selectedSiteName);
                Site selectedSite = searchSite(siteId);
                if (selectedSite != null) {
                    showSiteData(selectedSite);
                }
            }
        }
    }
});
        int index = sitesJList.getSelectedIndex();
        if (index >= 0)
        {
        String selectedSiteName = sitesJList.getSelectedValue();
        int siteId = siteID(selectedSiteName);
        Site selectedSite = searchSite(siteId);
        showSiteData(selectedSite);
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(nameJRadioButtonMenuItem);
        buttonGroup.add(populationJRadioButtonMenuItem);
        
        }
        catch(SQLException exp)
        {
            // Show error message
            JOptionPane.showMessageDialog(null, "Input error -- SQL error.", "SQL Error!", JOptionPane.ERROR_MESSAGE);
            exp.printStackTrace();  
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method       readSites
    * Description  Read the players from the external file.
    * @author      <i>Kirill Grichanichenko</i>
    * @param       file String
    * Date         10/26/2023     
    *****************************************************************************/
    public void readSites(String file)
    {
        try
        {            
           FileReader fileReader = new FileReader(file);
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
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method       displaySites
    * Description  Display the sites to sitesJList using
    *              the name sort or population sort.
    * @author      <i>Kirill Grichanichenko</i>
    * Date         10/26/2023     
    * @param       mySite Site
    *****************************************************************************/
    private void displaySites(Site mySite)
    {
        
        // If the nameRadioButton is selected
        if(nameJRadioButtonMenuItem.isSelected())
        {
            try {
        // Obtain connection to constants from interface
        String url = DB_URL;
        String user = USER;
        String password = PASS;

        Connection con = DriverManager.getConnection(url, user, password);

        // Create a statement
        Statement stmt = con.createStatement();

        // Execute a query to get all site details
        ResultSet rs = stmt.executeQuery("SELECT * FROM SitesBook ORDER BY Name ASC");

        // Create an ArrayList to hold the sites
        ArrayList<Site> siteList = new ArrayList<>();

        // Iterate through the ResultSet to retrieve site details
        while (rs.next()) {
            Site site = new Site();
            site.setID(rs.getInt("SiteID"));
            site.setName(rs.getString("Name"));
            site.setCountry(rs.getString("Country"));
            site.setPopulation(rs.getFloat("Population"));
            site.setCapital(rs.getString("Capital"));
            site.setArea(rs.getFloat("Area"));
            siteList.add(site);
        }

        // Update the ArrayList with the new site list
        sites = siteList;

        // Convert the ArrayList of sites to an array of strings
        String[] siteNamesArray = sites.stream().map(Site::getName).toArray(String[]::new);

        sitesJList.setListData(siteNamesArray);
        
        nameJTextField.setText(mySite.getName());
        countryJTextField.setText(mySite.getCountry());
        populationJTextField.setText(String.valueOf(mySite.getPopulation()));
        capitalJTextField.setText(mySite.getCapital());
        areaJTextField.setText(String.valueOf(mySite.getArea()));

        // Close the connection and statement
        rs.close();
        stmt.close();
        con.close();

        } 
        catch (SQLException exp) 
        {
        JOptionPane.showMessageDialog(null, "Input error -- SQL error.", "SQL Error!", JOptionPane.ERROR_MESSAGE);
        } 
    }
        // if the populationJRadioButton is selected.
        else
        {
            try 
            {
                // Obtain connection to constants from interface
                String url = DB_URL;
                String user = USER;
                String password = PASS;

                Connection con = DriverManager.getConnection(url, user, password);

                // Create a statement
                Statement stmt = con.createStatement();
                
                String query = "SELECT * FROM SitesBook ORDER BY Area ASC";
                ResultSet rs = stmt.executeQuery(query);
                
                // Create an ArrayList to hold the sites
                ArrayList<Site> siteList = new ArrayList<>();

                // Iterate through the ResultSet to retrieve site details
                while (rs.next()) {
                Site site = new Site();
                site.setID(rs.getInt("SiteID"));
                site.setName(rs.getString("Name"));
                site.setCountry(rs.getString("Country"));
                site.setPopulation(rs.getFloat("Population"));
                site.setCapital(rs.getString("Capital"));
                site.setArea(rs.getFloat("Area"));
                siteList.add(site);
                }

                // Update the global ArrayList with the new site list
                sites = siteList;

                // Convert the ArrayList of sites to an array of strings
                String[] siteNamesArray = sites.stream().map(Site::getName).toArray(String[]::new);

                sitesJList.setListData(siteNamesArray);
                
                nameJTextField.setText(mySite.getName());
                countryJTextField.setText(mySite.getCountry());
                populationJTextField.setText(String.valueOf(mySite.getPopulation()));
                capitalJTextField.setText(mySite.getCapital());
                areaJTextField.setText(String.valueOf(mySite.getArea()));

                // Close the connection and statement
                rs.close();
                stmt.close();
                con.close();   
            }
            catch(SQLException exp)
            {
                JOptionPane.showMessageDialog(null, "Input error -- SQL error.", "SQL Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        sitesJList.setSelectedIndex(0);
}
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method       searchSite
    * Description  finds a site from the SQL Database using the SiteID and returns the Site.
    * @author      <i>Kirill Grichanichenko</i>
    * @param       id int
    * Date         10/26/2023     
    * @return      mySite Site
    *****************************************************************************/
    private Site searchSite(int id)
    {
        try
        {
            // Obtain connection to constants from interface
            String url = DB_URL;
            String user = USER;
            String password = PASS;
        
            Connection con = DriverManager.getConnection(url, user, password);
            
            mySite = new Site(); // Create new Site
            
            // Set prepared statement query to search for person by id.
            String query = "SELECT * FROM SitesBook WHERE SiteID = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet results = pstmt.executeQuery();
            results.next(); // Move to first record.
            mySite.setID(results.getInt(1));
            mySite.setName(results.getString(2));
            mySite.setCountry(results.getString(3));
            mySite.setPopulation(results.getFloat(4));
            mySite.setCapital(results.getString(5));
            mySite.setArea(results.getFloat(6));
            
            results.close(); // Close the results set
            pstmt.close(); // Close the prepared statement
            con.close(); // Close the connection.
            return mySite;
        }
        catch(SQLException exp)
        {
            // Error message
            JOptionPane.showMessageDialog(null, "Error searching database for Site", "Search Error", JOptionPane.ERROR_MESSAGE);
            return new Site();
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       showSiteData()
     * Description  Display information about selected Site.
     * @parem       selectedSite Site
     * Date         10/26/23
     * @author      <i>Kirill Grichanichenko</i>
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void showSiteData(Site selectedSite) {
    if (selectedSite != null) {
        nameJTextField.setText(selectedSite.getName());
        countryJTextField.setText(selectedSite.getCountry());
        populationJTextField.setText(String.valueOf(selectedSite.getPopulation()));
        capitalJTextField.setText(selectedSite.getCapital());
        areaJTextField.setText(String.valueOf(selectedSite.getArea()));
    }
}
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       exists()
     * Description  Check if parameter-given site exists in the DB. 
     * @param       mySite Site
     * @return      found Boolean
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private boolean exists(Site mySite)
    {
        boolean found = false;
    try {
        // Obtain connection to constants from interface
        String url = DB_URL;
        String user = USER;
        String password = PASS;
        
        Connection con = DriverManager.getConnection(url, user, password);
        
        // Create a statement
        Statement stmt = con.createStatement();

        // Check if a record with the same ID or name already exists
        String query = "SELECT * FROM SitesBook WHERE SiteID = ? OR Name = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, mySite.getID());
        pstmt.setString(2, mySite.getName());
        ResultSet rs = pstmt.executeQuery();

        // If a record is found, set 'found' to true
        if (rs.next()) {
            found = true;
        }

        // Close the connection, statement, and result set
        rs.close();
        pstmt.close();
        stmt.close();
        con.close();
    } catch(SQLException exp) {
        exp.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error in exists.", 
                "Error!", JOptionPane.ERROR_MESSAGE);
    }
    return found;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Method       siteID()
     * Description  Search Site given the name and returns the site's id..
     * Date:        10/26/2023
     * @author      <i>Kirill Grichanichenko</i>
     * @param       name String
     * @return      siteId int
     *
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/ 
    public int siteID(String name)
    {
        int siteId = -1;

        try {
            // Obtain connection to constants from interface
            String url = DB_URL;
            String user = USER;
            String password = PASS;
                
            // Make connection to mySQL DB
            Connection con = DriverManager.getConnection(url, user, password);
            
            PreparedStatement stmt = con.prepareStatement("SELECT siteID FROM SitesBook WHERE Name = ?");
            stmt.setString(1, name);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) 
            {
                siteId = resultSet.getInt("siteID");
            }
        } 
        catch (SQLException exp) 
        {
            JOptionPane.showMessageDialog(null, "Error updating to database", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        return siteId;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleJPanel = new javax.swing.JPanel();
        logoJLabel = new javax.swing.JLabel();
        titleJLabel = new javax.swing.JLabel();
        listJPanel = new javax.swing.JPanel();
        listJScrollPane = new javax.swing.JScrollPane();
        sitesJList = new javax.swing.JList<>();
        displayJPanel = new javax.swing.JPanel();
        nameJLabel = new javax.swing.JLabel();
        nameJTextField = new javax.swing.JTextField();
        countryJLabel = new javax.swing.JLabel();
        countryJTextField = new javax.swing.JTextField();
        populationJLabel = new javax.swing.JLabel();
        populationJTextField = new javax.swing.JTextField();
        capitalJLabel = new javax.swing.JLabel();
        capitalJTextField = new javax.swing.JTextField();
        areaJLabel = new javax.swing.JLabel();
        areaJTextField = new javax.swing.JTextField();
        controlJPanel = new javax.swing.JPanel();
        addJButton = new javax.swing.JButton();
        editJButton = new javax.swing.JButton();
        deleteJButton = new javax.swing.JButton();
        exitJButton = new javax.swing.JButton();
        sitesJMenuBar = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        newJMenuItem = new javax.swing.JMenuItem();
        printJMenuItem = new javax.swing.JMenuItem();
        printFormJMenuItem = new javax.swing.JMenuItem();
        fileJSeparator = new javax.swing.JPopupMenu.Separator();
        exitJMenuItem = new javax.swing.JMenuItem();
        sortJMenu = new javax.swing.JMenu();
        nameJRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        populationJRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        dataBaseManagementJMenu = new javax.swing.JMenu();
        addJMenuItem = new javax.swing.JMenuItem();
        deleteJMenuItem = new javax.swing.JMenuItem();
        editJMenuItem = new javax.swing.JMenuItem();
        searchJMenuItem = new javax.swing.JMenuItem();
        detailsJMenuItem = new javax.swing.JMenuItem();
        HelpJMenu = new javax.swing.JMenu();
        aboutJMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vacation Sites");
        setResizable(false);

        logoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Splash.jpeg"))); // NOI18N

        titleJLabel.setFont(new java.awt.Font("Tahoma", 2, 48)); // NOI18N
        titleJLabel.setForeground(new java.awt.Color(255, 153, 0));
        titleJLabel.setText("Vacation Sites");

        javax.swing.GroupLayout titleJPanelLayout = new javax.swing.GroupLayout(titleJPanel);
        titleJPanel.setLayout(titleJPanelLayout);
        titleJPanelLayout.setHorizontalGroup(
            titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleJPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(logoJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(titleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        titleJPanelLayout.setVerticalGroup(
            titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titleJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        sitesJList.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        sitesJList.setToolTipText("");
        sitesJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                sitesJListValueChanged(evt);
            }
        });
        listJScrollPane.setViewportView(sitesJList);

        javax.swing.GroupLayout listJPanelLayout = new javax.swing.GroupLayout(listJPanel);
        listJPanel.setLayout(listJPanelLayout);
        listJPanelLayout.setHorizontalGroup(
            listJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );
        listJPanelLayout.setVerticalGroup(
            listJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                .addContainerGap())
        );

        displayJPanel.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        nameJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        nameJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameJLabel.setText("Name of Site:");
        displayJPanel.add(nameJLabel);

        nameJTextField.setEditable(false);
        nameJTextField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        nameJTextField.setToolTipText("Name of Site");
        displayJPanel.add(nameJTextField);

        countryJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        countryJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        countryJLabel.setText("Country:");
        displayJPanel.add(countryJLabel);

        countryJTextField.setEditable(false);
        countryJTextField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        countryJTextField.setToolTipText("Country");
        displayJPanel.add(countryJTextField);

        populationJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        populationJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        populationJLabel.setText("Population:");
        displayJPanel.add(populationJLabel);

        populationJTextField.setEditable(false);
        populationJTextField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        populationJTextField.setToolTipText("Population");
        displayJPanel.add(populationJTextField);

        capitalJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        capitalJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        capitalJLabel.setText("Capital:");
        displayJPanel.add(capitalJLabel);

        capitalJTextField.setEditable(false);
        capitalJTextField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        capitalJTextField.setToolTipText("Capital");
        displayJPanel.add(capitalJTextField);

        areaJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        areaJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        areaJLabel.setText("Area:");
        displayJPanel.add(areaJLabel);

        areaJTextField.setEditable(false);
        areaJTextField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        areaJTextField.setToolTipText("Area");
        displayJPanel.add(areaJTextField);

        controlJPanel.setLayout(new java.awt.GridLayout(1, 4, 3, 3));

        addJButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addJButton.setText("Add");
        addJButton.setToolTipText("Add Site");
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(addJButton);

        editJButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        editJButton.setText("Edit");
        editJButton.setToolTipText("Edit Existing Site");
        editJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(editJButton);

        deleteJButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        deleteJButton.setText("Delete");
        deleteJButton.setToolTipText("Delete Site");
        deleteJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(deleteJButton);

        exitJButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        exitJButton.setText("Exit");
        exitJButton.setToolTipText("Exit Program");
        exitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJButtonActionPerformed(evt);
            }
        });
        controlJPanel.add(exitJButton);

        fileJMenu.setMnemonic('F');
        fileJMenu.setText("File");
        fileJMenu.setToolTipText("Open File Menu");

        newJMenuItem.setMnemonic('N');
        newJMenuItem.setText("New");
        newJMenuItem.setToolTipText("Create New Table");
        newJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(newJMenuItem);

        printJMenuItem.setMnemonic('P');
        printJMenuItem.setText("Print");
        printJMenuItem.setToolTipText("Print Site Details");
        printJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(printJMenuItem);

        printFormJMenuItem.setMnemonic('P');
        printFormJMenuItem.setText("Print Form");
        printFormJMenuItem.setToolTipText("Print GUI");
        printFormJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printFormJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(printFormJMenuItem);
        fileJMenu.add(fileJSeparator);

        exitJMenuItem.setMnemonic('x');
        exitJMenuItem.setText("Exit");
        exitJMenuItem.setToolTipText("Exit Program");
        exitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(exitJMenuItem);

        sitesJMenuBar.add(fileJMenu);

        sortJMenu.setMnemonic('S');
        sortJMenu.setText("Sort");
        sortJMenu.setToolTipText("Open Sort Menu");

        nameJRadioButtonMenuItem.setMnemonic('n');
        nameJRadioButtonMenuItem.setSelected(true);
        nameJRadioButtonMenuItem.setText("By name");
        nameJRadioButtonMenuItem.setToolTipText("Sort by Name");
        nameJRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameJRadioButtonMenuItemActionPerformed(evt);
            }
        });
        sortJMenu.add(nameJRadioButtonMenuItem);

        populationJRadioButtonMenuItem.setMnemonic('B');
        populationJRadioButtonMenuItem.setSelected(true);
        populationJRadioButtonMenuItem.setText("By population");
        populationJRadioButtonMenuItem.setToolTipText("Sort by Population");
        populationJRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                populationJRadioButtonMenuItemActionPerformed(evt);
            }
        });
        sortJMenu.add(populationJRadioButtonMenuItem);

        sitesJMenuBar.add(sortJMenu);

        dataBaseManagementJMenu.setMnemonic('t');
        dataBaseManagementJMenu.setText("DataBase Management");
        dataBaseManagementJMenu.setToolTipText("Open DataBase Management Menu");

        addJMenuItem.setMnemonic('A');
        addJMenuItem.setText("Add");
        addJMenuItem.setToolTipText("Add Site");
        addJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJMenuItemActionPerformed(evt);
            }
        });
        dataBaseManagementJMenu.add(addJMenuItem);

        deleteJMenuItem.setMnemonic('D');
        deleteJMenuItem.setText("Delete");
        deleteJMenuItem.setToolTipText("Delete Site");
        deleteJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJMenuItemActionPerformed(evt);
            }
        });
        dataBaseManagementJMenu.add(deleteJMenuItem);

        editJMenuItem.setMnemonic('E');
        editJMenuItem.setText("Edit");
        editJMenuItem.setToolTipText("Edit Existing Site");
        editJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJMenuItemActionPerformed(evt);
            }
        });
        dataBaseManagementJMenu.add(editJMenuItem);

        searchJMenuItem.setMnemonic('r');
        searchJMenuItem.setText("Search");
        searchJMenuItem.setToolTipText("Search Site");
        searchJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchJMenuItemActionPerformed(evt);
            }
        });
        dataBaseManagementJMenu.add(searchJMenuItem);

        detailsJMenuItem.setMnemonic('l');
        detailsJMenuItem.setText("Details");
        detailsJMenuItem.setToolTipText("Site Details");
        detailsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailsJMenuItemActionPerformed(evt);
            }
        });
        dataBaseManagementJMenu.add(detailsJMenuItem);

        sitesJMenuBar.add(dataBaseManagementJMenu);

        HelpJMenu.setMnemonic('H');
        HelpJMenu.setText("Help");
        HelpJMenu.setToolTipText("Open Help Menu");

        aboutJMenuItem.setMnemonic('u');
        aboutJMenuItem.setText("About");
        aboutJMenuItem.setToolTipText("About Form");
        aboutJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutJMenuItemActionPerformed(evt);
            }
        });
        HelpJMenu.add(aboutJMenuItem);

        sitesJMenuBar.add(HelpJMenu);

        setJMenuBar(sitesJMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(listJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(displayJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(controlJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(listJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(displayJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(controlJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       newJMenuItemActionPerformed()
     * Description  Event handler to create a new table.
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023     
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void newJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newJMenuItemActionPerformed
        JFileChooser chooser = new JFileChooser("src/Data");
        // Filter only txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Txt Files", "txt");
        chooser.setFileFilter(filter);
        int choice = chooser.showOpenDialog(null);

    if(choice == JFileChooser.APPROVE_OPTION)
    {
        sites.clear();
        sitesJList.removeAll();
        File chosenFile = chooser.getSelectedFile();
        String file = "src/Data/" + chosenFile.getName();

        // Update fileName
        fileName = file;
        
        JOptionPane.showMessageDialog(null, "Creating new Table. Please reload the sitesGUI.");

        CreateSiteDB createSite = new CreateSiteDB();
        createSite.createTable(fileName);  
    }
    else
    {
        JOptionPane.showMessageDialog(null, "Unable to read file" + "File Input Error" + JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_newJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method        printJMenuItemActionPerformed()
    * Description   Event handler to print selected site details.
    * @param        evt ActionEvent
    * Date          10/26/2023
    * @author       <i>Kirill Grichanichenko</i> 
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void printJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printJMenuItemActionPerformed
        int index = sitesJList.getSelectedIndex();
        JTextArea printSite = new JTextArea();
        printSite.setLineWrap(true);
        printSite.setWrapStyleWord(true);
        if(index >= 0)
        {
            try
            {
                Site temp = new Site(sites.get(index));
                String output = "Site: " + temp.getName() + "\n" +
                        "Country: " + temp.getCountry() + "\n" +
                        "Population: " + temp.getPopulation() + "\n" +
                        "Capital: " + temp.getCapital() + "\n" +
                        "Area: " + temp.getArea();
                printSite.setText(output);
                printSite.print();
            }
            catch(PrinterException exp)
            {
                JOptionPane.showMessageDialog(null, "Site not Printed", "Print Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_printJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method        nameJRadioButtonMenuItemActionPerformed()
    * Description   Event handler for nameJRadioButtonMenuItem to display site
    * @param        evt ActionEvent
    * Date          10/26/2023
    * @author       <i>Kirill Grichanichenko</i> 
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void nameJRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameJRadioButtonMenuItemActionPerformed
        displaySites(mySite); // Might have to change.
    }//GEN-LAST:event_nameJRadioButtonMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       ExitJMenuItemActionPerformed()
     * Description  Event handler to exit the program. 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023    
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void exitJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       aboutJMenuItemActionPerformed()
     * Description  Event handler to bring up the about GUI form. 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023   
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void aboutJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutJMenuItemActionPerformed
        About aboutWindow = new About(this, true);
        aboutWindow.setVisible(true);
    }//GEN-LAST:event_aboutJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       printFormJMenuItemActionPerformed()
     * Description  Event handler to print the GUI form. 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023   
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void printFormJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printFormJMenuItemActionPerformed
         PrintUtilities.printComponent(this);
    }//GEN-LAST:event_printFormJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       exitJButtonActionPerformed()
     * Description  Event handler to exit the program. 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023    
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void exitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitJButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitJButtonActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method        sitesJListValueChanged()
    * Description   Event handler for sitesJListValueChanged to update
    *               information on selected site
    * @param        evt ListSelectionEvent
    * Date          10/26/2023 
    * @author       <i>Kirill Grichanichenko</i> 
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void sitesJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_sitesJListValueChanged
       int index = (sitesJList.getSelectedIndex());
       if(index >= 0)
       {
           showSiteData(mySite);
       }
    }//GEN-LAST:event_sitesJListValueChanged

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * Method        populationJRadioButtonMenuItemActionPerformed()
    * Description   Event handler for populationJRadioButtonMenuItemActionPerformed to display sites
    * @param        evt ActionEvent
    * Date          10/26/2023
    * @author       <i>Kirill Grichanichenko</i> 
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void populationJRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_populationJRadioButtonMenuItemActionPerformed
        displaySites(mySite);
    }//GEN-LAST:event_populationJRadioButtonMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       detailsJMenuItemActionPerformed()
     * Description  Event handler to bring up the site details GUI form. 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023     
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void detailsJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailsJMenuItemActionPerformed
        String siteDetails = sitesJList.getSelectedValue();
        
        if(siteDetails.contains(","))
        {
            siteDetails = siteDetails.substring(0, siteDetails.indexOf(","));
        }
        
        int siteId = siteID(siteDetails);
        Site siteDetailsok = new Site(searchSite(siteId));

        
        int siteIdDetail = siteDetailsok.getID();
        String nameDetail = siteDetailsok.getName();
        String countryDetail = siteDetailsok.getCountry();
        float populationDetail = siteDetailsok.getPopulation();
        String capitalDetail = siteDetailsok.getCapital();
        float areaDetail = siteDetailsok.getArea();
        Details fullSiteDetails = new Details(this, true, siteIdDetail, nameDetail, countryDetail, populationDetail,
                capitalDetail, areaDetail);
        
        fullSiteDetails.setVisible(true); 
    }//GEN-LAST:event_detailsJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       addJMenuItemActionPerformed()
     * Description  Event handler to add a site in to the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023   
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void addJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJMenuItemActionPerformed
        String message = "Site not added";
        
        try
        {
            Add myAddForm = new Add(this, true);
            myAddForm.setVisible(true);
            int lastIndex = 0;
            
            // Get the new Site
            Site newSite = myAddForm.getSite();
            
            if(newSite != null && !exists(newSite))
            {
                // Add new Site to arrayList and DB
                // Obtain connection to constants from interface
                String url = DB_URL;
                String user = USER;
                String password = PASS;
                
                // Make connection to mySQL DB
                Connection con = DriverManager.getConnection(url, user, password);
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String query = "SELECT * FROM SitesBook";
                
                ResultSet rs = stmt.executeQuery(query);
                rs.last(); // Move to last record.
                
                lastIndex = sizeOfDB;
                newSite.setID(lastIndex + 1);
                
                stmt.executeUpdate("INSERT INTO SitesBook VALUES (" + newSite.getID() + ",'" +
                        newSite.getName() + "','" + newSite.getCountry() +
                        "'," + newSite.getPopulation() + ",'" + newSite.getCapital() +
                        "','" + newSite.getArea() + "')");
                sites.add(newSite);
                displaySites(newSite);
                sitesJList.setSelectedValue(newSite.getName(), true);
                sizeOfDB++;
                con.close();
            }
            else
            {
                message = "Site not added";
                newSite.setID(lastIndex);
                displaySites(newSite);
                throw new NullPointerException();
            }
        }
        catch(NullPointerException exp)
        {
         JOptionPane.showMessageDialog(null, message, "Input Error", JOptionPane.WARNING_MESSAGE);   
        }
        catch(SQLException exp)
        {
            JOptionPane.showMessageDialog(null, "Error updating to database", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       addJButtonActionPerformed()
     * Description  Event handler to add a site in to the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023     
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void addJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJButtonActionPerformed
        addJMenuItemActionPerformed(evt);
    }//GEN-LAST:event_addJButtonActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       deleteJMenuItemActionPerformed()
     * Description  Event handler to delete a site from the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023    
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void deleteJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteJMenuItemActionPerformed
        int selectedSiteIndex = siteID(sitesJList.getSelectedValue());
        Site siteToDelete = searchSite(selectedSiteIndex);
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "
            + siteToDelete.getName() + "?", "Delete person", JOptionPane.YES_NO_OPTION);
    if(result == JOptionPane.YES_OPTION) {
        try {
            String url = DB_URL;
            String user = USER;
            String password = PASS;

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String query = "DELETE FROM SitesBook WHERE SiteID = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, selectedSiteIndex);
            pstmt.execute();

            // Get the updated list of sites
            query = "SELECT * FROM SitesBook";
            ResultSet rs = stmt.executeQuery(query);

            // Reassign siteIDs
            int newID = 1;
            while (rs.next()) {
                int oldID = rs.getInt("siteID");
                if (newID != oldID) {
                    query = "UPDATE SitesBook SET siteID = ? WHERE siteID = ?";
                    PreparedStatement updateStmt = con.prepareStatement(query);
                    updateStmt.setInt(1, newID);
                    updateStmt.setInt(2, oldID);
                    updateStmt.execute();
                }
                newID++;
            }

            // Refresh the data in the ArrayList and JList
            readSites(fileName);
            displaySites(searchSite(1));
            con.close();
            sitesJList.setSelectedIndex(0);

        } catch(SQLException exp) {
            JOptionPane.showMessageDialog(null, "Error deleting.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_deleteJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       deleteJButtonActionPerformed()
     * Description  Event handler to delete a site from the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023    
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void deleteJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteJButtonActionPerformed
        deleteJMenuItemActionPerformed(evt);
    }//GEN-LAST:event_deleteJButtonActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       editJMenuItemActionPerformed()
     * Description  Event handler to edit an existing site from the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023    
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void editJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJMenuItemActionPerformed
       String message = "Site not edited";
       try
       {
           currentID = siteID(sitesJList.getSelectedValue());
           mySite = searchSite(currentID);
           
           Edit myEditForm = new Edit(mySite);
           myEditForm.setVisible(true);
           
           Site editSite = myEditForm.getSite();
           editSite.setID(currentID);
           if(editSite != null)
           {
               mySite.setID(currentID);
               mySite.setName(editSite.getName());
               mySite.setCountry(editSite.getCountry());
               mySite.setPopulation(editSite.getPopulation());
               mySite.setCapital(editSite.getCapital());
               mySite.setArea(editSite.getArea());
               
               String url = DB_URL;
               String user = USER;
               String password = PASS;
               
               Connection con = DriverManager.getConnection(url, user, password);
               
               Statement stmt = con.createStatement();
               String query = "UPDATE SitesBook SET Name = " + "'" +
                       mySite.getName() + "', Country = '" +
                       mySite.getCountry() + "', Population = '" +
                       mySite.getPopulation() + "', Capital = '" +
                       mySite.getCapital() + "', Area = '" +
                       mySite.getArea() + "' WHERE siteID = " +
                       mySite.getID();
               stmt.executeUpdate(query);
               
               displaySites(mySite);
               stmt.close();
               con.close();
               sitesJList.setSelectedIndex(0);
           }
           else
           {
               JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
           }
       }
       catch(SQLException exp)
       {
           JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
       }
    }//GEN-LAST:event_editJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       editJButtonActionPerformed()
     * Description  Event handler to edit an existing site from the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023    
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void editJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJButtonActionPerformed
        editJMenuItemActionPerformed(evt);
    }//GEN-LAST:event_editJButtonActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       searchJMenuItemActionPerformed()
     * Description  Event handler to search an existing site from the database 
     * @author      <i>Kirill Grichanichenko</i>
     * Date         10/26/2023     
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private void searchJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchJMenuItemActionPerformed
        String siteName = JOptionPane.showInputDialog("Enter name of Site");
        int index = siteID(siteName);
        if(index == -1)
        {
            if(siteName == null)
            JOptionPane.showMessageDialog(null, "Site Search Cancelled", "Search Result", JOptionPane.WARNING_MESSAGE);

            else
            JOptionPane.showMessageDialog(null, "Site " + siteName + " not found", "Search Result", JOptionPane.WARNING_MESSAGE);
            
            sitesJList.setSelectedIndex(0);
        }
        else
        {
            sitesJList.setSelectedValue(siteName, true);
        }
    }//GEN-LAST:event_searchJMenuItemActionPerformed

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       main()
     * Description  Displays splash screen and the main GUI form.
     * Date         10/26/2023    
     * History log  7/18/2018, 4/3/2020
     * @param       args are the command line strings
     * @author      <i>Kirill Grichanichenko</i>
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SitesGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SitesGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SitesGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SitesGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        // Splash screen
         Splash mySplash = new Splash(4000);
            mySplash.showSplash();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SitesGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu HelpJMenu;
    private javax.swing.JMenuItem aboutJMenuItem;
    private javax.swing.JButton addJButton;
    private javax.swing.JMenuItem addJMenuItem;
    private javax.swing.JLabel areaJLabel;
    private javax.swing.JTextField areaJTextField;
    private javax.swing.JLabel capitalJLabel;
    private javax.swing.JTextField capitalJTextField;
    private javax.swing.JPanel controlJPanel;
    private javax.swing.JLabel countryJLabel;
    private javax.swing.JTextField countryJTextField;
    private javax.swing.JMenu dataBaseManagementJMenu;
    private javax.swing.JButton deleteJButton;
    private javax.swing.JMenuItem deleteJMenuItem;
    private javax.swing.JMenuItem detailsJMenuItem;
    private javax.swing.JPanel displayJPanel;
    private javax.swing.JButton editJButton;
    private javax.swing.JMenuItem editJMenuItem;
    private javax.swing.JButton exitJButton;
    private javax.swing.JMenuItem exitJMenuItem;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.JPopupMenu.Separator fileJSeparator;
    private javax.swing.JPanel listJPanel;
    private javax.swing.JScrollPane listJScrollPane;
    private javax.swing.JLabel logoJLabel;
    private javax.swing.JLabel nameJLabel;
    private javax.swing.JRadioButtonMenuItem nameJRadioButtonMenuItem;
    private javax.swing.JTextField nameJTextField;
    private javax.swing.JMenuItem newJMenuItem;
    private javax.swing.JLabel populationJLabel;
    private javax.swing.JRadioButtonMenuItem populationJRadioButtonMenuItem;
    private javax.swing.JTextField populationJTextField;
    private javax.swing.JMenuItem printFormJMenuItem;
    private javax.swing.JMenuItem printJMenuItem;
    private javax.swing.JMenuItem searchJMenuItem;
    private javax.swing.JList<String> sitesJList;
    private javax.swing.JMenuBar sitesJMenuBar;
    private javax.swing.JMenu sortJMenu;
    private javax.swing.JLabel titleJLabel;
    private javax.swing.JPanel titleJPanel;
    // End of variables declaration//GEN-END:variables
}
