package com.testcase.emuchat.dataProviders.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testcase.emuchat.model.ChatItem;
import com.testcase.emuchat.myLibrary.sql.IBaseConfig;
import com.testcase.emuchat.myLibrary.sql.MySqlHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public class ChatBase implements IBaseConfig {

    private static final String DB_NAME = "MySQLiteChatDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "chatList";
    //fields
    private static final String ID = "_id";
    private static final String USER_NAME = "username";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String MINE = "mine";
    /////////
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( "+ID+" integer primary key autoincrement, "
            + TIMESTAMP + " INT, "
            + MINE + " INT, "
            + USER_NAME + " TEXT, "
            + MESSAGE + " TEXT)";

    private static MySqlHelper helper;


    public ChatBase(Context context) {
        if (helper==null) helper = new MySqlHelper(context,this);
    }

    @Override
    public int getDBVersion() {
        return DB_VERSION;
    }

    @Override
    public String getDBName() {
        return DB_NAME;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getTableCreationString() {
        return CREATE_TABLE;
    }

    @Override
    public void upgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean insert(ChatItem chatItem) {
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME,chatItem.getName());
        cv.put(MESSAGE,chatItem.getMessage());
        cv.put(MINE,chatItem.isMine()?1:0);
        cv.put(TIMESTAMP,chatItem.getTimeStamp());
        return helper.insert(cv);
    }

    public boolean dropBase(){
        return helper.dropTable();
    }

    public List<ChatItem> getMessagesFromTime(long fromTime) {
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE "+TIMESTAMP+">"+String.valueOf(fromTime);
        List<ChatItem> list = new ArrayList<>();
        Cursor cursor = helper.select(query);
        if (cursor.moveToFirst()) {
            do list.add(loadParamFromCursor(cursor));
            while (cursor.moveToNext());
        }
        return list;
    }

    private ChatItem loadParamFromCursor(Cursor cursor) {
        String userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
        String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
        long timeStamp = cursor.getLong(cursor.getColumnIndex(TIMESTAMP));
        boolean mine = cursor.getInt(cursor.getColumnIndex(MINE)) == 1;

        return new ChatItem(userName,message,mine,timeStamp);
    }

    public void release(){
        helper.release();
        helper=null;
    }

}
