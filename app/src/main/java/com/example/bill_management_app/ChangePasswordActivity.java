package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    LinearLayout navIcons;
    ImageButton btnHome;
    Button btnLogout, buttonUpdatePassword, buttonUpdateCancel;
    EditText txtCurrentPassword, txtNewPassword, txtConfirmNewPassword;
    FirebaseDatabase fbaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        txtCurrentPassword = findViewById(R.id.txtCurrentPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmNewPassword = findViewById(R.id.txtConfirmNewPassword);
        buttonUpdatePassword = findViewById(R.id.buttonUpdatePassword);
        buttonUpdateCancel = findViewById(R.id.buttonUpdateCancel);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");

        String oldPassword = oneClient.getPassword();

        buttonUpdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ClientProfilePageActivity.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });

        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = txtCurrentPassword.getText().toString().trim();
                if (oldPassword.equals(currentPassword)) {
                    String newPassword = txtNewPassword.getText().toString().trim();
                    String newConfirmPassword = txtConfirmNewPassword.getText().toString().trim();

                    if(!Validator.isValidPassword(newPassword)) {
                        txtNewPassword.setError("Password must contain the following: \n - Between 8 and 12 characters in length.\n" +
                                "Contain at least one digit.\n" +
                                "Contain at least one special character [!@#$%^&*()_+\\-=[]{};':\"|,.<>/?].\n" +
                                "Contain at least one alphabet character.");
                        txtNewPassword.setText("");
                        txtNewPassword.requestFocus();
                        return;
                    }

                    if(!newConfirmPassword.equals(newPassword)) {
                        txtConfirmNewPassword.setError("Password and Confirm Password does not match");
                        txtConfirmNewPassword.setText("");
                        return;
                    }

                    oneClient.setPassword(newPassword);

                    // update auth password
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // update clients table
                                fbaseDB = FirebaseDatabase.getInstance();
                                DatabaseReference clients = fbaseDB.getReference("clients");
                                clients.child(oneClient.getUserID()).setValue(oneClient);

                                Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePasswordActivity.this, ClientProfilePageActivity.class);
                                intent.putExtra("oneClient", oneClient);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Password update failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });



                } else {
                    txtCurrentPassword.setError("Incorrect Password");
                    txtCurrentPassword.setText("");
                    return;
                }
            }
        });

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnLogout = navIcons.findViewById(R.id.btnLogout);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ClientDashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}