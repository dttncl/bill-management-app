package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CustomTransactionsAdapter extends BaseAdapter {
    Context context;
    //String transactionId[];
    //String billerId[];
    //String transactionDate[];

    private ArrayList<Transaction> transactionList;
    private Admin oneAdmin;
    private String displayType;

    LayoutInflater inflater;


    public CustomTransactionsAdapter(Context appContext, ArrayList<Transaction> transactionList, Admin oneAdmin, String displayType) {
        context = appContext;
        this.transactionList = transactionList;
        this.oneAdmin = oneAdmin;
        this.displayType = displayType;

        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        if (displayType.equals("manager_dashboard")) {
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
        convertView = inflater.inflate(R.layout.list_mngr_transactions,null);
        TextView tId = convertView.findViewById(R.id.transactionItem);
        TextView bId = convertView.findViewById(R.id.billerItem);
        TextView tDate = convertView.findViewById(R.id.dateItem);

        tId.setText(transactionList.get(position).getTransactionID());
        bId.setText(transactionList.get(position).getBillerID());
        tDate.setText(transactionList.get(position).getDateUpdated().toString());

        return convertView;
    }
}
