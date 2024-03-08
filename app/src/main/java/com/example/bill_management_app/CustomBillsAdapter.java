package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CustomBillsAdapter extends BaseAdapter {

    Context context;
    String billerId[];
    String dueDates[];
    String stat[];
    LayoutInflater inflater;

    public CustomBillsAdapter(Context appContext, String[] billerId, String[] dueDates, String[] stat) {
        context = appContext;
        this.billerId = billerId;
        this.dueDates = dueDates;
        this.stat = stat;

        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() {
        return billerId.length;
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
        TextView bId = convertView.findViewById(R.id.billerItem);
        TextView dId = convertView.findViewById(R.id.duedateItem);
        Button status = convertView.findViewById(R.id.statusItem);

        bId.setText(billerId[position]);
        dId.setText(dueDates[position]);
        status.setText(stat[position]);

        return convertView;
    }
}
