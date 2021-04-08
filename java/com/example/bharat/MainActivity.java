package com.example.bharat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bharat.Model.Users;
import com.example.bharat.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button Registerbtn, Loginbtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Registerbtn = (Button) findViewById(R.id.main_register_btn);
        Loginbtn = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this ,Login_Activity.class);
                startActivity(intent);
            }
        });

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this ,Register_Activity.class);
                startActivity(intent);
            }
        });

        String UserMobileKey = Paper.book().read(Prevalent.UserMobileKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserMobileKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserMobileKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserMobileKey, UserPasswordKey);

                loadingBar.setTitle("Already logged in!!!");
                loadingBar.setMessage("Have patience..");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String mobile, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(mobile).exists())
                {
                    Users userData = dataSnapshot.child("Users").child(mobile).getValue(Users.class);

                    if (userData.getMobile().equals(mobile))
                    {
                        if (userData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this,"Already Logged in!!",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this ,Home_Activity.class);
                            startActivity(intent);
                        }

                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Account with credential: "+mobile +" number do not exist..",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}