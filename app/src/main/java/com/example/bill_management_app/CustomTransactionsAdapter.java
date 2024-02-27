package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;

public class CustomTransactionsAdapter extends BaseAdapter {
    Context context;
    String transactionId[];
    String billerId[];
    String transactionDate[];

    LayoutInflater inflater;


    public CustomTransactionsAdapter(Context appContext, String[] transactionId, String[] billerId, String[] transactionDate) {
        context = appContext;
        this.transactionId = transactionId;
        this.billerId = billerId;
        this.transactionDate = transactionDate;

        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        // limit display to 5 items
        if (transactionId != null) {
            return Math.min(transactionId.length, 5);
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_mngr_transactions,null);
        TextView tId = convertView.findViewById(R.id.transactionItem);
        TextView bId = convertView.findViewById(R.id.billerItem);
        TextView tDate = convertView.findViewById(R.id.dateItem);

        tId.setText(transactionId[position]);
        bId.setText(billerId[position]);
        tDate.setText(transactionDate[position]);

        return convertView;
    }
}
