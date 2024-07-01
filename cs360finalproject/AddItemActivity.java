package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cs360finalproject.InventoryActivity;
import com.example.cs360finalproject.InventoryDB;
import com.example.cs360finalproject.R;

public class AddItemActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item); // sets the content view layout to activity_add_new_item.xml configuration

        InventoryDB db = new InventoryDB(getApplicationContext()); // initialize the database

        // initialize layout variables
        Button addItem = findViewById(R.id.buttonAddNewItem);
        EditText textViewName = findViewById(R.id.editTextAddItemName);
        EditText textViewQuantity = findViewById(R.id.editTextAddItemQuantity);

        String user = getIntent().getStringExtra("user"); // gets the username from the intent

        // sets click listener for the add item button
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = textViewName.getText().toString(); // gets the item name from the EditText component
                // checks if the item name field is empty
                if (textViewName.getText().toString().isEmpty()) {
                    Toast.makeText(AddItemActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // checks if the quantity field is empty
                if (textViewQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(AddItemActivity.this, "Quatity is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // checks if the item already exists in the database
                if (!db.checkItem(itemName)) {
                    // adds the item to the database
                    db.addItem(itemName, textViewQuantity.getText().toString());
                    // navigates back to InventoryActivity and passes the username
                    Intent intent = new Intent(AddItemActivity.this, InventoryActivity.class);
                    intent.putExtra("user", user);
                    AddItemActivity.this.startActivity(intent);
                }
                else {
                    // shows error message if the item already exists
                    Toast.makeText(AddItemActivity.this, "Item already exists.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
