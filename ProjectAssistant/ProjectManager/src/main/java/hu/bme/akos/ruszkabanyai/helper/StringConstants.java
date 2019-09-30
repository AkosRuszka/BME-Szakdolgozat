package hu.bme.akos.ruszkabanyai.helper;

public class StringConstants {

    /* JWT */
    public static final String COOKIE_NAME = "token";
    public static final int EXPIRATION = 30 * 60 * 1000;
    public static final byte[] SECRET = "secret".getBytes();

}
