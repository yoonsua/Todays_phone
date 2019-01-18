package com.example.shinsoyoung.todays_phone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ShinSoyoung on 2017-09-24.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String dbName = "MyDB";

    //DbOpenHelper 생성자
    public DBOpenHelper(Context context) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBases.myTable._CREATE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBases.myTable.tableName);
        onCreate(db);
    }

}
