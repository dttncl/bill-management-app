package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LoginPageActivity extends AppCompatActivity {

    AppCompatButton buttonLogInPage;
    Button buttonBottomOfLoginPage;
    TextView editTextInputEmail;
    TextView editTextInputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        buttonLogInPage = findViewById(R.id.buttonLogInPage);
        buttonBottomOfLoginPage = findViewById(R.id.buttonBottomOfLoginPage);
        editTextInputEmail = findViewById(R.id.editTextInputEmail);
        editTextInputPassword = findViewById(R.id.editTextInputPassword);

        buttonLogInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputEmail = editTextInputEmail.getText().toString().toLowerCase().trim();
                String inputPassword = editTextInputPassword.getText().toString();

                UserModel userModel = UserModel.getInstance();
                Validator validator = new Validator();

                boolean isValidEmail = validator.isValidEmail(inputEmail);
                boolean isValidCredentials = userModel.isValidCredentials(inputEmail,inputPassword);

                if(inputEmail.isEmpty()) {
                    Toast.makeText(LoginPageActivity.this, "Email field is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    if(!isValidEmail) {
                        Toast.makeText(LoginPageActivity.this, "Wrong email format.", Toast.LENGTH_SHORT).show();
                    }
                }

                if(inputPassword.isEmpty()){
                    Toast.makeText(LoginPageActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
                }

                if(isValidCredentials){
                    Intent intent = new Intent(LoginPageActivity.this, ProfilePageActivity.class);

                    User user = userModel.getUserByEmail(inputEmail);

                    intent.putExtra("firstName",user.getFirstName());
                    intent.putExtra("lastName",user.getLastName());
                    intent.putExtra("phone",user.getPhone());
                    intent.putExtra("email",user.getEmail());

                    startActivity(intent);
                } else {
                    Toast.makeText(LoginPageActivity.this, "Wrong credentials. Please review email or password.", Toast.LENGTH_SHORT).show();

                    editTextInputEmail.setText("");
                    editTextInputPassword.setText("");
                }
            }
        });

        buttonBottomOfLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, RegisterPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
