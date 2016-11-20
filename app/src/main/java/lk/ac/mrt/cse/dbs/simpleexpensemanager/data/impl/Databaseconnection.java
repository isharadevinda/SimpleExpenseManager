package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Ishara on 11/20/2016.
 */

public class Databaseconnection extends SQLiteOpenHelper {

    protected static final String Name_OF_DATABASE = "140686T"; //index number
    private static Databaseconnection Connect = null; //to build database connection
    private static final int Version_OF_DATABASE = 1;

    public static final String ACCOUNTTABLENAME = "Accounts"; //account table
    public static final String Bank_Account_No = "accountNo";
    public static final String Name_of_Bank = "bankname";
    public static final String Name_of_Account_Holder = "accountHoldername";
    public static final String Final_Balance = "balance";

    public static final String TRANSACTIONTABLENAME = "Transactions"; //transaction table
    public static final String Transaction_Id = "transactionId";
    public static final String Log_Date = "date";
    public static final String Account_No = "accountNo";
    public static final String Expence_Type = "expenseType";
    public static final String Amount = "amount";

    String SQL_CREATE_TABLE_ACCOUNTTABLENAME = String.format("CREATE TABLE Accounts(Bank_Account_No VARCHAR(20) NOT NULL PRIMARY KEY, Name_of_Bank VARCHAR(100) NULL, accountHoldername VARCHAR(100) NULL, balance FLOAT NULL)"
            );

    String SQL_CREATE_TABLE_TRANSACTIONTABLENAME = String.format("CREATE TABLE Transactionss(transactionId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,accountNo VARCHAR(100) NOT NULL, date DATE NULL, amount FLOAT) NULL,expenseTypes VARCHAR(100) NULL, FOREIGN KEY(accountNo) REFERENCES Accounts(accountNo))"
            );



    public Databaseconnection(Context context) {
        super(context,Name_OF_DATABASE, null, Version_OF_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ACCOUNTTABLENAME);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TRANSACTIONTABLENAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+SQL_CREATE_TABLE_ACCOUNTTABLENAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+SQL_CREATE_TABLE_TRANSACTIONTABLENAME);
        onCreate(sqLiteDatabase);
    }

    public static Databaseconnection getDatabase(Context context){
        if(Connect== null){
            Connect = new Databaseconnection(context);
        }
        return Connect;
    }
}

