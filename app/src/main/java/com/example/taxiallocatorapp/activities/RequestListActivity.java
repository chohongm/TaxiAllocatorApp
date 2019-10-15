package com.example.taxiallocatorapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.taxiallocatorapp.R;
import com.example.taxiallocatorapp.classes.RideRequest;
import com.example.taxiallocatorapp.database.DataOperations;
import com.example.taxiallocatorapp.database.DatabaseHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestListActivity extends AppCompatActivity {

    Intent intent;
    String username;
    Long riderId, driverId;
    boolean isRider;

    DatabaseHelper dbHelper;
    DataOperations dataOps;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        intent = getIntent();
        username = intent.getStringExtra("username");
        riderId = intent.getLongExtra("riderId", -1);
        driverId = intent.getLongExtra("driverId", -1);
        isRider = (riderId >= 0) ? true : false;

        dbHelper = new DatabaseHelper(this);
        dataOps = new DataOperations(dbHelper);

        List<RideRequest> requests = dbHelper.getAllRideRequests();
        ListView lv = (ListView) findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(RequestListActivity.this, convertToMap(requests),
                R.layout.list_row, new String[]{"rider","requestedTimestamp","driver", "acceptedTimestamp"},
                new int[]{R.id.rider, R.id.requestedTimestamp, R.id.driver, R.id.acceptedTimestamp});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                RideRequest request = requests.get(position);
                intent = new Intent(getApplicationContext(), RequestDetail.class);
                intent.putExtra("username", username);
                intent.putExtra("riderId", riderId);
                intent.putExtra("driverId", driverId);
                intent.putExtra("requestId", request.getRequestId());
                startActivity(intent);
            }
        });

        Button logout = (Button)findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Map<String, String>> convertToMap(List<RideRequest> requests) {

        List<Map<String, String>> map = requests.stream().map(r -> {
            // Deal with nullable values.
            String driverValue = (r.getDriverId() != null) ?
                    dbHelper.getDriver(r.getDriverId()).getUsername() : "N/A";
            String acceptedTimestampValue = (r.getAcceptedTimestamp() != null) ?
                    dataOps.epochToStringConverter(r.getAcceptedTimestamp()) : "N/A";

            Map<String, String> temp = new HashMap<>();

            temp.put("rider", dbHelper.getRider(r.getRiderId()).getUsername());
            temp.put("requestedTimestamp", dataOps.epochToStringConverter(r.getRequestTimestamp()));
            temp.put("driver", driverValue);
            temp.put("acceptedTimestamp", acceptedTimestampValue);
            return temp;
        }).collect(Collectors.toList());

        return map;
    }


}
