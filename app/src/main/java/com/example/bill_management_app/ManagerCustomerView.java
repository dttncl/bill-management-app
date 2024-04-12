package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManagerCustomerView extends AppCompatActivity {

    ListView listViewTransactions;
    LinearLayout navIcons;
    ImageButton btnHome, btnProfile;
    TextView textViewManagerName, textViewClientId;
    EditText editTextFirstName, editTextLastName, editTextPhone, editTextEmail;
    AppCompatButton buttonFirstNameManagerPage, buttonLastNameManagerPage, buttonPhoneManagerPage, buttonEmailManagerPage;
    AppCompatButton buttonSortTransactionManager, buttonSortDateManager, buttonSortStatusManager, buttonSaveChangesDetail;
    Button linkAllTransactionsManager;

    FirebaseDatabase fbaseDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_customer_view);

        listViewTransactions = findViewById(R.id.listTransactions);
        fbaseDB = FirebaseDatabase.getInstance();

        // extract the intent extras
        Intent intent = getIntent();
        Admin oneAdmin = (Admin) intent.getSerializableExtra("oneAdmin");
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");

        textViewManagerName = findViewById(R.id.managerName);

        editTextFirstName = findViewById(R.id.customerFName);
        editTextLastName = findViewById(R.id.customerLName);
        editTextPhone = findViewById(R.id.customerPhone);
        editTextEmail = findViewById(R.id.customerEmail);
        textViewClientId = findViewById(R.id.customerId);

        buttonFirstNameManagerPage = findViewById(R.id.buttonFirstNameManagerPage);
        buttonLastNameManagerPage = findViewById(R.id.buttonLastNameManagerPage);
        buttonPhoneManagerPage = findViewById(R.id.buttonPhoneManagerPage);
        buttonEmailManagerPage = findViewById(R.id.buttonEmailManagerPage);

        buttonSaveChangesDetail = findViewById(R.id.buttonSaveChangesDetail);

        linkAllTransactionsManager = findViewById(R.id.linkAllTransactionsManager);

        textViewManagerName.setText("Hello, " + oneAdmin.getFirstName());

        editTextFirstName.setText(oneClient.getFirstName());
        editTextLastName.setText(oneClient.getLastName());

        String formattedPhone = PhoneNumberUtils.formatNumber(oneClient.getPhone());

        editTextPhone.setText(formattedPhone);
        editTextEmail.setText(oneClient.getEmail());
        textViewClientId.setText(oneClient.getUserID());

        // set header for transaction history list
        LayoutInflater inflaterTransacHistory = getLayoutInflater();
        ViewGroup headerTransacHistory = (ViewGroup)inflaterTransacHistory.inflate(R.layout.list_cust_transactions_header,listViewTransactions,false);
        listViewTransactions.addHeaderView(headerTransacHistory,null,false);

        // display list of transaction history
        ArrayList<Transaction> listOfTransactions = new ArrayList<>();
        DatabaseReference transactions = fbaseDB.getReference("transactions");
        CustomTransactionsHistoryAdapter adapterTransacHistory = new CustomTransactionsHistoryAdapter(getApplicationContext(),listOfTransactions,oneAdmin,oneClient,"manager_customer_dashboard");
        listViewTransactions.setAdapter(adapterTransacHistory);

        transactions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Transaction oneTransaction;
                    String transactionID = snapshot.child("transactionID").getValue(String.class);

                    String billerID = snapshot.child("billerID").getValue(String.class);
                    int billID = snapshot.child("billID").getValue(Integer.class);
                    if (oneClient.getListOfBills().contains(String.valueOf(billID))) {
                        DataSnapshot dateSnapshot = snapshot.child("dateUpdated");
                        int day = dateSnapshot.child("day").getValue(Integer.class);
                        int month = dateSnapshot.child("month").getValue(Integer.class);
                        int year = dateSnapshot.child("year").getValue(Integer.class);
                        DateModel dateUpdated = new DateModel(day, month, year);

                        double amount = snapshot.child("amount").getValue(Double.class);
                        EnumTransactionStatus status = EnumTransactionStatus.valueOf(snapshot.child("status").getValue(String.class));

                        oneTransaction = new Transaction(transactionID, billerID, billID, dateUpdated, amount, status);
                        listOfTransactions.add(oneTransaction);
                    }
                }

                adapterTransacHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        buttonSortTransactionManager = findViewById(R.id.btnSortTransactionsManager);
        buttonSortDateManager = findViewById(R.id.btnSortDateManager);
        buttonSortStatusManager = findViewById(R.id.btnSortStatusManager);

        final boolean[] isAscendingTransaction = {true};
        final boolean[] isAscendingDate = {true};
        final boolean[] isAscendingStatus = {true};

        buttonSortTransactionManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscendingTransaction[0] = !isAscendingTransaction[0];
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = t1.getTransactionID().compareTo(t2.getTransactionID());
                        return isAscendingTransaction[0] ? result : -result;
                    }
                });
                adapterTransacHistory.notifyDataSetChanged();
            }
        });

        buttonSortDateManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort transactions by date
                isAscendingDate[0] = !isAscendingDate[0];

                // Sort transactions by date
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = new DateModelComparator().compare(t1.getDateUpdated(), t2.getDateUpdated());
                        // If sorting in descending order, reverse the result
                        return isAscendingDate[0] ? result : -result;
                    }
                });
                adapterTransacHistory.notifyDataSetChanged();
            }
        });

        buttonSortStatusManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscendingTransaction[0] = !isAscendingTransaction[0];
                Collections.sort(listOfTransactions, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction t1, Transaction t2) {
                        int result = t1.getStatus().compareTo(t2.getStatus());
                        return isAscendingTransaction[0] ? result : -result;
                    }
                });
                adapterTransacHistory.notifyDataSetChanged();
            }
        });


        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerView.this, ManagerDashboard.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerView.this, ActivityManagerProfile.class);
                intent.putExtra("oneAdmin", oneAdmin);
                startActivity(intent);
                finish();
            }
        });

        linkAllTransactionsManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerCustomerView.this, ViewAllTransactionsHistoryLink.class);
                intent.putExtra("oneAdmin", oneAdmin);
                intent.putExtra("oneClient",oneClient);
                startActivity(intent);
                finish();
            }
        });

        final String[] tempFirstName = {oneClient.getFirstName()};
        final String[] tempLastName = {oneClient.getLastName()};
        final String[] tempPhone = {oneClient.getPhone()};
        final String[] tempEmail = {oneClient.getEmail()};

        buttonSaveChangesDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder messageBuilder = new StringBuilder("Are you sure you want to make the following changes?\n");
                StringBuilder messageNotValidated = new StringBuilder("Wrong format for:\n");

                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String email = editTextEmail.getText().toString();

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
                    AlertDialog.Builder noChangesDialog = new AlertDialog.Builder(ManagerCustomerView.this);
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
                    AlertDialog.Builder noChangesDialog = new AlertDialog.Builder(ManagerCustomerView.this);
                    noChangesDialog.setTitle("Error")
                            .setMessage(messageNotValidated)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    editTextFirstName.setText(tempFirstName[0]);
                                    editTextLastName.setText(tempLastName[0]);
                                    editTextPhone.setText(tempPhone[0]);
                                    editTextEmail.setText(tempEmail[0]);

                                    buttonFirstNameManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonLastNameManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonPhoneManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                    buttonEmailManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                    editTextFirstName.setFocusable(false);
                                    editTextLastName.setFocusable(false);
                                    editTextPhone.setFocusable(false);
                                    editTextEmail.setFocusable(false);

                                    DisplayProfile(oneClient);

                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerCustomerView.this);
                builder.setTitle("Review your changes")
                        .setMessage(messageBuilder)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nfName = editTextFirstName.getText().toString().trim();
                                String nlName = editTextLastName.getText().toString().trim();
                                String nPhone = editTextPhone.getText().toString().trim();
                                String nEmail = editTextEmail.getText().toString().trim();

                                oneClient.setFirstName(nfName);
                                oneClient.setLastName(nlName);
                                oneClient.setPhone(nPhone);
                                oneClient.setEmail(nEmail);

                                tempFirstName[0] = nfName;
                                tempLastName[0] = nlName;
                                tempPhone[0] = nPhone;
                                tempEmail[0] = nEmail;

                                Toast.makeText(ManagerCustomerView.this, "Successfully Updated Profile", Toast.LENGTH_LONG).show();

                                fbaseDB = FirebaseDatabase.getInstance();
                                DatabaseReference clients = fbaseDB.getReference("clients");

                                //clients.child(oneClient.getUserID()).setValue(oneClient);
                                DatabaseReference clientsRef = clients.child(oneClient.getUserID());
                                clientsRef.child("firstName").setValue(oneClient.getFirstName());
                                clientsRef.child("lastName").setValue(oneClient.getLastName());
                                clientsRef.child("email").setValue(oneClient.getEmail());
                                clientsRef.child("phone").setValue(oneClient.getPhone());

                                buttonFirstNameManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonLastNameManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonPhoneManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonEmailManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                editTextFirstName.setFocusable(false);
                                editTextLastName.setFocusable(false);
                                editTextPhone.setFocusable(false);
                                editTextEmail.setFocusable(false);

                                DisplayProfile(oneClient);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editTextFirstName.setText(tempFirstName[0]);
                                editTextLastName.setText(tempLastName[0]);
                                editTextPhone.setText(tempPhone[0]);
                                editTextEmail.setText(tempEmail[0]);

                                buttonFirstNameManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonLastNameManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonPhoneManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                                buttonEmailManagerPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                                editTextFirstName.setFocusable(false);
                                editTextLastName.setFocusable(false);
                                editTextPhone.setFocusable(false);
                                editTextEmail.setFocusable(false);

                                DisplayProfile(oneClient);

                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });



        onClickEditButton(editTextFirstName,buttonFirstNameManagerPage);
        onClickEditButton(editTextLastName,buttonLastNameManagerPage);
        onClickEditButton(editTextPhone,buttonPhoneManagerPage);
        onClickEditButton(editTextEmail,buttonEmailManagerPage);

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

    private void DisplayProfile (Client oneClient){
        String firstName = oneClient.getFirstName();
        String lastName = oneClient.getLastName();
        String phone = oneClient.getPhone();
        String email = oneClient.getEmail();

        String formattedPhone = PhoneNumberUtils.formatNumber(phone);

        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextPhone.setText(formattedPhone);
        editTextEmail.setText(email);
    }
}