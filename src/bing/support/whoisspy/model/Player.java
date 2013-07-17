package bing.support.whoisspy.model;

import java.util.List;

import android.content.Context;
import bing.support.whoisspy.constant.Rank;
import bing.support.whoisspy.dao.CharacterDAO;
import bing.support.whoisspy.dao.DAOFactory;
import bing.support.whoisspy.dao.PlayerDAO;
import bing.support.whoisspy.utils.Logger;

public class Player {
    private Long id = null;
    private String playerName = null;
    private byte[] playerImage = null;
    private long playerScore = 0L;
    private String playerRank = null;
    private static DAOFactory daoFactory = DAOFactory.getInstance();

    public Player(String playerName, byte[] playerImage, long playerScore, String playerRank) {
        if (playerImage == null) {
            throw new IllegalArgumentException("Player name must not be null");
        }
        this.playerName = playerName.trim();
        this.playerImage = playerImage;
        this.playerScore = playerScore;
        this.playerRank = playerRank.trim();
    }

    public Player(Long id, String playerName, byte[] playerImage, long playerScore, String playerRank) {
    	this(playerName, playerImage, playerScore, playerRank);
    	this.id = id;
    }
    public Player(Long id, String playerName) {
        this(playerName, null, 0L, Rank.LEVEL_1);
        this.id = id;
    }
    public Player(String playerName) {
    	this(playerName, null, 0L, Rank.LEVEL_1);
    }

    public Long getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }
    public byte[] getPlayerImage() {
    	return playerImage;
    }
    public long getPlayerScore() {
    	return playerScore;
    }
    public String getPlayerRank() {
    	return playerRank;
    }

    public void delete(Context context) {
        PlayerDAO dao = null;
        try {
            dao = daoFactory.getPlayerDAO(context);
            dao.delete(this);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }
    
    public static void deleteAll(Context context){
    	PlayerDAO dao = null;
    	try {
    		dao = daoFactory.getPlayerDAO(context);
    		dao.deleteAll();
    	} catch (Exception e) {
    		Logger.e(e.getMessage());
    	} finally {
    		dao.close();
    	}
    	
    }

    public static Player create(String playerName, Context context) {
    	PlayerDAO dao = null;
        Player player = null;
        try {
            dao = daoFactory.getPlayerDAO(context);
            player = dao.save(new Player(playerName));
        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            dao.close();
        }

        return player;
    }



    public static Player findById(Long id, Context context) {
    	PlayerDAO dao = null;
        try {
            dao = daoFactory.getPlayerDAO(context);
            return dao.findById(id);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }

    public static List<String> getAllMajorNames(Context context) {
    	CharacterDAO dao = null;
        try {
            dao = daoFactory.getCharacterDAO(context);
            return dao.getAllMajorNames();
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }
    public static List<String> getAllSpyNames(Context context) {
    	CharacterDAO dao = null;
    	try {
    		dao = daoFactory.getCharacterDAO(context);
    		return dao.getAllSpyNames();
    	} finally {
    		if (dao != null) {
    			dao.close();
    		}
    	}
    }
}
