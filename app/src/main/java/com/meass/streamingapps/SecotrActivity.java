package com.meass.streamingapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecotrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secotr);
    }

    public void hostt(View view) {
        startActivity(new Intent(getApplicationContext(),HostRegisteration.class));
    }

    public void user(View view) {
        startActivity(new Intent(getApplicationContext(),Register2.class));
    }
}