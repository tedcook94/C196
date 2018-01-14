package tk.tedcook.wgutermtracker;

import android.content.ContentValues;
import android.content.Context;

public class CourseNote {
    public long courseNoteId;
    public long courseId;
    public String text;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseId);
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        context.getContentResolver().update(DataProvider.COURSE_NOTES_URI, values, DBOpenHelper.COURSE_NOTES_TABLE_ID
                + " = " + courseNoteId, null);
    }
}
