package com.kopyl.financeorganaizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.kopyl.financeorganaizer.database.ExpenseBaseHelper;
import com.kopyl.financeorganaizer.database.ExpenseDbSchema;
import com.kopyl.financeorganaizer.database.ExpenseDbSchema.ExpenseTable.Cols;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExpenseLab {
    private List<Expense> mExpenses;
    private static ExpenseLab mExpenseLab;

    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static ExpenseLab get(Context context)
    {
        if(mExpenseLab == null)
            mExpenseLab = new ExpenseLab(context);
        return mExpenseLab;
    }

    private ExpenseLab(Context context){
        mExpenses = new ArrayList<>();
        mContext = context;
        mSQLiteDatabase = new ExpenseBaseHelper(mContext).getWritableDatabase();
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        ExpenseCursorWrapper cursor = queryExpense(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return expenses;
    }

    public Expense getExpense(final UUID id)
    {
        Expense expense = null;
        ExpenseCursorWrapper cursor = queryExpense("UUID = ?", new String[] {id.toString()});
        try {
            cursor.moveToFirst();
            expense = cursor.getExpense();
        }
        finally {
            cursor.close();
        }
        return expense;
    }

    public void addExpense(Expense expense)
    {
        ContentValues values = getContentValues(expense);
        mSQLiteDatabase.insert(ExpenseDbSchema.ExpenseTable.NAME, null, values);
    }

    public void updateExpense(Expense expense)
    {
        ContentValues values = getContentValues(expense);
        mSQLiteDatabase.update(ExpenseDbSchema.ExpenseTable.NAME, values, Cols.UUID + "= ?", new String[]{expense.getId().toString()});
    }

    public void removeExpense(Expense expense)
    {
        mSQLiteDatabase.delete(ExpenseDbSchema.ExpenseTable.NAME, Cols.UUID + "= ?", new String[]{expense.getId().toString()});
    }

    public class ExpenseCursorWrapper extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public ExpenseCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Expense getExpense() {
            String uuidString = getString(getColumnIndex(Cols.UUID));
            String name = getString(getColumnIndex(Cols.NAME));
            long date = getLong(getColumnIndex(Cols.DATE));
            double cost = getDouble(getColumnIndex(Cols.COST));
            boolean isComing = getInt(getColumnIndex(Cols.IS_COMING)) == 0 ? false : true;

            return new Expense(UUID.fromString(uuidString), new Date(date), name, cost, isComing);
        }
    }

    public class MonthLimitCursorWrapper extends CursorWrapper{

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public MonthLimitCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public MonthLimit getMonthLimit() {
            double limit = getDouble(getColumnIndex(ExpenseDbSchema.MonthLimitTable.Cols.LIMIT));
            int month = getInt(getColumnIndex(ExpenseDbSchema.MonthLimitTable.Cols.MONTH));
            int year = getInt(getColumnIndex(ExpenseDbSchema.MonthLimitTable.Cols.YEAR));
            return new MonthLimit(limit, month, year);
        }
    }

    private ExpenseCursorWrapper queryExpense(String whereClause, String[] whereArgs)
    {
        Cursor cursor =  mSQLiteDatabase.query(ExpenseDbSchema.ExpenseTable.NAME, null//Wybierajutsa wse stolbcy
            , whereClause, whereArgs, null, null, null);
        ExpenseCursorWrapper wrapper = new ExpenseCursorWrapper(cursor);
        return wrapper;
    }

    private MonthLimitCursorWrapper queryManthLimit (String whereClause, String[] whereArgs)
    {
        Cursor cursor =  mSQLiteDatabase.query(ExpenseDbSchema.MonthLimitTable.NAME, null//Wybierajutsa wse stolbcy
                , whereClause, whereArgs, null, null, null);
        MonthLimitCursorWrapper wrapper = new MonthLimitCursorWrapper(cursor);
        return wrapper;
    }

    private ContentValues getContentValues(Expense expense)
    {
        ContentValues values = new ContentValues();
        values.put(Cols.NAME, expense.getName());
        values.put(Cols.DATE, expense.getDate().getTime());
        values.put(Cols.COST, expense.getCost());
        values.put(Cols.UUID, expense.getId().toString());
        values.put(Cols.IS_COMING, expense.isComing() ? 1: 0);
        return values;
    }

    private ContentValues getContentValues(MonthLimit limit)
    {
        ContentValues values = new ContentValues();
        values.put(ExpenseDbSchema.MonthLimitTable.Cols.LIMIT, limit.getLimit());
        values.put(ExpenseDbSchema.MonthLimitTable.Cols.MONTH, limit.getMonth());
        values.put(ExpenseDbSchema.MonthLimitTable.Cols.YEAR, limit.getYear());
        return values;
    }

    public List<MonthLimit> getMonthLimits()
    {
        MonthLimitCursorWrapper cursor = queryManthLimit(null, null);
        List<MonthLimit> monthLimits = new ArrayList<>();
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                monthLimits.add(cursor.getMonthLimit());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return monthLimits;
    }

    public MonthLimit getLastMonthLimit()
    {
        /* TODO */
        MonthLimitCursorWrapper cursor = queryManthLimit(null, null);
        MonthLimit limit = null;
        try{
            if(cursor.moveToLast())
                limit = cursor.getMonthLimit();
        }
        finally {
            cursor.close();
        }
        return limit;
    }


    public void addMonthLimit(MonthLimit limit)
    {
        ContentValues values = getContentValues(limit);
        mSQLiteDatabase.insert(ExpenseDbSchema.MonthLimitTable.NAME, null, values);
    }

    public void setMonthLimit(MonthLimit limit)
    {
        ContentValues values = getContentValues(limit);
        mSQLiteDatabase.update(ExpenseDbSchema.MonthLimitTable.NAME,
                values,
                ExpenseDbSchema.MonthLimitTable.Cols.MONTH
                        + " = " + limit.getMonth() + " AND "
                        + ExpenseDbSchema.MonthLimitTable.Cols.YEAR
                        + " = " +  limit.getYear() , null);
    }

    public File getFileDir(Expense expense)
    {
        File curDir = mContext.getFilesDir();
        return new File(curDir, expense.getFileName());
    }
}
