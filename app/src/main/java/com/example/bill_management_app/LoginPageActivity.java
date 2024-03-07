package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {

    private AppCompatButton buttonLogIn;
    private Button buttonSignUp;
    private TextView editTextInputEmail;
    private TextView editTextInputPassword;
    private FirebaseAuth fbaseAuth;
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
                //UserModel userModel = UserModel.getInstance();
                //Validator validator = new Validator();
//
                //boolean isValidEmail = validator.isValidEmail(inputEmail);
                //boolean isValidCredentials = userModel.isValidCredentials(inputEmail,inputPassword);
//
                //if(inputEmail.isEmpty()) {
                //    Toast.makeText(LoginPageActivity.this, "Email field is empty.", Toast.LENGTH_SHORT).show();
                //} else {
                //    if(!isValidEmail) {
                //        Toast.makeText(LoginPageActivity.this, "Wrong email format.", Toast.LENGTH_SHORT).show();
                //    }
                //}
//
                //if(inputPassword.isEmpty()){
                //    Toast.makeText(LoginPageActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
                //}
//
                //if(isValidCredentials){
                //    Intent intent = new Intent(LoginPageActivity.this, ProfilePageActivity.class);
//
                //    User user = userModel.getUserByEmail(inputEmail);
//
                //    intent.putExtra("firstName",user.getFirstName());
                //    intent.putExtra("lastName",user.getLastName());
                //    intent.putExtra("phone",user.getPhone());
                //    intent.putExtra("email",user.getEmail());
//
                //    startActivity(intent);
                //} else {
                //    Toast.makeText(LoginPageActivity.this, "Wrong credentials. Please review email or password.", Toast.LENGTH_SHORT).show();
//
                //    editTextInputEmail.setText("");
                //    editTextInputPassword.setText("");
                //}
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
        String email = editTextInputEmail.getText().toString().toLowerCase().trim();
        String password = editTextInputPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields should be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        fbaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPageActivity.this, ProfilePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPageActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
