package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.DatabaseHandler;

public class persistantMemoryAccount implements AccountDAO {

    private final DatabaseHandler dbhelper;


    //Constructor
    public persistantMemoryAccount(DatabaseHandler dbhelper) {

        this.dbhelper = dbhelper;
    }

    public List<String> getAccountNumbersList() {
        ArrayList<String> resultSet = new ArrayList<>();

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        //query
        String qry = "SELECT " + dbhelper.getACCOUNT_NO() + " FROM "+ dbhelper.getTable_name_1();

        //execute
        Cursor cursor = db.rawQuery(qry, null);

        while(cursor.moveToNext()) {
            resultSet.add(cursor.getString(0));
        }

        cursor.close();

        return resultSet;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> resultSet = new ArrayList<>();

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        //Query to select all the details about all the accounts in the account table
        String query = "SELECT * FROM " + dbhelper.getTable_name_1();

        Cursor cursor = db.rawQuery(query, null);

        //Add account details to a list
        while (cursor.moveToNext())
        {
            String no = cursor.getString(0);
            String bank_name = cursor.getString(1);
            String holder_name = cursor.getString(2);
            double balance = cursor.getDouble(3);
            Account account = new Account(no,bank_name,holder_name,balance);
            resultSet.add(account);
        }

        cursor.close();

        //Return list of account objects
        return resultSet;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        //query
        String qry = "SELECT* FROM " + dbhelper.getTable_name_1() + " WHERE "+ dbhelper.getACCOUNT_NO() + " =?;";
        Cursor cursor = db.rawQuery(qry, new String[]{accountNo});
        if(cursor.moveToFirst()){
            String no = cursor.getString(0);
            String bank_name = cursor.getString(1);
            String holder_name = cursor.getString(2);
            double balance = cursor.getDouble(3);
            Account account = new Account(no,bank_name,holder_name,balance);
            //Return the account object
            cursor.close();
            return account;
        }
        else {
            cursor.close();
            throw new InvalidAccountException("invalid account number...!");
        }


    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbhelper.getACCOUNT_NO(),account.getAccountNo());
        contentValues.put(dbhelper.getBANK_NAME(),account.getBankName());
        contentValues.put(dbhelper.getHOLDER_NAME(),account.getAccountHolderName());
        contentValues.put(dbhelper.getBALANCE(),account.getBalance());

        db.insert(dbhelper.getTable_name_1(),null,contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long delete = db.delete(dbhelper.getTable_name_1(),dbhelper.getACCOUNT_NO()+ "= ?",new String[]{accountNo});
        if(delete==-1){
            throw new InvalidAccountException("invalid account number...!");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Account account = this.getAccount(accountNo);

        double updatedBalance;
        ContentValues values = new ContentValues();
        switch (expenseType){
            case EXPENSE:
                if (account.getBalance() - amount < 0){
                    throw new InvalidAccountException("Insufficient Balance. Available balance is " + account.getBalance());
                } else {
                    updatedBalance = account.getBalance() - amount;
                    values.put(dbhelper.getBALANCE(), updatedBalance);
                    long updateExpense = db.update(dbhelper.getTable_name_1(), values,dbhelper.getACCOUNT_NO() + " =?", new String[]{accountNo});
                    if (updateExpense == 0) {
                        throw new InvalidAccountException("Database Error");
                    }
                }
                break;

            case INCOME:
                updatedBalance = account.getBalance() + amount;
                values.put(dbhelper.getBALANCE(), updatedBalance);
                long updateIncome = db.update(dbhelper.getTable_name_1(), values,dbhelper.getACCOUNT_NO() + " =?", new String[]{accountNo});

                if (updateIncome == 0) {
                    throw new InvalidAccountException("Database Error");
                }
                break;
        }

    }
}
