package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillDetailsActivity extends AppCompatActivity {

    LinearLayout navIcons;
    LinearLayout clientDetails, layoutDate;
    ConstraintLayout billerDetails,paymentAmount;
    ImageButton btnHome, btnProfile;
    TextView textViewFirstName, textViewAvailableCreditNumeric;
    EditText textViewDueDateFormat;
    Button buttonEditDate;
    TextView textViewBillerNameBold,textViewAccountNumberFormat,textViewStatusChangeable,textViewPaymentAmountBold;
    FirebaseDatabase fbaseDB;

    Button buttonModify, buttonDelete, buttonPayNow;

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

        // BILL DETAILS
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

        // DELETE BILL
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseReference bills = fbaseDB.getReference("bills");
                String billID = String.valueOf(oneBill.getBillID());

                // delete from bills table
                //bills.child(billID).addListenerForSingleValueEvent(new ValueEventListener() {
                //    @Override
                //    public void onDataChange(DataSnapshot snapshot) {
                //        if (snapshot.exists()) {
                //            snapshot.getRef().removeValue();
                //            Toast.makeText(BillDetailsActivity.this, "Bill successfully removed!", Toast.LENGTH_SHORT).show();
                //        }
                //    }
                //    @Override
                //    public void onCancelled(@NonNull DatabaseError error) {}
                //});

                // delete from one client list
                ArrayList<String> listExistingBills = oneClient.getListOfBills();
                for (String oneBillID : listExistingBills) {
                    if (oneBillID.equals(billID)) {
                        listExistingBills.remove(oneBillID);
                        break;
                    }
                }
                oneClient.setListOfBills(listExistingBills);

                // update in clients table
                DatabaseReference clients = fbaseDB.getReference("clients").child(oneClient.getUserID());
                DatabaseReference listOfBills = clients.child("listOfBills");
                listOfBills.child(billID).setValue(false);

                Toast.makeText(BillDetailsActivity.this, "Bill deleted successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BillDetailsActivity.this, ClientDashboard.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });


    }
}