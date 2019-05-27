package com.example.ahmeda47.gastracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    private TextView mMonth;
    private TextView mGasPrice;
    private TextView mMainPrice;
    private TextView mInsurancePrice;
    private TextView mMiscPrice;
    private TextView mTotal;
    private List<Transaction> transactionMonth;
    private int month;
    private int year;
    private String[] monthNames = {"January","February","March", "April", "May", "June", "July",
                                    "August", "September", "October", "November", "December"};
    String[] costTypes={"ALL","GAS","MAINTENANCE","INSURANCE", "MISCELLANEOUS"};



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mMonth = view.findViewById(R.id.tvMonth);
        mGasPrice = view.findViewById(R.id.tvGasPrice);
        mMainPrice = view.findViewById(R.id.tvMainPrice);
        mInsurancePrice = view.findViewById(R.id.tvInsurancePrice);
        mMiscPrice = view.findViewById(R.id.tvMiscPrice);
        mTotal = view.findViewById(R.id.tvTotalPrice);

        Calendar cal =  Calendar.getInstance();
         month = cal.get(Calendar.MONTH)+1;
         year = cal.get(Calendar.YEAR);

        mMonth.setText(monthNames[month-1]);

        new FetchDatabase1().execute();

        return view;

    }


    private class FetchDatabase1 extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String monthString = ""+month;
            String yearString = ""+year;
            transactionMonth = MainActivity.myAppDatabase.myDao().getTransactionForMonth(monthString,yearString);

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

            for(Transaction transaction: transactionMonth){

                if(transaction.getCostType().compareTo(costTypes[1]) == 0){
                    gasPrice += transaction.getPrice();
                }else if(transaction.getCostType().compareTo(costTypes[2]) == 0){
                    mainPrice += transaction.getPrice();
                }else if(transaction.getCostType().compareTo(costTypes[3]) == 0){
                    insurancePrice += transaction.getPrice();
                }else if(transaction.getCostType().compareTo(costTypes[4]) == 0){
                    miscPrice += transaction.getPrice();
                }
            }
            total = gasPrice + mainPrice + insurancePrice + miscPrice;

            mGasPrice.setText(""+String.format("%.2f", gasPrice));
            mMainPrice.setText(""+String.format("%.2f", mainPrice));
            mInsurancePrice.setText(""+String.format("%.2f", insurancePrice));
            mMiscPrice.setText(""+String.format("%.2f", miscPrice));
            mTotal.setText(""+String.format("%.2f", total));
        }
    }
}


