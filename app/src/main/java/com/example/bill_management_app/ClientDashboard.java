package com.example.bill_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ClientDashboard extends AppCompatActivity {
    ListView listBills;
    Button buttonAddBill;
    LinearLayout navIcons;
    LinearLayout clientDetails;
    ImageButton btnHome, btnProfile;
    TextView textViewFirstName, textViewAvailableCreditNumeric;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");

        listBills = findViewById(R.id.listBills);

        String[] billers = {"Rogers","Fido","HydroQuebec","Insurancce","Fido"};
        String[] dueDates = {"02/12/2024","02/07/2024","01/28/2024","12/30/2023","12/28/2023"};
        String[] status = {"Unpaid","Unpaid","Paid","Paid","Paid"};

        // set header for bills list
        LayoutInflater inflaterBill = getLayoutInflater();
        ViewGroup headerBills = (ViewGroup)inflaterBill.inflate(R.layout.list_bills_header,listBills,false);
        listBills.addHeaderView(headerBills,null,false);

        CustomBillsAdapter adapterBills = new CustomBillsAdapter(getApplicationContext(),billers,dueDates,status);
        listBills.setAdapter(adapterBills);

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientDashboard.this, ClientProfilePageActivity.class);
                intent.putExtra("oneClient", oneClient);
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


    }
}