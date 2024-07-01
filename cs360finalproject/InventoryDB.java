package com.example.cs360finalproject;
// Developer: Steven Rodas (contact@stevenrodas.com)
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class InventoryDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "inventory.db"; // database name
    Context context;

    public InventoryDB(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    // constants for UserLoginTable
    private static final class UserLoginTable {
        private static final String TABLE = "login";
        private static final String ID_COLUMN = "_id";
        private static final String USERNAME_COLUMN = "username";
        private static final String PASS_COLUMN = "password";
        private static final String NUMBER_COL = "number"; // number as in user's phone number
    }

    // constants for InventoryTable
    private static final class InventoryTable {
        private static final String TABLE = "inventory";
        private static final String ID_COLUMN = "_id";
        private static final String NAME_COLUMN = "name";
        private static final String QUANTITY_COLUMN = "quantity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creates UserLoginTable
        db.execSQL("create table " + UserLoginTable.TABLE + " (" +
                UserLoginTable.ID_COLUMN + " integer primary key autoincrement, " +
                UserLoginTable.USERNAME_COLUMN + " text, " +
                UserLoginTable.PASS_COLUMN + " text," +
                UserLoginTable.NUMBER_COL + " text)");

        // Create InventoryTable
        db.execSQL("create table " + InventoryTable.TABLE + " (" +
                InventoryTable.ID_COLUMN + " integer primary key autoincrement, " +
                InventoryTable.NAME_COLUMN + " text, " +
                InventoryTable.QUANTITY_COLUMN + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drops existing tables if they exist and create new ones
        db.execSQL("drop table if exists " + UserLoginTable.TABLE);
        db.execSQL("drop table if exists " + InventoryTable.TABLE);
        onCreate(db);
    }

    // functions to add a new user to the UserLoginTable
    public long addUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserLoginTable.USERNAME_COLUMN, username);
        values.put(UserLoginTable.PASS_COLUMN, password);

        // returns the ID of the new user
        return db.insert(UserLoginTable.TABLE, null, values);
    }

    // function to check user login credentials
    public Boolean checkLoginCred(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        //moves cursor to first row of table query
        String sql = "select * from " + UserLoginTable.TABLE + " where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {username, password});

        return cursor.moveToFirst();
    }

    // function to check if a username exists in the UserLoginTable
    public Boolean checkUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        //moves cursor to first row of table query
        String sql = "select * from " + UserLoginTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {username});

        return cursor.moveToFirst();
    }

    // function to SET the phone number for a user
    public void setPhoneNumber(String user, String number) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserLoginTable.NUMBER_COL, number);

        String userPosition = UserLoginTable.USERNAME_COLUMN + " = ?";
        String [] userArgs = new String[] {user};

        db.update(UserLoginTable.TABLE, values, userPosition, userArgs);
    }

    // function to GET the phone number from a user
    public String getPhoneNumber (String user) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + UserLoginTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {user});

        cursor.moveToFirst();
        return cursor.getString(3);
    }

    // function to add an item to the InventoryTable
    public long addItem(String name, String quantity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryTable.NAME_COLUMN, name);
        values.put(InventoryTable.QUANTITY_COLUMN, quantity);

        return db.insert(InventoryTable.TABLE, null, values);
    }

    // function to check if an item exists in the InventoryTable
    public Boolean checkItem(String name) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + InventoryTable.TABLE + " where name = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {name});

        return cursor.moveToFirst();
    }

    // function to increment the quantity of an item in the InventoryTable
    public String incrementQuantity(String name) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + InventoryTable.TABLE + " where name =?";
        Cursor cursor = db.rawQuery(sql, new String[] {name});
        cursor.moveToFirst();
        String quantity = cursor.getString(2);
        Integer newIntQuantity = (Integer.parseInt(quantity)) + 1;
        String newQuantity = newIntQuantity.toString();

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryTable.QUANTITY_COLUMN, newQuantity);

        String userPosition = InventoryTable.NAME_COLUMN + " = ?";
        String [] userArgs = new String[] {name};

        db.update(InventoryTable.TABLE, values, userPosition, userArgs);

        return newQuantity;
    }

    // function to decrement the quantity of an item in the InventoryTable
    public String decrementQuantity(String name) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + InventoryTable.TABLE + " where name = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {name});
        cursor.moveToFirst();
        String quantity = cursor.getString(2);
        Integer intQuantity = (Integer.parseInt(quantity));

        if(intQuantity <= 0) {
            intQuantity = 0;
        } else {
            intQuantity -= 1;
        }
        String newQuantity = intQuantity.toString();

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryTable.QUANTITY_COLUMN, newQuantity);

        String userPosition = InventoryTable.NAME_COLUMN + " = ?";
        String [] userArgs = new String[] {name};

        db.update(InventoryTable.TABLE, values, userPosition, userArgs);

        return newQuantity;
    }

    // function to check if the quantity of an item is low (or finished)
    public Boolean checkItemQuantityLow (String name) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + InventoryTable.TABLE + " where name = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {name});
        cursor.moveToFirst();
        String quantity = cursor.getString(2);
        Integer intQuantity = (Integer.parseInt(quantity));

        return intQuantity <= 0;
    }

    // function to delete an item from the InventoryTable
    public void deleteItem(String name) {
        SQLiteDatabase db = getWritableDatabase();

        String userPosition = InventoryTable.NAME_COLUMN + " = ?";
        String [] userArgs = new String[] {name};

        db.delete(InventoryTable.TABLE, userPosition, userArgs);
    }

    // function to GET the name of an item at a specific index
    public String getName(Integer index) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + InventoryTable.TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        cursor.move(index);

        return cursor.getString(0);
    }

    // function to get all data from the InventoryTable
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + InventoryTable.TABLE, null);
    }


}
