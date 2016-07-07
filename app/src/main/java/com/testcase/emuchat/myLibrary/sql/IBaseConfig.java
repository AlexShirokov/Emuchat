package com.testcase.emuchat.myLibrary.sql;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by AlexShredder on 06.07.2016.
 */
public interface IBaseConfig {

    int getDBVersion();
    String getDBName();
    String getTableName();
    String getTableCreationString();

    void upgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion);

}
