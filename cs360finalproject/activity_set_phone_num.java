package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class activity_set_phone_num extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_layout); // sets content view layout to activity_sms_layout.xml configuration

        InventoryDB db = new InventoryDB(getApplicationContext()); // initializes the database

        String user = getIntent().getStringExtra("user"); // gets the username from the intent

        // initialize layout variables
        Button buttonSetNum = findViewById(R.id.buttonSetNum);
        EditText editTextPhoneNum = findViewById(R.id.editTextNumber);

        // gets the current phone number from the database and set it in the EditText
        String currentNum = db.getPhoneNumber(user);
        editTextPhoneNum.setText(currentNum);

        // sets click listener for SetNum button
        buttonSetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the editText for the phone # is empty or has spaces, do nothing
                if (editTextPhoneNum.getText().toString().isEmpty() || editTextPhoneNum.getText().toString().contains(" ")) {
                    return;
                }
                String phoneNumber = editTextPhoneNum.getText().toString(); // gets the phone number from the EditText field
                db.setPhoneNumber(user, phoneNumber); // sets the phone number in the database
                // After number is set, navigate back to InventoryActivity
                Intent intent = new Intent(activity_set_phone_num.this, InventoryActivity.class);
                intent.putExtra("user", user); // pass the username back to the next activity
                activity_set_phone_num.this.startActivity(intent);
            }
        });
    }
}
