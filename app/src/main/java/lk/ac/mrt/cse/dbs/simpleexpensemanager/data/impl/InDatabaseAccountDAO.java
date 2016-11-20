package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import  android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.Databaseconnection;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;



/**
 * Created by Ishara on 11/20/2016.
 */
public class InDatabaseAccountDAO implements AccountDAO {

    private Context context;
    private Databaseconnection connectdatabase;

    public InDatabaseAccountDAO(Context context) {
        this.context = context;
        connectdatabase = Databaseconnection.getDatabase(context);
    }
    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = connectdatabase.getReadableDatabase();

        String query = String.format("SELECT %s FROM %s",connectdatabase.Bank_Account_No,connectdatabase.ACCOUNTTABLENAME);
        Cursor cursor = db.rawQuery(query , null);

        ArrayList<String> result = new ArrayList<String>();

        while(cursor.moveToNext())
        {
            result.add(cursor.getString(cursor.getColumnIndex(connectdatabase.Bank_Account_No)));
        }
        cursor.close();
        return result;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = connectdatabase.getReadableDatabase();

        String query = String.format("SELECT * FROM %s",connectdatabase.ACCOUNTTABLENAME);
        Cursor cursor = db.rawQuery(query , null);

        ArrayList<Account> result = new ArrayList<>();

        while(cursor.moveToNext())
        {
            Account account = new Account(cursor.getString(cursor.getColumnIndex(connectdatabase.Bank_Account_No)),
                    cursor.getString(cursor.getColumnIndex(connectdatabase.Name_of_Bank)),
                    cursor.getString(cursor.getColumnIndex(connectdatabase.Name_of_Account_Holder)),
                    cursor.getDouble(cursor.getColumnIndex(connectdatabase.Final_Balance)));

            result.add(account);
        }
        cursor.close();
        return result;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = connectdatabase.getReadableDatabase();

        String query = "SELECT * FROM " + connectdatabase.ACCOUNTTABLENAME + " WHERE " + connectdatabase.Bank_Account_No + " =  '" + accountNo + "'";

        Cursor cursor = db.rawQuery(query , null);

        Account account = null;

        if(cursor.moveToFirst())
        {
            account = new Account(cursor.getString(cursor.getColumnIndex(connectdatabase.Bank_Account_No)),
                    cursor.getString(cursor.getColumnIndex(connectdatabase.Name_of_Bank)),
                    cursor.getString(cursor.getColumnIndex(connectdatabase.Name_of_Account_Holder)),
                    cursor.getDouble(cursor.getColumnIndex(connectdatabase.Final_Balance)));


        }

        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = connectdatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(connectdatabase.Account_No, account.getAccountNo());
        values.put(connectdatabase.Name_of_Bank, account.getBankName());
        values.put(connectdatabase.Name_of_Account_Holder, account.getAccountHolderName());
        values.put(connectdatabase.Final_Balance, account.getBalance());

        db.insert(connectdatabase.ACCOUNTTABLENAME, null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = connectdatabase.getWritableDatabase();

        String query = "SELECT * FROM " + connectdatabase.ACCOUNTTABLENAME + " WHERE " + connectdatabase.Bank_Account_No + " =  '" + accountNo + "'";

        Cursor cursor = db.rawQuery(query, null);

        Account account = null;


        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(cursor.getColumnIndex(connectdatabase.Bank_Account_No)),
                    cursor.getString(cursor.getColumnIndex(connectdatabase.Name_of_Bank)),
                    cursor.getString(cursor.getColumnIndex(connectdatabase.Name_of_Account_Holder)),
                    cursor.getFloat(cursor.getColumnIndex(connectdatabase.Final_Balance)));
            db.delete(connectdatabase.ACCOUNTTABLENAME, connectdatabase.Bank_Account_No + " = ?", new String[] { accountNo });
            cursor.close();

        }



    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase db = connectdatabase.getWritableDatabase();

        ContentValues values = new ContentValues();

        Account account = getAccount(accountNo);

        if (account!=null) {

            double new_amount=0;

            if (expenseType.equals(ExpenseType.EXPENSE)) {
                new_amount = account.getBalance() - amount;
            }

            else if (expenseType.equals(ExpenseType.INCOME)) {
                new_amount = account.getBalance() + amount;
            }

            String strSQL = "UPDATE "+connectdatabase.ACCOUNTTABLENAME+" SET "+connectdatabase.Final_Balance+" = "+new_amount+" WHERE "+connectdatabase.Bank_Account_No+" = '"+ accountNo+"'";

            db.execSQL(strSQL);

        }


    }
}
