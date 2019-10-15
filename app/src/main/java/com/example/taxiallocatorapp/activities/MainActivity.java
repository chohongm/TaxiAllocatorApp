package com.example.taxiallocatorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taxiallocatorapp.R;
import com.example.taxiallocatorapp.database.DataOperations;
import com.example.taxiallocatorapp.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    DataOperations dataOps;

    EditText et_username, et_password, et_cpassword;
    Button btn_register, btn_login;
    RadioGroup radio_user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        dataOps = new DataOperations(dbHelper);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        radio_user_type = findViewById(R.id.radio_user_type);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_cpassword.getText().toString();

                if(username.equals("") || password.equals("") || confirm_password.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields Required",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(confirm_password)){
                        if (!dataOps.isUsernameValid(username)) {
                            Toast.makeText(getApplicationContext(), "Username must be a " +
                                    "valid email address.", Toast.LENGTH_SHORT).show();
                        } else {

                            boolean usernameFree = dbHelper.getUser(username) == null;
                            if(usernameFree){
                                RadioButton selectedUserType = findViewById(
                                        radio_user_type.getCheckedRadioButtonId());
                                boolean isRider = selectedUserType.getText().equals("Rider");

                                long inserted = -1;
                                if (isRider) {
                                    inserted = dataOps.createUserAsRider(username, password);
                                } else {
                                    inserted = dataOps.createUserAsDriver(username, password);
                                }

                                if(inserted >= 0) {
                                    Toast.makeText(getApplicationContext(), "Registered",
                                            Toast.LENGTH_SHORT).show();
                                    et_username.setText("");
                                    et_password.setText("");
                                    et_cpassword.setText("");
                                    Intent intent = new Intent(getApplicationContext(),
                                            LoginActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Username already taken", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Password does not match",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
