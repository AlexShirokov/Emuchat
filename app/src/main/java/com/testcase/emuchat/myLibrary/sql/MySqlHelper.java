package com.testcase.emuchat.myLibrary.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class MySqlHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLHelper";
    private IBaseConfig baseConfig;
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

    public MySqlHelper(Context context, IBaseConfig baseConfig) {
        super(context, baseConfig.getDBName(), null, baseConfig.getDBVersion());
        this.baseConfig = baseConfig;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(baseConfig.getTableCreationString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {

    }

    public Cursor select(String query){
        if (query.isEmpty()) return null;
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(query, null);
    }

    public boolean update(ContentValues cv, String where){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.update(baseConfig.getTableName(), cv, where, new String[]{});
        db.close();
        return result != -1;
    }

    public boolean insert(ContentValues cv){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(baseConfig.getTableName(), null, cv);
        db.close();
        return result != -1;
    }

    public boolean delete(String where){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(baseConfig.getTableName(), where, new String[]{});
        db.close();
        return result != -1;
    }

    public boolean dropTable(){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(baseConfig.getTableName(),"",null);
        db.close();
        return result != -1;
    }

    public void release(){
        baseConfig=null;
        getWritableDatabase().releaseReference();
    }

}
