package com.example.jchatandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameText = this.findViewById(R.id.usernameTextViewId);
        passwordText = this.findViewById(R.id.passwordTextViewId);
        signUpButton = this.findViewById(R.id.signUpButton);
    }

    public void handleSignUptButton(View view) {
        String username= usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if(username.trim().isEmpty() || password.trim().isEmpty()) return;
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    if(ChatClient.getInstance().signUp(username,password)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
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

    public void handleLoginText(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}