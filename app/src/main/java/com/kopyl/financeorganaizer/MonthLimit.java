package com.kopyl.financeorganaizer;

import java.util.Calendar;
import java.util.Date;

public class MonthLimit {
    private int mYear;
    private int mMonth;
    private double mLimit;

    public MonthLimit() {}
    public MonthLimit(double limit, int month, int year)
    {
        mLimit = limit;
        mMonth = month;
        mYear = year;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public double getLimit() {
        return mLimit;
    }

    public void setLimit(double limit) {
        mLimit = limit;
    }

    public Date getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        return DateTime.getZeroDayDate(calendar.getTime());
    }


}
