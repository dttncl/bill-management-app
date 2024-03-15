package com.example.bill_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ManagerCustomerView extends AppCompatActivity {

    ListView listViewTransactions;

    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_customer_view);

        listViewTransactions = findViewById(R.id.listTransactions);

        String[] transactions = {"BBT0000101","BBT0000102","BBT0000106","BBT0000107","BBT0000108","extra"};
        String[] tDates = {"02/12/2024","02/07/2024","01/12/2024","01/07/2024","12/12/2023","extra"};
        String[] status = {"Success","Success","Success","Refunded","Success","extra"};

        // set header for transaction history list
        LayoutInflater inflaterTransacHistory = getLayoutInflater();
        ViewGroup headerTransacHistory = (ViewGroup)inflaterTransacHistory.inflate(R.layout.list_cust_transactions_header,listViewTransactions,false);
        listViewTransactions.addHeaderView(headerTransacHistory,null,false);

        CustomTransactionsHistoryAdapter adapterTransacHistory = new CustomTransactionsHistoryAdapter(getApplicationContext(),transactions,tDates,status);

        listViewTransactions.setAdapter(adapterTransacHistory);

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerView.this, ClientProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerView.this, ManagerDashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }
}