package com.kad.carbrokefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextInputLayout nameT, emailT,phoneT,passT,confirmT;
    private Button btnEdit;
    private TextView name, email, phone;
    boolean isProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnEdit = findViewById(R.id.editProfile);

        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        phone = findViewById(R.id.profilePhone);
        //  btnEdit = findViewById(R.id.editProfile);
        getProfile();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateDialogue();
            }
        });


    }

    private void getProfile() {
        checkIfProvider(mAuth.getUid());
    }

    private void openUpdateDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialogue_profile_update, null);

        builder.setTitle("تعديل البيانات");

        emailT = view.findViewById(R.id.editEmail);
        nameT = view.findViewById(R.id.editName);
        phoneT = view.findViewById(R.id.editPhone);
        passT = view.findViewById(R.id.pass);
        confirmT = view.findViewById(R.id.confirmPass);

        nameT.getEditText().setText(name.getText().toString());
        phoneT.getEditText().setText(phone.getText().toString());
        emailT.getEditText().setText(email.getText().toString());


        builder.setView(view);

        builder.setNeutralButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                updateData(nameT.getEditText().getText().toString(),phoneT.getEditText().getText().toString(),emailT.getEditText().getText().toString());
                getProfile();
            }
        });
        builder.setNegativeButton(R.string.cancel,null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    void checkIfProvider(final String id) {

        mDatabase.child("Auth").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot provider) {
                if ((boolean) provider.child("isProvider").getValue()) {
                    isProvider = true;
                    loadData("providers", id);
                } else {
                    isProvider = false;
                    loadData("users", id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EEEEE", error.getDetails());
            }
        });
    }

    void loadData(String tableName, String id) {
        mDatabase.child(tableName).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("SSSS", snapshot.getValue().toString());
                name.setText(snapshot.child("name").getValue().toString());
                phone.setText(snapshot.child("phone").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", error.toString());
            }
        });
    }
    void updateData(final String name, String phone, final String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        final DatabaseReference userRef;
        if (isProvider) { userRef = mDatabase.child("providers").child(user.getUid());
        } else{ userRef = mDatabase.child("users").child(user.getUid());}

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userRef.child("name").setValue(name);
            }
        });
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userRef.child("email").setValue(email);
            }
        });



        userRef.child("phone").setValue(phone);

        //.addOnCompleteListener(new OnCompleteListener<Void>() {
              //  .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("TAG", "User profile updated.");
//                        }
//                    }
//                });
//        user.updatePhoneNumber(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        })
        //.ddOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("TAG", "User email address updated.");
//                        }
//                    }
//                });

    }
}