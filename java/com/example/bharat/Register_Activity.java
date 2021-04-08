package com.example.bharat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.app.ProgressDialog.show;

public class Register_Activity extends AppCompatActivity {

    private Button CreateAccountbtn;
    private EditText InputName,InputMobileNumber,InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        CreateAccountbtn = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputMobileNumber = (EditText) findViewById(R.id.register_mobile_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        CreateAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });


    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String mobile = InputMobileNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Enter your #Username",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(mobile))
        {
            Toast.makeText(this, "Please Enter your #Mobile_Number",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your #Password",Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Have patience, while the credentials are being added...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateMobileNumber(name,mobile,password);
        }

    }

    private void ValidateMobileNumber(String name, String mobile, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(mobile).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("mobile",mobile);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(mobile).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Register_Activity.this,"Congrats, Succesful Account Creation",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Register_Activity.this ,Login_Activity.class);
                                        startActivity(intent);
                                    }

                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Register_Activity.this,"Network Error!!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Register_Activity.this, "This " + mobile + " already exist..",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Register_Activity.this, "Please Try Again with another mobile number...",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register_Activity.this ,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}