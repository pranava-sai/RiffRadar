package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;

    private EditText nameEditText;

    private LinearLayout addressLayout;
    private LinearLayout descriptionLayout;
    private LinearLayout capacityLayout;
    private LinearLayout genreLayout;
    private Button selectImg;

    private Button signupButton;        // define signup button variable

    private Spinner userType;           // define user type variable

    private String userCategory;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        /* initialize UI elements */
        emailEditText = findViewById(R.id.email);  // link to username edtext in the Signup activity XML
        passwordEditText = findViewById(R.id.password);  // link to password edtext in the Signup activity XML
        confirmEditText = findViewById(R.id.confirm_password);    // link to confirm edtext in the Signup activity XML
        userType = findViewById(R.id.spinner7); // Dropdown for user type selection
        signupButton = findViewById(R.id.signup_button);
        nameEditText = findViewById(R.id.name);// link to signup button in the Signup activity XML
        selectImg = findViewById(R.id.selectImageButton);
        addressLayout = findViewById(R.id.addressLayout);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        capacityLayout = findViewById(R.id.capacityLayout);
        genreLayout = findViewById(R.id.genreLayout);


    String[] userArray = {"Select Category", "Attendee", "Band", "Venue"};
    ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userArray);
        userType.setAdapter(userAdapter);

        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

    {
        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        userCategory = parent.getItemAtPosition(position).toString();
        // Handle visibility based on userCategory here if needed
        handleVisibility();


    }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){

    }

        private void handleVisibility () {
            if (userCategory.equals("Band")) {
                addressLayout.setVisibility(View.VISIBLE);
                descriptionLayout.setVisibility(View.VISIBLE);
                capacityLayout.setVisibility(View.GONE);
                genreLayout.setVisibility(View.VISIBLE);
            } else if (userCategory.equals("Venue")) {
                addressLayout.setVisibility(View.VISIBLE);
                descriptionLayout.setVisibility(View.VISIBLE);
                capacityLayout.setVisibility(View.VISIBLE);
                genreLayout.setVisibility(View.GONE);
            } else  {
                addressLayout.setVisibility(View.GONE);
                descriptionLayout.setVisibility(View.GONE);
                capacityLayout.setVisibility(View.GONE);
                genreLayout.setVisibility(View.GONE);
            }
        }
    });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String user = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                if (password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), userCategory, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
