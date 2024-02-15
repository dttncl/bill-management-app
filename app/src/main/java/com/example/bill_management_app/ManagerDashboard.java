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
        String[] billers = {"BBB500","BBB501","BBB502","BBB501","BBB503"};
        String[] tDates = {"02/12/2024","02/07/2024","01/28/2024","12/30/2023","12/28/2023"};

        // set header for customers list
        LayoutInflater inflaterCustomer = getLayoutInflater();
        ViewGroup headerCustomer = (ViewGroup)inflaterCustomer.inflate(R.layout.list_mngr_customer_header,listViewCustomers,false);
        listViewCustomers.addHeaderView(headerCustomer,null,false);

        // set header for transactions list
        LayoutInflater inflaterTransaction = getLayoutInflater();
        ViewGroup headerTransaction = (ViewGroup)inflaterTransaction.inflate(R.layout.list_mngr_transactions_header,listViewCustomers,false);
        listViewTransactions.addHeaderView(headerTransaction,null,false);

        ArrayAdapter<String> adapterCustomers = new ArrayAdapter<>(this,R.layout.list_mngr_customer,R.id.customerItem,customers);
        CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(),transactions,billers,tDates);

        listViewCustomers.setAdapter(adapterCustomers);
        listViewTransactions.setAdapter(adapterTransactions);

    }
}