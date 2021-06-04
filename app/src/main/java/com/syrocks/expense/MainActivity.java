package com.syrocks.expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgetPassword;
    private TextView mSignupHere;
    private ProgressDialog mDialog;

    //  Firebase...

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        mDialog = new ProgressDialog(this);

        loginDetails();
    }

    private void loginDetails() {

        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        mForgetPassword = findViewById(R.id.forget_password);
        mSignupHere = findViewById(R.id.signup_reg);

        //  Login button functioning
        btnLogin.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String pass = mPass.getText().toString().trim();

            if(TextUtils.isEmpty(email)) {
                mEmail.setError("Email Required!!");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                mPass.setError("Password required!!");
                return;
            }

            mDialog.setMessage("Processing...");
            mDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Login Successful...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

        //  Registration activity
        mSignupHere.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));

        });

        //  Reset password activity
        mForgetPassword.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(),ResetActivity.class));

        });

    }

}