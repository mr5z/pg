package com.nkraft.pogo.controller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nkraft.pogo.utils.Debug;

/**
 * Created by mark on 21/07/2016.
 */
public class AccountDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "com.nkraft.pogo:db_account";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "account";
    public static final String COLUMN_GOOGLE_ID = "google_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DISPLAY_NAME = "display_name";
    public static final String COLUMN_SIGNED_IN = "signed_in";


    public AccountDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s UNSIGNED BIG INT" +
                        "%s CHAR(100)," +
                        "%s CHAR(100)," +
                        "%s BOOLEAN" +
                        ")",
                TABLE_NAME,
                COLUMN_GOOGLE_ID,
                COLUMN_EMAIL,
                COLUMN_DISPLAY_NAME,
                COLUMN_SIGNED_IN);

        Debug.log("dropping '%s' table", TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Debug.log("dropped?");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }
}
