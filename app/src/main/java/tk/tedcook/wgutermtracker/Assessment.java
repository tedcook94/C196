package tk.tedcook.wgutermtracker;

import android.content.ContentValues;
import android.content.Context;

public class Assessment {
    public long assessmentId;
    public long courseId;
    public String code;
    public String name;
    public String description;
    public String datetime;
    public int notifications;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_CODE, code);
        values.put(DBOpenHelper.ASSESSMENT_NAME, name);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, description);
        values.put(DBOpenHelper.ASSESSMENT_DATETIME, datetime);
        values.put(DBOpenHelper.ASSESSMENT_NOTIFICATIONS, notifications);
        context.getContentResolver().update(DataProvider.ASSESSMENTS_URI, values, DBOpenHelper.ASSESSMENTS_TABLE_ID
                + " = " + assessmentId, null);
    }
}
