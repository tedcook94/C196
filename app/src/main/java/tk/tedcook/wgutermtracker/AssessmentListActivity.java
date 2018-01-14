package tk.tedcook.wgutermtracker;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AssessmentListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ASSESSMENT_VIEWER_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 22222;

    private CursorAdapter cursorAdapter;
    private long courseId;
    private Uri courseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        courseId = Long.parseLong(courseUri.getLastPathSegment());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentEditorActivity.class);
                intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, courseUri);
                startActivityForResult(intent, ASSESSMENT_EDITOR_ACTIVITY_CODE);
            }
        });
        bindAssessmentList();
        getLoaderManager().initLoader(0, null, this);
    }

    protected void bindAssessmentList() {
        String[] from = {DBOpenHelper.ASSESSMENT_CODE, DBOpenHelper.ASSESSMENT_NAME, DBOpenHelper.ASSESSMENT_DATETIME};
        int[] to = {R.id.tvAssessmentCode, R.id.tvAssessmentName, R.id.tvAssessmentDatetime};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.assessment_list_item, null, from, to, 0);
        DataProvider database = new DataProvider();
        ListView list = (ListView) findViewById(R.id.assessmentListView);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.ASSESSMENTS_URI + "/" + id);
                intent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_VIEWER_ACTIVITY_CODE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.ASSESSMENTS_URI, DBOpenHelper.ASSESSMENTS_COLUMNS,
                DBOpenHelper.ASSESSMENT_COURSE_ID + " = " + this.courseId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }
}
