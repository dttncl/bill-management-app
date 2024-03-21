package com.example.bill_management_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomCustomersAdapter extends BaseAdapter {

    Context context;
    ArrayList<Client> clientList;
    LayoutInflater inflater;

    public CustomCustomersAdapter(Context appContext, ArrayList<Client> clientList) {
        this.context = appContext;
        this.clientList = clientList;

        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() {
        // limit display to 5 items
        if (clientList != null) {
            return Math.min(clientList.size(), 5);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_mngr_customer, parent, false);
        }

        TextView cId = convertView.findViewById(R.id.customerItem);
        Button seeMoreButton = convertView.findViewById(R.id.btnSeeMore); // Assuming btnSeeMore is the ID of the button in your item layout

        Client client = clientList.get(position);
        cId.setText(client.getUserID());

        if (seeMoreButton != null) {

            int uniqueId = generateUniqueId(position);
            seeMoreButton.setId(uniqueId);

            // Set click listener for the button
            seeMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ManagerCustomerView.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        intent.putExtra("client", client);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        // Create and set unique ID for the button

        return convertView;
    }
    private int generateUniqueId(int position) {
        return (int) System.currentTimeMillis() + position;
    }

    public void updateData(ArrayList<Client> clientList) {
        this.clientList.clear();
        this.clientList.addAll(clientList);
        notifyDataSetChanged();
    }
}
