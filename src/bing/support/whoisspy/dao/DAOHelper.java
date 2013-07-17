package bing.support.whoisspy.dao;

import static android.provider.BaseColumns._ID;
import bing.support.whoisspy.constant.Rank;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public abstract class DAOHelper extends SQLiteOpenHelper implements DatabaseConstants {

    protected static final String CHARACTER_TABLE_NAME = "characters";
    protected static final String MAJOR_NAME = "major_name";
    protected static final String SPY_NAME = "spy_name";
    protected static final String IS_USED = "is_used";
    protected static final boolean NOT_USED = false;
    protected static final boolean USED = true;
    protected static final String[] CHARACTERE_ALL_COLUMS = { _ID, MAJOR_NAME,  SPY_NAME, IS_USED};

    protected static final String PLAYER_TABLE_NAME = "players";
    protected static final String PLAYER_NAME = "player_name";
    protected static final String PLAYER_IMAGE = "player_image";
    protected static final String PLAYER_SCORE = "player_score";
    protected static final String PLAYER_RANK = "player_rank";
    protected static final String[] PLAYER_ALL_COLUMS = { _ID, PLAYER_TABLE_NAME, PLAYER_NAME, PLAYER_IMAGE,
    	PLAYER_SCORE, PLAYER_RANK};

    public DAOHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CHARACTER_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MAJOR_NAME + " TEXT NOT NULL," +
                SPY_NAME + " TEXT NOT NULL," +
                IS_USED + " INTEGER NOT NULL DEFAULT 0" +
                ");");

        db.execSQL("CREATE TABLE " + PLAYER_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLAYER_NAME + " TEXT NOT NULL, " +
                PLAYER_IMAGE + " BLOB, " +
                PLAYER_SCORE + " INTEGER NOT NULL DEFAULT 0, " +
                PLAYER_RANK + String.format(" TEXT NOT NULL DEFAULT %s ", Rank.LEVEL_1) +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE_NAME);
        onCreate(db);
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
