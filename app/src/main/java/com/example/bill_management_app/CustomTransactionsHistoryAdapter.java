package com.example.bill_management_app;

import android.content.Context;
import android.content.Intent;
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
    private Client oneClient;
    private String displayType;



    public CustomTransactionsHistoryAdapter(Context appContext, ArrayList<Transaction> transactionList, Admin oneAdmin, Client oneClient,String displayType) {
        context = appContext;
        this.transactionList = transactionList;
        this.oneAdmin = oneAdmin;
        this.oneClient = oneClient;
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

        DateModel updatedDate = transactionList.get(position).getDateUpdated();
        String formattedDate = String.format("%02d/%02d/%d", updatedDate.getDay(), updatedDate.getMonth(), updatedDate.getYear());

        tId.setText(transactionList.get(position).getTransactionID());
        tDate.setText(formattedDate);

        if (transactionList.get(position).getStatus().toString().equals("RequestRefund")) {
            st.setText("Refund Pending");
        }
        else {
            st.setText(transactionList.get(position).getStatus().toString());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ManagerCustomerTransactionView.class);
                intent.putExtra("transactionID", transactionList.get(position).getTransactionID());
                intent.putExtra("oneAdmin", oneAdmin);
                intent.putExtra("oneClient", oneClient);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
