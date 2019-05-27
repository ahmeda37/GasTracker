package com.example.ahmeda47.gastracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class DeleteDialog extends AppCompatDialogFragment {
    private EditText etId;
    private int transactionId;
    int result;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        etId = view.findViewById(R.id.etId);

        builder.setView(view).setTitle("Delete Transaction")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etId.getText().toString().compareTo("")!= 0) {
                            transactionId = Integer.valueOf(etId.getText().toString());
                            new FetchDatabase5().execute();
                        }
                        else{
                            Toast.makeText(getActivity(), "Enter Transaction #", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return builder.create();
    }
    private class FetchDatabase5 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            result = MainActivity.myAppDatabase.myDao().deleteTransaction(transactionId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(result == 0){
                //Toast.makeText(getActivity(), "Could Not Delete Transaction#: " + transactionId, Toast.LENGTH_LONG).show();

            }else if(result == 1){
                //Toast.makeText(getActivity(), "Successfully Deleted Transaction#: " + transactionId, Toast.LENGTH_LONG).show();
            }
        }
    }
}
