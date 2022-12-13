package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransationDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistantMemoryAccount;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DatabaseHandler;

public class PersistantExpenceManager extends ExpenseManager  {

    private DatabaseHandler db;


    public PersistantExpenceManager(Context context) {
        this.db = new DatabaseHandler(context);
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO persisttransactionDAO = new PersistantTransationDAO(db);
        setTransactionsDAO(persisttransactionDAO);

        AccountDAO persistantMemoryAccount = new persistantMemoryAccount(db);
        setAccountsDAO(persistantMemoryAccount);


        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        persistantMemoryAccount.addAccount(dummyAcct1);
        persistantMemoryAccount.addAccount(dummyAcct2);

        /*** End ***/
    }
}
