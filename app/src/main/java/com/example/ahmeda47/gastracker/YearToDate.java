package com.example.ahmeda47.gastracker;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class YearToDate  extends Fragment {

    private TextView mYear;
    private TextView mGasPrice;
    private TextView mMainPrice;
    private TextView mInsurancePrice;
    private TextView mMiscPrice;
    private TextView mTotal;
    private TextView mCount;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private List<Transaction> transactionYear;
    private int sMonth;
    private int sYear;
    String[] costTypes = {"ALL", "GAS", "MAINTENANCE", "INSURANCE", "MISCELLANEOUS"};


    public YearToDate() {
        //        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ytd, container, false);

        mYear = view.findViewById(R.id.tvYear);
        mGasPrice = view.findViewById(R.id.tvGasPrice);
        mMainPrice = view.findViewById(R.id.tvMainPrice);
        mInsurancePrice = view.findViewById(R.id.tvInsurancePrice);
        mMiscPrice = view.findViewById(R.id.tvMiscPrice);
        mTotal = view.findViewById(R.id.tvTotalPrice);
        mCount = view.findViewById(R.id.displayCount);


        Calendar cal = Calendar.getInstance();
        sMonth = cal.get(Calendar.MONTH) + 1;
        sYear = cal.get(Calendar.YEAR);

        mYear.setText(( "" + sYear));

        new YearToDate.FetchDatabase().execute();

        mYear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day= cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(),
                        android.R.style.Theme_Holo,
                        mDateSetListener,
                        year, month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        getDate();
        return view;

    }
    public void getDate(){
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear.setText(""+year);
                sYear = year;
                new YearToDate.FetchDatabase().execute();
            }
        };
    }

    private class FetchDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String monthString = "" + sMonth;
            String yearString = "" + sYear;
            transactionYear = MainActivity.myAppDatabase.myDao().getTransactionForYear(yearString);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Double gasPrice = 0.0;
            Double mainPrice = 0.0;
            Double insurancePrice = 0.0;
            Double miscPrice = 0.0;
            Double total;

            mCount.setText(""+transactionYear.size());
            for (Transaction transaction : transactionYear) {

                if (transaction.getCostType().compareTo(costTypes[1]) == 0) {
                    gasPrice += transaction.getPrice();
                } else if (transaction.getCostType().compareTo(costTypes[2]) == 0) {
                    mainPrice += transaction.getPrice();
                } else if (transaction.getCostType().compareTo(costTypes[3]) == 0) {
                    insurancePrice += transaction.getPrice();
                } else if (transaction.getCostType().compareTo(costTypes[4]) == 0) {
                    miscPrice += transaction.getPrice();
                }
            }
            total = gasPrice + mainPrice + insurancePrice + miscPrice;

            mGasPrice.setText("" + String.format("%.2f", gasPrice));
            mMainPrice.setText("" + String.format("%.2f", mainPrice));
            mInsurancePrice.setText("" + String.format("%.2f", insurancePrice));
            mMiscPrice.setText("" + String.format("%.2f", miscPrice));
            mTotal.setText("" + String.format("%.2f", total));
        }
    }

}