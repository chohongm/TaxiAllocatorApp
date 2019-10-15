package com.example.taxiallocatorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taxiallocatorapp.R;
import com.example.taxiallocatorapp.classes.Driver;
import com.example.taxiallocatorapp.classes.Rider;
import com.example.taxiallocatorapp.classes.User;
import com.example.taxiallocatorapp.database.DataOperations;
import com.example.taxiallocatorapp.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    Button btn_lregister, btn_llogin;
    EditText et_lusername, et_lpassword;

    DatabaseHelper dbHelper;
    DataOperations dataOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        dataOps = new DataOperations(dbHelper);

        et_lusername = (EditText)findViewById(R.id.et_lusername);
        et_lpassword = (EditText)findViewById(R.id.et_lpassword);

        btn_llogin = (Button)findViewById(R.id.btn_llogin);
        btn_lregister = (Button)findViewById(R.id.btn_lregister);

        btn_lregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_lusername.getText().toString();
                String password = et_lpassword.getText().toString();

                if (!dataOps.isUsernameValid(username)) {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty and " +
                            "must be a valid email address.", Toast.LENGTH_SHORT).show();
                }

                User user = dbHelper.getUser(username);
                if(user != null && user.getPassword().equals(password)){
                    Toast.makeText(getApplicationContext(), "Login Successful",
                            Toast.LENGTH_SHORT).show();
                    Long riderId = user.getRiderId();
                    Long driverId = user.getDriverId();

                    // User is a rider.
                    if (riderId != null) {
                        Rider rider = dbHelper.getRider(riderId);
                        if (rider == null) {
                            Toast.makeText(getApplicationContext(), "Could not load the " +
                                            "rider with rider id.", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent;
                            Long requestId = rider.getRequestId();
                            if (requestId == null) {
                                intent = new Intent(getApplicationContext(), RequestFormActivity.class);

                            } else {
                                intent = new Intent(getApplicationContext(), RequestListActivity.class);
                            }
                            intent.putExtra("username", username);
                            intent.putExtra("riderId", riderId);
                            startActivity(intent);
                        }

                    // User is a driver.
                    } else {
                        Driver driver = dbHelper.getDriver(driverId);
                        if (driver == null) {
                            Toast.makeText(getApplicationContext(), "Could not load the " +
                                    "driver with driverId.", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent;
                            Long requestId = driver.getRequestId();
                            if (requestId == null) {
                                intent = new Intent(getApplicationContext(), RequestListActivity.class);

                            } else {
                                intent = new Intent(getApplicationContext(), RequestDetail.class);
                            }
                            intent.putExtra("username", username);
                            intent.putExtra("driverId", driverId);
                            startActivity(intent);
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Invalid username or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
