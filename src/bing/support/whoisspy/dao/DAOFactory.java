package bing.support.whoisspy.dao;

import android.content.Context;

public class DAOFactory {
    private static DAOFactory instance = null;

    private Context globalContext = null;
    private boolean cacheDAOInstances = false;
    private CoupleDAO cachedCoupleDAO = null;
//    private MeetingDAO cachedMeetingDAO = null;

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    private DAOFactory() {
    }

    public CoupleDAO getCoupleDAO(Context context) {
        if (cacheDAOInstances) {
            if (cachedCoupleDAO == null) {
            	cachedCoupleDAO = new CoupleDAO(getProperDAOContext(context));
            }
            return cachedCoupleDAO;
        } else {
            return new CoupleDAO(getProperDAOContext(context));
        }
    }
//
//    public MeetingDAO getMeetingDAO(Context context) {
//        if (cacheDAOInstances) {
//            if (cachedMeetingDAO == null) {
//                cachedMeetingDAO = new MeetingDAO(getProperDAOContext(context));
//            }
//            return cachedMeetingDAO;
//        } else {
//            return new MeetingDAO(getProperDAOContext(context));
//        }
//    }

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
