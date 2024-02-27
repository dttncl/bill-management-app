package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ActivityRegisterPage extends AppCompatActivity {

    AppCompatButton btnSignUpCreateYourAccountPage;
    Button btnBottomOfRegisterPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        btnSignUpCreateYourAccountPage = findViewById(R.id.buttonSignUpCreateYourAccountPage);
        btnBottomOfRegisterPage = findViewById(R.id.buttonBottomOfRegisterPage);

        btnSignUpCreateYourAccountPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegisterPage.this, ActivityLoginPage.class);
                startActivity(intent);
            }
        });

        btnBottomOfRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRegisterPage.this, ActivityLoginPage.class);
                startActivity(intent);
            }
        });



    }
}
