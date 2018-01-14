package tk.tedcook.wgutermtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AssessmentNoteViewerActivity extends AppCompatActivity {

    private static final int ASSESSMENT_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int CAMERA_ACTIVITY_CODE = 22222;

    private long assessmentNoteId;
    private Uri assessmentNoteUri;
    private TextView tvAssessmentNoteText;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvAssessmentNoteText = (TextView) findViewById(R.id.tvAssessmentNoteText);
        assessmentNoteUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE);
        if (assessmentNoteUri != null) {
            assessmentNoteId = Long.parseLong(assessmentNoteUri.getLastPathSegment());
            setTitle(getString(R.string.view_assessment_note));
            loadNote();
        }
    }

    private void loadNote() {
        AssessmentNote assessmentNote = DataManager.getAssessmentNote(this, assessmentNoteId);
        tvAssessmentNoteText.setText(assessmentNote.text);
        tvAssessmentNoteText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadNote();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_note_viewer, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        AssessmentNote assessmentNote = DataManager.getAssessmentNote(this, assessmentNoteId);
        Assessment assessment = DataManager.getAssessment(this, assessmentNote.assessmentId);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareSubject = assessment.code + " " + assessment.name + ": Assessment Note";
        String shareBody = assessmentNote.text;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_assessment_note:
                return deleteAssessmentNote();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAssessmentNote() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    DataManager.deleteAssessmentNote(AssessmentNoteViewerActivity.this, assessmentNoteId);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(AssessmentNoteViewerActivity.this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_note)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
        return true;
    }

    private boolean addPicture() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("PARENT_URI", assessmentNoteUri);
        startActivityForResult(intent, CAMERA_ACTIVITY_CODE);
        return true;
    }

    public void handleEditNote(View view) {
        Intent intent = new Intent(this, AssessmentNoteEditorActivity.class);
        intent.putExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE, assessmentNoteUri);
        startActivityForResult(intent, ASSESSMENT_NOTE_EDITOR_ACTIVITY_CODE);
    }

    public void handleViewImages(View view) {
        Intent intent = new Intent(this, ImageListActivity.class);
        intent.putExtra("ParentUri", assessmentNoteUri);
        startActivityForResult(intent, 0);
    }

    public void handleAddPicture(View view) {
        addPicture();
    }
}
