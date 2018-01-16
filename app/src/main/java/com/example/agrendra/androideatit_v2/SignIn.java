package com.example.agrendra.androideatit_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agrendra.androideatit_v2.Common.Common;
import com.example.agrendra.androideatit_v2.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    MaterialEditText edtPassword, edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
    }

    public void btnSignIn(View view) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final ProgressDialog mDialog = new ProgressDialog((SignIn.this));
        mDialog.setMessage("Please waiting ...");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check database
                if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                    //get User information
                    mDialog.dismiss();
                    User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                    if (user.getPassword().equals(edtPassword.getText().toString())) {
                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                        Common.currentUser = user;
                        startActivity(homeIntent);
                        finish();
                    } else {
                        Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
