package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerDashboard extends AppCompatActivity {

    ListView listViewCustomers, listViewTransactions;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    CustomCustomersAdapter adapterCustomers;
    TextView textViewManagerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        listViewCustomers = findViewById(R.id.listCustomers);
        listViewTransactions = findViewById(R.id.listTransactions);
        textViewManagerName = findViewById(R.id.managerName);

        Admin admin = AdminManager.getInstance().getAdmin();

        if (admin != null) {
            textViewManagerName.setText("Hello, " + admin.getFirstName());
        } else {
            textViewManagerName.setText("Welcome");
        }

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("clients");

        adapterCustomers = new CustomCustomersAdapter(getApplicationContext(), new ArrayList<>());
        listViewCustomers.setAdapter(adapterCustomers);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Client> clientList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Client client = snapshot.getValue(Client.class);
                    clientList.add(client);
                    Log.d("FirebaseData", "Client: " + client.toString());
                }
                adapterCustomers.updateData(clientList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());

            }
        });

//        String[] customers = {"BBC0001","BBC0002","BBC0003","BBC0004","BBC0005","extra"};
        String[] transactions = {"BBT0000101","BBT0000102","BBT0000103","BBT0000104","BBT0000105","extra"};
        String[] billers = {"BBB500","BBB501","BBB502","BBB501","BBB503","extra"};
        String[] tDates = {"02/12/2024","02/07/2024","01/28/2024","12/30/2023","12/28/2023","extra"};

        // set header for customers list
        LayoutInflater inflaterCustomer = getLayoutInflater();
        ViewGroup headerCustomer = (ViewGroup)inflaterCustomer.inflate(R.layout.list_mngr_customer_header,listViewCustomers,false);
        listViewCustomers.addHeaderView(headerCustomer,null,false);

        // set header for transactions list
        LayoutInflater inflaterTransaction = getLayoutInflater();
        ViewGroup headerTransaction = (ViewGroup)inflaterTransaction.inflate(R.layout.list_mngr_transactions_header,listViewCustomers,false);
        listViewTransactions.addHeaderView(headerTransaction,null,false);

        //ArrayAdapter<String> adapterCustomers = new ArrayAdapter<>(this,R.layout.list_mngr_customer,R.id.customerItem,customers);
        CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(),transactions,billers,tDates);
        listViewTransactions.setAdapter(adapterTransactions);

//        listViewCustomers.setAdapter(adapterCustomers);

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ClientProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /*
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ManagerDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        */

    }
}