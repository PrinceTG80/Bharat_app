package com.example.bharat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bharat.Model.Users;
import com.example.bharat.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login_Activity extends AppCompatActivity {

    private EditText InputNumber,InputPassword;
    private Button Loginbtn;

    private ProgressDialog loadingBar;
    private TextView Adminlink;

    private String parentDbName = "Users";
    private CheckBox chkBoxLoggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        Loginbtn = (Button) findViewById(R.id.login_btn);
        InputNumber = (EditText) findViewById(R.id.login_mobile_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        Adminlink = (TextView) findViewById(R.id.admin_panel_link);

        loadingBar = new ProgressDialog(this);

        chkBoxLoggedin = (CheckBox) findViewById(R.id.Remember_me_chkb);
        Paper.init(this);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        Adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("TG_innovator");
                Adminlink.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });


    }

    private void LoginUser()
    {
        String mobile = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(mobile))
        {
            Toast.makeText(this, "Please Enter your #Mobile_Number",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your #Password",Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Have patience, while the credentials are being checked...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(mobile,password);
        }

    }

    private void AllowAccessToAccount(String mobile, String password)
    {
        if (chkBoxLoggedin.isChecked())
        {
            Paper.book().write(Prevalent.UserMobileKey, mobile);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(mobile).exists())
                {
                    Users userData = dataSnapshot.child(parentDbName).child(mobile).getValue(Users.class);

                    if (userData.getMobile().equals(mobile))
                    {
                        if (userData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(Login_Activity.this, "Welcome Admin, You Logged in Successfully!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login_Activity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }

                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(Login_Activity.this, "Logged in Successfully!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                                startActivity(intent);
                            }
                        }

                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(Login_Activity.this,"Password is incorrect!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Login_Activity.this,"Account with credential: "+mobile +" number do not exist..",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}