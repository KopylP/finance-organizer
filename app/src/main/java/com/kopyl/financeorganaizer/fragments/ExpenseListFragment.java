package com.kopyl.financeorganaizer.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kopyl.financeorganaizer.Expense;
import com.kopyl.financeorganaizer.ExpenseActivity;
import com.kopyl.financeorganaizer.ExpenseLab;
import com.kopyl.financeorganaizer.ExpensePagerActivity;
import com.kopyl.financeorganaizer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;




public class ExpenseListFragment extends Fragment{


     class ExpenseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;
        private TextView mCost;
        private TextView mSign;
        private TextView mDate;
        private ImageButton mRemove;
        private Expense mExpense;

        public ExpenseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.expense_row, parent, false));
            mName = itemView.findViewById(R.id.name);
            mCost = itemView.findViewById(R.id.cost);
            mSign = itemView.findViewById(R.id.sign);
            mDate = itemView.findViewById(R.id.date);
            mRemove = itemView.findViewById(R.id.remove);

            itemView.setOnClickListener(this);


        }

        public void bind(Expense expense)
        {
            mExpense = expense;
            mName.setText(expense.getName());
            mCost.setText(Double.toString(expense.getCost()));
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            mDate.setText(format.format(expense.getDate()));
            mSign.setText(expense.isComing() ? "+" : "-");

            int colorg = getResources().getColor(R.color.colorGreen, getActivity().getTheme());
            int colorr = getResources().getColor(R.color.colorPrimary, getActivity().getTheme());
            mSign.setTextColor(expense.isComing() ?  colorg : colorr);
            mCost.setTextColor(expense.isComing() ? colorg : colorr);
        }

         @Override
         public void onClick(View v) {
            if(!mDeleteMode) {
                Intent intent = ExpenseActivity.newIntent(getActivity(), mExpense.getId());
                startActivity(intent);
            }
            else {
                ExpenseLab.get(getContext()).removeExpense(mExpense);
                mExpenseAdapter.delete(getAdapterPosition());
            }
         }
     }

    class ExpenseAdapter extends  RecyclerView.Adapter<ExpenseHolder>
    {
        List<Expense> mExpenses;
        private Context mContext;
        public ExpenseAdapter(Context context, List<Expense> expenses)
        {
            mContext = context;
            mExpenses = new ArrayList<>(expenses);
        }

        @NonNull
        @Override
        public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            return new ExpenseHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseHolder expenseHolder, int i) {
            Expense expense = mExpenses.get(i);
            expenseHolder.bind(expense);
        }

        @Override
        public int getItemCount() {
            return mExpenses.size();
        }

        public void delete(int position) {
            mExpenses.remove(position);
            synchronized (this)
            {
                this.notifyItemRemoved(position);
            }
            //notifyItemRangeChanged(position, getItemCount() - position);
        }
    }


    private static final String ARG_EXP_ID ="expense_id";
    private boolean mDeleteMode = false;
    private RecyclerView mExpenseList;
    private List<Expense> mExpenses;
    private Date mDate;
    private ExpenseAdapter mExpenseAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_EXP_ID);
        getExpenses();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_expenselist, container, false);
        mExpenseList =  v.findViewById(R.id.expenseList);
        mExpenseList.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();//Zmiana adaptera
        //mExpenseList.setOnItemClickListener(this);
        //mExpenseList.getAdapter().getItemId(0);
        //--mExpenseList.findViewHolderForAdapterPosition(0).itemView;
        return v;
    }

     public static ExpenseListFragment newInstance(Date expensesDate) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXP_ID, expensesDate);
        ExpenseListFragment fragment = new ExpenseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void getExpenses()
    {

        Calendar calendarSelectedDate = Calendar.getInstance();
        calendarSelectedDate.setTime(mDate);

        mExpenses =  ExpenseLab.get(getActivity()).getExpenses().stream().filter(p-> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(p.getDate());
            return calendarSelectedDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
        }).collect(Collectors
                .toCollection(ArrayList::new));

    }
    private void setAdapter()
    {
//        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
//        for(Expense expense: mExpenses)
//        {
//            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put(COL0, expense.getName());
//            hashMap.put(COL1, Double.toString(expense.getCost()));
//            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//            hashMap.put(COL2, format.format(expense.getDate()).trim());
//            hashMap.put(COL3, expense.isComing() ? "+" : "-");
//            arrayList.add(hashMap);
//        }
//
//        String[] from={COL0, COL1, COL2, COL3};
//        int[] to = {R.id.name, R.id.cost, R.id.date, R.id.sign};
//        SimpleAdapter simpleAdapter =
//                new SimpleAdapter(getActivity(), arrayList, R.layout.expense_row, from, to);
//        mExpenseList.setAdapter(simpleAdapter);
        mExpenseAdapter = new ExpenseAdapter(getContext(), mExpenses);
        mExpenseList.setAdapter(mExpenseAdapter);
        //mExpenseList.setAdapter(new ArrayAdapter<String>((ExpensePagerActivity)getContext().getApplicationContext(), android.R.layout.simple_list_item_1, new String[] {"AA", "BB", "CC"}));
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Expense ex = mExpenses.get(position);
//        Intent intent = ExpenseActivity.newIntent(getActivity(), ex.getId());
//        startActivity(intent);
//    }

    @Override
    public void onResume() {
        super.onResume();
        getExpenses();
        setAdapter();
    }

    public RecyclerView getExpenseList()
    {
        return mExpenseList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_expenselist, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.menu_remove:
                if(!mDeleteMode) {
                    for (int i = 0; i < mExpenseList.getAdapter().getItemCount(); i++) {
                        ImageButton btn = mExpenseList
                                .findViewHolderForAdapterPosition(i)
                                .itemView.findViewById(R.id.remove);
                        btn.setVisibility(View.VISIBLE);
                        item.setTitle(R.string.apply);
                    }
                }
                else{
                    for (int i = 0; i < mExpenseList.getAdapter().getItemCount(); i++) {
                        ImageButton btn = mExpenseList
                                .findViewHolderForAdapterPosition(i)
                                .itemView.findViewById(R.id.remove);
                        btn.setVisibility(View.GONE);
                        item.setTitle(R.string.remove);
                    }
                }
                mDeleteMode = !mDeleteMode;
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
