package tk.tedcook.wgutermtracker;

import android.content.ContentValues;
import android.content.Context;

public class Course {
    public long courseId;
    public long termId;
    public String name;
    public String description;
    public String start;
    public String end;
    public CourseStatus status;
    public String mentor;
    public String mentorPhone;
    public String mentorEmail;
    public int notifications;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_NAME, name);
        values.put(DBOpenHelper.COURSE_DESCRIPTION, description);
        values.put(DBOpenHelper.COURSE_START, start);
        values.put(DBOpenHelper.COURSE_END, end);
        values.put(DBOpenHelper.COURSE_STATUS, status.toString());
        values.put(DBOpenHelper.COURSE_MENTOR, mentor);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, mentorPhone);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSE_NOTIFICATIONS, notifications);
        context.getContentResolver().update(DataProvider.COURSES_URI, values, DBOpenHelper.COURSES_TABLE_ID
                + " = " + courseId, null);
    }
}
