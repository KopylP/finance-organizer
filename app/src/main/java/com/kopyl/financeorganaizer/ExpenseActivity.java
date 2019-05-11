package com.kopyl.financeorganaizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class ExpenseActivity extends AppCompatActivity {
    private static final String UUID_EXTRA_KEY = "com.kopyl.financeorganaizer.uuid extra key";

    private TextView mExpenseName;
    private TextView mExpenseCost;
    private Expense mExpense;
    private TextView mViewDate;
    private Button mEditButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        mExpenseName = findViewById(R.id.expenseNameShow);
        mExpenseCost = findViewById(R.id.expenseCostShow);
        mViewDate = findViewById(R.id.viewDate);
        mEditButton = findViewById(R.id.editButton);

        UUID uuid = UUID.fromString(getIntent().getStringExtra(UUID_EXTRA_KEY));
        mExpense = ExpenseLab.get(this).getExpense(uuid);
        bind();

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ExpenseEditActivity.newIntent(ExpenseActivity.this, mExpense.getId());
                startActivity(intent);
            }
        });
    }

    void bind()
    {
        mExpenseName.setText(mExpense.getName());
        char sign = mExpense.isComing() ? '+' : '-';
        mExpenseCost.setText(sign + Double.toString(mExpense.getCost()));

        int colorg = getResources().getColor(R.color.colorGreen, getTheme());
        int colorr = getResources().getColor(R.color.colorPrimary, getTheme());
        mExpenseCost.setTextColor(mExpense.isComing() ?  colorg : colorr);

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy hh:mm");
        mViewDate.setText(format.format(mExpense.getDate()));
    }
    public static Intent newIntent(Context context, UUID uuid)
    {
        Intent intent = new Intent(context, ExpenseActivity.class);
        intent.putExtra(UUID_EXTRA_KEY, uuid.toString());
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UUID uuid = UUID.fromString(getIntent().getStringExtra(UUID_EXTRA_KEY));
        mExpense = ExpenseLab.get(this).getExpense(uuid);
        bind();
    }
}
