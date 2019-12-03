package com.kopyl.financeorganaizer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kopyl.financeorganaizer.Expense;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.kopyl.financeorganaizer.database.ExpenseDbSchema.*;

public class ExpenseBaseHelper extends SQLiteOpenHelper {
    public ExpenseBaseHelper(@Nullable Context context) {
        super(context,ExpenseTable.NAME,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE " + ExpenseTable.NAME + " ("
                + " id integer primary key autoincrement, " +
                ExpenseTable.Cols.UUID + ", " +
                ExpenseTable.Cols.NAME + ", " +
                ExpenseTable.Cols.COST + ", " +
                ExpenseTable.Cols.IS_COMING + ", " +
                ExpenseTable.Cols.DATE + ")";
        db.execSQL(SQL);

        SQL = "CREATE TABLE " + MonthLimitTable.NAME + " ("
                + " id integer primary key autoincrement, " +
                MonthLimitTable.Cols.MONTH + ", " +
                MonthLimitTable.Cols.YEAR + ", "  +
                MonthLimitTable.Cols.LIMIT + ")";

        db.execSQL(SQL);

        ContentValues item = new ContentValues();
        for(int i = 0; i < 10; i++)
        {
            item.clear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2019);
            calendar.set(Calendar.MONTH, 3);


            if(i%2 == 0)
            {
                calendar.set(Calendar.DAY_OF_MONTH, 12);
                item.put(ExpenseTable.Cols.IS_COMING, false);
            }
            else
            {
                calendar.set(Calendar.DAY_OF_MONTH, 10);
                item.put(ExpenseTable.Cols.IS_COMING, true);
            }
            item.put(ExpenseTable.Cols.UUID, UUID.randomUUID().toString());
            item.put(ExpenseTable.Cols.NAME, "Simple name " + i);
            item.put(ExpenseTable.Cols.COST, i+10.1d);
            item.put(ExpenseTable.Cols.DATE, calendar.getTime().getTime());

            db.insert(ExpenseTable.NAME, null, item);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                String SQL = "CREATE TABLE " + MonthLimitTable.NAME + " ("
                        + " id integer primary key autoincrement, " +
                        MonthLimitTable.Cols.MONTH + ", " +
                        MonthLimitTable.Cols.YEAR + ", "  +
                        MonthLimitTable.Cols.LIMIT + ")";

                db.execSQL(SQL);
                break;
        }
    }
}
