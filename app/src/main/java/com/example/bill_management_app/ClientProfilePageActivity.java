package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ClientProfilePageActivity extends AppCompatActivity {

    TextView textViewFirstName;
    TextView textViewLastName;
    TextView textViewPhone;
    TextView textViewEmail;

    TextView textViewCredit;

    LinearLayout navIcons;
    ImageButton btnHome;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_page);

        textViewFirstName = findViewById(R.id.textViewRealFirstNameProfilePage);
        textViewLastName = findViewById(R.id.textViewRealLastNameProfilePage);
        textViewPhone = findViewById(R.id.textViewRealPhoneProfilePage);
        textViewEmail = findViewById(R.id.textViewRealEmailProfilePage);
        textViewCredit = findViewById(R.id.textViewBalanceProfilePage);
        Intent intent = getIntent();

        // extract the intent extras
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        String firstName = oneClient.getFirstName();
        String lastName = oneClient.getLastName();
        String phone = oneClient.getPhone();
        String email = oneClient.getEmail();
        double credit = oneClient.getCredit();

        // display
        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewPhone.setText(phone);
        textViewEmail.setText(email);
        textViewCredit.setText(String.valueOf(credit));


        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnLogout = navIcons.findViewById(R.id.btnLogout);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ClientProfilePageActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientProfilePageActivity.this, ClientDashboard.class);
                startActivity(intent);
            }
        });


    }

}
