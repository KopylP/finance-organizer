package com.kopyl.financeorganaizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText mSettingsMonthLimit;
    private Button mSettingsSetMonthLimit;
    private boolean mIsSetMonthLimit;
    private MonthLimit mMonthLimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSettingsMonthLimit = findViewById(R.id.settingsMonthLimit);
        mSettingsSetMonthLimit = findViewById(R.id.settingsSetMonthLimit);

        mMonthLimit = ExpenseLab.get(this).getLastMonthLimit();
        double monthLimit = mMonthLimit.getLimit();
        mSettingsMonthLimit.setText(Double.toString(monthLimit));

        mSettingsSetMonthLimit.setOnClickListener(p -> setMonthLimit());

    }
    void setMonthLimit()
    {
        if(!mIsSetMonthLimit){
            mSettingsSetMonthLimit.setText(R.string.apply);
            mSettingsMonthLimit.setEnabled(true);

        }
        else {
            double limit = Double.parseDouble(mSettingsMonthLimit.getText().toString());
            mMonthLimit.setLimit(limit);
            ExpenseLab.get(this).setMonthLimit(mMonthLimit);
            mSettingsMonthLimit.setEnabled(false);
            mSettingsSetMonthLimit.setText(R.string.set);
            SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
            preferences.edit().clear().apply();
        }
        mIsSetMonthLimit = !mIsSetMonthLimit;
    }
}
