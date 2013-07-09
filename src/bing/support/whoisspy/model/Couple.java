package bing.support.whoisspy.model;

import java.util.List;

import android.content.Context;
import bing.support.whoisspy.dao.CoupleDAO;
import bing.support.whoisspy.dao.DAOFactory;
import bing.support.whoisspy.utils.Logger;

public class Couple {
    private Long id = null;
    private String majorName = null;
    private String spyName = null;
    private boolean isUsed = false;
    private static DAOFactory daoFactory = DAOFactory.getInstance();

    public Couple(String majorName, String spyName, boolean isUsed) {
        if (majorName == null) {
            throw new IllegalArgumentException("Major name must not be null");
        }
        if (spyName == null) {
        	throw new IllegalArgumentException("Spy name must not be null");
        }
        this.majorName = majorName.trim();
        this.spyName = spyName.trim();
        this.isUsed = isUsed;
    }

    public Couple(Long id, String majorName, String spyName, boolean isUsed) {
    	this(majorName, spyName, isUsed);
    	this.id = id;
    }
    public Couple(Long id, String majorName, String spyName) {
        this(majorName, spyName, false);
        this.id = id;
    }
    public Couple(String majorName, String spyName) {
    	this(majorName, spyName, false);
    }

    public Long getId() {
        return id;
    }

    public String getMajorName() {
        return majorName;
    }
    public String getSpyName() {
    	return spyName;
    }
    public boolean isUsed() {
    	return isUsed;
    }

    public void delete(Context context) {
        CoupleDAO dao = null;
        try {
            dao = daoFactory.getCoupleDAO(context);
            dao.delete(this);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }
    
    public static void deleteAll(Context context){
    	CoupleDAO dao = null;
    	try {
    		dao = daoFactory.getCoupleDAO(context);
    		dao.deleteAll();
    	} catch (Exception e) {
    		Logger.e(e.getMessage());
    	} finally {
    		dao.close();
    	}
    	
    }

    public static Couple create(String majorName, String spyName, Context context) {
    	CoupleDAO dao = null;
        Couple couple = null;
        try {
            dao = daoFactory.getCoupleDAO(context);
            couple = dao.save(new Couple(majorName, spyName));
        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            dao.close();
        }

        return couple;
    }



    public static Couple findById(Long id, Context context) {
    	CoupleDAO dao = null;
        try {
            dao = daoFactory.getCoupleDAO(context);
            return dao.findById(id);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }

    public static List<String> getAllMajorNames(Context context) {
    	CoupleDAO dao = null;
        try {
            dao = daoFactory.getCoupleDAO(context);
            return dao.getAllMajorNames();
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }
    public static List<String> getAllSpyNames(Context context) {
    	CoupleDAO dao = null;
    	try {
    		dao = daoFactory.getCoupleDAO(context);
    		return dao.getAllSpyNames();
    	} finally {
    		if (dao != null) {
    			dao.close();
    		}
    	}
    }
}
