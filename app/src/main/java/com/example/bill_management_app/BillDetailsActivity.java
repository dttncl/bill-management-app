package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDetailsActivity extends AppCompatActivity {

    LinearLayout navIcons;
    LinearLayout clientDetails, layoutDate;
    ConstraintLayout billerDetails,paymentAmount;
    ImageButton btnHome, btnProfile;
    TextView textViewFirstName, textViewAvailableCreditNumeric;
    EditText textViewDueDateFormat;
    Button buttonEditDate;
    TextView textViewBillerNameBold,textViewAccountNumberFormat,textViewStatusChangeable,textViewPaymentAmountBold;
    FirebaseDatabase fbaseDB;

    Button buttonModify, buttonDelete, buttonPayNow;
    String publishableKey = "PUBLISHABLE_KEY";
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
        textViewAvailableCreditNumeric.setText(String.valueOf(oneClient.getCredit()));

        // BILL DETAILS
        layoutDate = findViewById(R.id.includeDate);
        textViewDueDateFormat = findViewById(R.id.textViewDueDateFormat);
        textViewDueDateFormat.setText(oneBill.getDateDue().toString());

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
        textViewPaymentAmountBold.setText("$"+oneBill.getAmount());

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

        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
           onPaymentResult(paymentSheetResult, oneBill, oneClient);
        });

        buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}