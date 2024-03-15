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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;



public class RegisterPageActivity extends AppCompatActivity {

    private AppCompatButton btnSignUp, btnSignUpAdmin;
    private Button btnLogin;
    private EditText editTextFirstName,editTextLastName, editTextEmail, editTextPhone, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth fbaseAuth;

    FirebaseDatabase fbaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignUpAdmin = findViewById(R.id.buttonSignUpAdmin);

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
                Register(EnumUserType.Client);
            }
        });

        btnSignUpAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register(EnumUserType.Admin);
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

    private void Register(EnumUserType type) {
        //Client newClient = CreateUser(type);
        User newUser = CreateUser(type);
        if (newUser != null) {
            RegisterUserAuth(newUser);
        }
    }

    private void RegisterUserAuth(User newUser) {

        String email = newUser.getEmail();
        String password = newUser.getPassword();

        fbaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    GenerateUniqueID(newUser);
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

    private User CreateUser(EnumUserType type) {
        //Client newClient = null;
        User newUser = null;

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
            if (type == EnumUserType.Client) {
                newUser = new Client("BBC0000",firstName,lastName,email,phone,password, type,0,null);
            } else {
                //newClient = new Admin("BBC0000",firstName,lastName,email,phone,password, type, null, null);
                newUser = new Admin("BBC0000",firstName,lastName,email,phone,password, type, null, null);
            }

        }

        return newUser;
    }

    private String generateRandomID() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "BBC" + randomNumber;
    }

    private void handleGeneratedID(User newUser, String generatedID) {
        // update client ID of newClient object
        newUser.setUserID(generatedID);

        if (newUser.getType().equals(EnumUserType.Client)) {
            // add newClient to clients table
            DatabaseReference clients = fbaseDB.getReference("clients");
            clients.child(newUser.getUserID()).setValue(newUser);
        } else {
            // add newClient to admins table
            DatabaseReference admins = fbaseDB.getReference("admins");
            admins.child(newUser.getUserID()).setValue(newUser);
        }

    }

    private void GenerateUniqueID(User newUser) {

        fbaseDB = FirebaseDatabase.getInstance();
        DatabaseReference users_clients = fbaseDB.getReference("users").child("clients");
        DatabaseReference users_admins = fbaseDB.getReference("users").child("admins");

        if (newUser.getType().equals(EnumUserType.Client)) {
            users_clients.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String generatedID = generateRandomID();

                    // check if generated ID exists
                    if (!snapshot.hasChild(generatedID)) {
                        users_clients.child(generatedID).setValue(true);
                        handleGeneratedID(newUser,generatedID);
                    } else {
                        GenerateUniqueID(newUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            users_admins.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String generatedID = generateRandomID();

                    // check if generated ID exists
                    if (!snapshot.hasChild(generatedID)) {
                        users_admins.child(generatedID).setValue(true);
                        handleGeneratedID(newUser,generatedID);
                    } else {
                        GenerateUniqueID(newUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }

}
