package bing.support.whoisspy.dao;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public abstract class DAOHelper extends SQLiteOpenHelper implements DatabaseConstants {

    protected static final String COUPLES_TABLE_NAME = "couples";
    protected static final String MAJOR_NAME = "major_name";
    protected static final String SPY_NAME = "spy_name";
    protected static final String IS_USED = "is_used";
    protected static final boolean NOT_USED = false;
    protected static final boolean USED = true;
    protected static final String[] TEAMS_ALL_COLUMS = { _ID, MAJOR_NAME,  SPY_NAME, IS_USED};

//    protected static final String MEETINGS_TABLE_NAME = "meetings";
//    protected static final String MEETINGS_TEAM_NAME = "team_name";
//    protected static final String MEETINGS_MEETING_TIME = "meeting_time";
//    protected static final String MEETINGS_NUM_PARTICIPANTS = "num_participants";
//    protected static final String MEETINGS_INDIVIDUAL_STATUS_LENGTH = "individual_status_length";
//    protected static final String MEETINGS_MEETING_LENGTH = "meeting_length";
//    protected static final String MEETINGS_QUICKEST_STATUS = "quickest_status";
//    protected static final String MEETINGS_LONGEST_STATUS = "longest_status";
//    protected static final String[] MEETINGS_ALL_COLUMS = { _ID, MEETINGS_TEAM_NAME, MEETINGS_MEETING_TIME, MEETINGS_NUM_PARTICIPANTS,
//        MEETINGS_INDIVIDUAL_STATUS_LENGTH, MEETINGS_MEETING_LENGTH, MEETINGS_QUICKEST_STATUS, MEETINGS_LONGEST_STATUS};

    public DAOHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COUPLES_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MAJOR_NAME + " TEXT NOT NULL," +
                SPY_NAME + " TEXT NOT NULL," +
                IS_USED + " INTEGER NOT NULL DEFAULT 0" +
                ");");

//        db.execSQL("CREATE TABLE " + MEETINGS_TABLE_NAME + " (" +
//                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                MEETINGS_TEAM_NAME + " TEXT NOT NULL, " +
//                MEETINGS_MEETING_TIME + " INTEGER NOT NULL, " +
//                MEETINGS_NUM_PARTICIPANTS + " INTEGER NOT NULL, " +
//                MEETINGS_INDIVIDUAL_STATUS_LENGTH + " INTEGER NOT NULL, " +
//                MEETINGS_MEETING_LENGTH + " INTEGER NOT NULL, " +
//                MEETINGS_QUICKEST_STATUS + " INTEGER NOT NULL, " +
//                MEETINGS_LONGEST_STATUS + " INTEGER NOT NULL" +
//                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COUPLES_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + MEETINGS_TABLE_NAME);
        onCreate(db);
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
