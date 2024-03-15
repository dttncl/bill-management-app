package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ClientProfilePageActivity extends AppCompatActivity {

    EditText textViewFirstName;
    EditText textViewLastName;
    EditText textViewPhone;
    EditText textViewEmail;

    TextView textViewCredit;

    LinearLayout navIcons;
    ImageButton btnHome;
    Button btnLogout, btnChangePassword, btnSaveProfile;

    boolean isClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_page);

        textViewFirstName = findViewById(R.id.textViewRealFirstNameProfilePage);
        textViewLastName = findViewById(R.id.textViewRealLastNameProfilePage);
        textViewPhone = findViewById(R.id.textViewRealPhoneProfilePage);
        textViewEmail = findViewById(R.id.textViewRealEmailProfilePage);
        textViewCredit = findViewById(R.id.textViewBalanceProfilePage);

        btnChangePassword = findViewById(R.id.buttonChangePassword);
        btnSaveProfile = findViewById(R.id.buttonSaveProfile);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        Toast.makeText(this, oneClient.getPassword(), Toast.LENGTH_SHORT).show();
        DisplayProfile(oneClient);

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
                finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nfName = textViewFirstName.getText().toString().trim();
                String nlName = textViewLastName.getText().toString().trim();
                String nPhone = textViewPhone.getText().toString().trim();
                String nEmail = textViewEmail.getText().toString().trim();
                String nCredit = textViewCredit.getText().toString().trim();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientProfilePageActivity.this, ChangePasswordActivity.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });

    }

    private void DisplayProfile (Client oneClient){
        String firstName = oneClient.getFirstName();
        String lastName = oneClient.getLastName();
        String phone = oneClient.getPhone();
        String email = oneClient.getEmail();
        double credit = oneClient.getCredit();

        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewPhone.setText(phone);
        textViewEmail.setText(email);
        textViewCredit.setText(String.valueOf(credit));
    }

    private void ToggleEdit (View view){

        if (isClicked) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setClickable(true);
            view.setLongClickable(true);
            isClicked = false;
        } else {
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
            view.setClickable(false);
            view.setLongClickable(false);
            isClicked = true;
        }
    }

    public void onEditMode(View view) {
        int viewId = view.getId();
        if (viewId == R.id.buttonFirstNameProfilePage) {
            ToggleEdit(textViewFirstName);
            textViewFirstName.setText(textViewFirstName.getText().toString());
        } else if (viewId == R.id.buttonLastName) {
            ToggleEdit(textViewLastName);
            textViewLastName.setText(textViewLastName.getText().toString());
        } else if (viewId == R.id.buttonPhone) {
            ToggleEdit(textViewPhone);
            textViewPhone.setText(textViewPhone.getText().toString());
        } else if (viewId == R.id.buttonEmail) {
            ToggleEdit(textViewEmail);
            textViewEmail.setText(textViewEmail.getText().toString());
        }
    }

}
