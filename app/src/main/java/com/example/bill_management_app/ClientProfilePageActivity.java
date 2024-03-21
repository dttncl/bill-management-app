package com.example.bill_management_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientProfilePageActivity extends AppCompatActivity {

    EditText textViewFirstName, textViewLastName, textViewPhone,textViewEmail, textViewCredit;
    LinearLayout navIcons;
    ImageButton btnHome;
    Button btnLogout, btnChangePassword, btnSaveProfile;

    AppCompatButton buttonFirstNameProfilePage, buttonLastNameProfilePage, buttonPhoneProfilePage, buttonEmailProfilePage, buttonAddCredit;

    FirebaseDatabase fbaseDB;

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

        buttonFirstNameProfilePage = findViewById(R.id.buttonFirstNameProfilePage);
        buttonLastNameProfilePage = findViewById(R.id.buttonLastNameProfilePage);
        buttonPhoneProfilePage = findViewById(R.id.buttonPhoneProfilePage);
        buttonEmailProfilePage = findViewById(R.id.buttonEmailProfilePage);
        buttonAddCredit = findViewById(R.id.buttonAddCredit);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        //Toast.makeText(this, oneClient.getPassword(), Toast.LENGTH_SHORT).show();
        DisplayProfile(oneClient);

        final String[] tempFirstName = {oneClient.getFirstName()};
        final String[] tempLastName = {oneClient.getLastName()};
        final String[] tempPhone = {oneClient.getPhone()};
        final String[] tempEmail = {oneClient.getEmail()};
        double doubleCredit = oneClient.getCredit();
        final double[] tempCredit = new double[]{doubleCredit};

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
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
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

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder messageBuilder = new StringBuilder("Are you sure you want to make the following changes?\n");
                StringBuilder messageNotValidated = new StringBuilder("Wrong format for:\n");

                String firstName = textViewFirstName.getText().toString();
                String lastName = textViewLastName.getText().toString();
                String phone = textViewPhone.getText().toString();
                String email = textViewEmail.getText().toString();
                String credit = textViewCredit.getText().toString();

                Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
                Matcher matcher = pattern.matcher(credit);
                StringBuilder digitsBuilder = new StringBuilder();

                while (matcher.find()) {
                    digitsBuilder.append(matcher.group());
                }

                String doubleCredit = String.valueOf(digitsBuilder);

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

                if(!String.valueOf(tempCredit[0]).equals(doubleCredit)) {
                    messageBuilder.append("\n - Credit from " + tempCredit[0] + " to " + credit + ".");
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

                if(!Validator.isValidAmount(doubleCredit)) {
                    messageNotValidated.append("\n - Amount.");
                    isValidated = false;
                }

                if (!isChangesMade) {
                    AlertDialog.Builder noChangesDialog = new AlertDialog.Builder(ClientProfilePageActivity.this);
                    noChangesDialog.setTitle("No Changes Made")
                            .setMessage("No changes were made.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Dismiss the dialog
                                    DisplayProfile(oneClient);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }

                if (!isValidated) {
                    AlertDialog.Builder noChangesDialog = new AlertDialog.Builder(ClientProfilePageActivity.this);
                    noChangesDialog.setTitle("Error")
                            .setMessage(messageNotValidated)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    textViewFirstName.setText(tempFirstName[0]);
                                    textViewLastName.setText(tempLastName[0]);
                                    textViewPhone.setText(tempPhone[0]);
                                    textViewEmail.setText(tempEmail[0]);
                                    textViewCredit.setText(String.valueOf(tempCredit[0]));

                                    buttonFirstNameProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonLastNameProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonPhoneProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonEmailProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                    textViewFirstName.setFocusable(false);
                                    textViewLastName.setFocusable(false);
                                    textViewPhone.setFocusable(false);
                                    textViewEmail.setFocusable(false);
                                    textViewCredit.setFocusable(false);

                                    DisplayProfile(oneClient);

                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientProfilePageActivity.this);
                builder.setTitle("Review your changes")
                        .setMessage(messageBuilder)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nfName = textViewFirstName.getText().toString().trim();
                                String nlName = textViewLastName.getText().toString().trim();
                                String nPhone = textViewPhone.getText().toString().trim();
                                String nEmail = textViewEmail.getText().toString().trim();
                                String nCredit = doubleCredit;

                                oneClient.setFirstName(nfName);
                                oneClient.setLastName(nlName);
                                oneClient.setPhone(nPhone);
                                oneClient.setEmail(nEmail);
                                oneClient.setCredit(Double.valueOf(nCredit));

                                tempFirstName[0] = nfName;
                                tempLastName[0] = nlName;
                                tempPhone[0] = nPhone;
                                tempEmail[0] = nEmail;
                                tempCredit[0] = Double.parseDouble(nCredit);

                                Toast.makeText(ClientProfilePageActivity.this, "Successfully Updated Profile", Toast.LENGTH_LONG).show();

                                fbaseDB = FirebaseDatabase.getInstance();
                                DatabaseReference clients = fbaseDB.getReference("clients");

                                clients.child(oneClient.getUserID()).setValue(oneClient);

                                buttonFirstNameProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonLastNameProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonPhoneProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonEmailProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                textViewFirstName.setFocusable(false);
                                textViewLastName.setFocusable(false);
                                textViewPhone.setFocusable(false);
                                textViewEmail.setFocusable(false);
                                textViewCredit.setFocusable(false);

                                DisplayProfile(oneClient);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewFirstName.setText(tempFirstName[0]);
                                textViewLastName.setText(tempLastName[0]);
                                textViewPhone.setText(tempPhone[0]);
                                textViewEmail.setText(tempEmail[0]);
                                textViewCredit.setText(String.valueOf(tempCredit[0]));

                                buttonFirstNameProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonLastNameProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonPhoneProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonEmailProfilePage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                textViewFirstName.setFocusable(false);
                                textViewLastName.setFocusable(false);
                                textViewPhone.setFocusable(false);
                                textViewEmail.setFocusable(false);
                                textViewCredit.setFocusable(false);

                                DisplayProfile(oneClient);

                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


        onClickEditButton(textViewFirstName,buttonFirstNameProfilePage);
        onClickEditButton(textViewLastName,buttonLastNameProfilePage);
        onClickEditButton(textViewPhone,buttonPhoneProfilePage);
        onClickEditButton(textViewEmail,buttonEmailProfilePage);
        onClickAddCreditButton(textViewCredit, buttonAddCredit);
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
        textViewCredit.setText(formatCreditText(credit));
    }

    private String formatCreditText (double credit) {
        return String.format("%.2f", credit);
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

    public void onClickAddCreditButton(EditText editText, AppCompatButton button) {
        final boolean[] isChecked = {false};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked[0] = !isChecked[0];
                toggleFocusable(editText, isChecked[0]);
            }
        });
    }
}
