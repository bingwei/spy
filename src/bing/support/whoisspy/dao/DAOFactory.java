package bing.support.whoisspy.dao;

import android.content.Context;

public class DAOFactory {
    private static DAOFactory instance = null;

    private Context globalContext = null;
    private boolean cacheDAOInstances = false;
    private CharacterDAO cachedCharacterDAO = null;
    private PlayerDAO cachedPlayerDAO = null;

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    private DAOFactory() {
    }

    public CharacterDAO getCharacterDAO(Context context) {
        if (cacheDAOInstances) {
            if (cachedCharacterDAO == null) {
            	cachedCharacterDAO = new CharacterDAO(getProperDAOContext(context));
            }
            return cachedCharacterDAO;
        } else {
            return new CharacterDAO(getProperDAOContext(context));
        }
    }

    public PlayerDAO getPlayerDAO(Context context) {
        if (cacheDAOInstances) {
            if (cachedPlayerDAO == null) {
            	cachedPlayerDAO = new PlayerDAO(getProperDAOContext(context));
            }
            return cachedPlayerDAO;
        } else {
            return new PlayerDAO(getProperDAOContext(context));
        }
    }

    public void setGlobalContext(Context context) {
        globalContext = context;
    }

    public void setCacheDAOInstances(boolean cacheDAOInstances) {
        this.cacheDAOInstances = cacheDAOInstances;
    }

    private Context getProperDAOContext(Context context) {
        if (globalContext != null) {
            return globalContext;
        } else {
            return context;
        }
    }
}
