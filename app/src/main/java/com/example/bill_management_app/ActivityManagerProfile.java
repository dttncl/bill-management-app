package com.example.bill_management_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.telephony.PhoneNumberUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityManagerProfile extends AppCompatActivity {

    EditText textViewRealFirstNameManager, textViewRealLastNameManager, textViewRealPhoneManager, textViewRealEmailManager;
    AppCompatButton buttonFirstNameManager, buttonLastNameManager, buttonPhoneManager, buttonEmailManager, buttonChangePasswordManager, buttonSaveChanges, btnLogout;
    ImageButton btnHome;
    FirebaseDatabase fbaseDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);

        Intent intent = getIntent();
        Admin oneAdmin = (Admin) intent.getSerializableExtra("oneAdmin");

        textViewRealFirstNameManager = findViewById(R.id.textViewRealFirstNameManager);
        textViewRealLastNameManager = findViewById(R.id.textViewRealLastNameManager);
        textViewRealPhoneManager = findViewById(R.id.textViewRealPhoneManager);
        textViewRealEmailManager = findViewById(R.id.textViewRealEmailManager);

        buttonFirstNameManager = findViewById(R.id.buttonFirstNameManager);
        buttonLastNameManager = findViewById(R.id.buttonLastNameManager);
        buttonPhoneManager = findViewById(R.id.buttonPhoneManager);
        buttonEmailManager = findViewById(R.id.buttonEmailManager);

        buttonChangePasswordManager = findViewById(R.id.buttonChangePasswordManager);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        btnHome = findViewById(R.id.btnHome);
        btnLogout = findViewById(R.id.btnLogout);

        assert oneAdmin != null;
        textViewRealFirstNameManager.setText(oneAdmin.getFirstName());
        textViewRealLastNameManager.setText(oneAdmin.getLastName());

        String formattedPhone = PhoneNumberUtils.formatNumber(oneAdmin.getPhone());

        textViewRealPhoneManager.setText(formattedPhone);
        textViewRealEmailManager.setText(oneAdmin.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ActivityManagerProfile.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityManagerProfile.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        buttonChangePasswordManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityManagerProfile.this, ChangePasswordActivity.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        final String[] tempFirstName = {oneAdmin.getFirstName()};
        final String[] tempLastName = {oneAdmin.getLastName()};
        final String[] tempPhone = {oneAdmin.getPhone().trim()};
        final String[] tempEmail = {oneAdmin.getEmail()};

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder messageBuilder = new StringBuilder("Are you sure you want to make the following changes?\n");
                StringBuilder messageNotValidated = new StringBuilder("Wrong format for:\n");

                String firstName = textViewRealFirstNameManager.getText().toString();
                String lastName = textViewRealLastNameManager.getText().toString();
                String phone = textViewRealPhoneManager.getText().toString().trim();
                String email = textViewRealEmailManager.getText().toString();

                boolean isChangesMade = false;
                boolean isValidated = true;

                if(!tempFirstName[0].equals(firstName)) {
                    messageBuilder.append("\n - First name from " + tempFirstName[0] + " to " + firstName + ".");
                    isChangesMade = true;
                }

                if(!tempLastName[0].equals(lastName)) {
                    messageBuilder.append("\n - Last name from " + tempLastName[0] + " to " + lastName + ".");
                    isChangesMade = true;
                }

                if(!tempPhone[0].equals(phone)) {
                    messageBuilder.append("\n - Phone from " + tempPhone[0] + " to " + phone + ".");
                    isChangesMade = true;
                }

                if(!tempEmail[0].equals(email)) {
                    messageBuilder.append("\n - Email from " + tempEmail[0] + " to " + email + ".");
                    isChangesMade = true;
                }

                if(!Validator.isValidName(firstName)) {
                    messageNotValidated.append("\n - First Name.");
                    isValidated = false;
                }

                if(!Validator.isValidName(lastName)) {
                    messageNotValidated.append("\n - Last Name.");
                    isValidated = false;
                }

                if(!Validator.isValidPhone(phone)) {
                    messageNotValidated.append("\n - Phone.");
                    isValidated = false;
                }

                if(!Validator.isValidEmail(email)) {
                    messageNotValidated.append("\n - Email.");
                    isValidated = false;
                }

                if (!isChangesMade) {
                    AlertDialog.Builder noChangesDialog = new AlertDialog.Builder(ActivityManagerProfile.this);
                    noChangesDialog.setTitle("No Changes Made")
                            .setMessage("No changes were made.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Dismiss the dialog
                                    DisplayProfile(oneAdmin);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }

                if (!isValidated) {
                    AlertDialog.Builder noChangesDialog = new AlertDialog.Builder(ActivityManagerProfile.this);
                    noChangesDialog.setTitle("Error")
                            .setMessage(messageNotValidated)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    textViewRealFirstNameManager.setText(tempFirstName[0]);
                                    textViewRealLastNameManager.setText(tempLastName[0]);
                                    textViewRealPhoneManager.setText(tempPhone[0]);
                                    textViewRealEmailManager.setText(tempEmail[0]);

                                    buttonFirstNameManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonLastNameManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonPhoneManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonEmailManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                    textViewRealFirstNameManager.setFocusable(false);
                                    textViewRealLastNameManager.setFocusable(false);
                                    textViewRealPhoneManager.setFocusable(false);
                                    textViewRealEmailManager.setFocusable(false);

                                    DisplayProfile(oneAdmin);

                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManagerProfile.this);
                builder.setTitle("Review your changes")
                        .setMessage(messageBuilder)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nfName = textViewRealFirstNameManager.getText().toString().trim();
                                String nlName = textViewRealLastNameManager.getText().toString().trim();
                                String nPhone = textViewRealPhoneManager.getText().toString().trim();
                                String nEmail = textViewRealEmailManager.getText().toString().trim();

                                oneAdmin.setFirstName(nfName);
                                oneAdmin.setLastName(nlName);
                                oneAdmin.setPhone(nPhone);
                                oneAdmin.setEmail(nEmail);

                                tempFirstName[0] = nfName;
                                tempLastName[0] = nlName;
                                tempPhone[0] = nPhone;
                                tempEmail[0] = nEmail;

                                Toast.makeText(ActivityManagerProfile.this, "Successfully Updated Profile", Toast.LENGTH_LONG).show();

                                fbaseDB = FirebaseDatabase.getInstance();
                                DatabaseReference admins = fbaseDB.getReference("admins");

                                //clients.child(oneClient.getUserID()).setValue(oneClient);
                                DatabaseReference adminRef = admins.child(oneAdmin.getUserID());
                                adminRef.child("firstName").setValue(oneAdmin.getFirstName());
                                adminRef.child("lastName").setValue(oneAdmin.getLastName());
                                adminRef.child("email").setValue(oneAdmin.getEmail());
                                adminRef.child("phone").setValue(oneAdmin.getPhone());
                                adminRef.child("password").setValue(oneAdmin.getPassword());

                                buttonFirstNameManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonLastNameManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonPhoneManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonEmailManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                textViewRealFirstNameManager.setFocusable(false);
                                textViewRealLastNameManager.setFocusable(false);
                                textViewRealPhoneManager.setFocusable(false);
                                textViewRealEmailManager.setFocusable(false);

                                DisplayProfile(oneAdmin);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewRealFirstNameManager.setText(tempFirstName[0]);
                                textViewRealFirstNameManager.setText(tempLastName[0]);
                                textViewRealPhoneManager.setText(tempPhone[0]);
                                textViewRealEmailManager.setText(tempEmail[0]);

                                buttonFirstNameManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonLastNameManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonPhoneManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonEmailManager.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                textViewRealFirstNameManager.setFocusable(false);
                                textViewRealFirstNameManager.setFocusable(false);
                                textViewRealPhoneManager.setFocusable(false);
                                textViewRealEmailManager.setFocusable(false);

                                DisplayProfile(oneAdmin);

                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        onClickEditButton(textViewRealFirstNameManager,buttonFirstNameManager);
        onClickEditButton(textViewRealLastNameManager,buttonLastNameManager);
        onClickEditButton(textViewRealPhoneManager,buttonPhoneManager);
        onClickEditButton(textViewRealEmailManager,buttonEmailManager);
    }

    public void toggleButtonIcon(AppCompatButton button, boolean isButtonIconChecked) {
        if(isButtonIconChecked) {
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24,0,0,0);
        } else {
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
        }
    }

    public void toggleFocusable(EditText editText, boolean isChecked) {
        if (isChecked) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.selectAll();
            editText.requestFocus();
        } else {
            editText.setFocusable(false);
        }
    }

    public void onClickEditButton(EditText editText, AppCompatButton button) {
        final boolean[] isChecked = {false};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked[0] = !isChecked[0];
                toggleButtonIcon(button, isChecked[0]);
                toggleFocusable(editText, isChecked[0]);
            }
        });
    }

    private void DisplayProfile (Admin oneAdmin){
        String firstName = oneAdmin.getFirstName();
        String lastName = oneAdmin.getLastName();
        String phone = oneAdmin.getPhone();
        String email = oneAdmin.getEmail();

        String formattedPhone = PhoneNumberUtils.formatNumber(phone);

        textViewRealFirstNameManager.setText(firstName);
        textViewRealLastNameManager.setText(lastName);
        textViewRealPhoneManager.setText(formattedPhone);
        textViewRealEmailManager.setText(email);
    }
}