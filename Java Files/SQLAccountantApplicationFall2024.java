import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;
import com.mysql.cj.jdbc.MysqlDataSource;

public class SQLAccountantApplicationFall2024 extends JFrame
{
    // Declare reference variables for labels
    private JLabel connectionDetailsLabel, dbURLPropertiesLabel, userPropertiesLabel, usernameLabel, 
                   passwordLabel, opsLogPropertiesLabel, accountantPropertiesLabel, enterSQLCommandLabel, sqlExecutionResultLabel; 
    private static JLabel connectionEstablishedLabel; 

    // Declare reference variables for user's text inputs
    private JTextField usernameTextField;

    // Declare reference variable for user's password
    private JPasswordField passwordField;

    // Declare reference variables for buttons
    private JButton connectToDB, discFromDB, clearSQLCommandBtn, executeSQLCommandBtn, clearResultBtn, closeAppBtn;

    // Declate reference variable for text area
    private JTextArea sqlCommandArea;

    // Declare reference variable for results table
    private JTable resultTableWindow;

    // Declare reference variables for event handlers' functions
    private ConnectToDBHandler connectToDBHandler;
    private DiscFromDBHandler discFromDBHandler;
    private ClearSQLCommandHandler clearSQLCommand;
    private ExecuteSQLCommandHandler executeSQLCommand;
    private ClearResultHandler clearResult;
    private CloseAppHandler closeApp;

    // Connection object
    private Connection connection;

    // Keep track of database connection status
    private boolean connectedToDatabase = false;

    // Declare reference variable for result set table model
    private ResultSetTableModel rSetTableModel;

    public SQLAccountantApplicationFall2024()
    {
        // Defines a content pane to hold everything
        Container pane = getContentPane();
        pane.setBackground(Color.LIGHT_GRAY);
        pane.setLayout(null);

        // *************************************************DB Connection Area************************************************************

        // *****************************J-Labels***************************************

        // Defines labels
        connectionDetailsLabel = new JLabel("Connection Details");
        dbURLPropertiesLabel = new JLabel("DB URL PROPERTIES");
        userPropertiesLabel = new JLabel("User Properties");
        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        connectionEstablishedLabel = new JLabel("NO CONNECTION ESTABLISHED");
        opsLogPropertiesLabel = new JLabel("operationslog.properties");
        accountantPropertiesLabel = new JLabel("theaccountant.properties");


        // Styles labels' font color
        connectionDetailsLabel.setForeground(Color.BLUE);
        dbURLPropertiesLabel.setForeground(Color.BLACK);
        userPropertiesLabel.setForeground(Color.BLACK);
        usernameLabel.setForeground(Color.BLACK);
        passwordLabel.setForeground(Color.BLACK);
        connectionEstablishedLabel.setForeground(Color.RED);
        opsLogPropertiesLabel.setForeground(Color.BLACK);
        accountantPropertiesLabel.setForeground(Color.BLACK);

        // Styles labels' font
        connectionDetailsLabel.setFont(new Font("Calibri", Font.BOLD, 18));
        dbURLPropertiesLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        userPropertiesLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        passwordLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        connectionEstablishedLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        opsLogPropertiesLabel.setFont(new Font("Calibri", Font.PLAIN, 16));
        accountantPropertiesLabel.setFont(new Font("Calibri", Font.PLAIN, 16));

        // Styles labels' background color
        dbURLPropertiesLabel.setBackground(Color.GRAY);
        dbURLPropertiesLabel.setOpaque(true);
        userPropertiesLabel.setBackground(Color.GRAY);
        userPropertiesLabel.setOpaque(true);
        usernameLabel.setBackground(Color.GRAY);
        usernameLabel.setOpaque(true);
        passwordLabel.setBackground(Color.GRAY);
        passwordLabel.setOpaque(true);
        connectionEstablishedLabel.setBackground((Color.BLACK));
        connectionEstablishedLabel.setOpaque(true);
        opsLogPropertiesLabel.setBackground(Color.WHITE);
        opsLogPropertiesLabel.setOpaque(true);
        accountantPropertiesLabel.setBackground(Color.WHITE);
        accountantPropertiesLabel.setOpaque(true);

        // Positioning for labels
        connectionDetailsLabel.setBounds(20,20,150,20);
        dbURLPropertiesLabel.setBounds(20, 50, 150, 30);
        userPropertiesLabel.setBounds(20, 90, 150, 30);
        usernameLabel.setBounds(20, 130, 150, 30);
        passwordLabel.setBounds(20, 170, 150, 30);
        connectionEstablishedLabel.setBounds(25, 255, 825, 30);
        opsLogPropertiesLabel.setBounds(175, 50, 245, 30);
        accountantPropertiesLabel.setBounds(175, 90, 245, 30);

        // *****************************J-Labels***************************************

        // *****************************J-Text Fields***************************************

        // Define text fields
        usernameTextField = new JTextField("");
        passwordField = new JPasswordField("");

        // Styles text fields' font
        usernameTextField.setFont(new Font("Calibri", Font.PLAIN, 16));
        passwordField.setFont(new Font("Calibri", Font.PLAIN, 16));

        // Positioning for text fields
        usernameTextField.setBounds(175, 130, 245, 30);
        passwordField.setBounds(175, 170, 245, 30);

        // *****************************J-Text Fields***************************************

        // *****************************J-Buttons***************************************

        // Instantiate buttons and register handlers
        connectToDB = new JButton("Connect to Database");
        connectToDBHandler = new ConnectToDBHandler();
        connectToDB.addActionListener(connectToDBHandler);

        discFromDB = new JButton("Disconnect From Database");
        discFromDBHandler = new DiscFromDBHandler();
        discFromDB.addActionListener(discFromDBHandler);

        // Styles buttons' font
        connectToDB.setFont(new Font("Calibri", Font.BOLD, 14));
        discFromDB.setFont(new Font("Calibri", Font.BOLD, 14));

        // Styles buttons' font color
        connectToDB.setForeground(Color.WHITE);
        discFromDB.setForeground(Color.WHITE);

        // Styles buttons' background color and its borders
        connectToDB.setBackground(Color.BLUE);
        connectToDB.setOpaque(true);
        connectToDB.setBorderPainted(false);
        discFromDB.setBackground(Color.RED);
        discFromDB.setOpaque(true);
        discFromDB.setBorderPainted(false);

        // Positioning for buttons
        connectToDB.setBounds(25, 215, 175, 30);
        discFromDB.setBounds(205, 215, 195, 30);

        // *****************************J-Buttons***************************************

        // *****************************Construct GUI***************************************

        // Adds the labels
        pane.add(connectionDetailsLabel);
        pane.add(dbURLPropertiesLabel);
        pane.add(userPropertiesLabel);
        pane.add(usernameLabel);
        pane.add(passwordLabel);
        pane.add(connectionEstablishedLabel);
        pane.add(opsLogPropertiesLabel);
        pane.add(accountantPropertiesLabel);

        // Adds text fields
        pane.add(usernameTextField);
        pane.add(passwordField);

        // Adds buttons
        pane.add(connectToDB);
        pane.add(discFromDB);

        // *****************************Construct GUI***************************************

        // *************************************************DB Connection Area************************************************************

        // *************************************************SQL Command Window************************************************************

        // *****************************J-Labels***************************************

        // Defines labels
        enterSQLCommandLabel = new JLabel ("Enter A SQL Command");

        // Styles labels' font color
        enterSQLCommandLabel.setForeground(Color.BLUE);

        // Styles labels' font
        enterSQLCommandLabel.setFont(new Font("Calibri", Font.BOLD, 18));

        // Positioning for labels
        enterSQLCommandLabel.setBounds(450,20,200,20);

        // *****************************J-Labels***************************************

        // *****************************J-Text Area***************************************

        // Defines text area
        sqlCommandArea = new JTextArea(20, 20);

        // Styles the content inside text area
        sqlCommandArea.setFont(new Font("Calibri", Font.PLAIN, 14));

        // Allows for line & word wrapping
        sqlCommandArea.setLineWrap(true);
        sqlCommandArea.setWrapStyleWord(true);

        // Defines a scroll pane to scroll in the command window
        JScrollPane sp = new JScrollPane(sqlCommandArea);

        // Creates a box for the text area
        Box sqlCommandBox = Box.createHorizontalBox();
        sqlCommandBox.add(sp);

        // Positioning for text area
        sqlCommandBox.setBounds(450, 45, 420, 130);
        
        // *****************************J-Text Area***************************************

        // *****************************J-Buttons***************************************

        // Instantiate buttons and register handlers
        clearSQLCommandBtn = new JButton("Clear SQL Command");
        clearSQLCommand = new ClearSQLCommandHandler();
        clearSQLCommandBtn.addActionListener(clearSQLCommand);

        executeSQLCommandBtn = new JButton("Execute SQL Command");
        executeSQLCommand = new ExecuteSQLCommandHandler();
        executeSQLCommandBtn.addActionListener(executeSQLCommand);

        // Styles buttons' font
        clearSQLCommandBtn.setFont(new Font("Calibri", Font.BOLD, 14));
        executeSQLCommandBtn.setFont(new Font("Calibri", Font.BOLD, 14));

        // Styles buttons' font color
        clearSQLCommandBtn.setForeground(Color.RED);
        executeSQLCommandBtn.setForeground(Color.BLACK);

        // Styles buttons' background color and its borders
        clearSQLCommandBtn.setBackground(Color.WHITE);
        clearSQLCommandBtn.setOpaque(true);
        clearSQLCommandBtn.setBorderPainted(false);
        executeSQLCommandBtn.setBackground(Color.GREEN);
        executeSQLCommandBtn.setOpaque(true);
        executeSQLCommandBtn.setBorderPainted(false);

        // Positioning for buttons
        clearSQLCommandBtn.setBounds(475, 190, 175, 30);
        executeSQLCommandBtn.setBounds(680, 190, 175, 30);

        // *****************************J-Buttons***************************************

        // *****************************Construct GUI***************************************

        // Adds the labels
        pane.add(enterSQLCommandLabel);

        // Adds the SQL Command Text area
        pane.add(sqlCommandBox);

        // Adds buttons
        pane.add(clearSQLCommandBtn);
        pane.add(executeSQLCommandBtn);

        // *****************************Construct GUI**********************************

        // *************************************************SQL Command Window************************************************************

        // *************************************************SQL Execution Window************************************************************

        // *****************************J-Labels***************************************

        // Defines labels
        sqlExecutionResultLabel = new JLabel ("SQL Execution Result Window");

        // Styles labels' font color
        sqlExecutionResultLabel.setForeground(Color.BLUE);

        // Styles labels' font
        sqlExecutionResultLabel.setFont(new Font("Calibri", Font.BOLD, 18));

        // Positioning for labels
        sqlExecutionResultLabel.setBounds(25,295,225,20);

        // *****************************J-Labels***************************************

        // *****************************J-Table Area***************************************

        // Define table for query results
        resultTableWindow = new JTable();

        // Prevents user from writing into result table
        resultTableWindow.setEnabled(false);

        // Sets grid color to black
        resultTableWindow.setGridColor(Color.BLACK);

        // Defines a scroll pane to scroll in the command window
        JScrollPane sp2 = new JScrollPane(resultTableWindow);

        // Creates a box for the text area
        Box sqlExecuteBox = Box.createHorizontalBox();
        sqlExecuteBox.add(sp2);

        // Positioning for text area
        sqlExecuteBox.setBounds(20, 315, 845, 250);
        
        // *****************************J-Text Area***************************************

        // *****************************J-Buttons***************************************

        // Instantiate buttons and register handlers
        clearResultBtn = new JButton("Clear Result Window");
        clearResult = new ClearResultHandler();
        clearResultBtn.addActionListener(clearResult);

        closeAppBtn = new JButton("Close Application");
        closeApp = new CloseAppHandler();
        closeAppBtn.addActionListener(closeApp);

        // Styles buttons' font
        clearResultBtn.setFont(new Font("Calibri", Font.BOLD, 14));
        closeAppBtn.setFont(new Font("Calibri", Font.BOLD, 14));

        // Styles buttons' font color
        clearResultBtn.setForeground(Color.BLACK);
        closeAppBtn.setForeground(Color.BLACK);

        // Styles buttons' background color and its borders
        clearResultBtn.setBackground(Color.YELLOW);
        clearResultBtn.setOpaque(true);
        clearResultBtn.setBorderPainted(false);
        closeAppBtn.setBackground(Color.RED);
        closeAppBtn.setOpaque(true);
        closeAppBtn.setBorderPainted(false);

        // Positioning for buttons
        clearResultBtn.setBounds(25, 580, 175, 30);
        closeAppBtn.setBounds(680, 580, 175, 30);

        // *****************************J-Buttons***************************************

        // *****************************Construct GUI***************************************

        // Adds the labels
        pane.add(sqlExecutionResultLabel);

        // Adds the SQL Command Text area
        pane.add(sqlExecuteBox);

        // Adds buttons
        pane.add(clearResultBtn);
        pane.add(closeAppBtn);

        // *****************************Construct GUI**********************************

        // *************************************************SQL Execution Window************************************************************
    }

    private class ConnectToDBHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try 
            {
                // If already connected, close previous connection
                if (connection != null)
                {
                    connection.close();
                }

                // Declares and stores user's inputs
                String usernameInput = usernameTextField.getText();
                @SuppressWarnings("deprecation")
                String passwordInput = passwordField.getText();


                // Verifies that neither input field is empty
                if (usernameInput.equals("") || passwordInput.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "No username or password detected", 
                                    "SQLAccountantApplicationFall2024 - CREDENTIALS ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else 
                {
                    // Declares and stores properties selected by user
                    String urlPropertiesSelected = opsLogPropertiesLabel.getText();
                    String userPropertiesSelected = accountantPropertiesLabel.getText();

                    // Creates properties and file stream instances
                    Properties urlProperties = new Properties();
                    Properties userProperties = new Properties();
                    FileInputStream ulrPropertiesFile = null;
                    FileInputStream userPropertiesFile = null;
                    MysqlDataSource dataSource = null;

                    // Read properties files
                    try 
                    {
                        ulrPropertiesFile = new FileInputStream("C:\\Users\\ticop\\OneDrive\\Desktop\\CNT4714\\Project3\\Source_Code\\PropertiesFiles\\" + urlPropertiesSelected);
                        urlProperties.load(ulrPropertiesFile);

                        userPropertiesFile = new FileInputStream("C:\\Users\\ticop\\OneDrive\\Desktop\\CNT4714\\Project3\\Source_Code\\PropertiesFiles\\" + userPropertiesSelected);
                        userProperties.load(userPropertiesFile);

                        // Verifies that the user's input matches the username and password in the property file
                        if (usernameInput.equals(userProperties.getProperty("MYSQL_DB_USERNAME")) && passwordInput.equals(userProperties.getProperty("MYSQL_DB_PASSWORD"))) 
                        {
                            dataSource = new MysqlDataSource();
                            dataSource.setURL(urlProperties.getProperty("MYSQL_DB_URL"));
                            dataSource.setUser(userProperties.getProperty("MYSQL_DB_USERNAME"));
                            dataSource.setPassword(userProperties.getProperty("MYSQL_DB_PASSWORD"));

                            // Establishes connection to the database
                            connection = dataSource.getConnection();

                            // Updates the database connection status
                            connectionEstablishedLabel.setText("CONNECTED TO: " + urlProperties.getProperty("MYSQL_DB_URL"));
                            connectionEstablishedLabel.setForeground(Color.YELLOW);

                            // Updates the database connection status
                            connectedToDatabase = true;
                        } 
                        else
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Username or Password incorrect", "SQLAccountantApplicationFall2024 - CREDENTIALS ERROR", JOptionPane.ERROR_MESSAGE);
                            connectionEstablishedLabel.setText("NOT CONNECTED - USER Credentials Do Not Match Properties File!");
                        }
                    }
                    catch (SQLException sqlException) 
                    {
                        JOptionPane.showMessageDialog(null,
                                    sqlException.getMessage(), "SQLAccountantApplicationFall2024 - DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IOException io) 
                    {
                        JOptionPane.showMessageDialog(null,
                                    io.getMessage(), "SQLAccountantApplicationFall2024 - PROPERTY FILE ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                }
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "SQLAccountantApplicationFall2024 - ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DiscFromDBHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Clears the results displayed in the window
            resultTableWindow.setModel(new DefaultTableModel());

            // Clears the command area
            sqlCommandArea.setText("");

            // Returns connection status label back to its original state
            connectionEstablishedLabel.setText("NO CONNECTION ESTABLISHED");
            connectionEstablishedLabel.setForeground(Color.RED);

            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "SQLAccountantApplicationFall2024 - DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ClearSQLCommandHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Clears the command area
            sqlCommandArea.setText("");
        }
    }

    private class ExecuteSQLCommandHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Ensure database connection is available
            if (!connectedToDatabase) 
            {
                JOptionPane.showMessageDialog(null,
                        "Not Connected to Database", "SQLAccountantApplicationFall2024 - CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            { 
                // Define local variables
                String query = sqlCommandArea.getText();
                int rowsAffected = 0;

                try 
                {
                    // Allows for scrolling of the execution window and to highlight rows
                    resultTableWindow.setAutoscrolls(true);
                    resultTableWindow.setEnabled(true);

                    // Processes query if its is a Select command
                    if (query.toUpperCase().contains("SELECT")) 
                    {
                        rSetTableModel = new ResultSetTableModel(connection, query);
                        rSetTableModel.setQuery(query);
                        resultTableWindow.setModel(rSetTableModel);
                    } 
                    // Processes query for all other commands such as update, insert and delete
                    else 
                    {
                        rSetTableModel = new ResultSetTableModel(connection, query);
                        rowsAffected = rSetTableModel.setUpdate(query);
                        if (rowsAffected != 0) {
                            JOptionPane.showMessageDialog(null, rowsAffected + " row(s) have been updated successfully in the SQL Database", "SQLAccountantApplicationFall2024 - UPDATE SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if (rowsAffected == 0)
                        {
                            JOptionPane.showMessageDialog(null, rowsAffected + " rows were updated.", "SQLAccountantApplicationFall2024 - UPDATE NOTICE", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "SQLAccountantApplicationFall2024 - DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
                } 
            }
        }
    }

    private class ClearResultHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Clears the results displayed in the window
            resultTableWindow.setModel(new DefaultTableModel());
        }
    }

    private class CloseAppHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Shuts down the application
            System.exit(1);
        }
    }

    public static void main(String[] args)
    {
        JFrame SQLAccountantApplication= new SQLAccountantApplicationFall2024(); // Create the frame object
        SQLAccountantApplication.setTitle("SQL Accountant Application - (EPS - CNT 4714 - FALL 2024 - PROJECT 3)"); // Sets the title of the frame
        SQLAccountantApplication.setSize(900,700); // Sets the frame size
        SQLAccountantApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Closes the application if the user clicks upon the "X" icon.
        SQLAccountantApplication.setVisible(true); // Display the frame
    }
}