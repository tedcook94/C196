package tk.tedcook.wgutermtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AssessmentNoteEditorActivity extends AppCompatActivity {

    private long assessmentNoteId;
    private Uri assessmentNoteUri;
    private AssessmentNote assessmentNote;
    private long assessmentId;
    private Uri assessmentUri;
    private EditText assessmentNoteTextField;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assessmentNoteTextField = (EditText) findViewById(R.id.etAssessmentNoteText);
        assessmentNoteUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE);
        if (assessmentNoteUri == null) {
            setTitle(R.string.enter_new_note);
            assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
            assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            action = Intent.ACTION_INSERT;
        }
        else {
            setTitle(R.string.edit_note);
            assessmentNoteId = Long.parseLong(assessmentNoteUri.getLastPathSegment());
            assessmentNote = DataManager.getAssessmentNote(this, assessmentNoteId);
            assessmentId = assessmentNote.assessmentId;
            assessmentNoteTextField.setText(assessmentNote.text);
            action = Intent.ACTION_EDIT;
        }
    }

    public void saveAssessmentNote(View view) {
        if (action == Intent.ACTION_INSERT) {
            DataManager.insertAssessmentNote(this, assessmentId, assessmentNoteTextField.getText().toString().trim());
            setResult(RESULT_OK);
            finish();
        }
        if (action == Intent.ACTION_EDIT) {
            assessmentNote.text = assessmentNoteTextField.getText().toString().trim();
            assessmentNote.saveChanges(this);
            setResult(RESULT_OK);
            finish();
        }
    }
}
