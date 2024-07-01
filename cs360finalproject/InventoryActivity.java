package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class InventoryActivity extends AppCompatActivity implements RecyclerViewAdapter.AdapterCallback{

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1; // request code for SMS permission
    private RecyclerView recyclerView;
    ArrayList<String> name, quantity;
    InventoryDB db;
    RecyclerViewAdapter adapter;
    String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list); // sets the content view layout to activity_inventory_list.xml configuration

        db = new InventoryDB(this); // initialize the database
        name = new ArrayList<>();
        quantity = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView); // initialize RecyclerView
        user = getIntent().getStringExtra("user"); // gets the username from the intent
        adapter = new RecyclerViewAdapter(this, name, quantity, user); // initializes adapter
        recyclerView.setAdapter(adapter); // sets adapter to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // sets layout manager for RecyclerView

        Button add = findViewById(R.id.buttonAddItem); // initialize add item button
        Button settings = findViewById(R.id.buttonSMS); // initialize settings button
        displayInfo(); // display inventory items

        // checks manifest for SMS perms. if false, requests permission from user
        if (!checkPermission(android.Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        // sets click listener for add item button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryActivity.this, AddItemActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        // sets click listener for SMS button
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryActivity.this, activity_set_phone_num.class);
                intent.putExtra("user", user);
                InventoryActivity.this.startActivity(intent);
            }
        });
    }

    // Override method to send SMS when item quantity is low
    @Override
    public void sendSMSCallback(String itemName) {
        if (checkPermission(android.Manifest.permission.SEND_SMS)) {
            onSend(null, itemName);
        }
    }

    // method to send SMS notification
    public void onSend(View v, String itemName) {
        String phoneNum = db.getPhoneNumber(user); // gets the phone number from the database
        String smsMessage = itemName + " quantity is low. Reorder more!"; // actual SMS message content

        // if phone number is null or empty, do nothing
        if (phoneNum == null || phoneNum.isEmpty()){
            return;
        }

        // sends user SMS notification if SMS permission is granted
        if (checkPermission(android.Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNum, null, smsMessage, null, null);
        }
    }

    // method to check if a specific permission is granted
    private boolean checkPermission(String sendPermission) {
        int check = ContextCompat.checkSelfPermission(this, sendPermission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    // method to display inventory items
    private void displayInfo() {
        Cursor cursor = db.getData(); // gets data from the database
        name.clear();
        quantity.clear();
        // if not items exist, shows a toast message alerting user
        if(cursor.getCount() == 0) {
            Toast.makeText(InventoryActivity.this, "No Item Entries Exist", Toast.LENGTH_SHORT).show();
        } else {
            // adds items to the lists
            while (cursor.moveToNext()) {
                name.add(cursor.getString(1));
                quantity.add(cursor.getString(2));
            }
        }

        // closes the cursor and notifies the adapter of the data changes
        cursor.close();
        adapter.notifyDataSetChanged();
    }



}
