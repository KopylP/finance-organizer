package com.kopyl.financeorganaizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class ExpenseEditActivity extends AppCompatActivity {
    private static final String UUID_EXTRA = "com.kopyl.financeogranizer.uuid_extra";
    private Expense mExpense;

    private EditText mEditName;
    private EditText mEditCost;
    private Button mApplyButton;
    private CheckBox mComingBox;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_edit);

        String uuid = getIntent().getStringExtra(UUID_EXTRA);

        if(!uuid.equals(""))
        {
            isEdit = true;
            mExpense = ExpenseLab.get(this).getExpense(UUID.fromString(uuid));
        }
        else
        {
            isEdit = false;
            mExpense = new Expense();
        }

        mEditCost = findViewById(R.id.editCost);
        mEditName = findViewById(R.id.editName);
        mApplyButton = findViewById(R.id.editApplyButton);
        mComingBox = findViewById(R.id.coming);

        mComingBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) mComingBox.setTextColor(getResources().getColor(R.color.colorGreen, getTheme()));
                else mComingBox.setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
            }
        });


        if(isEdit) bind();

        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpense.setCost(Double.valueOf(mEditCost.getText().toString()));
                mExpense.setName(mEditName.getText().toString());
                mExpense.setComing(mComingBox.isChecked());
                if(isEdit)
                {
                    ExpenseLab.get(ExpenseEditActivity.this).updateExpense(mExpense);
                }
                else
                {
                    ExpenseLab.get(ExpenseEditActivity.this).addExpense(mExpense);
                }
                finish();
            }
        });
    }
    public static Intent newIntent(Context context, UUID uuid)
    {
        Intent intent = new Intent(context, ExpenseEditActivity.class);
        if(uuid == null)
            intent.putExtra(UUID_EXTRA, "");
        else
            intent.putExtra(UUID_EXTRA, uuid.toString());
        return intent;
    }

    private void bind()
    {
        mEditName.setText( mExpense.getName() );
        mEditCost.setText( Double.toString(mExpense.getCost()) );
        mComingBox.setChecked(mExpense.isComing());
    }

}
