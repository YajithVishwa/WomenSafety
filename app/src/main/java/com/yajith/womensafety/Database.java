package com.yajith.womensafety;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;
    String email=Login.email;
    String mobile=Login.mobileno;
    String flag="no";
    String fla="yes";
    String pass=Login.pass;
    public static String dbname="Women";
    public static int dbver=1;
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, null, dbver);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create="CREATE TABLE Women(ID INT NOT NULL PRIMARY KEY,EMAIL VARCHAR(20),MOBILE VARCHAR(20),PASS VARCHAR(20),LOGIN VARCHAR(20));";
        sqLiteDatabase.execSQL(create);
        insertfirst();
    }
    public void insertfirst()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ID",1);
        contentValues.put("EMAIL",email);
        contentValues.put("MOBILE",mobile);
        contentValues.put("PASS",pass);
        contentValues.put("LOGIN","no");
        sqLiteDatabase.insert(dbname,null,contentValues);
        sqLiteDatabase.close();
    }
    public void updatetick()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE Women SET LOGIN="+fla+" WHERE ID=1");
    }
    public void updateno()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE Women SET LOGIN = "+flag+" WHERE ID=1");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Women");
    }

}
