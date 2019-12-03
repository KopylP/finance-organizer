package com.kopyl.financeorganaizer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.math.MathContext;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kopyl.financeorganaizer.fragments.ExpensesDateDialog;
import com.kopyl.financeorganaizer.fragments.LimitPerMonthDialog;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpensesDateDialog.SelectedDate {

    private static final String DATE_DIALOG = "date_dialog";
    private static final String LIMIT_PER_MONTH_DIALOG = "limit_per_month_dialog";
    public static final String PREFERENCES = "com.kopyl.financeorganaizer";

    //private DatePicker mDatePicker;
    private ImageButton mAddExpense;
    private Button mCurrentDateButton;

    private TextView mViewCostPerMonth;
    private TextView mViewCostPerDay;
    private TextView mViewIncPerMonth;
    //private TextView mViewExpPerDay;
    //private TextView mViewExpOnStartDay;
    private TextView mViewLimPerNextDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mDatePicker = findViewById(R.id.datePicker);
        //disableDates();
        mAddExpense = findViewById(R.id.addExpense);

        mCurrentDateButton = findViewById(R.id.current_date_button);

        //mViewExpPerDay = findViewById(R.id.viewExPerDay);
        mViewIncPerMonth = findViewById(R.id.viewIncPerMonth);
        mViewCostPerMonth = findViewById(R.id.viewCostPerMonth);
        mViewCostPerDay = findViewById(R.id.viewCostPerDay);
        //mViewExpOnStartDay = findViewById(R.id.viewExpOnStartDay);
        mViewLimPerNextDays = findViewById(R.id.viewLimPerNextDays);

        mAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ExpenseEditActivity.newIntent(MainActivity.this, null);
                startActivity(intent);
            }
        });

        mCurrentDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = DateTime.getZeroTimedDate(new Date());
                Intent intent = ExpensePagerActivity.newIntent(MainActivity.this, date);
                startActivity(intent);
            }
        });
        checkMonthLimit();
        updateStatistics();
    }



    @Override
    public void selected(Date date) {
        Intent intent = ExpensePagerActivity.newIntent(MainActivity.this, date);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.history:
                FragmentManager fragmentManager = getSupportFragmentManager();

                ExpensesDateDialog dialog = new ExpensesDateDialog();
                dialog.show(fragmentManager, DATE_DIALOG);
                break;
            case R.id.setting:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void updateStatistics()
    {
        double incPerMonth = Statistics.getIncomingPerMonth(this);
        double costPerMonth = Statistics.getCostPerMonth(this);
        double costPerDay = Statistics.getCostsPerDay(this);
        double exPerDay = Statistics.getExpectedExpensePerDay(this);
        incPerMonth = Math.round(incPerMonth * 100.0) / 100.0;
        costPerMonth = Math.round(costPerMonth * 100.0) / 100.0;
        costPerDay = Math.round(costPerDay * 100.0) / 100.0;
        //exPerDay = Math.round(exPerDay * 100.0) / 100.0;



        SharedPreferences preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        //Date date = DateTime.getZeroTimedDate(new Date());


        //String valueStr = preferences.getString(date.toString(), "");
        //double value;
        //if(valueStr.equals("")) {

            //preferences.edit().clear();
            //preferences.edit().putString(date.toString(), Double.toString(exPerDay)).apply();
            //mViewExpOnStartDay.setText("");
            //value = exPerDay;
        //}
       // else {
           // value = Double.parseDouble(valueStr);
        /*TODO*/
        double coming = Statistics.getComingPerDay(this);
        Double exOnStartDay;
        if(coming == 0 && costPerDay == 0) {
            preferences.edit().clear();
            preferences.edit().putFloat("value", (float)exPerDay).apply();
            exOnStartDay = exPerDay;
        }
        else {
            exOnStartDay = (double)preferences.getFloat("value", (float)exPerDay);
            exPerDay = exOnStartDay + coming - costPerDay;
        }
        exPerDay = Math.round(exPerDay * 100.0) / 100.0;
        exOnStartDay = Math.round(exOnStartDay * 100.0) / 100.0;
        //mViewExpOnStartDay.setText(Double.toString(Math.round(exOnStartDay * 100.0) / 100.0));

        //mViewExpPerDay.setText(Double.toString(Math.round(exPerDay * 100.0) / 100.0));
//        if(exPerDay > 0) mViewExpPerDay
//                .setTextColor(getResources().getColor(R.color.colorGreen, getTheme()));
//        else mViewExpPerDay
//                .setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));


        exPerDay = Statistics.getExpectedExpensePerDay(this, 1);
        exPerDay = Math.round(exPerDay * 100.0) / 100.0;

        mViewIncPerMonth.setText(Double.toString(incPerMonth));
        mViewCostPerMonth.setText(Double.toString(costPerMonth));
        mViewCostPerDay.setText(Double.toString(costPerDay));
        mViewLimPerNextDays.setText(Double.toString(exPerDay));

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }

    void checkMonthLimit() {
        MonthLimit limit = ExpenseLab.get(this).getLastMonthLimit();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if(limit == null || (year > limit.getYear() && month > limit.getMonth()))
        {

            FragmentManager fragmentManager = getSupportFragmentManager();

            LimitPerMonthDialog dialog = LimitPerMonthDialog.newInstance(month, year);
            dialog.show(fragmentManager, LIMIT_PER_MONTH_DIALOG);
        }

    }
}
