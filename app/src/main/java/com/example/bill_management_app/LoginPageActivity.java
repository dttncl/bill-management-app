package com.example.bill_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginPageActivity extends AppCompatActivity {

    private AppCompatButton buttonLogIn;
    private Button buttonSignUp;
    private EditText editTextInputEmail, editTextInputPassword;
    private FirebaseAuth fbaseAuth;
    FirebaseDatabase fbaseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextInputEmail = findViewById(R.id.editTextInputEmail);
        editTextInputPassword = findViewById(R.id.editTextInputPassword);
        fbaseAuth = FirebaseAuth.getInstance();

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, RegisterPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login() {
        User oneUser = ValidateUser();
        if (oneUser != null) {
            SearchUserAuth(oneUser);
        }
    }

    private void SearchUserAuth(User oneUser) {
        String email = oneUser.getEmail();
        String password = oneUser.getPassword();

        fbaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // query the user
                    fbaseDB = FirebaseDatabase.getInstance();
                    DatabaseReference users = fbaseDB.getReference("users");
                    String currentUID = fbaseAuth.getCurrentUser().getUid();

                    Query searchUID = users.orderByChild("uId").equalTo(currentUID);

                    searchUID.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String userId = userSnapshot.child("userId").getValue(String.class);
                                String userType = userSnapshot.child("userType").getValue(String.class);

                                if (userType.equals(EnumUserType.Client.toString())) {
                                    LoginAsClient(userId);

                                } else {
                                    LoginAsAdmin(userId);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(LoginPageActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private User ValidateUser() {
        User oneUser = null;

        String email = editTextInputEmail.getText().toString().trim();
        String password = editTextInputPassword.getText().toString().trim();
        Boolean isValidUser = true;

        ArrayList<EditText> listRequiredFields = new ArrayList<>();
        listRequiredFields.add(editTextInputEmail);
        listRequiredFields.add(editTextInputPassword);

        // Validation block for Required Fields
        for (EditText field: listRequiredFields) {
            if (field.getText().toString().trim().isEmpty()) {
                field.setError("This field is required.");
                field.requestFocus();
                return null;
            }
        }

        // Validation block for Formats
        if(!Validator.isValidEmail(email)) {
            editTextInputEmail.setError("Enter a valid email");
            editTextInputEmail.requestFocus();
            isValidUser = false;
        }

        if (isValidUser) {
            oneUser = new User("BBC0000","firstName","lastName",email,"phone",password,null);
        }

        return oneUser;
    }

    private void LoginAsClient(String userID) {
        DatabaseReference clients = fbaseDB.getReference("clients");
        Query searchClient = clients.orderByChild("userID").equalTo(userID);
        searchClient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        //Client oneClient = childSnapshot.getValue(Client.class);
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
                        Intent intent = new Intent(LoginPageActivity.this, ClientProfilePageActivity.class);
                        intent.putExtra("oneClient", oneClient);
                        startActivity(intent);
                        finish();
                        break;
                    }
                } else {
                    Toast.makeText(LoginPageActivity.this, "No snapshot", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void LoginAsAdmin(String userID) {
        DatabaseReference admins = fbaseDB.getReference("admins");
        Query searchAdmin = admins.orderByChild("userID").equalTo(userID);
        searchAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                        Admin oneAdmin = new Admin();
                        oneAdmin.setUserID(childSnapshot.child("userID").getValue(String.class));
                        oneAdmin.setFirstName(childSnapshot.child("firstName").getValue(String.class));
                        oneAdmin.setLastName(childSnapshot.child("lastName").getValue(String.class));
                        oneAdmin.setEmail(childSnapshot.child("email").getValue(String.class));
                        oneAdmin.setPhone(childSnapshot.child("phone").getValue(String.class));
                        oneAdmin.setPassword(childSnapshot.child("password").getValue(String.class));
                        oneAdmin.setType(childSnapshot.child("type").getValue(EnumUserType.class));

                        // populate list of clients
                        DatabaseReference clientsRef = fbaseDB.getReference("clients");
                        clientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> listOfClients = new ArrayList<>();
                                for (DataSnapshot clientSnapshot : dataSnapshot.getChildren()) {
                                    String clientId = clientSnapshot.getKey();
                                    listOfClients.add(clientId);
                                }

                                oneAdmin.setListOfClients(listOfClients);

                                DatabaseReference billersRef = fbaseDB.getReference("billers");
                                billersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList<String> listOfBillers = new ArrayList<>();
                                        for (DataSnapshot billerSnapshot : dataSnapshot.getChildren()) {
                                            String billerId = billerSnapshot.getKey();
                                            listOfBillers.add(billerId);
                                        }

                                        oneAdmin.setListOfBillers(listOfBillers);

                                        Intent intent = new Intent(LoginPageActivity.this, ManagerDashboard.class);
                                        intent.putExtra("oneAdmin", oneAdmin);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(LoginPageActivity.this, "Failed to fetch billers", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(LoginPageActivity.this, "Failed to fetch clients", Toast.LENGTH_LONG).show();
                            }
                        });

                        break;
                    }
                } else {
                    Toast.makeText(LoginPageActivity.this, "Admin not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginPageActivity.this, "Failed to search admin", Toast.LENGTH_LONG).show();
            }
        });
    }

}
