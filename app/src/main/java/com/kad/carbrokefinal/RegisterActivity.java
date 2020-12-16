package com.kad.carbrokefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private EditText mName, mPhone, mEmail, mPassword, mPasswordCnf;
    private Button mLogin;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mName = findViewById(R.id.etxtName);
        mPhone = findViewById(R.id.etxtPhone);
        mEmail = findViewById(R.id.etxtNewUser);
        mPassword = findViewById(R.id.etxtPassword);
        mPasswordCnf = findViewById(R.id.etxtPasswordCnf);
        mLogin = findViewById(R.id.buttonSignUp);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String passwordCnf = mPasswordCnf.getText().toString();
                final String name = mName.getText().toString();
                final String phoneNum = mPhone.getText().toString();

                if (mPhone.length() == 10) {
                    if (password.equals(passwordCnf)) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("aa",task.getException().toString());
                                } else {
                                    String user_id = mAuth.getCurrentUser().getUid();

                                    DatabaseReference current_user_db = mDatabase.child("Users").child(user_id);
                                    DatabaseReference authDb = mDatabase.child("Auth");

                                    HashMap<String, Object> newPost = new HashMap<>();
                                    newPost.put("Name", name);
                                    newPost.put("Phone", phoneNum);
                                    current_user_db.setValue(newPost);


                                    HashMap<String, Object> authData = new HashMap<>();
                                    authData.put("isProvider", false);
                                    authDb.child(user_id).setValue(authData);

                                    Toast.makeText(RegisterActivity.this, "تم تسجيـلك بنجاح.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.error_passwordCnf), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_phoneNum), Toast.LENGTH_LONG).show();
                }


            }
        });

    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent registerIntent = new Intent(RegisterActivity.this, MapsActivity.class);
            startActivity(registerIntent);
            finish();
            // return;
        }
    }
}
