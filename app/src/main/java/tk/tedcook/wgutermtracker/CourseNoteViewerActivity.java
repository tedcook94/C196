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

public class CourseNoteViewerActivity extends AppCompatActivity {

    private static final int COURSE_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int CAMERA_ACTIVITY_CODE = 22222;

    private long courseNoteId;
    private Uri courseNoteUri;
    private TextView tvCourseNoteText;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCourseNoteText = (TextView) findViewById(R.id.tvCourseNoteText);
        courseNoteUri = getIntent().getParcelableExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE);

        if (courseNoteUri != null) {
            courseNoteId = Long.parseLong(courseNoteUri.getLastPathSegment());
            setTitle(getString(R.string.view_course_note));
            loadNote();
        }
    }

    private void loadNote() {
        CourseNote courseNote = DataManager.getCourseNote(this, courseNoteId);
        tvCourseNoteText.setText(courseNote.text);
        tvCourseNoteText.setMovementMethod(new ScrollingMovementMethod());
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
        getMenuInflater().inflate(R.menu.menu_course_note_viewer, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        CourseNote courseNote = DataManager.getCourseNote(this, courseNoteId);
        Course course = DataManager.getCourse(this, courseNote.courseId);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareSubject = course.name + ": Course Note";
        String shareBody = courseNote.text;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_course_note:
                return deleteCourseNote();
            case R.id.action_add_picture:
                return addPicture();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteCourseNote() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    DataManager.deleteCourseNote(CourseNoteViewerActivity.this, courseNoteId);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(CourseNoteViewerActivity.this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
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
        intent.putExtra("PARENT_URI", courseNoteUri);
        startActivityForResult(intent, CAMERA_ACTIVITY_CODE);
        return true;
    }

    public void handleEditNote(View view) {
        Intent intent = new Intent(this, CourseNoteEditorActivity.class);
        intent.putExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE, courseNoteUri);
        startActivityForResult(intent, COURSE_NOTE_EDITOR_ACTIVITY_CODE);
    }

    public void handleViewImages(View view) {
        Intent intent = new Intent(this, ImageListActivity.class);
        intent.putExtra("ParentUri", courseNoteUri);
        startActivityForResult(intent, 0);
    }

    public void handleAddPicture(View view) {
        addPicture();
    }
}
