package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityProfilePage extends AppCompatActivity {

    TextView textViewFirstName;
    TextView textViewLastName;
    TextView textViewPhone;
    TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Intent intent = getIntent();

        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String phone = intent.getStringExtra("phone");
        String email = intent.getStringExtra("email");

        textViewFirstName = findViewById(R.id.textViewRealFirstNameProfilePage);
        textViewLastName = findViewById(R.id.textViewRealLastNameProfilePage);
        textViewPhone = findViewById(R.id.textViewRealPhoneProfilePage);
        textViewEmail = findViewById(R.id.textViewRealEmailProfilePage);

        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewPhone.setText(phone);
        textViewEmail.setText(email);

    }
}
