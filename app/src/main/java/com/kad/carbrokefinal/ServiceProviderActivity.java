package com.kad.carbrokefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ServiceProviderActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private FirebaseAuth mAuth;
  //  private FirebaseAuth.AuthStateListener firebaseAuthListener;
  private DatabaseReference mDatabase;
    private EditText mUsername,  mPassword;
    private Button mButtonLogin;
    boolean isProvider = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUsername = findViewById(R.id.etxtUsername);
        mPassword = findViewById(R.id.etxtPassword);
        mButtonLogin  = findViewById(R.id.buttonLogin);

        mAuth= FirebaseAuth.getInstance();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(ServiceProviderActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Log.d("AAAA",task.toString());
                            Toast.makeText(ServiceProviderActivity.this,R.string.error_signIn,Toast.LENGTH_LONG).show();}
                        else {
                            Intent loginIntent = new Intent(ServiceProviderActivity.this,ServiceProMapsActivity.class);
                            startActivity(loginIntent);
                            finish();
                            // return;
                        }
                    }
                });

            }
        });
    }


    boolean checkIfProvider(String id){

        mDatabase.child("Auth").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot provider) {
                isProvider = (boolean) provider.child("isProvider").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("EEEEE", error.getDetails());
            }
        });
        return isProvider;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            if(checkIfProvider(user.getUid())) {
                startActivity(new Intent(ServiceProviderActivity.this, ServiceProMapsActivity.class));
            }else{
                startActivity(new Intent(ServiceProviderActivity.this, MapsActivity.class));
            }
            finish();
        }
    }
}
