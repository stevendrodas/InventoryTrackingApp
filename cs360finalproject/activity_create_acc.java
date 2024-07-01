package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activity_create_acc extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acc); // sets the content view layout to activity_create_new_acc.xml configuration

        InventoryDB db = new InventoryDB(getApplicationContext()); // initializes the database

        // initialize layout variables
        Button create = findViewById(R.id.buttonCreateNewAcc);
        EditText editNewUsername = findViewById(R.id.editTextCreateAccUsername);
        EditText editNewPassword = findViewById(R.id.editTextCreateAccPassword);

        // sets click listener for create account button
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if username field is empty
                if (editNewUsername.getText().toString().isEmpty()) {
                    Toast.makeText(activity_create_acc.this, "Username is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // checks if the password field is empty
                if (editNewPassword.getText().toString().isEmpty()) {
                    Toast.makeText(activity_create_acc.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // checks if the username or password contains spaces
                if (editNewUsername.getText().toString().contains(" ") || editNewPassword.getText().toString().contains(" ")) {
                    Toast.makeText(activity_create_acc.this, "Username and password have spaces", Toast.LENGTH_SHORT).show();
                    return;
                }
                // checks if the username already exists in the database
                if (db.checkUsername(editNewUsername.getText().toString())) {
                    Toast.makeText(activity_create_acc.this, "Username already in database", Toast.LENGTH_SHORT).show();
                    // sets username and password component field text back to default
                    editNewUsername.setText("");
                    editNewPassword.setText("");
                }
                else {
                    // adds new user to the database
                    db.addUser(editNewUsername.getText().toString(), editNewPassword.getText().toString());
                    // navigates back to the main activity
                    Intent intent = new Intent(activity_create_acc.this, MainActivity.class);
                    activity_create_acc.this.startActivity(intent);
                }
            }
        });
    }
}
