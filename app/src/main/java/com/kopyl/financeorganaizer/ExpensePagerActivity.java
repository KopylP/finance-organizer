package com.kopyl.financeorganaizer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kopyl.financeorganaizer.fragments.ExpenseListFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class ExpensePagerActivity extends AppCompatActivity {

    class ExpensePagerAdapter extends FragmentStatePagerAdapter {

        public ExpensePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            ExpenseListFragment fragment = ExpenseListFragment.newInstance(mDates.get(i));
            return fragment;
        }

        @Override
        public int getCount() {
            return mDates.size();
        }
    }


    private ViewPager mExpensePager;
    private List<Date> mDates;
    private Date mSelectedDate;
    private TextView mDateTextView;
    private TextView mPrevDate;
    private TextView mNextDate;
    private ExpensePagerAdapter mAdapter;
    private static final String DATE_EXTRA = "com.kopyl.financeogranizer.date_extra";
    private SimpleDateFormat format = new  SimpleDateFormat("dd MMMM yyyy");
    private SimpleDateFormat litleFormat = new SimpleDateFormat("dd MMM. yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectedDate = new Date();
        mSelectedDate.setTime(getIntent().getLongExtra(DATE_EXTRA, 0));

        setContentView(R.layout.activity_expense_pager);

        mDateTextView = findViewById(R.id.datePagerTextView);
        mNextDate = findViewById(R.id.nextDateTextView);
        mPrevDate = findViewById(R.id.previosDateTextView);
        mExpensePager = findViewById(R.id.expensePager);
        mDates =  ExpenseLab.get(this).getExpenses().stream().map(p->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(p.getDate());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }).distinct().collect(Collectors
                .toCollection(ArrayList::new));
        mDates.sort(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new ExpensePagerAdapter(fm);
        mExpensePager.setAdapter(mAdapter);
        int i = 0;

        for(; i < mDates.size(); i++)
        {

            if(mDates.get(i).equals(mSelectedDate)){
                mExpensePager.setCurrentItem(i);
                break;
            }
        }
        if(i == mDates.size())
        {
            mExpensePager.setCurrentItem(0);
            Toast.makeText(this, "No Expenses in this date", Toast.LENGTH_SHORT).show();
            finish();
        }


        culcDateViews();
        mExpensePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

                culcDateViews();
            }
        });
        //makeRemoved(true);
    }
    public static Intent newIntent(Context context, Date date)
    {
        Intent intent = new Intent(context, ExpensePagerActivity.class);
        intent.putExtra(DATE_EXTRA, date.getTime());
        return intent;
    }
    private void culcDateViews()
    {
        mSelectedDate = mDates.get( mExpensePager.getCurrentItem() );
        mDateTextView.setText(format.format(mSelectedDate));
        int prevInd  = mExpensePager.getCurrentItem() - 1;
        int nextInd = mExpensePager.getCurrentItem() + 1;

        if(prevInd < 0)
        {
            mPrevDate.setText("");
        }
        else
        {
            mPrevDate.setText("< " + litleFormat.format(mDates.get(prevInd)));
        }
        if(nextInd == mDates.size())
        {
            mNextDate.setText("");
        }
        else
        {
            mNextDate.setText(litleFormat.format(mDates.get(nextInd)) + " >");
        }
    }

}
