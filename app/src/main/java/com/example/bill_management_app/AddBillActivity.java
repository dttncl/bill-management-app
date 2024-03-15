package com.example.bill_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AddBillActivity extends AppCompatActivity {

    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill_page);

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBillActivity.this, ClientProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBillActivity.this, ClientDashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}