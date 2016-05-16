package com.example.administrator.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/12.
 */
public class NoteDB extends SQLiteOpenHelper{

    public  static final String TABLE_NOTES = "notes";
    public  static final String TABLE_MEDIA = "media";

    public  static final String COLUMN_ID = "_id";
    public  static final String COLUMN_NOTE_NAME = "name";
    public  static final String COLUMN_NOTE_CONTENT = "content";
    public  static final String COLUMN_NOTE_DATE = "date";


    public  static final String COLUMN_MEDIA_PATH = "path";
    public  static final String COLUMN_MEDIA_OWNER_ID = "ower_id";

    public NoteDB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NOTES + " (" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_NOTE_NAME + " text not null default \"\"," +
                COLUMN_NOTE_CONTENT  + " text not null default \"\"," +
                COLUMN_NOTE_DATE + " text not null default \"\"" + ")"
        );

        db.execSQL("create table " + TABLE_MEDIA + " (" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_NOTE_NAME + " text not null default \"\"," +
                COLUMN_MEDIA_PATH  + " text not null default \"\"," +
                COLUMN_MEDIA_OWNER_ID + " integer not null default 0" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
