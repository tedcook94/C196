package tk.tedcook.wgutermtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    // Constants for database name and version number
    private static final String DATABASE_NAME = "wgu_terms.db";
    private static final int DATABASE_VERSION = 1;

    // Constants for database tables and columns
    // Terms table
    public static final String TABLE_TERMS = "terms";
    public static final String TERMS_TABLE_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_ACTIVE = "termActive";
    public static final String TERM_CREATED = "termCreated";
    public static final String[] TERMS_COLUMNS = {TERMS_TABLE_ID, TERM_NAME, TERM_START, TERM_END, TERM_ACTIVE, TERM_CREATED};

    // Courses table
    public static final String TABLE_COURSES = "courses";
    public static final String COURSES_TABLE_ID = "_id";
    public static final String COURSE_TERM_ID = "courseTermId";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_DESCRIPTION = "courseDescription";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END = "courseEnd";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_MENTOR = "courseMentor";
    public static final String COURSE_MENTOR_PHONE = "courseMentorPhone";
    public static final String COURSE_MENTOR_EMAIL = "courseMentorEmail";
    public static final String COURSE_NOTIFICATIONS = "courseNotifications";
    public static final String COURSE_CREATED = "courseCreated";
    public static final String[] COURSES_COLUMNS = {COURSES_TABLE_ID, COURSE_TERM_ID, COURSE_NAME, COURSE_DESCRIPTION,
            COURSE_START, COURSE_END, COURSE_STATUS, COURSE_MENTOR, COURSE_MENTOR_PHONE, COURSE_MENTOR_EMAIL,
            COURSE_NOTIFICATIONS, COURSE_CREATED};

    // Course Notes table
    public static final String TABLE_COURSE_NOTES = "courseNotes";
    public static final String COURSE_NOTES_TABLE_ID = "_id";
    public static final String COURSE_NOTE_COURSE_ID = "courseNoteCourseId";
    public static final String COURSE_NOTE_TEXT = "courseNoteText";
    public static final String COURSE_NOTE_IMAGE_URI = "courseNoteUri";
    public static final String COURSE_NOTE_CREATED = "courseNoteCreated";
    public static final String[] COURSE_NOTES_COLUMNS = {COURSE_NOTES_TABLE_ID, COURSE_NOTE_COURSE_ID, COURSE_NOTE_TEXT,
            COURSE_NOTE_IMAGE_URI, COURSE_NOTE_CREATED};

    // Assessments table
    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENTS_TABLE_ID = "_id";
    public static final String ASSESSMENT_COURSE_ID = "assessmentCourseId";
    public static final String ASSESSMENT_CODE = "assessmentCode";
    public static final String ASSESSMENT_NAME = "assessmentName";
    public static final String ASSESSMENT_DESCRIPTION = "assessmentDescription";
    public static final String ASSESSMENT_DATETIME = "assessmentDatetime";
    public static final String ASSESSMENT_NOTIFICATIONS = "assessmentNotifications";
    public static final String ASSESSMENT_CREATED = "assessmentCreated";
    public static final String[] ASSESSMENTS_COLUMNS = {ASSESSMENTS_TABLE_ID, ASSESSMENT_COURSE_ID, ASSESSMENT_CODE,
            ASSESSMENT_NAME, ASSESSMENT_DESCRIPTION, ASSESSMENT_DATETIME, ASSESSMENT_NOTIFICATIONS, ASSESSMENT_CREATED};

    // Assessment Notes table
    public static final String TABLE_ASSESSMENT_NOTES = "assessmentNotes";
    public static final String ASSESSMENT_NOTES_TABLE_ID = "_id";
    public static final String ASSESSMENT_NOTE_ASSESSMENT_ID = "assessmentNoteAssessmentId";
    public static final String ASSESSMENT_NOTE_TEXT = "assessmentNoteText";
    public static final String ASSESSMENT_NOTE_IMAGE_URI = "assessmentNoteUri";
    public static final String ASSESSMENT_NOTE_CREATED = "assessmentNoteCreated";
    public static final String[] ASSESSMENT_NOTES_COLUMNS = {ASSESSMENT_NOTES_TABLE_ID, ASSESSMENT_NOTE_ASSESSMENT_ID,
            ASSESSMENT_NOTE_TEXT, ASSESSMENT_NOTE_IMAGE_URI, ASSESSMENT_NOTE_CREATED};

    // Images table
    public static final String TABLE_IMAGES = "images";
    public static final String IMAGES_TABLE_ID = "_id";
    public static final String IMAGE_PARENT_URI = "imageUri";
    public static final String IMAGE_TIMESTAMP = "imageTimestamp";
    public static final String IMAGE_CREATED = "imageCreated";
    public static final String[] IMAGES_COLUMNS = {IMAGES_TABLE_ID, IMAGE_PARENT_URI, IMAGE_TIMESTAMP,
            IMAGE_CREATED};

    // Create strings for SQLite commands
    // Terms table
    private static final String TERMS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TERMS + " (" +
                    TERMS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_START + " DATE, " +
                    TERM_END + " DATE, " +
                    TERM_ACTIVE + " INTEGER, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    // Courses table
    private static final String COURSES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + " (" +
                    COURSES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TERM_ID + " INTEGER, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_DESCRIPTION + " TEXT, " +
                    COURSE_START + " DATE, " +
                    COURSE_END + " DATE, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_MENTOR + " TEXT, " +
                    COURSE_MENTOR_PHONE + " TEXT, " +
                    COURSE_MENTOR_EMAIL + " TEXT, " +
                    COURSE_NOTIFICATIONS + " INTEGER, " +
                    COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" + TERMS_TABLE_ID + ")" +
                    ")";

    // Course Notes table
    private static final String COURSE_NOTES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_NOTES + " (" +
                    COURSE_NOTES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NOTE_COURSE_ID + " INTEGER, " +
                    COURSE_NOTE_TEXT + " TEXT, " +
                    COURSE_NOTE_IMAGE_URI + " TEXT, " +
                    COURSE_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_NOTE_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Assessments table
    private static final String ASSESSMENTS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENTS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_COURSE_ID + " INTEGER, " +
                    ASSESSMENT_NAME + " TEXT, " +
                    ASSESSMENT_DESCRIPTION + " TEXT, " +
                    ASSESSMENT_CODE + " TEXT, " +
                    ASSESSMENT_DATETIME + " TEXT, " +
                    ASSESSMENT_NOTIFICATIONS + " INTEGER, " +
                    ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + ASSESSMENT_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Assessment Notes table
    private static final String ASSESSMENT_NOTES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ASSESSMENT_NOTES + " (" +
                    ASSESSMENT_NOTES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_NOTE_ASSESSMENT_ID + " INTEGER, " +
                    ASSESSMENT_NOTE_TEXT + " TEXT, " +
                    ASSESSMENT_NOTE_IMAGE_URI + " TEXT, " +
                    ASSESSMENT_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + ASSESSMENT_NOTE_ASSESSMENT_ID + ") REFERENCES " + TABLE_ASSESSMENTS + "(" + ASSESSMENTS_TABLE_ID + ")" +
                    ")";

    // Images table
    private static final String IMAGES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_IMAGES + " (" +
                    IMAGES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    IMAGE_PARENT_URI + " TEXT, " +
                    IMAGE_TIMESTAMP + " INTEGER, " +
                    IMAGE_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERMS_TABLE_CREATE);
        db.execSQL(COURSES_TABLE_CREATE);
        db.execSQL(COURSE_NOTES_TABLE_CREATE);
        db.execSQL(ASSESSMENTS_TABLE_CREATE);
        db.execSQL(ASSESSMENT_NOTES_TABLE_CREATE);
        db.execSQL(IMAGES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        onCreate(db);
    }
}
