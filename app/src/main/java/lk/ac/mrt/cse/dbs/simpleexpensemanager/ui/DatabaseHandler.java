package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHandler extends SQLiteOpenHelper {

    private  static final String Table_name_1 = "Accounts";
    private  static final String Table_name_2 = "Transactions";

    public static final int VERSION  = 1;
    private static final String DB_name = "Expense_db";


    //column names
    public static final String ACCOUNT_NO = "AccountNo";
    public static final String BANK_NAME = "BankName";
    public static final String HOLDER_NAME = "AccountHolderName";
    public static final String BALANCE  = "Balance";
    public static final String DATE  = "Date";
    public static final String AMOUNT  = "Amount";
    public static final String EXPENSE_TYPE  = "ExpType";
    private static final String TRANSACTION_ID = "Transaction_id";



    public DatabaseHandler(Context context) {
        super(context, DB_name,null,VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TABLE_CREATE_QUERY_1 = "CREATE TABLE IF NOT EXISTS Accounts" +
                " ("
                +ACCOUNT_NO+" TEXT PRIMARY KEY, "
                +BANK_NAME + " TEXT NOT NULL,"
                +HOLDER_NAME + " TEXT NOT NULL,"
                +BALANCE+ " REAL NOT NULL "+
                "CHECK( " +BALANCE + ">=0)" +
                ")";

        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY_1);  //Accounts table

        String TABLE_CREATE_QUERY_2 = "CREATE TABLE IF NOT EXISTS "+Table_name_2 +
                " ("
                +TRANSACTION_ID+" INTEGER PRIMARY KEY, "
                +DATE + " TEXT NOT NULL, "
                +ACCOUNT_NO+" TEXT NOT NULL, "
                +EXPENSE_TYPE + " TEXT NOT NULL, "
                +AMOUNT+ " REAL NOT NULL, " +
                "FOREIGN KEY(" + ACCOUNT_NO + ") REFERENCES " + Table_name_1 + "(" + ACCOUNT_NO + ")" +
                ")";



        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY_2);  //Transaction Table


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE_1 = "DROP TABLE IF EXISTS " + Table_name_1 ;
        String DROP_TABLE_2 = "DROP TABLE IF EXISTS " + Table_name_2 ;
        sqLiteDatabase.execSQL(DROP_TABLE_1);
        sqLiteDatabase.execSQL(DROP_TABLE_2);
        onCreate(sqLiteDatabase);

    }
    public void onDowngrade(SQLiteDatabase db, int i, int i1) {
        onUpgrade(db, i, i1);
    }


    public String getTable_name_1() {
        return Table_name_1;
    }

    public String getTable_name_2() {
        return Table_name_2;
    }

    public String getACCOUNT_NO() {
        return ACCOUNT_NO;
    }

    public String getBANK_NAME() {
        return BANK_NAME;
    }

    public String getHOLDER_NAME() {
        return HOLDER_NAME;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public String getDATE() {
        return DATE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public String getEXPENSE_TYPE() {
        return EXPENSE_TYPE;
    }

    public String getTransaction_ID(){
        return TRANSACTION_ID;
    }

}


