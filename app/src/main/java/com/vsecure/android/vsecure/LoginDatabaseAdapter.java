package com.vsecure.android.vsecure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class LoginDatabaseAdapter {
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_ID = "id";
    public static final String CONTACTS_NAME = "name";
    public static final String CONTACTS_PHONE = "phone";
    public static final String CONTACTS_RELATION = "relation";
    public static final int NAME_COLUMN = 1;
    static final String DATABASE_CREATE = "create table " + "contacts" + "( "
            + "id" + " integer primary key autoincrement,"+ "name  text,phone text,relation text); ";
    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    public LoginDatabaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public LoginDatabaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertEntry(String userName, String userPhone, String userRelation) {
        ContentValues newValues = new ContentValues();
        newValues.put("name", userName);
        newValues.put("phone", userPhone);
        newValues.put("relation", userRelation);
        db.insert("contacts", null, newValues);
    }

    public void addContact(UserInformation userInformation){
            db = dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("name", userInformation.getName());
        newValues.put("phone", userInformation.getPhone());
        newValues.put("relation", userInformation.getRelation());
        db.insert("contacts", null, newValues);
        db.close();
    }

    public UserInformation getContact(int id){
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contacts", new String[]{CONTACTS_ID, CONTACTS_NAME, CONTACTS_PHONE,CONTACTS_RELATION},CONTACTS_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor!=null){
            cursor.moveToFirst();
        }
        UserInformation userInformation = new UserInformation(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),cursor.getString(2),cursor.getString(3));
            return userInformation;
    }

    public Cursor getCursorContact(){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("contacts", new String[]{CONTACTS_NAME, CONTACTS_PHONE,CONTACTS_RELATION},CONTACTS_NAME,null,null,null,null);

        if (cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getListData(){
        db = dbHelper.getReadableDatabase();
        String str = "select * from contacts";
        Cursor cr = db.rawQuery(str,null);
        return cr;
    }


    public List<UserInformation> getAllContacts() {
        List<UserInformation> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM contacts" ;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInformation userInformation = new UserInformation();
                userInformation.setId(Integer.parseInt(cursor.getString(0)));
                userInformation.setName(cursor.getString(1));
                userInformation.setPhone(cursor.getString(2));
                // Adding contact to list
                contactList.add(userInformation);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public int deleteEntry(String UserName) {

        String where = "USERNAME=?";
        int numberOFEntriesDeleted = db.delete("LOGIN", where,
                new String[] { UserName });
        return numberOFEntriesDeleted;
    }

    public String getSinlgeEntry(String userName) {
        Cursor cursor = db.query("LOGIN", null, " USERNAME=?",
                new String[] { userName }, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    public void updateEntry(String userName, String password) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD", password);

        String where = "USERNAME = ?";
        db.update("LOGIN", updatedValues, where, new String[] { userName });
    }
}
