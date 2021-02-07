package com.example.jchatandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.usernameTextViewId);
        passwordText = findViewById(R.id.passwordTextViewId);
        loginButton = findViewById(R.id.loginButton);
        Thread t=new Thread(){
            @Override
            public void run() {
                ChatClient.getInstance().connect();
            }
        };
        t.start();
    }

    public void handleLoginButton(View view) throws IOException {
        String username= usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if(username.trim().isEmpty() || password.trim().isEmpty()) return;
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    if(ChatClient.getInstance().login(username,password)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void handleSignUpText(View view) {
        Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
}