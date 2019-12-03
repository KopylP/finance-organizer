package com.kopyl.financeorganaizer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.kopyl.financeorganaizer.DateTime;
import com.kopyl.financeorganaizer.R;

import java.util.Calendar;
import java.util.Date;

import static android.app.AlertDialog.Builder;

public class ExpensesDateDialog extends DialogFragment {
    public interface SelectedDate
    {
        void selected(Date date);
    }
    private SelectedDate mCallbacks;

    private DatePicker mDatePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.expenses_date_dialog, null);
        mDatePicker = v.findViewById(R.id.datePickerInDialog);
        mDatePicker.setMaxDate((new Date()).getTime());

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_date_title)
                .setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, mDatePicker.getYear());
                        calendar.set(Calendar.MONTH, mDatePicker.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
                        calendar.set(Calendar.HOUR, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        //Date date = DateTime.getZeroTimedDate(calendar.getTime());
                        mCallbacks.selected(calendar.getTime());
                    }
                })
                .setNegativeButton("Cencel", null)
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (SelectedDate) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
