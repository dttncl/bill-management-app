package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ActivityLoginPage extends AppCompatActivity {

    AppCompatButton buttonLogInPage;
    Button buttonBottomOfLoginPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        buttonLogInPage = findViewById(R.id.buttonLogInPage);
        buttonBottomOfLoginPage = findViewById(R.id.buttonBottomOfLoginPage);

        buttonLogInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLoginPage.this, ActivityProfilePage.class);
                startActivity(intent);
            }
        });

        buttonBottomOfLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLoginPage.this, ActivityRegisterPage.class);
                startActivity(intent);
            }
        });
    }
}
