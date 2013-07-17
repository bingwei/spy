package bing.support.whoisspy.dao;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bing.support.whoisspy.model.Character;
import bing.support.whoisspy.utils.Logger;

public class CharacterDAO extends DAOHelper {

    public CharacterDAO(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Character save(Character character) {
        SQLiteDatabase db = getWritableDatabase();
        if (character.getId() != null) {
            return updateExistingCharacter(db, character);
        } else {
            return createNewCharacter(db, character);
        }
    }

    public Character findById(Long id) {
        Cursor cursor = null;
        Character character = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(CHARACTER_TABLE_NAME, CHARACTERE_ALL_COLUMS, _ID + " = ?", new String[]{id.toString()}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    String majorName = cursor.getString(1);
                    String spyName = cursor.getString(2);
                    character = new Character(id, majorName, spyName);
                }
            }
        } finally {
            closeCursor(cursor);
        }

        return character;
    }

    public List<Character> findByMajorName(String majorName) {
        Cursor cursor = null;
        Character character = null;
        List<Character> characters = new ArrayList<Character>();
        majorName = majorName.trim();
        String spyName = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(CHARACTER_TABLE_NAME, CHARACTERE_ALL_COLUMS, MAJOR_NAME + " = ?", new String[]{majorName}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                	do{
                		long id = cursor.getLong(0);
                		majorName = cursor.getString(1);
                		spyName = cursor.getString(2);
                		character = new Character(id, majorName, spyName);
                		characters.add(character);
                	}while(cursor.moveToNext());
                }
            }
        } finally {
            closeCursor(cursor);
        }
        for(Character c: characters){
        	Logger.d((c == null ? "Unsuccessfully" : "Successfully") + String.format(" found character '%s: %s'", 
        			c.getMajorName(), c.getSpyName()));
        	
        }
        return characters;
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
            cursor = db.query(CHARACTER_TABLE_NAME, new String[]{name}, null, null, null, null, name);
            while (cursor.moveToNext()) {
            	names.add(cursor.getString(0));
            }
        } finally {
            closeCursor(cursor);
        }

        Logger.d("Found " + names.size());
        return names;
    }
    
    public List<Character> getAllUsed() {
    	return getAllWithCertainStatus(USED);
    }
    public List<Character> getAllUnused() {
    	return getAllWithCertainStatus(NOT_USED);
    }
    
    public List<Character> getAllWithCertainStatus(boolean status) {
    	List<Character> characters = new ArrayList<Character>();
    	Cursor cursor = null;
    	Character character = null;
    	long id = 0;
    	String majorName = null;
    	String spyName = null;
    	
    	try {
    		SQLiteDatabase db = getReadableDatabase();
    		cursor = db.query(CHARACTER_TABLE_NAME, CHARACTERE_ALL_COLUMS, IS_USED + " = ?", new String[]{status == true? "1": "0"}, null, null, null);
    		while (cursor.moveToNext()) {
            	id = cursor.getLong(0);
        		majorName = cursor.getString(1);
        		spyName = cursor.getString(2);
        		//Set all characters with unused. It's easy to get them updated or deleted 
        		character = new Character(id, majorName, spyName, false);
        		characters.add(character);
    		}
    	} finally {
    		closeCursor(cursor);
    	}
    	
    	Logger.d("Found " + characters.size());
    	return characters;
    }

    public void deleteAll() {
        Logger.d("Deleting all teams");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CHARACTER_TABLE_NAME, null, null);
    }

    public void delete(Character character) {
        Logger.d(String.format("Deleting character '%s: %s'", character.getMajorName(), character.getSpyName()));
        if (character.getId() != null) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(CHARACTER_TABLE_NAME, _ID + " = ?", new String[]{character.getId().toString()});
        }
    }

    private boolean attemptingToCreateDuplicateTeam(Character character) {
        return getAllMajorNames().contains(character.getMajorName())
        		&& getAllSpyNames().contains(character.getSpyName());
    }

    private Character createNewCharacter(SQLiteDatabase db, Character character) {
        if (character.getMajorName() == null || character.getMajorName().trim().length() == 0) {
            String msg = "Attempting to create a major name with an empty name";
            Logger.w(msg);
            throw new InvalidMajorNameException(msg);
        }
        if (character.getSpyName() == null || character.getSpyName().trim().length() == 0) {
        	String msg = "Attempting to create a spy name with an empty name";
        	Logger.w(msg);
        	throw new InvalidSpyNameException(msg);
        }

        if (attemptingToCreateDuplicateTeam(character)) {
            String msg = String.format("Attempting to create duplicate character with the major name: %s; spy name %s ",
            		character.getMajorName(), character.getSpyName());
            Logger.w(msg);
            throw new DuplicateTeamException(msg);
        }

        Logger.d(String.format("Creating new character with a major name of '%s', a spy name of '%s'",
        		character.getMajorName(), character.getSpyName()));
        ContentValues values = new ContentValues();
        values.put(MAJOR_NAME, character.getMajorName());
        values.put(SPY_NAME, character.getSpyName());
        values.put(IS_USED, character.isUsed()? 1: 0);
        long id = db.insertOrThrow(CHARACTER_TABLE_NAME, null, values);
        return new Character(id, character.getMajorName(), character.getSpyName());
    }

    private Character updateExistingCharacter(SQLiteDatabase db, Character character) {
        Logger.d(String.format("Updating character '%s: %s'",character.getMajorName(), character.getSpyName()));
        ContentValues values = new ContentValues();
        values.put(MAJOR_NAME, character.getMajorName());
        values.put(SPY_NAME, character.getSpyName());
        values.put(IS_USED, character.isUsed()? 1: 0);
        long id = db.update(CHARACTER_TABLE_NAME, values, _ID + " = ?", new String[]{character.getId().toString()});
        return new Character(id, character.getMajorName(), character.getSpyName(), character.isUsed());
    }
}
