package hu.bme.akosruszka.projectmanager.helper;


public class StringConstants {

    private StringConstants() {
    }

    /* JWT */
    public static final String COOKIE_NAME = "token";
    public static final int EXPIRATION = 30 * 60 * 1000;
    public static final byte[] SECRET = "secret".getBytes();

    /* Email Message */
    public static final String MEETING_NEW = "meeting.new";
    public static final String MEETING_UPDATED = "meeting.updated";
    public static final String MEETING_DELETED = "meeting.deleted";
    public static final String MEETING_REMOVED_USER = "meeting.removed.uder";
    public static final String MEETING_ADD_MINUTE = "meeting.add.minute";
    public static final String MEETING_UPDATE_MINUTE = "meeting.update.minute";

    public static final String PROJECT_NEW = "project.new";
    public static final String PROJECT_UPDATE = "project.updated";
    public static final String PROJECT_DELETED = "project.deletes";
    public static final String PROJECT_REMOVED_USER = "project.removed.user";

    /* Exception message */
    public static final String TEMPLATE_EXCEPTION = "Template exception";
    public static final String TEMPLATE_NOT_FOUND = "Nem található a megadott névvel template";
    public static final String PROJECT_NOT_FOUND = "Nem létezik a keresett projekt";
    public static final String PROJECT_FOUND = "Már létezik ilyen névvel projekt";
    public static final String MEETING_NOT_FOUND = "Nem létezik a keresett meeting";
    public static final String MEETING_FOUND = "Már létezik ilyen névvel meeting";
    public static final String USER_NOT_FOUND = "Nem létezik a keresett felhasználó";
    public static final String USER_FOUND = "Már létezik ilyen névvel felhasználó";

    /* Response message */


}
