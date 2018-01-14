package tk.tedcook.wgutermtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DataManager {

    // Terms
    public static Uri insertTerm(Context context, String termName, String termStart, String termEnd, int termActive) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        values.put(DBOpenHelper.TERM_ACTIVE, termActive);
        Uri termUri = context.getContentResolver().insert(DataProvider.TERMS_URI, values);
        return termUri;
    }

    public static Term getTerm(Context context, long termId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.TERMS_URI, DBOpenHelper.TERMS_COLUMNS,
                DBOpenHelper.TERMS_TABLE_ID + " = " + termId, null, null);
        cursor.moveToFirst();
        String termName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_NAME));
        String termStartDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_START));
        String termEndDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_END));
        int termActive = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TERM_ACTIVE));

        Term t = new Term();
        t.termId = termId;
        t.name = termName;
        t.start = termStartDate;
        t.end = termEndDate;
        t.active = termActive;
        return t;
    }

    // Courses
    public static Uri insertCourse(Context context, long termId, String courseName, String courseStart, String courseEnd,
                                   String courseMentor, String courseMentorPhone, String courseMentorEmail, CourseStatus status) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_MENTOR, courseMentor);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, courseMentorPhone);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, courseMentorEmail);
        values.put(DBOpenHelper.COURSE_STATUS, status.toString());
        Uri courseUri = context.getContentResolver().insert(DataProvider.COURSES_URI, values);
        return courseUri;
    }

    public static Course getCourse(Context context, long courseId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.COURSES_URI, DBOpenHelper.COURSES_COLUMNS,
                DBOpenHelper.COURSES_TABLE_ID + " = " + courseId, null, null);
        cursor.moveToFirst();
        Long termId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COURSE_TERM_ID));
        String courseName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
        String courseDescription = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_DESCRIPTION));
        String courseStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_START));
        String courseEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_END));
        CourseStatus courseStatus = CourseStatus.valueOf(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_STATUS)));
        String courseMentor = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR));
        String courseMentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_PHONE));
        String courseMentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_EMAIL));
        int courseNotifications = (cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTIFICATIONS)));

        Course c = new Course();
        c.courseId = courseId;
        c.termId = termId;
        c.name = courseName;
        c.description = courseDescription;
        c.start = courseStart;
        c.end = courseEnd;
        c.status = courseStatus;
        c.mentor = courseMentor;
        c.mentorPhone = courseMentorPhone;
        c.mentorEmail = courseMentorEmail;
        c.notifications = courseNotifications;
        return c;
    }

    public static boolean deleteCourse(Context context, long courseId) {
        Cursor notesCursor = context.getContentResolver().query(DataProvider.COURSE_NOTES_URI,
                DBOpenHelper.COURSE_NOTES_COLUMNS, DBOpenHelper.COURSE_NOTE_COURSE_ID + " = " + courseId,
                null, null);
        while (notesCursor.moveToNext()) {
            deleteCourseNote(context, notesCursor.getLong(notesCursor.getColumnIndex(DBOpenHelper.COURSE_NOTES_TABLE_ID)));
        }
        context.getContentResolver().delete(DataProvider.COURSES_URI, DBOpenHelper.COURSES_TABLE_ID + " = "
                + courseId, null);
        return true;
    }

    // Course Notes
    public static Uri insertCourseNote(Context context, long courseId, String text) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseId);
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        Uri courseNoteUri = context.getContentResolver().insert(DataProvider.COURSE_NOTES_URI, values);
        return courseNoteUri;
    }

    public static CourseNote getCourseNote(Context context, long courseNoteId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.COURSE_NOTES_URI, DBOpenHelper.COURSE_NOTES_COLUMNS,
                DBOpenHelper.COURSE_NOTES_TABLE_ID + " = " + courseNoteId, null, null);
        cursor.moveToFirst();
        long courseId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE_COURSE_ID));
        String text = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE_TEXT));

        CourseNote c = new CourseNote();
        c.courseNoteId = courseNoteId;
        c.courseId = courseId;
        c.text = text;
        return c;
    }

    public static boolean deleteCourseNote(Context context, long courseNoteId) {
        context.getContentResolver().delete(DataProvider.COURSE_NOTES_URI, DBOpenHelper.COURSE_NOTES_TABLE_ID + " = " + courseNoteId, null);
        return true;
    }

    // Assessments
    public static Uri insertAssessment(Context context, long courseId, String code, String name, String description, String datetime) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_CODE, code);
        values.put(DBOpenHelper.ASSESSMENT_NAME, name);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, description);
        values.put(DBOpenHelper.ASSESSMENT_DATETIME, datetime);
        Uri assessmentUri = context.getContentResolver().insert(DataProvider.ASSESSMENTS_URI, values);
        return assessmentUri;
    }

    public static Assessment getAssessment(Context context, long assessmentId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.ASSESSMENTS_URI, DBOpenHelper.ASSESSMENTS_COLUMNS,
                DBOpenHelper.ASSESSMENTS_TABLE_ID + " = " + assessmentId, null, null);
        cursor.moveToFirst();
        long courseId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_COURSE_ID));
        String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NAME));
        String description = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DESCRIPTION));
        String code = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_CODE));
        String datetime = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_DATETIME));
        int notifications = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NOTIFICATIONS));

        Assessment a = new Assessment();
        a.assessmentId = assessmentId;
        a.courseId = courseId;
        a.name = name;
        a.description = description;
        a.code = code;
        a.datetime = datetime;
        a.notifications = notifications;
        return a;
    }

    public static boolean deleteAssessment(Context context, long assessmentId) {
        Cursor notesCursor = context.getContentResolver().query(DataProvider.ASSESSMENT_NOTES_URI,
                DBOpenHelper.ASSESSMENT_NOTES_COLUMNS, DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID + " = " +
                        assessmentId, null, null);
        while (notesCursor.moveToNext()) {
            deleteAssessmentNote(context, notesCursor.getLong(notesCursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NOTES_TABLE_ID)));
        }
        context.getContentResolver().delete(DataProvider.ASSESSMENTS_URI, DBOpenHelper.ASSESSMENTS_TABLE_ID
                + " = " + assessmentId, null);
        return true;
    }

    // Assessment Notes
    public static Uri insertAssessmentNote(Context context, long assessmentId, String text) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID, assessmentId);
        values.put(DBOpenHelper.ASSESSMENT_NOTE_TEXT, text);
        Uri assessmentNoteUri = context.getContentResolver().insert(DataProvider.ASSESSMENT_NOTES_URI, values);
        return assessmentNoteUri;
    }

    public static AssessmentNote getAssessmentNote(Context context, long assessmentNoteId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.ASSESSMENT_NOTES_URI,
                DBOpenHelper.ASSESSMENT_NOTES_COLUMNS, DBOpenHelper.ASSESSMENT_NOTES_TABLE_ID + " = "
                        + assessmentNoteId, null, null);
        cursor.moveToFirst();
        long assessmentId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID));
        String text = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENT_NOTE_TEXT));

        AssessmentNote a = new AssessmentNote();
        a.assessmentNoteId = assessmentNoteId;
        a.assessmentId = assessmentId;
        a.text = text;
        return a;
    }

    public static boolean deleteAssessmentNote(Context context, long assessmentNoteId) {
        context.getContentResolver().delete(DataProvider.ASSESSMENT_NOTES_URI, DBOpenHelper.ASSESSMENT_NOTES_TABLE_ID
                + " = " + assessmentNoteId, null);
        return true;
    }

    // Images
    public static Uri insertImage(Context context, Uri parentUri, long timestamp) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.IMAGE_PARENT_URI, parentUri.toString());
        values.put(DBOpenHelper.IMAGE_TIMESTAMP, timestamp);
        Uri imageUri = context.getContentResolver().insert(DataProvider.IMAGES_URI, values);
        return imageUri;
    }

    public static Image getImage(Context context, long imageId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.IMAGES_URI, DBOpenHelper.IMAGES_COLUMNS, DBOpenHelper.IMAGES_TABLE_ID + " = " + imageId, null, null);
        cursor.moveToFirst();
        Uri parentUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DBOpenHelper.IMAGE_PARENT_URI)));
        long timestamp = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.IMAGE_TIMESTAMP));

        Image i = new Image();
        i.imageId = imageId;
        i.parentUri = parentUri;
        i.timestamp = timestamp;
        return i;
    }
}
