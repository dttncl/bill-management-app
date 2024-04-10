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

    private Context context;
    private ArrayList<String> clientList;
    private Admin oneAdmin;
    private String displayType;
    private LayoutInflater inflater;

    public CustomCustomersAdapter(Context context, ArrayList<String> clientList, Admin oneAdmin, String displayType) {
        this.context = context;
        this.clientList = clientList;
        this.oneAdmin = oneAdmin;
        this.displayType = displayType;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (displayType.equals("manager_dashboard")) {
            return Math.min(clientList.size(), 5); // Display up to 5 items
        } else {
            return clientList.size(); // Display all items in the list
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_mngr_customer, parent, false);
        }

        TextView cId = convertView.findViewById(R.id.customerItem);
        Button seeMoreButton = convertView.findViewById(R.id.btnSeeMore);

        String clientId = clientList.get(position);
        cId.setText(clientId);

        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve client details from Firebase based on userID
                DatabaseReference clientsRef = FirebaseDatabase.getInstance().getReference("clients");
                Query query = clientsRef.orderByChild("userID").equalTo(clientId);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                Client oneClient = childSnapshot.getValue(Client.class);

                                if (oneClient != null) {
                                    // Start ManagerCustomerView activity and pass oneClient object
                                    Intent intent = new Intent(context, ManagerCustomerView.class);
                                    intent.putExtra("oneClient", oneClient);
                                    intent.putExtra("oneAdmin", oneAdmin);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                                break; // Only process the first matching client
                            }
                        } else {
                            Toast.makeText(context, "Client not found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Database error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return convertView;
    }
}
