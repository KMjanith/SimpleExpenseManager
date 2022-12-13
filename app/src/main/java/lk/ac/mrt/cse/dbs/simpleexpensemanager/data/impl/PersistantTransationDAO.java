package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DatabaseHandler;

public class PersistantTransationDAO implements TransactionDAO {

    private final DatabaseHandler dbhelper;

    public PersistantTransationDAO(DatabaseHandler dbhndler){
        this.dbhelper = dbhndler;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        String qry = "SELECT " + dbhelper.getBALANCE()+ " FROM " + dbhelper.getTable_name_1() + " WHERE " + dbhelper.getACCOUNT_NO()+" =?";
        Cursor cursor = db.rawQuery(qry , new String[]{accountNo});
        if(cursor.moveToFirst()){
            double balance = cursor.getDouble(0);
            cursor.close();
            //checking the transaction utilities
            if(expenseType == ExpenseType.INCOME || balance - amount >= 0) {
                ContentValues values = new ContentValues();
                values.put(dbhelper.getDATE(), date.toString());
                values.put(dbhelper.getACCOUNT_NO(), accountNo);
                values.put(dbhelper.getEXPENSE_TYPE(), String.valueOf(expenseType));
                values.put(dbhelper.getAMOUNT(), amount);
                db.insert(dbhelper.getTable_name_2(), null, values);
            }

        }
    }


    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM " + dbhelper.getTable_name_2() + " ORDER BY " + dbhelper.getTransaction_ID() + " desc";
        Cursor cursor = db.rawQuery(query, null);
        List<Transaction> Alllogs = getTransactionList(cursor);
        return Alllogs;
    }


    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT * FROM " + dbhelper.getTable_name_2() + " ORDER BY " + dbhelper.getTransaction_ID() + " desc LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(limit)});
        return getTransactionList(cursor);
    }

    private List<Transaction> getTransactionList(Cursor cursor){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        List<Transaction> transactionList = new ArrayList<>();

        while (cursor.moveToNext()){
            Date date = new Date();
            try {
                date = dateFormat.parse(cursor.getString(1));

            }
            catch(ParseException e ) {
                e.printStackTrace();
            }
            String accountNo = cursor.getString(2);
            try {
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(3));
                double amount = cursor.getDouble(4);
                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactionList.add(transaction);

            }
            catch(Exception e ) {
                e.printStackTrace();
            }

            System.out.println(cursor.getString(3));


        }
        cursor.close();
        return transactionList;
    }


}
