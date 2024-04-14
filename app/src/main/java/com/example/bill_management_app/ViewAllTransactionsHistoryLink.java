package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewAllTransactionsHistoryLink extends AppCompatActivity {

    ListView listViewTransactions;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    TextView textViewManagerName, textViewClientId;
    EditText editTextFirstName, editTextLastName, editTextPhone, editTextEmail;
    AppCompatButton buttonFirstNameManagerPage, buttonLastNameManagerPage, buttonPhoneManagerPage, buttonEmailManagerPage;
    AppCompatButton buttonSortTransactionManager, buttonSortDateManager, buttonSortStatusManager;
    Button linkAllTransactionsManager;

    FirebaseDatabase fbaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_transactions_history_link);

        listViewTransactions = findViewById(R.id.listTransactions);
        fbaseDB = FirebaseDatabase.getInstance();

        // extract the intent extras
        Intent intent = getIntent();
        Admin oneAdmin = (Admin) intent.getSerializableExtra("oneAdmin");
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");

        textViewManagerName = findViewById(R.id.managerName);

        textViewManagerName.setText("Hello, " + oneAdmin.getFirstName());

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllTransactionsHistoryLink.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllTransactionsHistoryLink.this, ActivityManagerProfile.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        // set header for transaction history list
        LayoutInflater inflaterTransacHistory = getLayoutInflater();
        ViewGroup headerTransacHistory = (ViewGroup)inflaterTransacHistory.inflate(R.layout.list_cust_transactions_header,listViewTransactions,false);
        listViewTransactions.addHeaderView(headerTransacHistory,null,false);

        // display list of transaction history
        ArrayList<Transaction> listOfTransactions = new ArrayList<>();
        DatabaseReference transactions = fbaseDB.getReference("transactions");
        CustomTransactionsHistoryAdapter adapterTransacHistory = new CustomTransactionsHistoryAdapter(getApplicationContext(),listOfTransactions,oneAdmin,oneClient,"manager_customer_dashboard");
        listViewTransactions.setAdapter(adapterTransacHistory);

        transactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Transaction oneTransaction;
                    String transactionID = snapshot.child("transactionID").getValue(String.class);

                    String billerID = snapshot.child("billerID").getValue(String.class);
                    int billID = snapshot.child("billID").getValue(Integer.class);
                    if (oneClient.getListOfBills().contains(String.valueOf(billID))) {
                        DataSnapshot dateSnapshot = snapshot.child("dateUpdated");
                        int day = dateSnapshot.child("day").getValue(Integer.class);
                        int month = dateSnapshot.child("month").getValue(Integer.class);
                        int year = dateSnapshot.child("year").getValue(Integer.class);
                        DateModel dateUpdated = new DateModel(day, month, year);

                        double amount = snapshot.child("amount").getValue(Double.class);
                        EnumTransactionStatus status = EnumTransactionStatus.valueOf(snapshot.child("status").getValue(String.class));

                        oneTransaction = new Transaction(transactionID, billerID, billID, dateUpdated, amount, status);
                        listOfTransactions.add(oneTransaction);
                    }
                }

                adapterTransacHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        buttonSortTransactionManager = findViewById(R.id.btnSortTransactionsManager);
        buttonSortDateManager = findViewById(R.id.btnSortDateManager);
        buttonSortStatusManager = findViewById(R.id.btnSortStatusManager);

        final boolean[] isAscendingTransaction = {true};
        final boolean[] isAscendingDate = {true};
        final boolean[] isAscendingStatus = {true};

        buttonSortTransactionManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscendingTransaction[0] = !isAscendingTransaction[0];
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = t1.getTransactionID().compareTo(t2.getTransactionID());
                        return isAscendingTransaction[0] ? result : -result;
                    }
                });
                adapterTransacHistory.notifyDataSetChanged();
            }
        });

        buttonSortDateManager.setOnClickListener(new View.OnClickListener() {
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
                adapterTransacHistory.notifyDataSetChanged();
            }
        });

        buttonSortStatusManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscendingStatus[0] = !isAscendingStatus[0];
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = t1.getStatus().compareTo(t2.getStatus());
                        return isAscendingStatus[0] ? result : -result;
                    }
                });
                adapterTransacHistory.notifyDataSetChanged();
            }
        });

    }
}