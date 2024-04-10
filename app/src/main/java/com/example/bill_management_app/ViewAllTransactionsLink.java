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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllTransactionsLink extends AppCompatActivity {

    ListView listViewTransactions;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    TextView textViewManagerName;
    FirebaseDatabase fbaseDB;

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

                CustomTransactionsAdapter adapterTransactions = new CustomTransactionsAdapter(getApplicationContext(), listOfTransactions, oneAdmin, "manager_dashboard_expanded");
                listViewTransactions.setAdapter(adapterTransactions);
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

    }
}