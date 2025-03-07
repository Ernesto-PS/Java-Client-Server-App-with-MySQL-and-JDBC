import java.io.*;
import java.sql.*;
import javax.swing.table.*;
import java.util.*;
import com.mysql.cj.jdbc.MysqlDataSource;

public class ResultSetTableModel extends AbstractTableModel 
{
   // Declare global variables
   private Connection connection;
   private Connection accountConnection;
   private Statement statement;
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   private int numberOfRows;
   private String accountUsernameLoggedIn;

   // Keep track of database connection status
   private boolean connectedToDatabase = false;
   
   // Constructor initializes resultSet and obtains its meta data object;
   // Determines number of rows
   public ResultSetTableModel(Connection arrivingConnection, String query ) throws SQLException
   {         
	   try 
      {
  	      // Establish connection to database
   	   this.connection = arrivingConnection;

         DatabaseMetaData metaData = connection.getMetaData(); // Retrieves metadata information about the connection as an object
         accountUsernameLoggedIn = metaData.getUserName(); // stores the username logged in as a string
	
         // Create Statement to query database
         this.statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );

         // Update database connection status
         connectedToDatabase = true;
	   }
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
         System.exit( 1 );
      }  
   }

   // Get class that represents column type
   public Class getColumnClass( int column ) throws IllegalStateException
   {
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // Determine Java class of column
      try 
      {
         String className = metaData.getColumnClassName( column + 1 );
         
         // Return Class object that represents className
         return Class.forName( className );
      }
      catch ( Exception exception ) 
      {
         exception.printStackTrace();
      }
      
      return Object.class; // If problems occur above, assume type Object
   }

   // Get number of columns in ResultSet
   public int getColumnCount() throws IllegalStateException
   {   
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // Determine number of columns
      try 
      {
         return metaData.getColumnCount(); 
      }
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      }
      
      return 0; // If problems occur above, return 0 for number of columns
   }

   // Get name of a particular column in ResultSet
   public String getColumnName( int column ) throws IllegalStateException
   {    
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // Determine column name
      try 
      {
         return metaData.getColumnName( column + 1 );  
      } 
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } 
      
      return ""; // If problems, return empty string for column name
   } 

   // Return number of rows in ResultSet
   public int getRowCount() throws IllegalStateException
   {      
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );
 
      return numberOfRows;
   }

   // Obtain value in particular row and column
   public Object getValueAt( int row, int column ) throws IllegalStateException
   {
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // Obtain a value at specified ResultSet row and column
      try 
      {
		   resultSet.next();  /* Fixes a bug in MySQL/Java with date format */
         resultSet.absolute( row + 1 );
         return resultSet.getObject( column + 1 );
      } 
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } 
      
      return ""; // If problems, return empty string object
   } 
   
   // Set new database query string
   public void setQuery( String query ) throws SQLException, IllegalStateException 
   {
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // Specify query and execute it
      resultSet = statement.executeQuery(query);

      // Obtain meta data for ResultSet
      metaData = resultSet.getMetaData();

      // Determine number of rows in ResultSet
      resultSet.last();                   // Move to last row
      numberOfRows = resultSet.getRow();  // Get row number      
       
      try 
      {
         // Creates properties and file stream instances
         Properties opsLogProperties = new Properties();
         FileInputStream  opsDBFile = null;
         MysqlDataSource dataSource = null;

         // Read properties files
         opsDBFile = new FileInputStream("C:\\Users\\ticop\\OneDrive\\Desktop\\CNT4714\\Project3\\Source_Code\\PropertiesFiles\\project3app.properties");
         opsLogProperties.load(opsDBFile);

         dataSource = new MysqlDataSource();
         dataSource.setURL(opsLogProperties.getProperty("MYSQL_DB_URL"));
         dataSource.setUser(opsLogProperties.getProperty("MYSQL_DB_USERNAME"));
         dataSource.setPassword(opsLogProperties.getProperty("MYSQL_DB_PASSWORD"));

         accountConnection = dataSource.getConnection();
         try 
         {
            String selectQuery = "select * from operationscount where login_username = ?";
            PreparedStatement selectQueryPrepared = accountConnection.prepareStatement(selectQuery);
            selectQueryPrepared.setString(1, accountUsernameLoggedIn);
            ResultSet selectQueryResult = selectQueryPrepared.executeQuery();

            if (selectQueryResult.next()) 
            {
               int numQueries = selectQueryResult.getInt(2);
               String numUpdatesQuery = "update operationscount set num_queries = ? where login_username = ?";
               PreparedStatement numUpdatesQueryPrepared = accountConnection.prepareStatement(numUpdatesQuery);
               numUpdatesQueryPrepared.setInt(1, numQueries + 1);
               numUpdatesQueryPrepared.setString(2, accountUsernameLoggedIn);
               numUpdatesQueryPrepared.executeUpdate();
               numUpdatesQueryPrepared.close();
            } 
            else 
            {
               String insertQuery = "insert into operationscount (login_username, num_queries, num_updates) VALUES (?, ?, ?)";
               PreparedStatement insertQueryPrepared = accountConnection.prepareStatement(insertQuery);
               insertQueryPrepared.setString(1, accountUsernameLoggedIn);
               insertQueryPrepared.setInt(2, 1);
               insertQueryPrepared.setInt(3, 0);
               insertQueryPrepared.executeUpdate();
               insertQueryPrepared.close();
            }
            accountConnection.close();
         } 
         catch (SQLException e) 
         {
            throw new RuntimeException(e);
         }
      } 
      catch (IOException e) 
      {
         e.printStackTrace();
      }

      // Notify JTable that model has changed
      fireTableStructureChanged();
   } 


// Set new database update-query string
   public int setUpdate( String query ) throws SQLException, IllegalStateException 
   {
	   int res = 0;
      // Ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // Specify query and execute it
      res = statement.executeUpdate( query );

      try
      {
         // Creates properties and file stream instances
         Properties opsLogProperties = new Properties();
         FileInputStream  opsDBFile = null;
         MysqlDataSource dataSource = null;

         // Read properties files
         opsDBFile = new FileInputStream("C:\\Users\\ticop\\OneDrive\\Desktop\\CNT4714\\Project3\\Source_Code\\PropertiesFiles\\project3app.properties");
         opsLogProperties.load(opsDBFile);

         dataSource = new MysqlDataSource();
         dataSource.setURL(opsLogProperties.getProperty("MYSQL_DB_URL"));
         dataSource.setUser(opsLogProperties.getProperty("MYSQL_DB_USERNAME"));
         dataSource.setPassword(opsLogProperties.getProperty("MYSQL_DB_PASSWORD"));

         accountConnection = dataSource.getConnection();
         try 
         {
            String selectQuery = "select * from operationscount where login_username = ?";
            PreparedStatement selectQueryPrepared = accountConnection.prepareStatement(selectQuery);
            selectQueryPrepared.setString(1, accountUsernameLoggedIn);
            ResultSet selectQueryResult = selectQueryPrepared.executeQuery();

            if (selectQueryResult.next()) 
            {
               int numQueries = selectQueryResult.getInt(3);
               String numUpdateQuery = "update operationscount set num_updates = ? where login_username = ?";
               PreparedStatement numUpdateQueryPrepared = accountConnection.prepareStatement(numUpdateQuery);
               numUpdateQueryPrepared.setInt(1, numQueries + 1);
               numUpdateQueryPrepared.setString(2, accountUsernameLoggedIn);
               numUpdateQueryPrepared.executeUpdate();
            } 
            else 
            {
               String insertQuery = "insert into operationscount (login_username, num_queries, num_updates) values (?, ?, ?)";
               PreparedStatement insertQueryPrepared = accountConnection.prepareStatement(insertQuery);
               insertQueryPrepared.setString(1, accountUsernameLoggedIn);
               insertQueryPrepared.setInt(2, 0);
               insertQueryPrepared.setInt(3, 1);
               insertQueryPrepared.executeUpdate();
            }
         } 
         catch (SQLException e) 
         {
            System.out.println(e.getMessage());
         }
      }
      catch (IOException e) 
      {
         e.printStackTrace();
      }

      // notify JTable that model has changed
      fireTableStructureChanged();
      return res;
   } 

   // Close Statement and Connection               
   public void disconnectFromDatabase()            
   {              
      if ( !connectedToDatabase )                  
         return;
      // Close Statement and Connection            
      else try                                          
      {                                            
         statement.close();                        
         connection.close();                       
      }                                  
      catch ( SQLException sqlException )          
      {                                            
         sqlException.printStackTrace();           
      }                               
      finally  
      {                                            
         connectedToDatabase = false;  // update database connection status            
      }                             
   }          
}  // end class ResultSetTableModel





