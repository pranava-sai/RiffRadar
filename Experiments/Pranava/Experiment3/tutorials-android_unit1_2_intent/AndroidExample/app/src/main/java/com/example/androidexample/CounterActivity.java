package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button increaseBtn0; // define increase button variable
    private Button decreaseBtn0; // define decrease button variable

    private Button increaseBtn1; // define increase button variable
    private Button decreaseBtn1; // define decrease button variable

    private Button increaseBtn2; // define increase button variable
    private Button decreaseBtn2; // define decrease button variable

    private Button increaseBtn3; // define increase button variable
    private Button decreaseBtn3; // define decrease button variable

    private Button increaseBtn5; // define increase button variable
    private Button decreaseBtn5; // define decrease button variable

    private Button increaseBtn8; // define increase button variable
    private Button decreaseBtn8; // define decrease button variable

    private Button increaseBtn13; // define increase button variable
    private Button decreaseBtn13; // define decrease button variable
    private Button backBtn;     // define back button variable

    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        increaseBtn0 = findViewById(R.id.counter_increase_btn0);
        decreaseBtn0 = findViewById(R.id.counter_decrease_btn0);
        increaseBtn1 = findViewById(R.id.counter_increase_btn1);
        decreaseBtn1 = findViewById(R.id.counter_decrease_btn1);
        increaseBtn2 = findViewById(R.id.counter_increase_btn2);
        decreaseBtn2 = findViewById(R.id.counter_decrease_btn2);
        increaseBtn3 = findViewById(R.id.counter_increase_btn3);
        decreaseBtn3 = findViewById(R.id.counter_decrease_btn3);
        increaseBtn5 = findViewById(R.id.counter_increase_btn5);
        decreaseBtn5 = findViewById(R.id.counter_decrease_btn5);
        increaseBtn8 = findViewById(R.id.counter_increase_btn8);
        decreaseBtn8 = findViewById(R.id.counter_decrease_btn8);
        increaseBtn13 = findViewById(R.id.counter_increase_btn13);
        decreaseBtn13 = findViewById(R.id.counter_decrease_btn13);

        backBtn = findViewById(R.id.counter_back_btn);

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=0));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=0));
            }
        });

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=1));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=1));
            }
        });

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=2));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=2));
            }
        });

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=3));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=3));
            }
        });

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=5));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=5));
            }
        });

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=8));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=8));
            }
        });

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter+=13));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(counter-=13));
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

    }
}