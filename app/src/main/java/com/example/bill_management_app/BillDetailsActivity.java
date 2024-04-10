package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDetailsActivity extends AppCompatActivity {

    LinearLayout navIcons, clientDetails, layoutDate;
    ConstraintLayout billerDetails,paymentAmount;
    ImageButton btnHome, btnProfile;
    TextView textViewFirstName, textViewAvailableCreditNumeric,textViewStatusChangeable,textViewPaymentAmountBold;
    EditText textViewDueDateFormat, textViewBillerNameBold, textViewAccountNumberFormat;
    AppCompatButton btnEditAccountNumber, btnEditDueDate, btnModify;
    FirebaseDatabase fbaseDB;

    Button buttonModify, buttonDelete, buttonPayNow;
    String publishableKey = "PUB_KEY";
    String secretKey = "SECRET_KEY";
    String customerId, emphericalKey, clientSecret;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");
        Bill oneBill = (Bill) intent.getSerializableExtra("oneBill");

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);

        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnEditDueDate = findViewById(R.id.buttonEditDate);
        btnEditAccountNumber = findViewById(R.id.buttonAccountNumber);
        btnModify = findViewById(R.id.buttonModify);

        textViewDueDateFormat = findViewById(R.id.textViewDueDateFormat);
        textViewBillerNameBold = findViewById(R.id.textViewBillerNameBold);
        textViewAccountNumberFormat = findViewById(R.id.textViewAccountNumberFormat);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillDetailsActivity.this, ClientProfilePageActivity.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillDetailsActivity.this, ClientDashboard.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });

        // PROFILE DETAILS
        clientDetails = findViewById(R.id.includeCustomerHeader);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewAvailableCreditNumeric = findViewById(R.id.textViewAvailableCreditNumeric);

        textViewFirstName.setText("Hello, " + oneClient.getFirstName());

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        textViewAvailableCreditNumeric.setText(String.valueOf(currencyFormatter.format(oneClient.getCredit())));

        // BILL DETAILS
        layoutDate = findViewById(R.id.includeDate);
        textViewDueDateFormat = findViewById(R.id.textViewDueDateFormat);

        String formattedDate = String.format("%02d/%02d/%d", oneBill.getDateDue().getDay(), oneBill.getDateDue().getMonth(), oneBill.getDateDue().getYear());

        textViewDueDateFormat.setText(formattedDate);

        billerDetails = findViewById(R.id.billerDetails);
        textViewBillerNameBold = findViewById(R.id.textViewBillerNameBold);
        textViewAccountNumberFormat = findViewById(R.id.textViewAccountNumberFormat);
        textViewStatusChangeable = findViewById(R.id.textViewStatusChangeable);

        fbaseDB = FirebaseDatabase.getInstance();
        DatabaseReference billers = fbaseDB.getReference("billers");
        String billerID = oneBill.getBillerID();

        billers.child(billerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot billerSnapshot) {
                if (billerSnapshot.exists()) {
                    textViewBillerNameBold.setText(billerSnapshot.child("billerName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textViewAccountNumberFormat.setText(String.valueOf(oneBill.getAccountNumber()));
        textViewStatusChangeable.setText(String.valueOf(oneBill.getStatus()));

        paymentAmount = findViewById(R.id.paymentAmount);
        textViewPaymentAmountBold = findViewById(R.id.textViewPaymentAmountBold);
        textViewPaymentAmountBold.setText(currencyFormatter.format(oneBill.getAmount()));

        // DELETE BILL
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String billID = String.valueOf(oneBill.getBillID());

                // delete from one client list
                ArrayList<String> listExistingBills = oneClient.getListOfBills();
                for (String oneBillID : listExistingBills) {
                    if (oneBillID.equals(billID)) {
                        listExistingBills.remove(oneBillID);
                        break;
                    }
                }
                oneClient.setListOfBills(listExistingBills);

                // update in clients table
                DatabaseReference clients = fbaseDB.getReference("clients").child(oneClient.getUserID());
                DatabaseReference listOfBills = clients.child("listOfBills");
                listOfBills.child(billID).setValue(false);

                Toast.makeText(BillDetailsActivity.this, "Bill deleted successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BillDetailsActivity.this, ClientDashboard.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });

        // PAY BILL
        buttonPayNow = findViewById(R.id.buttonPayNow);
        PaymentConfiguration.init(this,publishableKey);

        if (textViewStatusChangeable.getText().equals("Paid")) {


            btnEditAccountNumber.setVisibility(View.GONE);
            btnEditDueDate.setVisibility(View.GONE);

            buttonPayNow.setText("Request Refund");
        }

        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
           onPaymentResult(paymentSheetResult, oneBill, oneClient);
        });

        buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textViewStatusChangeable.getText().equals("Paid")) {
                    //Request refund
                    return;
                }

                paymentFlow();
            }
        });
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        try {
                            JSONObject object = new JSONObject(res);
                            customerId = object.getString("id");
                            getEmphericalKey(customerId,oneBill);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(BillDetailsActivity.this, volleyError.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+secretKey);
                return header;
            }
        };

        final String[] dueDate = {textViewDueDateFormat.getText().toString()};
        final String[] billerName = {textViewBillerNameBold.getText().toString()};
        final String[] accountNumber = {textViewAccountNumberFormat.getText().toString()};

        String tempDueDate = dueDate[0];
        String tempBillerName = billerName[0];
        String tempAccountNumber = accountNumber[0];

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dueDate[0] = textViewDueDateFormat.getText().toString();
                accountNumber[0] = textViewAccountNumberFormat.getText().toString();

                StringBuilder validateMessage = new StringBuilder("Are you sure you want to make the following changes?\n\n");
                StringBuilder notValidateMessage = new StringBuilder("Please double check the following fields:\n\n");
                StringBuilder message = new StringBuilder();

                Boolean isValidated = true;


                if (dueDate[0].isEmpty()) {
                    notValidateMessage.append("\t- The date must not be empty.\n");
                    isValidated = false;
                }

                if (accountNumber[0].isEmpty()) {
                    notValidateMessage.append("\t- The account number must not be empty.\n");
                    isValidated = false;
                }

                if (!Validator.isValidDate(dueDate[0])) {
                    notValidateMessage.append("\t- The date must be in format dd/mm/yyyy. Example: 30/12/2024.\n");
                    isValidated = false;
                }

                if (!Validator.isValidAccountNumber(accountNumber[0])) {
                    notValidateMessage.append("\t- The account number must contain only digits.\n");
                    isValidated = false;
                }

                if(dueDate[0].equals(tempDueDate) && accountNumber[0].equals(tempAccountNumber)) {
                    notValidateMessage = new StringBuilder("No changes were made.");
                    isValidated = false;
                }

                if(!dueDate[0].equals(tempDueDate)) {
                    validateMessage = new StringBuilder("\t- Are you sure you want to modify the due date?");
                }

                if(!accountNumber[0].equals(tempAccountNumber)) {
                    validateMessage = new StringBuilder("\t- Are you sure you want to modify the account number?");
                }

                // For the Dialog button
                AlertDialog.Builder builder = new AlertDialog.Builder(BillDetailsActivity.this);

                if (!isValidated) {
                    message.append(notValidateMessage);
                } else {
                    message.append(validateMessage);
                }

                builder.setTitle("Modify Bill").setMessage(message);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        textViewDueDateFormat.setFocusable(false);
                        textViewAccountNumberFormat.setFocusable(false);

                        btnEditAccountNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                        btnEditDueDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                        if(!dueDate[0].equals(tempDueDate)) {
                            String dueDateText = dueDate[0];
                            String[] dates = dueDateText.split("/");

                            oneBill.setDateDue(new DateModel(
                                    Integer.valueOf(dates[0]),
                                    Integer.valueOf(dates[1]),
                                    Integer.valueOf(dates[2])
                            ));

                            String stringBillId = String.valueOf(oneBill.getBillID());

                            fbaseDB = FirebaseDatabase.getInstance();
                            DatabaseReference bills = fbaseDB.getReference("bills");

                            bills.child(stringBillId).setValue(oneBill);
                        }

                        if (!accountNumber[0].equals(tempAccountNumber)) {
                            oneBill.setAccountNumber(Integer.parseInt(accountNumber[0]));

                            fbaseDB = FirebaseDatabase.getInstance();
                            DatabaseReference bills = fbaseDB.getReference("bills");

                            String stringBillId = String.valueOf(oneBill.getBillID());

                            bills.child(stringBillId).setValue(oneBill);
                        }

                        if (!billerName[0].equals(tempBillerName)) {

                            fbaseDB = FirebaseDatabase.getInstance();
                            DatabaseReference billersRef = fbaseDB.getReference("billers");

                            Query billerQuery = billersRef.orderByChild("billerID").equalTo(billerID);
                            billerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot billerSnapshot : snapshot.getChildren()) {
                                        billerSnapshot.getRef().child("billerName").setValue(billerName[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Biller name updated successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }). addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Failed to update biller name", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Failed to query biller: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        textViewDueDateFormat.setText(tempDueDate);
                        textViewAccountNumberFormat.setText(tempAccountNumber);

                        textViewDueDateFormat.setFocusable(false);
                        textViewAccountNumberFormat.setFocusable(false);

                        btnEditAccountNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
                        btnEditDueDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);

                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        onClickEditButton(textViewDueDateFormat,btnEditDueDate);
        onClickEditButton(textViewAccountNumberFormat,btnEditAccountNumber);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration("BillBee",new PaymentSheet.CustomerConfiguration(
                customerId,
                emphericalKey
        )));
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult, Bill oneBill, Client oneClient) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
            oneBill.setStatus(EnumPaymentStatus.Paid);

            // update in bills table
            DatabaseReference bill = fbaseDB.getReference("bills").child(String.valueOf(oneBill.getBillID()));
            bill.setValue(oneBill);

            double existingCredit = oneClient.getCredit();
            double newCredit = existingCredit - oneBill.getAmount();
            oneClient.setCredit(newCredit);

            // update in clients table
            DatabaseReference client = fbaseDB.getReference("clients").child(oneClient.getUserID());
            client.child("credit").setValue(newCredit);

            // update transactions table
            String transactionID = "transaction_" + System.currentTimeMillis();
            String billerID = oneBill.getBillerID();
            int billID = oneBill.getBillID();

            DateModel dateUpdated = new DateModel();
            dateUpdated.setDay(DateFormatter.getCurrentDay());
            dateUpdated.setMonth(DateFormatter.getCurrentMonth());
            dateUpdated.setYear(DateFormatter.getCurrentYear());

            double amount = oneBill.getAmount();
            EnumTransactionStatus status = EnumTransactionStatus.Success;

            Transaction newTransaction = new Transaction(transactionID, billerID, billID, dateUpdated, amount, status);
            DatabaseReference transactions = fbaseDB.getReference("transactions").child(transactionID);
            transactions.setValue(newTransaction);

            // refresh table
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        } else {
            // update transactions table
            String transactionID = "transaction_" + System.currentTimeMillis();
            String billerID = oneBill.getBillerID();
            int billID = oneBill.getBillID();

            DateModel dateUpdated = new DateModel();
            dateUpdated.setDay(DateFormatter.getCurrentDay());
            dateUpdated.setMonth(DateFormatter.getCurrentMonth());
            dateUpdated.setYear(DateFormatter.getCurrentYear());

            double amount = oneBill.getAmount();
            EnumTransactionStatus status = EnumTransactionStatus.Failed;

            Transaction newTransaction = new Transaction(transactionID, billerID, billID, dateUpdated, amount, status);
            DatabaseReference transactions = fbaseDB.getReference("transactions").child(transactionID);
            transactions.setValue(newTransaction);

            // refresh table
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    private void getEmphericalKey(String customerId,Bill oneBill) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        try {
                            JSONObject object = new JSONObject(res);
                            emphericalKey = object.getString("id");
                            getClientSecret(customerId,emphericalKey,oneBill);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+secretKey);
                header.put("Stripe-Version","2023-10-16");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer",customerId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getClientSecret(String customerId, String emphericalKey, Bill oneBill) {

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        try {
                            JSONObject object = new JSONObject(res);
                            clientSecret = object.getString("client_secret");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(BillDetailsActivity.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+secretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer",customerId);
                params.put("amount",String.valueOf(Math.round(oneBill.getAmount() * 100)));
                params.put("currency","CAD");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void toggleButtonIcon(AppCompatButton button, boolean isButtonIconChecked) {
        if(isButtonIconChecked) {
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24,0,0,0);
        } else {
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_edit_24,0,0,0);
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
}