package kurki;

/**
 *
 * @author ahathoor
 */
public class ServiceName {
    private static int namehlp = 0;
    public static final String ENTRY = ++namehlp + "entry";
    public static final String PARTICIPANTS = ++namehlp + "participants";
    public static final String COURSE_BASICS = ++namehlp + "coursebasics";
    public static final String CHECKLIST = ++namehlp + "checklist";
    public static final String GRADES = ++namehlp + "grades";
    public static final String RESULT_LIST = ++namehlp + "resultlist";
    public static final String FREEZE = ++namehlp + "freezing";
    public static final String LOGOUT = ++namehlp + "logout";
}
