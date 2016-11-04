package com.example.amrit.french.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.amrit.french.list.Word;

import java.util.ArrayList;

/**
 * Created by Amrit on 26-10-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "task_management";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "word_employee";
    public static final String ID_FIELD = "_id";
    public static final String FRENCH_FIELD = "French";
    public static final String MEANING_FIELD = "Meaning";
    public Context context;

    public static final String EMPLOYEE_TABLE_SQL = "CREATE TABLE "+TABLE_NAME+" " +
            "("+ID_FIELD+" INTEGER PRIMARY KEY, "+ FRENCH_FIELD +" TEXT UNIQUE, "+ MEANING_FIELD +" TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EMPLOYEE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertWord(Word employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRENCH_FIELD, employee.getFrench());
        contentValues.put(MEANING_FIELD, employee.getMeaning());
        long inserted = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return inserted;
    }

    public ArrayList<Word> getAllWord() {
        ArrayList<Word> allEmployees = new ArrayList<Word>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(ID_FIELD)));
                String french = cursor.getString(cursor.getColumnIndex(FRENCH_FIELD));
                String meaning = cursor.getString(cursor.getColumnIndex(MEANING_FIELD));

                Word e = new Word(id, french, meaning);
                allEmployees.add(e);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return allEmployees;
    }

    private void toastIt(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
