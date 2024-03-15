package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class LoginPageActivity extends AppCompatActivity {

    private AppCompatButton buttonLogIn;
    private Button buttonSignUp;
    private EditText editTextInputEmail, editTextInputPassword;
    private FirebaseAuth fbaseAuth;
    FirebaseDatabase fbaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextInputEmail = findViewById(R.id.editTextInputEmail);
        editTextInputPassword = findViewById(R.id.editTextInputPassword);
        fbaseAuth = FirebaseAuth.getInstance();

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, RegisterPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login() {
        Client oneClient = SearchClient();
        if (oneClient != null) {
            SearchUserAuth(oneClient);
        }
    }

    private void SearchUserAuth(Client oneClient) {
        String email = oneClient.getEmail();
        String password = oneClient.getPassword();

        fbaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPageActivity.this, ProfilePageActivity.class);
                    // intent.putExtra("oneCient",oneClient);
                    intent.putExtra("firstName",oneClient.getFirstName());
                    intent.putExtra("lastName",oneClient.getLastName());
                    intent.putExtra("phone",oneClient.getPhone());
                    intent.putExtra("email",oneClient.getEmail());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPageActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Client SearchClient() {
        Client oneClient = null;

        String email = editTextInputEmail.getText().toString().trim();
        String password = editTextInputPassword.getText().toString().trim();
        Boolean isValidUser = true;

        ArrayList<EditText> listRequiredFields = new ArrayList<>();
        listRequiredFields.add(editTextInputEmail);
        listRequiredFields.add(editTextInputPassword);

        // Validation block for Required Fields
        for (EditText field: listRequiredFields) {
            if (field.getText().toString().trim().isEmpty()) {
                field.setError("This field is required.");
                field.requestFocus();
                return null;
            }
        }

        // Validation block for Formats
        if(!Validator.isValidEmail(email)) {
            editTextInputEmail.setError("Enter a valid email");
            editTextInputEmail.requestFocus();
            isValidUser = false;
        }

        if (isValidUser) {
            fbaseDB = FirebaseDatabase.getInstance();
            DatabaseReference users = fbaseDB.getReference("users");

            // find username equal to user_username
            //Query oneUser = UsersTable.orderByChild("username").equalTo(user_username);
            // if found:
            oneClient = new Client("BBC0000","firstName","lastName",email,"phone",password,EnumUserType.Client,0,null);
            // else
            // Toast.makeText(LoginPageActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
        }

        return oneClient;
    }
}
