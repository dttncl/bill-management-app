package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomTransactionsHistoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<Transaction> transactionList;
    LayoutInflater inflater;

    public CustomTransactionsHistoryAdapter(Context appContext, ArrayList<Transaction> transactionList) {
        context = appContext;
        this.transactionList = transactionList;
        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        // limit display to 5 items
        return transactionList != null ? (int) Math.min(transactionList.size(), 5) : 0;
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

        return convertView;
    }
}
