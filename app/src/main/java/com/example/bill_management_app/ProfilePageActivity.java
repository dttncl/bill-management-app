package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilePageActivity extends AppCompatActivity {

    TextView textViewFirstName;
    TextView textViewLastName;
    TextView textViewPhone;
    TextView textViewEmail;

    LinearLayout navIcons;
    ImageButton btnHome;
    Button btnLogout;

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


        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnLogout = navIcons.findViewById(R.id.btnLogout);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfilePageActivity.this, "Button Logout", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfilePageActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfilePageActivity.this, "Button Home", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilePageActivity.this, ClientDashboard.class);
                startActivity(intent);
            }
        });


    }

}
