package com.kopyl.financeorganaizer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.kopyl.financeorganaizer.ExpenseLab;
import com.kopyl.financeorganaizer.MonthLimit;
import com.kopyl.financeorganaizer.R;

import java.util.Calendar;
import java.util.Date;

public class LimitPerMonthDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private EditText mEditMonthLimit;
    private static final String YEAR_KEY = "year key";
    private static final String MONTH_KEY = "month key";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.limit_per_month_dialog, null);
        mEditMonthLimit = v.findViewById(R.id.editMonthLimit);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Your month limit")
                .setPositiveButton("OK", this)
                .create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        double limit = Double.parseDouble(mEditMonthLimit.getText().toString());
        int month = getArguments().getInt(MONTH_KEY);
        int year = getArguments().getInt(YEAR_KEY);
        MonthLimit mLimit = new MonthLimit(limit, month, year);
        ExpenseLab.get(getActivity()).addMonthLimit(mLimit);
    }

    public static LimitPerMonthDialog newInstance(int month, int year) {

        Bundle args = new Bundle();
        args.putInt(MONTH_KEY, month);
        args.putInt(YEAR_KEY, year);
        LimitPerMonthDialog fragment = new LimitPerMonthDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
