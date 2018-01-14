package tk.tedcook.wgutermtracker;

import android.content.ContentValues;
import android.content.Context;

public class AssessmentNote {
    public long assessmentNoteId;
    public long assessmentId;
    public String text;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID, assessmentId);
        values.put(DBOpenHelper.ASSESSMENT_NOTE_TEXT, text);
        context.getContentResolver().update(DataProvider.ASSESSMENT_NOTES_URI, values, DBOpenHelper.ASSESSMENT_NOTES_TABLE_ID
                + " = " + assessmentNoteId, null);
    }
}
