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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import static android.widget.Toast.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayTransaction extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextView txtDate;
    private TextView txtCost;
    private TextView txtCount;
    private TableLayout tableView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Spinner mSpinCost;
    private String sMonth;
    private String sYear;
    private List<Transaction> transactionMonth;
    String[] costTypes={"ALL","GAS","MAINTENANCE","INSURANCE", "MISCELLANEOUS"};

    public DisplayTransaction() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_transaction, container, false);
        mSpinCost = (Spinner) view.findViewById(R.id.spinner);
        txtCost = view.findViewById(R.id.tvTotalDisplay);
        txtCount = view.findViewById(R.id.tvCountDisplay);
        txtDate = view.findViewById(R.id.tvDate2);
        tableView = view.findViewById(R.id.tableView);
        txtDate.setText(new AddTransaction().getToday(1));
        StringTokenizer tokens = new StringTokenizer(txtDate.getText().toString(), "/");
        sMonth = tokens.nextToken();
        sYear = tokens.nextToken();
        new FetchDatabase3().execute();


        txtDate.setOnClickListener(new View.OnClickListener(){
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

        mSpinCost.setOnItemSelectedListener(this);

        ArrayAdapter bb = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, costTypes);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinCost.setAdapter(bb);
        return view;
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        StringTokenizer tokens = new StringTokenizer(txtDate.getText().toString(), "/");
        sMonth = tokens.nextToken();
        sYear = tokens.nextToken();
        new FetchDatabase3().execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {}
// TODO Auto-generated method stub

    public void getDate(){
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = month + "/" + year;
                txtDate.setText(date);
                sMonth = ""+month;
                sYear = ""+year;
                new FetchDatabase3().execute();
            }
        };
    }

    private class FetchDatabase3 extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tableView.removeAllViews();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionMonth = MainActivity.myAppDatabase.myDao().getTransactionForMonth(sMonth,sYear);
            if(mSpinCost.getSelectedItemPosition() > 0){
                transactionMonth = MainActivity.myAppDatabase.myDao().getTransactionForMonthType(sMonth,sYear,costTypes[mSpinCost.getSelectedItemPosition()]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int count = transactionMonth.size();
            Double total = 0.0;
            for(Transaction transaction: transactionMonth) {
                TableRow tableRow = new TableRow(getContext());
                TextView dateForRow = new TextView(getContext());
                TextView idForRow = new TextView(getContext());
                TextView paymentTypeForRow = new TextView(getContext());
                TextView costTypeForRow = new TextView(getContext());
                TextView totalForRow = new TextView(getContext());

                int id = transaction.getId();
                String day = transaction.getDay();
                String month = transaction.getMonth();
                String year = transaction.getYear();
                Double price = transaction.getPrice();
                String paymentType = transaction.getPaymentType();
                String costType = transaction.getCostType();

                total += price;


                dateForRow.setText(day + "/" + month + "/" + year);
                idForRow.setText("" + id);
                paymentTypeForRow.setText(paymentType);
                costTypeForRow.setText(costType);
                totalForRow.setText("" + price);

                dateForRow.setPadding(5, 10, 25, 10);
                idForRow.setPadding(10, 10, 25, 10);
                paymentTypeForRow.setPadding(15, 10, 25, 10);
                costTypeForRow.setPadding(20, 10, 25, 10);
                totalForRow.setPadding(25, 10, 5, 10);
                tableRow.addView(dateForRow);
                tableRow.addView(idForRow);
                tableRow.addView(paymentTypeForRow);
                tableRow.addView(costTypeForRow);
                tableRow.addView(totalForRow);

                tableView.addView(tableRow);
            }

                txtCost.setText(""+String.format("%.2f", total));
                txtCount.setText(""+count);
        }
    }
}
