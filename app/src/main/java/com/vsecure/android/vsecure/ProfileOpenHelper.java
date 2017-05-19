package com.vsecure.android.vsecure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ProfileOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PROFILE_DB";
    public static final String TABLE_NAME = "PROFILE_TABLE";
    public static final int VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String FNAME = "F_NAME";
    public static final String LNAME = "L_NAME";
    public static final String DOB = "DOB";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";

    public static final String SCRIPT2 = "create table " + TABLE_NAME + " ("
            + KEY_ID + " integer primary key autoincrement, " + FNAME
            + " text not null, " + LNAME + " text not null, " + DOB + " text not null, " + EMAIL
            + " text not null, " + PHONE + " text not null );";

    public ProfileOpenHelper(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //db.execSQL(SCRIPT);
        db.execSQL(SCRIPT2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }

}

