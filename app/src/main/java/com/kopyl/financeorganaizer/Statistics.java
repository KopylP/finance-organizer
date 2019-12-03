package com.kopyl.financeorganaizer;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Statistics {
    public static double getExpectedExpensePerDay(Context context, int i)
    {
        List<MonthLimit> limits = ExpenseLab.get(context).getMonthLimits();
        double allCosts = 0.0, allLimits = 0.0, allComing = 0.0;
        try {
            allLimits = limits.stream().map(p -> p.getLimit()).reduce((x, y) -> x + y).get();
            allComing = ExpenseLab.get(context)
                    .getExpenses()
                    .stream()
                    .filter(p -> p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y).get();

            allCosts = ExpenseLab.get(context)
                    .getExpenses()
                    .stream()
                    .filter(p -> !p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y).get();
        } catch (Exception ex){}

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        calendar.setTime(new Date());
        double days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) - i;
        if(days < 0) days = 1;
        return (allLimits + allComing - allCosts)/days;
    }

    public static double getExpectedExpensePerDay(Context context)
    {
        return getExpectedExpensePerDay(context, 0);
    }

    public static double getIncomingPerMonth(Context context)
    {
        Date date = DateTime.getZeroDayDate(new Date());
        try{
            return ExpenseLab.get(context).getExpenses()
                    .stream()
                    .filter(p -> p.getDate().compareTo(date) > 0)
                    .filter(p -> p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x+y)
                    .get();
        }
        catch (Exception ex){
            return 0.0;
        }

    }

    public static double getCostPerMonth(Context context)
    {
        Date date = DateTime.getZeroDayDate(new Date());
        try {
            return ExpenseLab.get(context).getExpenses()
                    .stream()
                    .filter(p -> p.getDate().compareTo(date) > 0)
                    .filter(p -> !p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y)
                    .get();
        }
        catch (Exception ex){
            return 0.0;
        }
    }

    public static double getCostsPerDay(Context context)
    {
        Date date = DateTime.getZeroTimedDate(new Date());
        try {
            return ExpenseLab.get(context).getExpenses()
                    .stream()
                    .filter(p -> p.getDate().compareTo(date) > 0)
                    .filter(p -> !p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y)
                    .get();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }

    public static double getComingPerDay(Context context)
    {
        Date date = DateTime.getZeroTimedDate(new Date());
        try {
            return ExpenseLab.get(context).getExpenses()
                    .stream()
                    .filter(p -> p.getDate().compareTo(date) > 0)
                    .filter(p -> p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y)
                    .get();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }


    public static double getComingPerAllTime(Context context)
    {
        try {
            return ExpenseLab.get(context).getExpenses()
                    .stream()
                    .filter(p -> p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y)
                    .get();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }

    public static double getCostsPerAllTime(Context context)
    {
        try {
            return ExpenseLab.get(context).getExpenses()
                    .stream()
                    .filter(p -> !p.isComing())
                    .map(p -> p.getCost())
                    .reduce((x, y) -> x + y)
                    .get();
        }
        catch (Exception ex) {
            return 0.0;
        }
    }

}
