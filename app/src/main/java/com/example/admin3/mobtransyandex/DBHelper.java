package com.example.admin3.mobtransyandex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String CREATE_NOTEBOOK = "create table Notebook("
            + "id integer primary key autoincrement,"
            + "input text,"
            + "output text,"
            + "dirs text,"
            + "fav integer)";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTEBOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Notebook");
        sqLiteDatabase.execSQL(CREATE_NOTEBOOK);
    }
}
