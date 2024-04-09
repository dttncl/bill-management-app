package com.example.bill_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClientDashboard extends AppCompatActivity {
    ListView listBills;
    Button buttonAddBill;
    LinearLayout navIcons;
    LinearLayout clientDetails;
    ImageButton btnHome, btnProfile;
    AppCompatButton btnSortBiller, btnSortDueDate, btnSortStatus;
    TextView textViewFirstName, textViewAvailableCreditNumeric;
    FirebaseDatabase fbaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        buttonAddBill = findViewById(R.id.buttonAddBill);

        // extract the intent extras
        Intent intent = getIntent();
        Client oneClient = (Client) intent.getSerializableExtra("oneClient");

        listBills = findViewById(R.id.listBills);
        fbaseDB = FirebaseDatabase.getInstance();
        DatabaseReference billers = fbaseDB.getReference("billers");
        DatabaseReference bills = fbaseDB.getReference("bills");

        ArrayList<String> listOfBillsFromClient = oneClient.getListOfBills();
        ArrayList<CustomBillsAdapterObject> listOfCustomBills = new ArrayList<>();

        CustomBillsAdapter adapterBills = new CustomBillsAdapter(getApplicationContext(),listOfCustomBills, oneClient);

        for (String bill_id : listOfBillsFromClient) {
            CustomBillsAdapterObject bill = new CustomBillsAdapterObject();
            bills.child(bill_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        int billID = Integer.valueOf(snapshot.getKey());
                        String billerID = snapshot.child("billerID").getValue(String.class);
                        int accountNumber = snapshot.child("accountNumber").getValue(Integer.class);
                        DateModel dateDue = snapshot.child("dateDue").getValue(DateModel.class);
                        double amount = snapshot.child("amount").getValue(Double.class);
                        EnumPaymentStatus status = snapshot.child("status").getValue(EnumPaymentStatus.class);

                        Bill selectedBill = new Bill();
                        selectedBill.setBillID(billID);
                        selectedBill.setBillerID(billerID);
                        selectedBill.setAccountNumber(accountNumber);
                        selectedBill.setDateDue(dateDue);
                        selectedBill.setAmount(amount);
                        selectedBill.setStatus(status);

                        bill.setOneBill(selectedBill);

                        billers.child(billerID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot billerSnapshot) {
                                if (billerSnapshot.exists()) {
                                    String billerName = billerSnapshot.child("billerName").getValue(String.class);
                                    bill.setBillerName(billerName);

                                    listOfCustomBills.add(bill);

                                    adapterBills.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        // set header for bills list
        LayoutInflater inflaterBill = getLayoutInflater();
        ViewGroup headerBills = (ViewGroup)inflaterBill.inflate(R.layout.list_bills_header,listBills,false);
        listBills.addHeaderView(headerBills,null,false);

        btnSortBiller = findViewById(R.id.btnSortBiller);
        btnSortDueDate = findViewById(R.id.btnSortDueDate);
        btnSortStatus = findViewById(R.id.btnSortStatus);

        final boolean[] isBillerAscending = {true};
        final boolean[] isDueDateAscending = {true};
        final boolean[] isStatusAscending = {true};

        btnSortBiller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBillerAscending[0]) {
                    sortByBillerAscending(listOfCustomBills);
                    isBillerAscending[0] = false; // Toggle the flag
                } else {
                    sortByBillerDescending(listOfCustomBills);
                    isBillerAscending[0] = true; // Toggle the flag
                }
                adapterBills.notifyDataSetChanged(); // Update the ListView after sorting
            }
        });

        btnSortDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDueDateAscending[0]) {
                    sortByDueDateAscending(listOfCustomBills);
                    isDueDateAscending[0] = false; // Toggle the flag
                } else {
                    sortByDueDateDescending(listOfCustomBills);
                    isDueDateAscending[0] = true; // Toggle the flag
                }
                adapterBills.notifyDataSetChanged(); // Update the ListView after sorting
            }
        });

        btnSortStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStatusAscending[0]) {
                    sortByStatusAscending(listOfCustomBills);
                    isStatusAscending[0] = false; // Toggle the flag
                } else {
                    sortByStatusDescending(listOfCustomBills);
                    isStatusAscending[0] = true; // Toggle the flag
                }
                adapterBills.notifyDataSetChanged(); // Update the ListView after sorting
            }
        });

        // set list of bills
        listBills.setAdapter(adapterBills);

        // HEADER ICONS FUNCTIONALITY
        navIcons = findViewById(R.id.includeTopIcons);
        btnProfile = navIcons.findViewById(R.id.btnProfile);
        btnHome = navIcons.findViewById(R.id.btnHome);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientDashboard.this, ClientProfilePageActivity.class);
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

        buttonAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientDashboard.this, AddBillActivity.class);
                intent.putExtra("oneClient", oneClient);
                startActivity(intent);
                finish();
            }
        });

    }

    public void sortByBillerAscending(List<CustomBillsAdapterObject> listOfCustomBills) {
        // Sort the list by biller name
        Collections.sort(listOfCustomBills, new Comparator<CustomBillsAdapterObject>() {
            @Override
            public int compare(CustomBillsAdapterObject o1, CustomBillsAdapterObject o2) {
                // Compare by biller name
                return o1.getBillerName().compareToIgnoreCase(o2.getBillerName());
            }
        });
    }

    public void sortByBillerDescending(List<CustomBillsAdapterObject> listOfCustomBills) {
        // Sort the list by biller name
        Collections.sort(listOfCustomBills, new Comparator<CustomBillsAdapterObject>() {
            @Override
            public int compare(CustomBillsAdapterObject o1, CustomBillsAdapterObject o2) {
                // Compare by biller name
                return o2.getBillerName().compareToIgnoreCase(o1.getBillerName());
            }
        });
    }

    public void sortByDueDateAscending(List<CustomBillsAdapterObject> listOfCustomBills) {
        // Sort the list using DateModelComparator
        Collections.sort(listOfCustomBills, new Comparator<CustomBillsAdapterObject>() {
            @Override
            public int compare(CustomBillsAdapterObject o1, CustomBillsAdapterObject o2) {
                // Compare by due date
                return new DateModelComparator().compare(o1.getOneBill().getDateDue(), o2.getOneBill().getDateDue());
            }
        });
    }

    public void sortByDueDateDescending(List<CustomBillsAdapterObject> listOfCustomBills) {
        // Sort the list using DateModelComparator
        Collections.sort(listOfCustomBills, new Comparator<CustomBillsAdapterObject>() {
            @Override
            public int compare(CustomBillsAdapterObject o1, CustomBillsAdapterObject o2) {
                // Compare by due date
                return new DateModelComparator().compare(o2.getOneBill().getDateDue(), o1.getOneBill().getDateDue());
            }
        });
    }

    public void sortByStatusAscending(List<CustomBillsAdapterObject> listOfCustomBills) {
        // Sort the list by status
        Collections.sort(listOfCustomBills, new Comparator<CustomBillsAdapterObject>() {
            @Override
            public int compare(CustomBillsAdapterObject o1, CustomBillsAdapterObject o2) {
                // Compare by status
                return o1.getOneBill().getStatus().compareTo(o2.getOneBill().getStatus());
            }
        });
    }

    public void sortByStatusDescending(List<CustomBillsAdapterObject> listOfCustomBills) {
        // Sort the list by status in descending order
        Collections.sort(listOfCustomBills, new Comparator<CustomBillsAdapterObject>() {
            @Override
            public int compare(CustomBillsAdapterObject o1, CustomBillsAdapterObject o2) {
                // Compare by status in descending order
                return o2.getOneBill().getStatus().compareTo(o1.getOneBill().getStatus());
            }
        });
    }



}


