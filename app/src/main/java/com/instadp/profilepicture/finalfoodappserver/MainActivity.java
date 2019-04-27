package com.instadp.profilepicture.finalfoodappserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instadp.profilepicture.finalfoodappserver.Common.Common;
import com.instadp.profilepicture.finalfoodappserver.Model.User;

public class MainActivity extends AppCompatActivity {

    TextView username,password;
    Button login;
    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idlistner();
        db=FirebaseDatabase.getInstance();
        users=db.getReference("User");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(username.getText().toString(),password.getText().toString());
            }
        });
    }

    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog=new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please wait");
        mDialog.show();
        final String localPhone=phone;
        final String localPassword=password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1=dataSnapshot.child(localPhone).getValue(User.class);
//                Toast.makeText(MainActivity.this,localPhone, Toast.LENGTH_SHORT).show();
//                Log.d("1234",user1.getIsStaff());

                if(dataSnapshot.child(localPhone).exists())
                {
                    mDialog.dismiss();
                    User user=dataSnapshot.child(localPhone).getValue(User.class);
                    Toast.makeText(MainActivity.this, Boolean.parseBoolean(user.getIsStaff())+"", Toast.LENGTH_SHORT).show();
                    if (Boolean.parseBoolean(user.getIsStaff()))
                    {
                        if(user.getPassword().equals(localPassword)){
                            Toast.makeText(MainActivity.this, "logined", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(MainActivity.this,Home.class);
                            Common.currentUser=user;
                            startActivity(i);
                            mDialog.dismiss();
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                             }
                        }
                        else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Please login with staff account", Toast.LENGTH_SHORT).show();
                    }
                    }
                    else
                    {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User not exist in database", Toast.LENGTH_SHORT).show();
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void idlistner() {
        username= (TextView) findViewById(R.id.textview_number_login);
        password= (TextView) findViewById(R.id.textview_password_login);
    login= (Button) findViewById(R.id.button_sign_in);
    }
}
