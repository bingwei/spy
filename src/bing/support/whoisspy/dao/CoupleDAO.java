package bing.support.whoisspy.dao;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bing.support.whoisspy.model.Couple;
import bing.support.whoisspy.utils.Logger;

public class CoupleDAO extends DAOHelper {

    public CoupleDAO(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Couple save(Couple couples) {
        SQLiteDatabase db = getWritableDatabase();
        if (couples.getId() != null) {
            return updateExistingCouple(db, couples);
        } else {
            return createNewCouple(db, couples);
        }
    }

    public Couple findById(Long id) {
        Cursor cursor = null;
        Couple team = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(COUPLES_TABLE_NAME, TEAMS_ALL_COLUMS, _ID + " = ?", new String[]{id.toString()}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    String majorName = cursor.getString(1);
                    String spyName = cursor.getString(2);
                    team = new Couple(id, majorName, spyName);
                }
            }
        } finally {
            closeCursor(cursor);
        }

        return team;
    }

    public List<Couple> findByMajorName(String majorName) {
        Cursor cursor = null;
        Couple couple = null;
        List<Couple> couples = new ArrayList<Couple>();
        majorName = majorName.trim();
        String spyName = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(COUPLES_TABLE_NAME, TEAMS_ALL_COLUMS, MAJOR_NAME + " = ?", new String[]{majorName}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                	do{
                		long id = cursor.getLong(0);
                		majorName = cursor.getString(1);
                		spyName = cursor.getString(2);
                		couple = new Couple(id, majorName, spyName);
                		couples.add(couple);
                	}while(cursor.moveToNext());
                }
            }
        } finally {
            closeCursor(cursor);
        }
        for(Couple c: couples){
        	Logger.d((c == null ? "Unsuccessfully" : "Successfully") + String.format(" found couple '%s: %s'", 
        			c.getMajorName(), c.getSpyName()));
        	
        }
        return couples;
    }

    public List<String> getAllMajorNames() {
    	return getAllNames(MAJOR_NAME);
    }
    public List<String> getAllSpyNames() {
    	return getAllNames(SPY_NAME);
    }
    public List<String> getAllNames(String name) {
        List<String> names = new ArrayList<String>();
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(COUPLES_TABLE_NAME, new String[]{name}, null, null, null, null, name);
            while (cursor.moveToNext()) {
            	names.add(cursor.getString(0));
            }
        } finally {
            closeCursor(cursor);
        }

        Logger.d("Found " + names.size());
        return names;
    }

    public void deleteAll() {
        Logger.d("Deleting all teams");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(COUPLES_TABLE_NAME, null, null);
    }

    public void delete(Couple couple) {
        Logger.d(String.format("Deleting couple '%s: %s'", couple.getMajorName(), couple.getSpyName()));
        if (couple.getId() != null) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(COUPLES_TABLE_NAME, _ID + " = ?", new String[]{couple.getId().toString()});
        }
    }

    private boolean attemptingToCreateDuplicateTeam(Couple couple) {
        return getAllMajorNames().contains(couple.getMajorName())
        		&& getAllSpyNames().contains(couple.getSpyName());
    }

    private Couple createNewCouple(SQLiteDatabase db, Couple couple) {
        if (couple.getMajorName() == null || couple.getMajorName().trim().length() == 0) {
            String msg = "Attempting to create a major name with an empty name";
            Logger.w(msg);
            throw new InvalidMajorNameException(msg);
        }
        if (couple.getSpyName() == null || couple.getSpyName().trim().length() == 0) {
        	String msg = "Attempting to create a spy name with an empty name";
        	Logger.w(msg);
        	throw new InvalidSpyNameException(msg);
        }

        if (attemptingToCreateDuplicateTeam(couple)) {
            String msg = String.format("Attempting to create duplicate couple with the major name: %s; spy name %s ",
            		couple.getMajorName(), couple.getSpyName());
            Logger.w(msg);
            throw new DuplicateTeamException(msg);
        }

        Logger.d(String.format("Creating new team with a major name of '%s', a spy name of '%s'",
        		couple.getMajorName(), couple.getSpyName()));
        ContentValues values = new ContentValues();
        values.put(MAJOR_NAME, couple.getMajorName());
        values.put(SPY_NAME, couple.getSpyName());
        long id = db.insertOrThrow(COUPLES_TABLE_NAME, null, values);
        return new Couple(id, couple.getMajorName(), couple.getSpyName());
    }

    private Couple updateExistingCouple(SQLiteDatabase db, Couple couple) {
        Logger.d(String.format("Updating team '%s: %s'",couple.getMajorName(), couple.getSpyName()));
        ContentValues values = new ContentValues();
        values.put(MAJOR_NAME, couple.getMajorName());
        values.put(SPY_NAME, couple.getSpyName());
        long id = db.update(COUPLES_TABLE_NAME, values, _ID + " = ?", new String[]{couple.getId().toString()});
        return new Couple(id, couple.getMajorName(), couple.getSpyName());
    }
}
