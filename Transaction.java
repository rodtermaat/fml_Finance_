
/**
 * This class is transaction.  It is composed of various finance related fields
 * And all encapsulated in a object.  This object is then put into a ListArray in the 
 * Menu class which makes up the ledger.  It would be better to have the Transaction class
 * inherit from a new Ledger class, but I am not that smart yet.
 *
 * @author (rod termaat)
 * @version (0.0)
 */
import java.util.Date;

public class Transaction
{
    private final int id;
    private final Date date;
    private final String category;
    private final String name;
    private final int amount;
    private int tbalance;

    //this takes the elements from the Menu class and created the object.  which is then
    //added to the ArrayList in Menu.  This way you can create a table where each
    //row in the ArrayList is a transaction
    public Transaction(int id, Date date, String category, String name, int amount, int tbalance)
    {
        this.id = id;
        this.date = date;
        this.category = category;
        this.name = name;
        this.amount = amount;
        this.tbalance = tbalance;
    }

    public void setTranBalance(int tbalance){
        this.tbalance = tbalance;
    }
    
    public Date getDate(){
        return date;}

    public String getCategory(){
        return category;}
    
    public String getName(){
        return name;}

    public int getAmount(){
        return amount;}
        
    public int gettBalance(){
        return tbalance;}
    
    public int getID(){
        return id;}

}
