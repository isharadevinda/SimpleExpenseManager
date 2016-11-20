package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Ishara on 11/20/2016.
 */
public class InDatabaseDemoExpenseManager extends ExpenseManager {

    private Context context= null;
    public InDatabaseDemoExpenseManager(Context context) {
        this.context =context;
        setup();
    }

    @Override
    public void setup(){
        AccountDAO DatabaseAccountDAO = new InDatabaseAccountDAO(context);
        setAccountsDAO(DatabaseAccountDAO);

        TransactionDAO DatabaseTransactionDAO = new InDatabaseTransactionDAO(context);
        setTransactionsDAO(DatabaseTransactionDAO );

    }
}
