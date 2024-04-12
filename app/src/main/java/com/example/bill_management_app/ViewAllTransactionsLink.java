package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewAllTransactionsLink extends AppCompatActivity {

    ListView listViewTransactions;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    TextView textViewManagerName;
    FirebaseDatabase fbaseDB;

    AppCompatButton btnSortTransactions, btnSortDate, btnSortBillerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_transactions_link);

        fbaseDB = FirebaseDatabase.getInstance();

        listViewTransactions = findViewById(R.id.listTransactions);
        textViewManagerName = findViewById(R.id.managerName);

        // extract the intent extras
        Intent intent = getIntent();
        Admin oneAdmin = (Admin) intent.getSerializableExtra("oneAdmin");
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        String sender = intent.getStringExtra("sender");


        if (oneAdmin != null) {
            textViewManagerName.setText("Hello, " + oneAdmin.getFirstName());
        } else {
            textViewManagerName.setText("Welcome");
        }

        // set header for transactions list
        LayoutInflater inflaterTransaction = getLayoutInflater();
        ViewGroup headerTransaction = (ViewGroup)inflaterTransaction.inflate(R.layout.list_mngr_transactions_header,listViewTransactions,false);
        listViewTransactions.addHeaderView(headerTransaction,null,false);

        // display list of transactions
        ArrayList<Transaction> listOfTransactions = new ArrayList<>();
        DatabaseReference transactions = fbaseDB.getReference("transactions");
        CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(), listOfTransactions, oneAdmin, "manager_dashboard_expanded");
        listViewTransactions.setAdapter(adapterTransactions);

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

                    adapterTransactions.notifyDataSetChanged();;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });




        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllTransactionsLink.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllTransactionsLink.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnSortTransactions = findViewById(R.id.btnSortTransactions);
        btnSortDate = findViewById(R.id.btnSortDate);
        btnSortBillerId = findViewById(R.id.btnSortBillerId);

        final boolean[] isAscendingTransactions = {true};
        final boolean[] isAscendingBillerId = {true};
        final boolean[] isAscendingDate = {true};

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

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllTransactionsLink.this, ActivityManagerProfile.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

    }
}