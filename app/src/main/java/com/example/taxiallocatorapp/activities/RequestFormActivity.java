package com.example.taxiallocatorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taxiallocatorapp.R;
import com.example.taxiallocatorapp.database.DataOperations;
import com.example.taxiallocatorapp.database.DatabaseHelper;

public class RequestFormActivity extends AppCompatActivity {

    Intent intent;
    String username;
    Long riderId, driverId;

    DatabaseHelper dbHelper;
    DataOperations dataOps;

    EditText et_location;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);

        intent = getIntent();
        username = intent.getStringExtra("username");
        riderId = intent.getLongExtra("riderId", -1);
        driverId = intent.getLongExtra("driverId", -1);

        dbHelper = new DatabaseHelper(this);
        dataOps = new DataOperations(dbHelper);

        et_location = (EditText) findViewById(R.id.et_location);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = et_location.getText().toString();
                if (location.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "You must provide a pick up location.", Toast.LENGTH_SHORT).show();

                } else if (location.length() > 100) {
                    Toast.makeText(getApplicationContext(),
                            "Location length cannot be greater than 100.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    long requestId = dataOps.createRideRequest(riderId, location);
                    Intent intent = new Intent(getApplicationContext(),
                            RequestListActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("requestId", requestId);
                    if (riderId >= 0) {
                        intent.putExtra("riderId", riderId);
                    } else {
                        intent.putExtra("driverId", driverId);
                    }
                    startActivity(intent);
                }
            }
        });


    }
}
