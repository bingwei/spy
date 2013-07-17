package bing.support.whoisspy.dao;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bing.support.whoisspy.constant.Limit;
import bing.support.whoisspy.model.Player;
import bing.support.whoisspy.utils.Logger;

@SuppressLint("DefaultLocale")
public class PlayerDAO extends DAOHelper {

    public PlayerDAO(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Player save(Player player) {
        SQLiteDatabase db = getWritableDatabase();
        if (player.getId() != null) {
            return updateExistingPlayer(db, player);
        } else {
            return createNewPlayer(db, player);
        }
    }

    public Player findById(Long id) {
        Cursor cursor = null;
        Player player = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(PLAYER_TABLE_NAME, PLAYER_ALL_COLUMS, _ID + " = ?", new String[]{id.toString()}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    String pName = cursor.getString(1);
                    byte[] pImage = cursor.getBlob(2);
                    long pScore = cursor.getLong(3);
                    String pRank = cursor.getString(4);
                    player = new Player(pName, pImage, pScore, pRank);
                }
            }
        } finally {
            closeCursor(cursor);
        }

        return player;
    }

    public Player findByPlayerName(String playerName) {
        Cursor cursor = null;
        Player player = null;
        playerName = playerName.trim();
        String pName = null;
        byte[] pImage = null;
        Long pScore = null;
        String pRank = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(PLAYER_TABLE_NAME, PLAYER_ALL_COLUMS, PLAYER_NAME + " = ?", new String[]{playerName}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                	do{
                		long id = cursor.getLong(0);
                		pName = cursor.getString(1);
                		pImage = cursor.getBlob(2);
                		pScore = cursor.getLong(3);
                		pRank = cursor.getString(4);
                		player = new Player(id, pName, pImage, pScore, pRank);
                	}while(cursor.moveToNext());
                }
            }
        } finally {
            closeCursor(cursor);
        }
    	Logger.d((player == null ? "Unsuccessfully" : "Successfully") + String.format(" found player '%s: score=%d, rank=%s'", 
    			pName, pScore, pRank));
        	
        return player;
    }

    public List<String> getAllPlayerNames() {
    	return getAllNames(PLAYER_NAME);
    }
    public List<String> getAllNames(String name) {
        List<String> names = new ArrayList<String>();
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(PLAYER_TABLE_NAME, new String[]{name}, null, null, null, null, name);
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
        db.delete(PLAYER_TABLE_NAME, null, null);
    }

    public void delete(Player player) {
        Logger.d(String.format("Deleting player '%s: %d'", player.getPlayerName(), player.getPlayerScore()));
        if (player.getId() != null) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(PLAYER_TABLE_NAME, _ID + " = ?", new String[]{player.getId().toString()});
        }
    }

    private boolean attemptingToCreateDuplicatePlayer(Player player) {
        return getAllPlayerNames().contains(player.getPlayerName());
    }

    private Player createNewPlayer(SQLiteDatabase db, Player player) {
    	
    	if (player.getPlayerName() == null || player.getPlayerName().trim().length() == 0) {
    		String msg = "Attempting to create a player with an empty name";
    		Logger.w(msg);
    		throw new InvalidMajorNameException(msg);
    	}
    	
    	if(isOverLimit()){
    		return null;
    	}
    	
		if (attemptingToCreateDuplicatePlayer(player)) {
			String msg = String.format("Attempting to create duplicate player with the player name: %s",
					player.getPlayerName());
			Logger.w(msg);
			throw new DuplicateTeamException(msg);
		}
    	
        Logger.d(String.format("Creating new team with name of '%s'", player.getPlayerName()));
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME, player.getPlayerName());
        values.put(PLAYER_IMAGE, player.getPlayerImage());
        values.put(PLAYER_SCORE, player.getPlayerScore());
        values.put(PLAYER_RANK, player.getPlayerRank());
        long id = db.insertOrThrow(PLAYER_TABLE_NAME, null, values);
        return new Player(id, player.getPlayerName(), player.getPlayerImage(), player.getPlayerScore(), player.getPlayerRank());
    }

    private boolean isOverLimit() {
		return getAllPlayerNames().size() >= Limit.AMOUNT;
	}

	private Player updateExistingPlayer(SQLiteDatabase db, Player player) {
        Logger.d(String.format("Updating player '%s: %s'",player.getPlayerName()));
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME, player.getPlayerName());
        values.put(PLAYER_IMAGE, player.getPlayerImage());
        values.put(PLAYER_SCORE, player.getPlayerScore());
        values.put(PLAYER_RANK, player.getPlayerRank());
        long id = db.update(PLAYER_TABLE_NAME, values, _ID + " = ?", new String[]{player.getId().toString()});
        return new Player(id, player.getPlayerName(), player.getPlayerImage(), player.getPlayerScore(), player.getPlayerRank());
    }
}
