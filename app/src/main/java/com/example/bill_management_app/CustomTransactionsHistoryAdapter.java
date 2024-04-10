package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomTransactionsHistoryAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<Transaction> transactionList;
    private Admin oneAdmin;
    private String displayType;



    public CustomTransactionsHistoryAdapter(Context appContext, ArrayList<Transaction> transactionList, Admin oneAdmin, String displayType) {
        context = appContext;
        this.transactionList = transactionList;
        this.oneAdmin = oneAdmin;
        this.displayType = displayType;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        // limit display to 5 items
        if (displayType.equals("manager_customer_dashboard")) {
            return Math.min(transactionList.size(), 5);
        } else {
            return transactionList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_cust_transactions,null);
        TextView tId = convertView.findViewById(R.id.transactionItem);
        TextView tDate = convertView.findViewById(R.id.dateItem);
        TextView st = convertView.findViewById(R.id.statusItem);

        tId.setText(transactionList.get(position).getTransactionID());
        tDate.setText(transactionList.get(position).getDateUpdated().toString());
        st.setText(transactionList.get(position).getStatus().toString());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, you can create an intent to open a new activity or perform any desired action
                // For example:
                // Intent intent = new Intent(context, YourActivity.class);
                // intent.putExtra("transaction_id", transactionList.get(position).getTransactionID());
                // context.startActivity(intent);
                //Toast.makeText(context, st.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
