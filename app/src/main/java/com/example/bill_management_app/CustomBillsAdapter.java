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


public class CustomBillsAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomBillsAdapterObject> listOfCustomBills;
    Client oneClient;
    LayoutInflater inflater;

    public CustomBillsAdapter(Context appContext, ArrayList<CustomBillsAdapterObject> listOfCustomBills, Client oneClient) {
        context = appContext;
        this.listOfCustomBills = listOfCustomBills;
        this.oneClient = oneClient;

        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() {
        return listOfCustomBills.size();
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

        convertView = inflater.inflate(R.layout.list_bills,null);
        TextView bName = convertView.findViewById(R.id.billerItem);
        TextView dDate = convertView.findViewById(R.id.duedateItem);
        TextView status = convertView.findViewById(R.id.statusItem);

        bName.setText(listOfCustomBills.get(position).getBillerName());

        DateModel dueDate = listOfCustomBills.get(position).getOneBill().getDateDue();
        String formattedDate = String.format("%02d/%02d/%d", dueDate.getDay(), dueDate.getMonth(), dueDate.getYear());

        dDate.setText(formattedDate);
        if (listOfCustomBills.get(position).getOneBill().getStatus().toString().equals("RequestRefund")) {
            status.setText("Refund Pending");
        } else {
            status.setText(listOfCustomBills.get(position).getOneBill().getStatus().toString());
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, BillDetailsActivity.class);
                intent.putExtra("oneClient", oneClient);
                intent.putExtra("oneBill", listOfCustomBills.get(position).getOneBill());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
