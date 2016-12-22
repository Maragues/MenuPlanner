package com.maragues.menu_planner.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by miguelaragues on 22/12/16.
 */

public class SQLBriteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "menu_planner.db";

    private static final int DATABASE_VERSION = 19;

    private static volatile BriteDatabase instance;

    public static BriteDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (SQLBriteHelper.class) {
                if (instance == null) {
                    SqlBrite sqlBrite = SqlBrite.create();

                    instance = sqlBrite.wrapDatabaseHelper(
                            new SQLBriteHelper(context.getApplicationContext())
                            , Schedulers.io()
                    );

                    instance.setLoggingEnabled(false);
                }
            }
        }

        return instance;
    }

    private SQLBriteHelper(Context context) {
// Remove comment to persist the database across sessions
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    super(context, null, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Recipe.CREATE_TABLE);
        db.execSQL(Meal.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i=0;i<=SQLMigrations.MIGRATION_MAP.size();i++){
            if(oldVersion < SQLMigrations.MIGRATION_MAP.keyAt(i)){
                for(String statement : SQLMigrations.MIGRATION_MAP.get(SQLMigrations.MIGRATION_MAP.keyAt(i))){
                    db.execSQL(statement);
                }
            }
        }
    }

    /*
    Not the best way to handle migrations, but there's no official way for now

    See https://github.com/square/sqldelight/issues/89
     */
    private static class SQLMigrations {
        static final SparseArray<String[]> MIGRATION_MAP = new SparseArray<>();

        static {
            /*MIGRATION_MAP.put(
                    19,
                    new String[]{
                            "CREATE TEMPORARY TABLE MEDICATION_SCHEDULE_backup(\n" +
                                    "                       id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                    "                       platformId INTEGER UNIQUE ON CONFLICT REPLACE,\n" +
                                    "                       timestamp INTEGER NOT NULL,\n" +
                                    "                       medication TEXT NOT NULL UNIQUE,\n" +
                                    "                       schedule TEXT NOT NULL,\n" +
                                    "                       nextReminderAt INTEGER NOT NULL,\n" +
                                    "                       adherence INTEGER);",
                            "INSERT INTO MEDICATION_SCHEDULE_backup SELECT id, platformId, timestamp, medication, schedule, nextReminderAt, adherence FROM MEDICATION_SCHEDULE;",
                            "DROP TABLE MEDICATION_SCHEDULE;",
                            "CREATE TABLE MEDICATION_SCHEDULE(\n" +
                                    "             id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                    "             platformId INTEGER UNIQUE ON CONFLICT REPLACE,\n" +
                                    "             timestamp INTEGER NOT NULL,\n" +
                                    "             medication TEXT NOT NULL UNIQUE,\n" +
                                    "             schedule TEXT NOT NULL,\n" +
                                    "             nextReminderAt INTEGER NOT NULL,\n" +
                                    "             adherence INTEGER);",
                            "INSERT INTO MEDICATION_SCHEDULE SELECT * FROM MEDICATION_SCHEDULE_backup;",
                            "DROP TABLE MEDICATION_SCHEDULE_backup;"
                    }
            );*/
        }
    }
}