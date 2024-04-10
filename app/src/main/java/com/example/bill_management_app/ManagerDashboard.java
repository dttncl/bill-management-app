package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView textViewManagerName;
    Button linkAllCustomers;

    FirebaseDatabase fbaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        fbaseDB = FirebaseDatabase.getInstance();

        listViewCustomers = findViewById(R.id.listCustomers);
        listViewTransactions = findViewById(R.id.listTransactions);
        textViewManagerName = findViewById(R.id.managerName);
        linkAllCustomers = findViewById(R.id.linkAllCustomers);

        // extract the intent extras
        Intent intent = getIntent();
        Admin oneAdmin = (Admin) intent.getSerializableExtra("oneAdmin");

        if (oneAdmin != null) {
            textViewManagerName.setText("Hello, " + oneAdmin.getFirstName());
        } else {
            textViewManagerName.setText("Welcome");
        }

        // set header for customers list
        LayoutInflater inflaterCustomer = getLayoutInflater();
        ViewGroup headerCustomer = (ViewGroup)inflaterCustomer.inflate(R.layout.list_mngr_customer_header,listViewCustomers,false);
        listViewCustomers.addHeaderView(headerCustomer,null,false);

        // set header for transactions list
        LayoutInflater inflaterTransaction = getLayoutInflater();
        ViewGroup headerTransaction = (ViewGroup)inflaterTransaction.inflate(R.layout.list_mngr_transactions_header,listViewCustomers,false);
        listViewTransactions.addHeaderView(headerTransaction,null,false);

        // display list of transactions
        ArrayList<Transaction> listOfTransactions = new ArrayList<>();
        DatabaseReference transactions = fbaseDB.getReference("transactions");

        transactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Transaction transaction = snapshot.getValue(Transaction.class);

                    Transaction oneTransaction;
                    String transactionID = snapshot.child("transactionID").getValue(String.class);

                    String billerID = snapshot.child("billerID").getValue(String.class);
                    int billID = snapshot.child("billID").getValue(Integer.class);

                    DataSnapshot dateSnapshot = snapshot.child("dateUpdated");
                    int day = dateSnapshot.child("day").getValue(Integer.class);
                    int month = dateSnapshot.child("month").getValue(Integer.class);
                    int year = dateSnapshot.child("year").getValue(Integer.class);
                    DateModel dateUpdated = new DateModel(day, month, year);

                    double amount = snapshot.child("amount").getValue(Double.class);
                    EnumTransactionStatus status = EnumTransactionStatus.valueOf(snapshot.child("status").getValue(String.class));

                    oneTransaction = new Transaction(transactionID, billerID, billID, dateUpdated, amount, status);

                    if (oneTransaction != null) {
                        listOfTransactions.add(oneTransaction);
                    }
                }

                CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(), listOfTransactions, oneAdmin, "manager_dashboard");
                listViewTransactions.setAdapter(adapterTransactions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(),transactions,billers,tDates);
        //listViewTransactions.setAdapter(adapterTransactions);

        // display list of clients
        ArrayList<String> listOfClientsFromAdmin = oneAdmin.getListOfClients();
        CustomCustomersAdapter adapterCustomers = new CustomCustomersAdapter(getApplicationContext(),listOfClientsFromAdmin,oneAdmin,"manager_dashboard");
        listViewCustomers.setAdapter(adapterCustomers);

        linkAllCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ViewAllCustomersLink.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ClientProfilePageActivity.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

    }
}