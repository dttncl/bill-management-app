package com.example.bill_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomDropdownBillersAdapter extends BaseAdapter {

    Context context;
    ArrayList<Biller> listOfBillers;
    LayoutInflater inflater;

    public CustomDropdownBillersAdapter(Context appContext, ArrayList<Biller> listOfBillers) {
        context = appContext;
        this.listOfBillers = listOfBillers;

        inflater = LayoutInflater.from(appContext);
    }
    @Override
    public int getCount() {
        return listOfBillers.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfBillers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_dropdown_billers,null);
        TextView billerId = convertView.findViewById(R.id.txtBillerName);

        billerId.setText(listOfBillers.get(position).getBillerName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_dropdown_billers,null);
        TextView billerId = convertView.findViewById(R.id.txtBillerName);

        billerId.setText(listOfBillers.get(position).getBillerName());

        return convertView;
    }
}
