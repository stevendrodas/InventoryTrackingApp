package com.example.cs360finalproject;

// Developer: Steven Rodas (contact@stevenrodas.com)
// Project: Inventory Tracking App
// 06/30/2024

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1; // constant to request SMS permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InventoryDB db = new InventoryDB(getApplicationContext()); // initialize database

        // initialize UI elements
        Button login = findViewById(R.id.buttonLogInSubmit);
        Button create = findViewById(R.id.buttonCreateAcc);
        EditText editUsername = findViewById(R.id.editTextUsername);
        EditText editPassword = findViewById(R.id.edittTextPassword);

        // sets click listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets username and password from EditText UI fields
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                // checks if username and password feels are empty.
                if (!username.isEmpty() && !password.isEmpty()) {
                    Boolean isUser = db.checkLoginCred(username, password); // validates user credentials on login/signup
                    if(isUser) {
                        // if valid, navigates to InventoryActivity and passes the username
                        Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                        intent.putExtra("user", username);
                        MainActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show(); // show error message if credentials are incorrect
                    }
                } else {
                    Log.d("Login", "Login Failed, please try again!"); // logs error message if fields are empty
                }
            }
        });

        // sets click listener for create account button
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigates to activity_create_acc activity upon button click
                Intent intent = new Intent(MainActivity.this, activity_create_acc.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    // method to check if a specific permission is granted
    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}