package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManagerDashboard extends AppCompatActivity {

    ListView listViewCustomers, listViewTransactions;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    TextView textViewManagerName;
    Button linkAllCustomers, linkAllTransactions;

    AppCompatButton btnSortClients, btnSortTransactions, btnSortDate, btnSortBillerId;

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
        linkAllTransactions = findViewById(R.id.linkAllTransactions);

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

        // display list of clients
        ArrayList<String> listOfClientsFromAdmin = oneAdmin.getListOfClients();
        CustomCustomersAdapter adapterCustomers = new CustomCustomersAdapter(getApplicationContext(),listOfClientsFromAdmin,oneAdmin,"manager_dashboard");
        listViewCustomers.setAdapter(adapterCustomers);

        // set header for transactions list
        LayoutInflater inflaterTransaction = getLayoutInflater();
        ViewGroup headerTransaction = (ViewGroup)inflaterTransaction.inflate(R.layout.list_mngr_transactions_header,listViewTransactions,false);
        listViewTransactions.addHeaderView(headerTransaction,null,false);

        // display list of transactions
        ArrayList<Transaction> listOfTransactions = new ArrayList<>();
        CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(), listOfTransactions, oneAdmin, "manager_dashboard");
        listViewTransactions.setAdapter(adapterTransactions);

        DatabaseReference transactions = fbaseDB.getReference("transactions");
        transactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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
                adapterTransactions.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // Initialize buttons
        btnSortClients = findViewById(R.id.btnSortClients);
        btnSortTransactions = findViewById(R.id.btnSortTransactions);
        btnSortDate = findViewById(R.id.btnSortDate);
        btnSortBillerId = findViewById(R.id.btnSortBillerId);

        final boolean[] isAscending = {false};
        final boolean[] isAscendingBillerId = {true};
        final boolean[] isAscendingDate = {true};

        btnSortClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAscending[0]) {
                    Collections.sort(listOfClientsFromAdmin);
                    isAscending[0] = false;
                } else {
                    Collections.sort(listOfClientsFromAdmin, Collections.reverseOrder());
                    isAscending[0] = true;
                }
                adapterCustomers.notifyDataSetChanged();
            }
        });

        final boolean[] isAscendingTransactions = {true};
        btnSortTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscendingTransactions[0] = !isAscendingTransactions[0];
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = t1.getTransactionID().compareTo(t2.getTransactionID());
                        return isAscendingTransactions[0] ? result : -result;
                    }
                });
                adapterTransactions.notifyDataSetChanged();
            }
        });

        btnSortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort transactions by date
                isAscendingDate[0] = !isAscendingDate[0];

                // Sort transactions by date
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = new DateModelComparator().compare(t1.getDateUpdated(), t2.getDateUpdated());
                        // If sorting in descending order, reverse the result
                        return isAscendingDate[0] ? result : -result;
                    }
                });
                adapterTransactions.notifyDataSetChanged();
            }
        });


        btnSortBillerId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort transactions by status
                isAscendingBillerId[0] = !isAscendingBillerId[0];

                // Sort transactions by biller ID
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = t1.getBillerID().compareTo(t2.getBillerID());
                        // If sorting in descending order, reverse the result
                        return isAscendingBillerId[0] ? result : -result;
                    }
                });
                adapterTransactions.notifyDataSetChanged();
            }
        });

        // Set listener for "View All Customers" button
        linkAllCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ViewAllCustomersLink.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        fbaseDB = FirebaseDatabase.getInstance();

        // Set listener for "View All Transactions" button
        linkAllTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ViewAllTransactionsLink.class);
                intent.putExtra("oneAdmin", oneAdmin);
//                intent.putExtra("clientId",clientID);
                startActivity(intent);
                finish();
            }
        });

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        // Set listener for "Profile" button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboard.this, ActivityManagerProfile.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });
    }
}
