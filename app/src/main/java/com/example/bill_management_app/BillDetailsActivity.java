package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BillDetailsActivity extends AppCompatActivity {

    LinearLayout navIcons;
    LinearLayout clientDetails;
    ConstraintLayout layoutDate,billerDetails,paymentAmount;
    ImageButton btnHome, btnProfile;
    TextView textViewFirstName, textViewAvailableCreditNumeric;
    TextView textViewDueDateFormat,textViewBillerNameBold,textViewAccountNumberFormat,textViewStatusChangeable,textViewPaymentAmountBold;
    FirebaseDatabase fbaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        Bill oneBill = (Bill) intent.getSerializableExtra("oneBill");




        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillDetailsActivity.this, ClientProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillDetailsActivity.this, ClientDashboard.class);
                startActivity(intent);
                finish();
            }
        });

        // PROFILE DETAILS
        clientDetails = findViewById(R.id.includeCustomerHeader);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewAvailableCreditNumeric = findViewById(R.id.textViewAvailableCreditNumeric);

        textViewFirstName.setText("Hello, " + oneClient.getFirstName());
        textViewAvailableCreditNumeric.setText(String.valueOf(oneClient.getCredit()));

        layoutDate = findViewById(R.id.includeDate);
        textViewDueDateFormat = findViewById(R.id.textViewDueDateFormat);
        textViewDueDateFormat.setText(oneBill.getDateDue().toString());

        billerDetails = findViewById(R.id.billerDetails);
        textViewBillerNameBold = findViewById(R.id.textViewBillerNameBold);
        textViewAccountNumberFormat = findViewById(R.id.textViewAccountNumberFormat);
        textViewStatusChangeable = findViewById(R.id.textViewStatusChangeable);

        fbaseDB = FirebaseDatabase.getInstance();
        DatabaseReference billers = fbaseDB.getReference("billers");
        String billerID = oneBill.getBillerID();
        billers.child(billerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot billerSnapshot) {
                if (billerSnapshot.exists()) {
                    textViewBillerNameBold.setText(billerSnapshot.child("billerName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        textViewAccountNumberFormat.setText(String.valueOf(oneBill.getAccountNumber()));
        textViewStatusChangeable.setText(String.valueOf(oneBill.getStatus()));

        paymentAmount = findViewById(R.id.paymentAmount);
        textViewPaymentAmountBold = findViewById(R.id.textViewPaymentAmountBold);
        textViewPaymentAmountBold.setText("$"+oneBill.getAmount());

    }
}