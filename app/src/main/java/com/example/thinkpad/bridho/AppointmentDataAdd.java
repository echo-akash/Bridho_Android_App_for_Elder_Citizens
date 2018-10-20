package com.example.thinkpad.bridho;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppointmentDataAdd extends SQLiteOpenHelper {

    private static final String TAG = "DataAdd";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Reminder";
    private static final String TABLE_NAME = "Appointment";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDR = "address";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String[] COLUMNS = { KEY_ID, KEY_NAME, KEY_ADDR,
            KEY_DATE, KEY_TIME };

    public AppointmentDataAdd(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Appointment ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
                + "address TEXT, " + "date TEXT, " + "time TEXT )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public boolean addAppoint(Appointment appoint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, appoint.getName());
        values.put(KEY_ADDR, appoint.getAddress());
        values.put(KEY_DATE, appoint.getDate());
        values.put(KEY_TIME, appoint.getTime());
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
        return true;
    }

    public boolean updateAppoint(Appointment appoint, int selectedID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + KEY_NAME +
                " = '" + appoint.getName() + "'," + KEY_ADDR +
                " = '" + appoint.getAddress() + "'," + KEY_DATE +
                " = '" + appoint.getDate() + "'," + KEY_TIME +
                " = '" + appoint.getTime() + "'" + " WHERE " + KEY_ID + " = '" + selectedID + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + appoint.getName());
        db.execSQL(query);
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_NAME +
                " WHERE " + KEY_NAME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        /*String query = "DELETE * FROM " + TABLE_NAME + " WHERE "
                + KEY_ID + " = '" + id + "'" +
                " AND " + KEY_NAME + " = '" + name + "'";*/
        //Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        //db.execSQL(query);
        //return true;

        long del=db.delete(TABLE_NAME, KEY_ID + "="+id,null);
        db.close();
        return true;
    }

    public Cursor fetch(int selectedID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_NAME +","+ KEY_ADDR +","+ KEY_DATE +","+ KEY_TIME + " FROM " + TABLE_NAME +
                " WHERE " + KEY_ID + " = '" + selectedID + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
