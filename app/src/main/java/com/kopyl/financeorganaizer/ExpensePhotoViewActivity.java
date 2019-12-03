package com.kopyl.financeorganaizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

public class ExpensePhotoViewActivity extends AppCompatActivity {

    private static final String UUID_EXTRA = "uuid extra";

    private File mFile;
    private ImageView mExpensePhotoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_expense_photo_view);

        mExpensePhotoView = findViewById(R.id.expense_photo_view);

        UUID uuid = UUID.fromString(getIntent().getStringExtra(UUID_EXTRA));
        mFile = ExpenseLab.get(this).getFileDir(ExpenseLab.get(this).getExpense(uuid));
        if(mFile.exists()) {
            Picasso.with(this)
                    .load(mFile)
                    .fit()
                    .centerInside()
                    .into(mExpensePhotoView);
        }
    }

    public static Intent newInstance(UUID uuid, Context context) {
        Intent intent = new Intent(context, ExpensePhotoViewActivity.class);
        intent.putExtra(UUID_EXTRA, uuid.toString());
        return intent;
    }

}
