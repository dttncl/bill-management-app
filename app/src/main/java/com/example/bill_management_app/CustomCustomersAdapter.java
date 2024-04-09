package com.example.bill_management_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomCustomersAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> clientList;
    LayoutInflater inflater;

    public CustomCustomersAdapter(Context appContext, ArrayList<String> clientList) {
        this.context = appContext;
        this.clientList = clientList;

        inflater = LayoutInflater.from(appContext);
    }

    @Override
    public int getCount() {
        if (clientList != null) {
            return Math.min(clientList.size(), 5);
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_mngr_customer, parent, false);
        }

        TextView cId = convertView.findViewById(R.id.customerItem);
        Button seeMoreButton = convertView.findViewById(R.id.btnSeeMore);

        cId.setText(clientList.get(position));


        if (seeMoreButton != null) {

            int uniqueId = generateUniqueId(position);
            seeMoreButton.setId(uniqueId);

            seeMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase fbaseDB = FirebaseDatabase.getInstance();

                    DatabaseReference clients = fbaseDB.getReference("clients");
                    Query searchClient = clients.orderByChild("userID").equalTo(clientList.get(position));
                    searchClient.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                                    Client oneClient = new Client();
                                    oneClient.setUserID(childSnapshot.child("userID").getValue(String.class));
                                    oneClient.setFirstName(childSnapshot.child("firstName").getValue(String.class));
                                    oneClient.setLastName(childSnapshot.child("lastName").getValue(String.class));
                                    oneClient.setEmail(childSnapshot.child("email").getValue(String.class));
                                    oneClient.setPhone(childSnapshot.child("phone").getValue(String.class));
                                    oneClient.setPassword(childSnapshot.child("password").getValue(String.class));
                                    oneClient.setType(childSnapshot.child("type").getValue(EnumUserType.class));
                                    oneClient.setCredit(childSnapshot.child("credit").getValue(double.class));

                                    ArrayList<String> listOfBills = new ArrayList<>();

                                    for (DataSnapshot billSnapshot : childSnapshot.child("listOfBills").getChildren()) {
                                        listOfBills.add(billSnapshot.getKey());
                                    }

                                    oneClient.setListOfBills(listOfBills);

                                    Intent intent = new Intent(context, ManagerCustomerView.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        intent.putExtra("oneClient", oneClient);
                                        context.startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                }
                            } else {
                                //Toast.makeText(LoginPageActivity.this, "No snapshot", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

        }

        return convertView;
    }
    private int generateUniqueId(int position) {
        return (int) System.currentTimeMillis() + position;
    }

    public void updateData(ArrayList<String> clientList) {
        this.clientList.clear();
        this.clientList.addAll(clientList);
        notifyDataSetChanged();
    }
}
