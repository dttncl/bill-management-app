package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RegisterPageActivity extends AppCompatActivity {

    private AppCompatButton btnSignUp;
    private Button btnLogin;
    private EditText editTextFirstName,editTextLastName, editTextEmail, editTextPhone, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth fbaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        btnSignUp = findViewById(R.id.buttonSignUp);
        btnLogin = findViewById(R.id.buttonLogin);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        fbaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPageActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Register() {
        Client newClient = CreateClient();
        if (newClient != null) {
            RegisterUserAuth(newClient);
        }
    }

    private void RegisterUserAuth(Client newClient) {

        String email = newClient.getEmail();
        String password = newClient.getPassword();
        fbaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterPageActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterPageActivity.this, LoginPageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterPageActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Client CreateClient() {
        Client newClient = null;

        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        Boolean isValidUser = true;

        ArrayList<EditText> listRequiredFields = new ArrayList<>();
        listRequiredFields.add(editTextFirstName);
        listRequiredFields.add(editTextEmail);
        listRequiredFields.add(editTextPassword);
        listRequiredFields.add(editTextConfirmPassword);

        // Validation block for Required Fields
        for (EditText field: listRequiredFields) {
            if (field.getText().toString().trim().isEmpty()) {
                field.setError("This field is required.");
                field.requestFocus();
                return null;
            }
        }

        // Validation block for Formats
        if(!Validator.isValidName(firstName)) {
            editTextFirstName.setError("Name can only contain letters and hyphens");
            editTextFirstName.requestFocus();
            //return;
            isValidUser = false;
        }

        if(!Validator.isValidEmail(email)) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            //return;
            isValidUser = false;
        }

        if(!Validator.isValidPassword(password)) {
            editTextPassword.setError("Password must contain the following: \n - Between 8 and 12 characters in length.\n" +
                    "Contain at least one digit.\n" +
                    "Contain at least one special character [!@#$%^&*()_+\\-=[]{};':\"|,.<>/?].\n" +
                    "Contain at least one alphabet character.");
            editTextPassword.setText("");
            editTextPassword.requestFocus();
            //return;
            isValidUser = false;
        }

        if(!confirmPassword.equals(password)) {
            editTextConfirmPassword.setError("Password and Confirm Password does not match");
            editTextConfirmPassword.setText("");
            //return;
            isValidUser = false;
        }

        if(lastName.trim().length() > 0) {
            if (!Validator.isValidName(lastName)) {
                editTextLastName.setError("Name can only contain letters and hyphens");
                editTextLastName.requestFocus();
                //return;
                isValidUser = false;
            }
        }

        if(phone.trim().length() > 0) {
            if (!Validator.isValidPhone(phone)) {
                editTextPhone.setError("Enter a valid phone number");
                editTextPhone.requestFocus();
                //return;
                isValidUser = false;
            }
        }

        if (isValidUser) {
            newClient = new Client("BBC0000",firstName,lastName,email,phone,password,0,null);
        }

        return newClient;
    }


}
