package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerCustomerTransactionView extends AppCompatActivity {
    LinearLayout navIcons;

    RelativeLayout mngr_transaction_detail;
    ImageButton btnHome, btnProfile;

    AppCompatButton buttonTransacCancel, buttonTransacRefund;
    TextView textViewManagerName, textViewClientId, txtTransactionId, txtBillerId, txtBillId, txtDateUpdated, txtAmount, txtStatus;
    EditText editTextFirstName, editTextLastName, editTextPhone, editTextEmail;
    FirebaseDatabase fbaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_customer_transaction_view);
        fbaseDB = FirebaseDatabase.getInstance();

        // extract the intent extras
        Intent intent = getIntent();
        Admin oneAdmin = (Admin) intent.getSerializableExtra("oneAdmin");
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        String transactionID = intent.getStringExtra("transactionID");

        textViewManagerName = findViewById(R.id.managerName);

        editTextFirstName = findViewById(R.id.customerFName);
        editTextLastName = findViewById(R.id.customerLName);
        editTextPhone = findViewById(R.id.customerPhone);
        editTextEmail = findViewById(R.id.customerEmail);
        textViewClientId = findViewById(R.id.customerId);
        buttonTransacCancel = findViewById(R.id.buttonTransacCancel);
        buttonTransacRefund = findViewById(R.id.buttonTransacRefund);

        textViewManagerName.setText("Hello, " + oneAdmin.getFirstName());

        editTextFirstName.setText(oneClient.getFirstName());
        editTextLastName.setText(oneClient.getLastName());
        editTextPhone.setText(oneClient.getPhone());
        editTextEmail.setText(oneClient.getEmail());
        textViewClientId.setText(oneClient.getUserID());



        DatabaseReference transactions = fbaseDB.getReference().child("transactions").child(transactionID);
        final Transaction[] oneTransaction = new Transaction[1];
        transactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String billerID = snapshot.child("billerID").getValue(String.class);
                    int billID = snapshot.child("billID").getValue(Integer.class);

                    DataSnapshot dateSnapshot = snapshot.child("dateUpdated");
                    int day = dateSnapshot.child("day").getValue(Integer.class);
                    int month = dateSnapshot.child("month").getValue(Integer.class);
                    int year = dateSnapshot.child("year").getValue(Integer.class);
                    DateModel dateUpdated = new DateModel(day, month, year);

                    double amount = snapshot.child("amount").getValue(Double.class);
                    EnumTransactionStatus status = EnumTransactionStatus.valueOf(snapshot.child("status").getValue(String.class));

                    oneTransaction[0] = new Transaction(transactionID, billerID, billID, dateUpdated, amount, status);

                    mngr_transaction_detail = findViewById(R.id.transactionLayout);
                    txtTransactionId = mngr_transaction_detail.findViewById(R.id.transactionId);
                    txtBillerId = mngr_transaction_detail.findViewById(R.id.billerId);
                    txtBillId = mngr_transaction_detail.findViewById(R.id.billId);
                    txtDateUpdated = mngr_transaction_detail.findViewById(R.id.dateUpdated);
                    txtAmount = mngr_transaction_detail.findViewById(R.id.amount);
                    txtStatus = mngr_transaction_detail.findViewById(R.id.status);

                    txtTransactionId.setText(transactionID);
                    txtBillerId.setText(billerID);
                    txtBillId.setText(String.valueOf(billID));
                    txtDateUpdated.setText(dateUpdated.toString());
                    txtAmount.setText("$ "+amount);

                    if (String.valueOf(status).equals("RequestRefund")) {
                        txtStatus.setText("Refund Pending");
                    } else {
                        txtStatus.setText(String.valueOf(status));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerTransactionView.this, ClientProfilePageActivity.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerTransactionView.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        buttonTransacCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerTransactionView.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        buttonTransacRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtStatus.getText().equals("Refund Pending")) {
                    double currentCredit = oneClient.getCredit();
                    double newCredit = currentCredit + oneTransaction[0].getAmount();

                    oneClient.setCredit(newCredit);

                    DatabaseReference client = fbaseDB.getReference("clients").child(oneClient.getUserID());
                    client.child("credit").setValue(newCredit);

                    // update bill
                    int billID = oneTransaction[0].getBillID();
                    DatabaseReference bill = fbaseDB.getReference("bills").child(String.valueOf(billID));
                    bill.child("status").setValue(EnumPaymentStatus.Refunded);


                    // update transaction
                    oneTransaction[0].setStatus(EnumTransactionStatus.Refunded);
                    DatabaseReference transaction = fbaseDB.getReference("transactions").child(String.valueOf(oneTransaction[0].getTransactionID()));
                    transaction.child("status").setValue(EnumTransactionStatus.Refunded);

                    Toast.makeText(ManagerCustomerTransactionView.this, "Refunded successfully!", Toast.LENGTH_SHORT).show();

                    // refresh
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }

            }
        });
    }
}