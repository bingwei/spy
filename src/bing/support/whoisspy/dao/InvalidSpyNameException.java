package bing.support.whoisspy.dao;

public class InvalidSpyNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidSpyNameException(String message) {
        super(message);
    }
}
