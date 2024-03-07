package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RegisterPageActivity extends AppCompatActivity {

    AppCompatButton btnSignUpCreateYourAccountPage;
    Button btnBottomOfRegisterPage;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        btnSignUpCreateYourAccountPage = findViewById(R.id.buttonSignUpCreateYourAccountPage);
        btnBottomOfRegisterPage = findViewById(R.id.buttonBottomOfRegisterPage);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        UserModel userModel = UserModel.getInstance();

        btnSignUpCreateYourAccountPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email = editTextEmail.getText().toString();
                String phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                Validator validator = new Validator();

                boolean isValidFirstName = validator.isValidFirstName(firstName);
                boolean isValidLastName = validator.isValidLastName(lastName);
                boolean isValidEmail = validator.isValidEmail(email);
                boolean isValidPhone = validator.isValidPhone(phone);
                boolean isValidPassword = validator.isValidPassword(password);
                boolean isValidSignUp = false;

                // Validation block for empty Fields
                if(firstName.isEmpty()) {
                    Toast.makeText(RegisterPageActivity.this, "First Name field is empty.", Toast.LENGTH_SHORT).show();
                    editTextFirstName.requestFocus();
                    return;
                }

                if(email.isEmpty()) {
                    Toast.makeText(RegisterPageActivity.this, "Email field is empty.", Toast.LENGTH_SHORT).show();
                    editTextEmail.requestFocus();
                    return;
                }

                if(password.isEmpty()) {
                    Toast.makeText(RegisterPageActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
                    editTextPassword.requestFocus();
                    return;
                }

                if(confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterPageActivity.this, "Confirm Password field is empty.", Toast.LENGTH_SHORT).show();
                    editTextConfirmPassword.requestFocus();
                    return;
                }

                // Validation block for formats
                if(!isValidFirstName) {
                    Toast.makeText(RegisterPageActivity.this, "First Name is not in right format.", Toast.LENGTH_SHORT).show();
                    editTextFirstName.setText("");
                    editTextFirstName.requestFocus();
                    return;
                }

                if(!isValidLastName) {
                    Toast.makeText(RegisterPageActivity.this, "Last Name is not in right format.", Toast.LENGTH_SHORT).show();
                    editTextLastName.setText("");
                    editTextLastName.requestFocus();
                    return;
                }

                if(!isValidEmail) {
                    Toast.makeText(RegisterPageActivity.this, "Email is not in right format.", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextEmail.requestFocus();
                    return;
                }

                if(!isValidPhone) {
                    Toast.makeText(RegisterPageActivity.this, "Phone number is not in right format.", Toast.LENGTH_SHORT).show();
                    editTextPhone.setText("");
                    editTextPhone.requestFocus();
                    return;
                }

                if(!isValidPassword) {
                    Toast.makeText(RegisterPageActivity.this, "Password is not valid.", Toast.LENGTH_SHORT).show();
                    editTextPassword.setText("");
                    editTextPassword.requestFocus();
                    return;
                }

                // Validation block to verify password field
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterPageActivity.this, "Password doesn't match.", Toast.LENGTH_SHORT).show();
                    isValidPassword = false;
                    editTextConfirmPassword.setText("");
                    return;
                }

                if(isValidFirstName && isValidLastName && isValidEmail && isValidPhone && isValidPassword) {

                    if(lastName.isEmpty()){
                        lastName = "";
                    }
                    if(phone.isEmpty()){
                        phone = "";
                    }

                    User user = new User("111",firstName,lastName,email,phone,password);
                    userModel.addUser(user);

                    isValidSignUp = true;
                }

                if(isValidSignUp) {
                    Intent intent = new Intent(RegisterPageActivity.this, LoginPageActivity.class);
                    startActivity(intent);
                }

            }
        });

        btnBottomOfRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPageActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });



    }
}
