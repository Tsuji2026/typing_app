package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "typing_database.db"; // データベース名
    private static final int DATABASE_VERSION = 1; //データベース更新時にバージョンの値を上げていくこと
    private static final String TABLE_NAME_TYPING = "typing"; //生成するテーブル名
    private static final String TYPING_COLUMN_ID = "text_data_id"; // テーブル内の属性1
    private static final String TYPING_COLUMN_STR = "text_data_str";// テーブル内の属性2
    private static final String TYPING_COLUMN_LEN = "text_data_len";// テーブル内の属性3

    //コンストラクタを定義
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // データベースが初めて作成されたときに呼び出される
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME_TYPING + " (" +
                TYPING_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TYPING_COLUMN_STR + " TEXT, " +
                TYPING_COLUMN_LEN + " INTEGER)";
        db.execSQL(createTableQuery);
    }


    //データベースのバージョンが変更されたときに呼び出されるテーブルをアップグレードするメソッド
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TYPING);
        onCreate(db);
    }

    //データベースからすべてのユーザーを取得するメソッド
    public int getAllTyping(List<ListData> list) {
        int max = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_TYPING, null, null, null, null, null, null);

        list.clear();
        while (cursor.moveToNext()) {
            ListData add_data = new ListData();

            int tmp_Id = cursor.getInt(cursor.getColumnIndexOrThrow(TYPING_COLUMN_ID));
            String tmp_TextData_str = cursor.getString(cursor.getColumnIndexOrThrow(TYPING_COLUMN_STR));
            int tmp_TextData_len = cursor.getInt(cursor.getColumnIndexOrThrow(TYPING_COLUMN_LEN));
            add_data.setId(tmp_Id);
            add_data.setTextData_str(tmp_TextData_str);
            add_data.setTextData_len(tmp_TextData_len);
            list.add(add_data);
            max++;
        }
        cursor.close();
        db.close();
        return max;
    }

    public long setTyping(int id, String str, int len) {
        ContentValues val = new ContentValues();
        SQLiteDatabase db = getReadableDatabase();
        long newRowId = 0;

        val.put(TYPING_COLUMN_ID, id);
        val.put(TYPING_COLUMN_STR, str);
        val.put(TYPING_COLUMN_LEN, len);

        newRowId = db.insert(TABLE_NAME_TYPING, null, val);

        return newRowId;
    }

    public int deleteTyping(int del_id){
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(TABLE_NAME_TYPING, TYPING_COLUMN_ID+"=?", new String[]{String.valueOf(del_id)});
    }

}

