
/**
 * The Balance Class keeps track of the current balance of your finances
 * Its pretty simple with just integers
 * 
 * @author (rod termaat)
 * @version (0.02)
 */
public class Balance
{
    // the maim point of this class
    private int balance = 0;
    private int initialBalance = 0;

    //Constructor for objects of class Balance
    public Balance(){}

    
    public int getBalance(){
        return balance;
    }
    
    //sets the inital balance of the ledger.  If the user
    //does not set it. It defaults to 0
    public void setBalance(int balance){
        this.balance = balance;
    }
    
    //reduces the ledger by the amount sent
    //returns a string to display of the new balance
    public String withdraw(int amount){
        balance += amount;
        return "Withdraw amount:  $" + amount + "  New balance:  $" + balance;
    }
    
    //adds money to the ledger and returns
    //a string of the new balance
    public String deposit(int amount){
        balance+=amount;
        return "Transaction amount:  $" + amount + "  New balance:  $" + balance;
    }
    
    public void setNeverChangesBalance(int initialBalance){
        this.initialBalance = initialBalance;
    }
    
    public int getNeverChangesBalance(){
        return initialBalance;
    }
}
