package com.example.taxiallocatorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxiallocatorapp.R;
import com.example.taxiallocatorapp.classes.RideRequest;
import com.example.taxiallocatorapp.classes.Rider;
import com.example.taxiallocatorapp.database.DataOperations;
import com.example.taxiallocatorapp.database.DatabaseHelper;

public class RequestDetail extends AppCompatActivity {

    Intent intent;
    String username;
    Long riderId, driverId, requestId;

    DatabaseHelper dbHelper;
    DataOperations dataOps;

    RideRequest request;

    TextView tv_request_info;
    Button btn_accept;
    Button btn_request_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        intent = getIntent();
        username = intent.getStringExtra("username");
        riderId = intent.getLongExtra("riderId",-1);
        driverId = intent.getLongExtra("driverId", -1);
        requestId = intent.getLongExtra("requestId", -1);

        dbHelper = new DatabaseHelper(this);
        dataOps = new DataOperations(dbHelper);

        if (requestId < 0) {
            if (riderId < 0) {
                requestId = dbHelper.getDriver(driverId).getRequestId();
            } else {
                requestId = dbHelper.getRider(riderId).getRequestId();
            }
        }

        dbHelper = new DatabaseHelper(this);
        dataOps = new DataOperations(dbHelper);

        tv_request_info = findViewById(R.id.tv_request_info);
        btn_accept = findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataOps.acceptRideRequest(driverId, requestId) >= 0) {
                    finish();
                    startActivity(getIntent());

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to accept the ride " +
                            "request.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (riderId >= 0) {
            btn_accept.setVisibility(View.INVISIBLE);
        } else {
            if (dbHelper.getDriver(driverId).getRequestId() == null) {
                btn_accept.setVisibility(View.VISIBLE);

            } else {
                btn_accept.setVisibility(View.INVISIBLE);
            }
        }

        btn_request_list = findViewById(R.id.btn_request_list);
        btn_request_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RequestListActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("riderId", riderId);
                intent.putExtra("driverId", driverId);
                startActivity(intent);
            }
        });

        request = dbHelper.getRideRequest(requestId);

        StringBuilder content = new StringBuilder();
        content.append("Rider: ").append(dbHelper.getRider(request.getRiderId()).getUsername());
        content.append("\nRequested Datetime: ").append(
                dataOps.epochToStringConverter(request.getRequestTimestamp()));
        content.append("\nPickup Location: ").append(request.getLocation());

        Long requestDriverId = request.getDriverId();
        if (requestDriverId != null) {
            content.append("\nDriver: ").append(dbHelper.getDriver(requestDriverId).getUsername());
        } else {
            content.append("\nDriver: ").append("N/A");
        }
        content.append("\nAccepted Datetime: ").append(
                dataOps.epochToStringConverter(request.getAcceptedTimestamp()));
        content.append("\nCompleted Datetime: ").append(
                dataOps.epochToStringConverter(request.getCompletedTimestamp()));
        tv_request_info.setText(content.toString());
    }
}
