
/**
 * Starting to persist the data by using SQLite
 * 
 * @author (rod termaat)
 * @version (v03)
 */
import java.sql.Date;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.*;
import java.io.File;

public class SQLite
{
    // instance variables - replace the example below with your own
    String url = "jdbc:sqlite:/Users/termaat/sqlite/db/Ledger.db";
    
    /**
     * Constructor for objects of class SQLite
     */
    public SQLite()
    {
        // initialise instance variables

    }

    //create the connection to the Ledger.db
    public Connection connect() {
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Create Connection Error: " + e.getMessage());
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return conn;
    } 
    
    // create the Ledger.db
    // location seems like it should be the same place as the app is installed
    // and not some random location like below
    public void createDB(){
        final File f = new File(url);
        
        if (!f.exists()){
          try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                //System.out.println("The driver name is " + meta.getDriverName());
                //System.out.println("A new database has been created.");
            }
 
          } catch (SQLException e) {
            //System.out.println("Create DB Error: " + e.getMessage());
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          }
       }
    }
    
    // create the ledger table in the Ledger DB
    public void createTranTbl(){ 
        
        String sql = "CREATE TABLE IF NOT EXISTS ledger (\n" +
                     " id INTEGER PRIMARY KEY,\n" +
                     " date DATE,\n" +
                     " category TEXT,\n" +
                     " name TEXT,\n" +
                     " amount INTEGER,\n" +
                     " time DATETIME DEFAULT CURRENT_TIMESTAMP);";
   
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            if(stmt != null){
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            }
            //System.out.println("Ledger table has been created.");            
        } catch (SQLException e) {
            //System.out.println("Create TranTbl Error: " + e.getMessage());
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return;
        }
 
    }
    
    // create the ledger table in the Ledger DB
    // seem like we should have another name so they do not match
    public void createBalTbl(){
        
        String sql = "CREATE TABLE IF NOT EXISTS acctBal (\n" +
                     " id INTEGER PRIMARY KEY,\n" +
                     " initialBal INTEGER,\n" +
                     " currentBal INTEGER,\n" +
                     " forecastBal INTEGER);";
          
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            if(stmt != null){
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            }     
            //System.out.println("Balance table has been created.");            
        } catch (SQLException e) {
            //System.out.println("Create acctTbl error: " + e.getMessage());
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    // add the one and only record into
    // the balance table
    public void initBalance(int initialBal, int currentBal, int forecastBal){
        String sql = "INSERT INTO acctBal(initialBal, currentBal, forecastBal) VALUES(?,?,?)";
    
        
        try (Connection conn = DriverManager.getConnection(url);
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,initialBal );
            pstmt.setInt(2, currentBal);
            pstmt.setInt(3, forecastBal);
            pstmt.executeUpdate(); 
            //System.out.println("Initial Balance added");
            if(pstmt != null){
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            }     
        } catch (SQLException e) {
            //System.out.println("Initial Balance Error: " + e.getMessage());
        }
    
    }
    
    // has the user already set up the account
    // they can only do this once.  After that
    // they need to delete the db and start
    // over.  Need to build this process
    public int IsAcctSetup(){
        String sql="SELECT count(*) from acctBal";
        int count=0;
        try {
           Class.forName("org.sqlite.JDBC");
           Connection conn = DriverManager.getConnection(url);
           conn.setAutoCommit(false);
           //System.out.println("Opened database successfully")
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
            while(rs.next()){
                count=rs.getInt(1);
            }
            
             if(rs != null) {
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            } 
            
        } catch (Exception e) {
        }
        return count;
    }
    
    //            TO DO
    // first we need to check balance
    // next we need to do the calculation
    // finally we update the new balance
    
    public void UpdCurrentBal(int newCurrentBal){
    
    int id = 1;
    String sql = "UPDATE acctBal SET currentBal = ? "
                + "WHERE id = ?";
 
        try (Connection conn = DriverManager.getConnection(url);
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setInt(1, newCurrentBal);
            pstmt.setInt(2, id);
            // update 
            pstmt.executeUpdate();
            //System.out.println("Current Balance updated");
            if(pstmt != null){
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            } 
            } catch (SQLException e) {
            //System.out.println("Update Current Balance Error: " + e.getMessage());
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    public void UpdForecastBal(int newForecastBal){

    int id = 1;
    String sql = "UPDATE acctBal SET forecastBal = ? "
                + "WHERE id = ?";
 
        try (Connection conn = DriverManager.getConnection(url);
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setInt(1, newForecastBal);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            //System.out.println("Forecast Balance updated");
            if(pstmt != null){
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            } 
        } catch (SQLException e) {
            //System.out.println("Update Forecast Balance Error: " + e.getMessage());
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    // add a record to the ledger table
    public int insertTran(Date date, String category, String name, int amount) {
        String sql = "INSERT INTO ledger(date,category,name,amount) VALUES(?,?,?,?)";
        int last_inserted_id = 0;
        
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, date);
            pstmt.setString(2, category);
            pstmt.setString(3, name);
            pstmt.setInt(4, amount);
            pstmt.executeUpdate();
            //System.out.println("Add Transaction successful");
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()){
              last_inserted_id = rs.getInt(1);
            }
            
            if(rs != null) {
                rs.close();
            }
            if(pstmt != null){
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            } 

        } catch (SQLException e) {
            //System.out.println("Insert Transaction Error: " + e.getMessage());
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return last_inserted_id;
        
    }
    
    public void deleteTran(int id) {
        String sql = "DELETE FROM ledger WHERE id = ?";
        //String url = "jdbc:sqlite:/Users/termaat/sqlite/db/Ledger.db";
        
        //try (Connection conn = this.connect();
        try (Connection conn = DriverManager.getConnection(url);
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
           // set the corresponding param
           pstmt.setInt(1, id);
           // execute the delete statement
           pstmt.executeUpdate();
           //System.out.println("Delete Transaction Successful");
            if(pstmt != null){
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            } 

        } catch (SQLException e) {
            //System.out.println("Delete Transaction Error: " + e.getMessage());
        }
    }
    
    // right now this just returns the initalBal, but
    // in the future it would seem like it should
    // return the object Balance?
    public int checkBal(String balType){
        String sql = "SELECT * from acctBal;";
        int initial = 0; int current = 0; int forecast = 0;
        int value = 0;
        try {
           Class.forName("org.sqlite.JDBC");
           Connection conn = DriverManager.getConnection(url);
           conn.setAutoCommit(false);
           //System.out.println("Opened database successfully");
           
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while ( rs.next() ) {
              initial = rs.getInt("initialBal");
              current = rs.getInt("currentBal");
              forecast = rs.getInt("forecastBal");
           }
           //System.out.println("Check Balance successful");
            if(rs != null) {
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            } 

        }
        catch ( Exception e ) {
           //System.out.println("Check Balance Error: " + e.getMessage());
           //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        if(balType == "INITIAL"){ value = initial;}
        if(balType == "CURRENT"){ value = current;}
        if(balType == "FORECAST"){ value = forecast;}
        return value;
    }
    

    //  This gets all the transactions and
    // puts them in an ArrayList object
    // it also created the running total
    // accumulated field and returns as the
    // balance field
    public ArrayList<Transaction> getAllObjects(){
       //String sql = "SELECT * from Ledger ORDER BY date ASC;";
       String sql = "select id, date, time, category, name, amount,\n" +
                    " (select sum(t2.amount) from ledger t2 where\n" +
                    " ((t2.date <= t1.date and t2.time <= t1.time) or\n" +
                    " (t2.date < t1.date))\n" +
                    " order by date ) as accumulated\n" +
                    " from ledger t1 order by date, time;";
       
       //String url = "jdbc:sqlite:/Users/termaat/sqlite/db/Ledger.db";
       //ArrayList<Object> objectList = new ArrayList<Object>();
       ArrayList<Transaction> rows2 = new ArrayList<>();
                    // create an ArrayList of the Transaction object and creates
                    // the ledger/checkbook of the application
       Transaction row2;
                    // not sure what this actually does, but is needed based
                    // on similar sample code I have studied
        try {
           Class.forName("org.sqlite.JDBC");
           Connection conn = DriverManager.getConnection(url);
           conn.setAutoCommit(false);
           //System.out.println("Opened database successfully");
           
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while ( rs.next() ) {
              int id = rs.getInt("id");
              Date date = rs.getDate("date");
              String  category = rs.getString("category");
              String  name = rs.getString("name");
              int amount  = rs.getInt("amount");
              int balance  = rs.getInt("accumulated");
              
              /**
              System.out.println( "ID = " + id );
              System.out.println( "DATE = " + date);
              System.out.println( "CATEGORY = " + category );
              System.out.println( "NAME = " + name );
              System.out.println( "AMOUNT = " + amount);
              System.out.println( "BALANCE = " + balance );
              System.out.println();
              */
             
              //objectList.add(object);
              row2 = new Transaction(id, date, category, name, amount, balance);
              rows2.add(row2);
           }
           
            if(rs != null) {
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            } 
        }
        catch ( Exception e ) {
            //System.out.println("Get ArrayList Error: " + e.getMessage());           
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        
        return rows2;
    }
    
}
