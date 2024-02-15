package com.example.bill_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ManagerDashboard extends AppCompatActivity {

    ListView listViewCustomers;
    ListView listViewTransactions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        listViewCustomers = findViewById(R.id.listCustomers);
        listViewTransactions = findViewById(R.id.listTransactions);


        String[] customers = {"BBC0001","BBC0002","BBC0003","BBC0004","BBC0005"};
        String[] transactions = {"BBT0000101","BBT0000102","BBT0000103","BBT0000104","BBT0000105"};

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_mngr_customer_header,listViewCustomers,false);
        listViewCustomers.addHeaderView(header,null,false);

        ArrayAdapter<String> adapterCustomers = new ArrayAdapter<>(this,R.layout.list_mngr_customer,R.id.customerItem,customers);
        ArrayAdapter<String> adapterTransactions = new ArrayAdapter<>(this,R.layout.list_mngr_customer,R.id.customerItem,transactions);


        listViewCustomers.setAdapter(adapterCustomers);
        listViewTransactions.setAdapter(adapterTransactions);

    }
}