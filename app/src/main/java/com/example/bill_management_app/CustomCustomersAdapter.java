package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomCustomersAdapter extends BaseAdapter {

    Context context;
    String customerId[];
    LayoutInflater inflater;

    public CustomCustomersAdapter(Context appContext, String[] customerId) {
        context = appContext;
        this.customerId = customerId;

        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() {
        // limit display to 5 items
        if (customerId != null) {
            return Math.min(customerId.length, 5);
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
        convertView = inflater.inflate(R.layout.list_mngr_customer,null);
        TextView cId = convertView.findViewById(R.id.customerItem);

        cId.setText(customerId[position]);

        return convertView;
    }
}
