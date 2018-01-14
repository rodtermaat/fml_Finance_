------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: fml Finance (Facilitating Minimal Living)
PURPOSE OF PROJECT:  Create a Java application that mimics my spreadsheet buget and forecast
                     system that I have developed over the years
VERSION or DATE: v01; 01/05/2018
HOW TO START THIS PROJECT:  Call the main method that call runMenu method in the Menu class
AUTHORS: rod termaat
USER INSTRUCTIONS:

Over the years I have developed a system that combines a budget, checkbook, and forecasting of
our $.  It has allowed us to get out of debt and get control over our finances

This is an attempt to write an application for this excel based system.  I was a developer years ago, but
fell into the management bs and lost my skills.  I was a COBOL and .NET developer for many years.
This application is my teaching myself to code again and make something useful - 
at least to me.

v.0
The program allows the user to create an initial balance and then enter transactions
into the ledger.  It list the transaction all as a string and keeps track of the ledger 
balance.

v0.01 enhancements
1- code clean up as I learn more and create methods to allow easier GUI implementation
2- add and actual date into the Transaction instead of a string
3- sort the transactions ascending each time the list is displayed
4- create repeating transactions to be used in forecasting

v0.02 enhancements
1- create a transaction balance that shows previous, current, and forecasted balance in the 
transaction list while the global balance remains true to current date
2- ability to delete transaction. currently only deleted the most recent transaction.
   sort of like an undo function.
3- since you can now delete you need to recast the transaction balance.  This will also be
   needed when you can edit transaction which is the whole point of the program.
   you have forecasted data and eventually it becomes current and might need to be updated.


v0.03
- persist the data in SQLite
- code clean up and consistent

v1 plan is to have it functioning with a Db and simple GUI java app.

v? Android app with server and log on capabilities and non profit finanial group
helping people get from financial hell to something more sustainable.

The way out there end state is a fully functioning financial app that combines
budget, actual and forecasted $.  
