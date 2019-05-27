package com.example.ahmeda47.gastracker;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;

import static android.widget.Toast.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTransaction extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button mSaveButton;
    private EditText mPrice;
    private Spinner mSpin;
    private Spinner mSpinCost;
    private String spinText;
    private Transaction transaction;
    String[] paymentTypes={"CREDIT", "CASH"};
    String[] costTypes={"GAS","MAINTENANCE","INSURANCE", "MISCELLANEOUS"};

    public AddTransaction() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        mSaveButton = (Button) view.findViewById(R.id.btSave);
        mPrice = (EditText) view.findViewById(R.id.evPrice);
        mSpin = (Spinner) view.findViewById(R.id.spPaymentType);
        mSpinCost = (Spinner) view.findViewById(R.id.spCostType);
//Getting the instance of Spinner and applying OnItemSelectedListener on it
        mDisplayDate = (TextView) view.findViewById(R.id.tvDate);
        mDisplayDate.setText(getToday(0));
        mDisplayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day= cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(view.getContext(),
                        android.R.style.Theme_Holo,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mPrice.getText().length() == 0) {
                    Toast.makeText(view.getContext(), "Must enter Price", LENGTH_LONG).show();
                } else {
                    new FetchDatabase2().execute();
                }
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        mSpin.setOnItemSelectedListener(this);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(view.getContext(),android.R.layout.simple_spinner_item,paymentTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        mSpin.setAdapter(aa);

        ArrayAdapter bb = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, costTypes);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinCost.setAdapter(bb);
        return view;
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        spinText = paymentTypes[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }
    public String getToday(int flip){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);

        month++;
        if(flip == 0) {
            return month + "/" + day + "/" + year;
        }
        else{
            return month+"/"+year;
        }
    }

    private class FetchDatabase2 extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("SAVING", mPrice.getText() + "/" + spinText + "/" + mDisplayDate.getText() + "/" + costTypes[mSpinCost.getSelectedItemPosition()]);
            StringTokenizer tokens = new StringTokenizer(mDisplayDate.getText().toString(), "/");
            transaction = new Transaction();
            transaction.setPrice(new Double(mPrice.getText().toString()));
            transaction.setPaymentType(spinText);
            transaction.setCostType(costTypes[mSpinCost.getSelectedItemPosition()]);
            transaction.setMonth(tokens.nextToken());
            transaction.setDay(tokens.nextToken());
            transaction.setYear(tokens.nextToken());

        }

        @Override
        protected Void doInBackground(Void... voids) {
            MainActivity.myAppDatabase.myDao().addTransaction(transaction);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Transaction added successfully", Toast.LENGTH_LONG).show();
        }
    }
}
