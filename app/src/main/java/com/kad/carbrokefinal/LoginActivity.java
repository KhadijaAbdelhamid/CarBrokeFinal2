package com.kad.carbrokefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.rpc.context.AttributeContext;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText mUsername, mPassword;
    private Button mButtonLogin, mButtonServicePro;
    private TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUsername = findViewById(R.id.etxtUsername);
        mPassword = findViewById(R.id.etxtPassword);
        mButtonLogin = findViewById(R.id.buttonLogin);
        mRegister = findViewById(R.id.txtViewRegister);
        mButtonServicePro = findViewById(R.id.buttonServicePro);

        mAuth = FirebaseAuth.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }

        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("AAAA", task.getException().toString());
                            Log.d("RRRR", task.getResult().toString());
                            Toast.makeText(LoginActivity.this, R.string.error_signIn, Toast.LENGTH_LONG).show();
                        } else {
                            checkLogin();
                        }
                    }
                });

            }
        });
        mButtonServicePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ServiceProviderLoginActivity.class));
                finish();
            }
        });
    }

    void checkIfProvider(String id) {

        mDatabase.child("Auth").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot provider) {
                if ((boolean) provider.child("isProvider").getValue()) {
                    logOutProvider();
                    Toast.makeText(LoginActivity.this,"You're provider use provider login",Toast.LENGTH_SHORT).show();
                    Log.d("EEEEE", "IT's Provider");
                } else {
                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EEEEE", error.getDetails());
            }
        });
    }

    void checkLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            checkIfProvider(user.getUid());
        } else {
            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void logOutProvider() {
        mAuth.signOut();
        startActivity(new Intent(LoginActivity.this, ServiceProviderLoginActivity.class));
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        checkLogin();
    }
}
