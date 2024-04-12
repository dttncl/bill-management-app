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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class ViewAllCustomersLink extends AppCompatActivity {

    ListView listViewCustomers;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    TextView textViewManagerName;

    AppCompatButton btnSortClients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_customers_link);

        listViewCustomers = findViewById(R.id.listCustomers);
        textViewManagerName = findViewById(R.id.managerName);

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
        CustomCustomersAdapter adapterCustomers = new CustomCustomersAdapter(getApplicationContext(),listOfClientsFromAdmin,oneAdmin,"manager_dashboard_expanded");
        listViewCustomers.setAdapter(adapterCustomers);

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllCustomersLink.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllCustomersLink.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnSortClients = findViewById(R.id.btnSortClients);
        final boolean[] isAscending = {false};
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

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllCustomersLink.this, ActivityManagerProfile.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });
    }
}