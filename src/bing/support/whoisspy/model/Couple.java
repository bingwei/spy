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
    private static DAOFactory daoFactory = DAOFactory.getInstance();

    public Couple(String majorName, String spyName) {
        if (majorName == null) {
            throw new IllegalArgumentException("Major name must not be null");
        }
        if (spyName == null) {
        	throw new IllegalArgumentException("Spy name must not be null");
        }
        this.majorName = majorName.trim();
        this.spyName = spyName.trim();
    }

    public Couple(Long id, String majorName, String spyName) {
        this(majorName, spyName);
        this.id = id;
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
