package com.example.ahmeda47.gastracker;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {

    private Button btImport;
    private Button btExport;
    private Button btDelete;
    private InputStream inputStream;
    private ArrayList<Transaction> transactionsContainer;
    private List<Transaction> transactions;
    private String[] data;
    private String[] date;
    private File f;
    private Transaction transaction;
    public SettingFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        btImport = view.findViewById(R.id.btImport);
        btExport = view.findViewById(R.id.btExport);
        btDelete = view.findViewById(R.id.btDelete);

        btImport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.e("import", "Click successful");
                showChooser();
            }
        });

        btExport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.e("export", "Click successful");
                exportFile();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete","Click successful");
                deleteTransaction();
            }
        });

        return view;


    }
    private void deleteTransaction(){
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.show(getFragmentManager(),"delete");
    }
    private void exportFile(){
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "expenses.csv";
        String filePath = baseDir + File.separator + fileName;
        f = new File(filePath);
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            new FetchDatabase4().execute();
            Log.e("WORKED","ITS GOOD");
        }else{
            Log.e("DIDNT WORK", "DIDNT WORK");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                new FetchDatabase4().execute();
        }
    }
    private void showChooser(){
        Intent intent = new Intent();
        intent.setType("text/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file to upload "),1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
        {
            Uri selectedFileUri = data.getData();
            if (requestCode == 1)
            {
                parseData(selectedFileUri);
                System.out.println("selectedPath1 : " + selectedFileUri);
            }

        }
    }
    public void parseData(Uri fileUri){
        try {
            inputStream = getActivity().getContentResolver().openInputStream(fileUri);
            transactionsContainer = new ArrayList<Transaction>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try{
                String cvsLine;
                int count = 0;
                while((cvsLine = reader.readLine()) != null){
                    count++;
                    transaction = new Transaction();
                    data = cvsLine.split(",");
                    transaction.setPaymentType(data[1]);
                    transaction.setPrice(new Double(data[2]));
                    transaction.setCostType(data[3]);

                    try {
                        date = data[0].split("/");
                        transaction.setMonth(date[0]);
                        transaction.setDay(date[1]);
                        transaction.setYear(date[2]);

                        transactionsContainer.add(transaction);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        for(Transaction tran : transactionsContainer){
            new FetchDatabase3().execute(tran);
        }
    }

    private class FetchDatabase3 extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Transaction... tran) {
                MainActivity.myAppDatabase.myDao().addTransaction(tran[0]);
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Transaction added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchDatabase4 extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Transaction... tran) {
            transactions = MainActivity.myAppDatabase.myDao().getTransactions();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String result = "";
            Log.e("Location", f.getAbsolutePath());
            for(Transaction tran: transactions){
                result += tran.getMonth()+"/"+tran.getDay()+"/"+tran.getYear()
                        +","+tran.getPaymentType()+","+tran.getPrice()
                        +","+tran.getCostType()+"\n";
            }
            try {
                BufferedWriter bfWriter = new BufferedWriter(new FileWriter(f));
                bfWriter.write(result);
                bfWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "Successfully Exported", Toast.LENGTH_LONG).show();
        }
    }
    }

