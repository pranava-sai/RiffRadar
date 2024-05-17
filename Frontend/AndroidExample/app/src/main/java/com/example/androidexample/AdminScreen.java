package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * AdminScreen class represents the main activity for the administrator interface.
 * It provides functionalities for an administrator such as deleting events, users, changing email,
 * resetting passwords, and deleting payment information.
 */
public class AdminScreen extends AppCompatActivity {
    private Button deleteUser;
    private Button deleteEvent;
    private Button changeEmail;
    private Button resetPassword;
    private Button paymentDelete;

    /**
     * Initializes the activity's user interface and sets up event listeners for buttons.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        deleteEvent = findViewById(R.id.deleteEvents);
        deleteUser = findViewById(R.id.deleteUsers);
        changeEmail = findViewById(R.id.changeEmail);
        resetPassword = findViewById(R.id.resetPassword);
        paymentDelete = findViewById(R.id.deletePayInfo);

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminScreen.this, DeleteEvents.class);
                startActivity(intent);
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminScreen.this, DeleteUsers.class);
                startActivity(intent);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iChangeEmail = new Intent(AdminScreen.this, ChangeEmail.class);
                startActivity(iChangeEmail);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iResetPassword = new Intent(AdminScreen.this, ResetPassword.class);
                startActivity(iResetPassword);
            }
        });

        paymentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminScreen.this, DeletePayInfo.class);
                startActivity(i);
            }
        });
    }
}