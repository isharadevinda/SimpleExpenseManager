package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.content.ContentValues;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Ishara on 11/20/2016.
 */
public class InDatabaseTransactionDAO  implements TransactionDAO {



    private Context context;
    private Databaseconnection connectdatabase;

    public InDatabaseTransactionDAO(Context context) {
        this.context = context;
        connectdatabase = Databaseconnection.getDatabase(context);
    }



    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {


        SQLiteDatabase db = connectdatabase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(connectdatabase.Account_No,accountNo);
        values.put(connectdatabase.Log_Date, date.getTime());
        values.put(connectdatabase.Amount, amount);
        values.put(connectdatabase.Expence_Type, expenseType.toString());
        db.insert(connectdatabase.TRANSACTIONTABLENAME , null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogs(0);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = connectdatabase.getReadableDatabase();
        String querySize = String.format("SELECT count(accountNo) FROM %s ",connectdatabase.TRANSACTIONTABLENAME);
        Cursor cursorsize = db.rawQuery(querySize, null);
        int size = cursorsize.getCount();
        String query;
        if(size<=limit){
            query = "SELECT "+ connectdatabase.Account_No + ", " +
                    connectdatabase.Log_Date + ", " +
                    connectdatabase.Expence_Type+", " +
                    connectdatabase.Amount +
                    " FROM " + connectdatabase.TRANSACTIONTABLENAME + " ORDER BY " + connectdatabase.Transaction_Id + " DESC";
        }
        else {
            query = "SELECT "+ connectdatabase.Account_No + ", " +
                    connectdatabase.Log_Date + ", " +
                    connectdatabase.Expence_Type+", " +
                    connectdatabase.Amount +
                    " FROM " + connectdatabase.TRANSACTIONTABLENAME + " ORDER BY " + connectdatabase.Transaction_Id + " DESC LIMIT" + limit;
        }

        Cursor cursor = db.rawQuery(query,null);

        ArrayList<Transaction> transactionLogData = new ArrayList<>();

        while (cursor.moveToNext())
        {
            ExpenseType expenseType = null;
            if(cursor.getString(cursor.getColumnIndex(connectdatabase.Expence_Type)).equals(ExpenseType.INCOME.toString())){
                expenseType = ExpenseType.INCOME;
            }
            else {
                expenseType = ExpenseType.EXPENSE;
            }

            Date date = new Date(cursor.getLong(cursor.getColumnIndex(connectdatabase.Log_Date)));

            Transaction transaction = new Transaction(date,cursor.getString(cursor.getColumnIndex(connectdatabase.Account_No)),
                    expenseType,
                    cursor.getDouble(cursor.getColumnIndex(connectdatabase.Amount))
            );
            transactionLogData.add(transaction);

        }
        return transactionLogData;
    }
}
