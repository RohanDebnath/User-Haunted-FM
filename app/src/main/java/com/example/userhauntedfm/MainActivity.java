package com.example.userhauntedfm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private CheckBox keepLoggedInCheckbox;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        emailEditText = findViewById(R.id.emaillogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        keepLoggedInCheckbox = findViewById(R.id.keepLoggedInCheckbox);
        Button loginButton = findViewById(R.id.loginBtn);
        TextView signUpTextView = findViewById(R.id.hyperlink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }

                // Sign in user with email and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User login successful
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    if (keepLoggedInCheckbox.isChecked()) {
                                        // Save login credentials to SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", email);
                                        editor.putString("password", password);
                                        editor.putBoolean("keepLoggedIn", true);
                                        editor.apply();
                                    }

                                    // Redirect to the logged-in activity
                                    Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // User login failed
                                    Toast.makeText(MainActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Check if the user is already logged in and if so, redirect to the logged-in activity
        boolean keepLoggedIn = sharedPreferences.getBoolean("keepLoggedIn", false);
        if (keepLoggedIn) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // User is already logged in
                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                startActivity(intent);
                finish();
            }
        }

        // Hyperlink setup
        String hyperlinkText = "Don't have an account? Register";
        SpannableString ss = new SpannableString(hyperlinkText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Redirect to the registration activity
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 23, hyperlinkText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpTextView.setText(ss);
        signUpTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
