
//                   TO DO
//  We no longer need the Balance field in the ledger
//     table as we recasting this in SQL
//  We need to update the initial balance section
//     again since we are building the balance
//     live this just gets in the way
//     We need to load the 1st transaction
//  We need to remove the reCast method
   
/**
 * This is the main driver of the application.  
 * It is the UI and eventually will be converted to 
 * a screen and for Android - which started this little 
 * adventure.  
 * See the read me for more details
 * 
 * @author (rod termaat)
 * @version (version 0.02 of fml finance)
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;

//does the sorting of the arraylist
import java.util.Collections;
import java.util.Comparator;


public class Menu
{
    // instance variables
    
    Scanner keyboard = new Scanner(System.in);
    
    boolean exit;   
            //keeps the program running
    
    int tranID = 0; 
            // unique id for each transaction  
    
    int lstInsertID = 0;

    ArrayList<Transaction> rows = new ArrayList<>();
            // ArrayList of the Transaction object
            // the ledger/checkbook of the application
    
    Transaction row;
            // not sure what this actually does, 
                    
    Balance g_balance = new Balance();
            // created a new Balance object g_balance
            // this is the global balance and always to 
            // current date like your atm balance
            // it is called for all deposits and expenses 
            // with dates of today or previous
    
   Balance t_balance = new Balance();
            // this it the transaction balance 
            // includes all transactions
            // previous, current, and future
                    
    //Balance balance;
            // It does not appear that this is needed here
            // why is it needed above in Transaction?                
    
    SQLite sqlite = new SQLite();
            // created a new object to do db interaction
                    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
    java.util.Date today = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(today.getTime());
    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/YYYY");
    String sqlToday = sdf2.format(sqlDate.getTime());
    
    
    // executes when the program starts                
    public static void Main(String[] args){
        Menu menu = new Menu();
        menu.runMenu();
    }

    // drives the program until exit is true 
    // -set in the printMenu method
    public void runMenu(){
        
        printHeader();
        while(!exit){
            printMenu();
            int choice = getInput();
            performAction(choice);
        }
    }
    
    private void printHeader(){
        System.out.println("---------------------------------");
        System.out.println("|    Welcome to fml Finance.    |");
        System.out.println("|          f ACILITATING        |");
        System.out.println("|          m INIMALIST          |");
        System.out.println("|          l IVING              |");
        System.out.println("---------------------------------");
    }
    
    // Prints out the menu constantly.
    private void printMenu(){
        System.out.println("");
        System.out.println("");
        System.out.println("   Todays date is..." + sqlToday);
        System.out.println("");
        System.out.println("     Please make a selection     ");
        System.out.println("  .............................  ");
        System.out.println("       1) make deposit           ");
        System.out.println("       2) enter expense          ");
        System.out.println("       3) list Transactions      ");
        System.out.println("       4) account setup          ");
        System.out.println("       5) undo last transaction  ");
        System.out.println("       0) exit                   ");
        System.out.println("");
    
    }
    
    // Reads the input of the user from the printMenu above
    // makes sure their choice is valid
    private int getInput(){
        String response;
        int choice = -1;
        do {
            printResponse("...");
            response = askQuestion("Enter a selection...");
        try {
            choice = Integer.parseInt(response);
        }
        catch(NumberFormatException e){
            printResponse("Error(1): Only numbers allowed");
        }
        if(choice < 0 || choice > 7){
            printResponse("Error(2): 1 - 7 only dumbass");
        }
       } while(choice < 0 || choice > 7);
        return choice;
    
    }
    
    // Attempt to simplify the call and response
    // nature of the console app
    // ask a question to the user and 
    // return the reponse string
    private String askQuestion(String question){
        String response = "";
        Scanner input = new Scanner(System.in);
        System.out.print(question);
        response = input.nextLine();
        return response;
    }  
    
    private boolean askYesNo(String question){
        String userInput = "";
        Scanner input = new Scanner(System.in);
        System.out.print(question);
        userInput = input.nextLine();
        if (userInput.equals("y") || userInput.equals("Y")){
            return true;
        }
        else { return false;}
    } 
    
    // generic method to print out a string
    private void printResponse(String response){
        System.out.println("...");
        System.out.println(response);
    }
    
    // Calls the various methods 
    // in response to the users choice
    private void performAction(int choice){
        switch(choice){
            case 0:
                printResponse("Thanks for playing. Now go save some $");
                System.exit(0);
                break;
            case 1:
                addTransaction("Deposit");
                break;
            case 2:
                addTransaction("Expense");
                break;
            case 3:
                listTransactions();
                break;
            case 4:
                sqlite.createDB();
                sqlite.createTranTbl();
                sqlite.createBalTbl();
                initialBalance();
                break;
            case 5:
                delTransaction();
                break;
             default: 
                printResponse("Error(3) this should not happen - fml");
            }
        }
    
    // Updates acctBal table with initial values
    private void initialBalance(){
        java.util.Date date;
       
        // have we already done the initialization?
        int money = sqlite.IsAcctSetup();
        
        if(money == 0){
           int iBalance = Integer.parseInt(askQuestion("Enter and initial balance (0 is aok)..."));
           if(iBalance < 0){ 
               iBalance = iBalance * -1;}
           
           sqlite.initBalance(iBalance, iBalance, iBalance);
           System.out.println("Account initialize with $" + iBalance);
          
          printResponse("Enter a starting date. Its important that this date be earliest entry in the Acct");
          String i_date = askQuestion("Enter Acct Balance date like 01/01/18... ");
          String expectedFormat = "MM/dd/yy";
          SimpleDateFormat formatDate = new SimpleDateFormat(expectedFormat);
        
          try
          {
            date = formatDate.parse(i_date);
            sqlDate = new java.sql.Date(date.getTime());
          }
          catch(ParseException e) {
            printResponse("I said enter date like 04/15/18, " +
                        "Account setup not complete!");
            return;
          }
           
           lstInsertID = sqlite.insertTran(sqlDate, "Account", "Balance", iBalance);
        }
        // prints this if they try to do set up twice.
        else {
            printResponse("Account already seeded, nice try sucker");
        }
    }   
      
    // method to add transactions
    // takes a string Deposit or Expense
    // and uses that to determine how to 
    // appropriately add the record to
    // SQlite
    private void addTransaction(String t_type){
        int t_amount = 0;
        java.util.Date date;
        boolean repeat = false;

        try {
          t_amount = Integer.parseInt(askQuestion("Enter $ amount..."));
        }
        catch(NumberFormatException e) { printResponse("Enter a number silly");
          return;}
        
        if (t_type == "Deposit") {
          if (t_amount < 0){
            t_amount = -1 * t_amount;
          }
        }
        
        if (t_type == "Expense") {
          if (t_amount > 0){
            t_amount = -1 * t_amount;
          }
        }
        
        String category = askQuestion("Enter category... ");
        String name = askQuestion("Enter description... ");
        
        do {
          String i_date = askQuestion("Enter date like 01/01/18... ");
          String expectedFormat = "MM/dd/yy";
          SimpleDateFormat formatDate = new SimpleDateFormat(expectedFormat);
        
          try
          {
            date = formatDate.parse(i_date);
            sqlDate = new java.sql.Date(date.getTime());
          }
          catch(ParseException e) {
            printResponse("I said enter date like 04/15/18 " +
                        "and not some other bogus format");
            return;
          }
          
          if(date.after(today)){
            }
          else{
             // update current balance
             int c_balance = sqlite.checkBal("CURRENT");
             c_balance = c_balance + t_amount;
             sqlite.UpdCurrentBal(c_balance);
            }
          
          int t_balance = sqlite.checkBal("FORECAST");
          t_balance = t_balance + t_amount;
          sqlite.UpdForecastBal(t_balance);
          
          addTransaction(sqlDate, category, name, t_amount);
          repeat = askYesNo("Repeat this transaction?");
        }
          while(repeat);
    
    }
    
    // new transaction method that removes questions within the method
    private void addTransaction(java.sql.Date date, String category, String name, int t_amount){
        
        lstInsertID = sqlite.insertTran(date, category, name, t_amount);
        //System.out.println("Last ID inserted is: " + lstInsertID);
    }
  
    // List out the rows of the Array list to the console. 
    // There is some silly tab (\t) stuff to try 
    // and control the look of the text on the console
    /**
    private void listTransactions(){
        System.out.println("----------------------");
        System.out.println("Your NC balance is...$" + g_balance.getNeverChangesBalance());
        System.out.println("Your Global balance is...$" + g_balance.getBalance());
        System.out.println("Your transaction balance is...$" + t_balance.getBalance());
        System.out.println("----------------------------------");
        System.out.println("Index\t Date\t\t Category\tName\t\tAmount\tBalance");
        System.out.println("---------------------------------------------------------------");
        String num;
        
        //sort the ArrayList before printing
        Collections.sort(rows, new Comparator<Transaction>() {
            public int compare(Transaction t1, Transaction t2) {
                return Long.valueOf(t1.getDate().getTime()).compareTo(t2.getDate().getTime());
            }
        });
        
        // loops thru the tranaction object 
        // and prints out the rows
        for (Transaction printRow : rows)
        {
            String t_cat, t_name;
            if(printRow.getCategory().length() < 7){
               t_cat = "\t\t";}
               else{ t_cat = "\t";}
            if(printRow.getName().length() < 8) {
                t_name = "\t\t";}
                else{ t_name = "\t";}
                
            System.out.println(
                    printRow.getID() + "\t" + 
                    sdf.format(printRow.getDate()) + "\t " +
                    printRow.getCategory() + t_cat +
                    printRow.getName() + t_name +
                    printRow.getAmount() + "\t" +
                    printRow.gettBalance()
                    );

        }
    }
    */
   
   // Remove last transaction. undo
   // need to add SQlite functionality
    private void delTransaction()
    {     
            if (!rows.isEmpty()){
                
            // need to determine if last record is <= today
            // and adjust global balance accordingly
            sqlite.deleteTran(lstInsertID);
            lstInsertID = 0;
            
            rows.remove(rows.get(rows.size()-1));
            //reCastList();
            listTransactions();
        }
    }
    
    // As you add more forecasted transactions, or 
    // delete (undo), or in the future update 
    // amounts you need to recast the balance
    // in the ledger.
    private void reCastList(){
        //step 1 set up variables
        
        //step 2 select all transaction
        //ordered by date acsending
        
        //step 3 update each transaction
        
        
        // get the neverchanges initial balance
        int nc_Balance = g_balance.getNeverChangesBalance();
        int recastedBalance = 0;
        boolean firstTime = false;
        
        //sort the ArrayList before recasting
        if (!rows.isEmpty()){
          Collections.sort(rows, new Comparator<Transaction>() {
              public int compare(Transaction t1, Transaction t2) {
                  return Long.valueOf(t1.getDate().getTime()).compareTo(t2.getDate().getTime());
              }
          });
        
        // recast the arraylist by looping and 
        // updating the transaction balance
          for (Transaction x : rows)
          {
              if (!firstTime){ 
                System.out.println("NC " + nc_Balance + " Tran amt " + x.getAmount());
                recastedBalance = nc_Balance + x.getAmount();    
                // never changes balance + trans amount
                System.out.println("Re-casted: " + recastedBalance);
                x.setTranBalance(recastedBalance);
                firstTime = true;
                
              }
              else{
                recastedBalance = recastedBalance + x.getAmount();     
                // remove transaction amount
                System.out.println("Tran amt " + x.getAmount());
                System.out.println("Re-casted: " + recastedBalance);
                x.setTranBalance(recastedBalance);
                
                
              }
              // here we need to update the transaction balance and set
              // recastedBalance back to 0
              
              //need to set the global balance is date <= today
                if(!x.getDate().after(today)){       
                   // in other words a current transaction
                   g_balance.setBalance(recastedBalance);
                }
              
                t_balance.setBalance(recastedBalance);  
                    // transaction balance
           
            
            
          }
        }
        else {
            // there are no rows, you deleted them all.  
            // reset balances
            g_balance.setBalance(nc_Balance);
            t_balance.setBalance(0);
            recastedBalance = 0;
        }
    }
    
    
    private void listTransactions() {
      rows = sqlite.getAllObjects();
      
      System.out.println("----------------------");
      System.out.println("Your initial balance was...$" + sqlite.checkBal("INITIAL"));
      System.out.println("Your current balance is...$" + sqlite.checkBal("CURRENT"));
      System.out.println("Your forecasted balance will be...$" + sqlite.checkBal("FORECAST"));
      System.out.println("----------------------------------");
      System.out.println("Index\t Date\t\t Category\tName\t\tAmount\tBalance");
      System.out.println("---------------------------------------------------------------");
      
      // loops thru the tranaction object 
      // and prints out the rows
        for (Transaction printRow2 : rows)
        {
            String t_cat, t_name;
            if(printRow2.getCategory().length() < 7){
               t_cat = "\t\t";}
               else{ t_cat = "\t";}
            if(printRow2.getName().length() < 8) {
                t_name = "\t\t";}
                else{ t_name = "\t";}
                
            System.out.println(
                    printRow2.getID() + "\t" + 
                    sdf.format(printRow2.getDate()) + "\t " +
                    printRow2.getCategory() + t_cat +
                    printRow2.getName() + t_name +
                    printRow2.getAmount() + "\t" +
                    printRow2.gettBalance()
                    );

        }
    }
    
    
}