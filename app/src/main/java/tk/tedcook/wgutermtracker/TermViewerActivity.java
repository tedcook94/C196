package tk.tedcook.wgutermtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

public class TermViewerActivity extends AppCompatActivity {

    private static final int TERM_EDITOR_ACTIVITY_CODE = 11111;
    private static final int COURSE_LIST_ACTIVITY_CODE = 22222;

    private Uri termUri;
    private Term term;

    private CursorAdapter cursorAdapter;

    private TextView tv_title;
    private TextView tv_start;
    private TextView tv_end;
    private Menu menu;

    private long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);
        findElements();
        loadTermData();
    }

    private void findElements() {
        tv_title = (TextView) findViewById(R.id.tvTermViewTermTitle);
        tv_start = (TextView) findViewById(R.id.tvTermViewStartDate);
        tv_end = (TextView) findViewById(R.id.tvTermViewEndDate);
    }

    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else {
            termId = Long.parseLong(termUri.getLastPathSegment());
            term = DataManager.getTerm(this, termId);

            setTitle(getString(R.string.view_term));
            tv_title.setText(term.name);
            tv_start.setText(term.start);
            tv_end.setText(term.end);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_viewer, menu);
        this.menu = menu;
        showAppropriateMenuOptions();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_mark_term_active:
                return markTermActive();
            case R.id.action_edit_term:
                Intent intent = new Intent(this, TermEditorActivity.class);
                Uri uri = Uri.parse(DataProvider.TERMS_URI + "/" + term.termId);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);
                break;
            case R.id.action_delete_term:
                return deleteTerm();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean markTermActive() {
        Cursor cursor = getContentResolver().query(DataProvider.TERMS_URI, null, null, null, null);
        ArrayList<Term> termList = new ArrayList<>();
        while (cursor.moveToNext()) {
            termList.add(DataManager.getTerm(this, cursor.getLong(cursor.getColumnIndex(DBOpenHelper.TERMS_TABLE_ID))));
        }

        for (Term term : termList) {
            term.deactivate(this);
        }

        this.term.activate(this);
        showAppropriateMenuOptions();

        Toast.makeText(TermViewerActivity.this, getString(R.string.term_marked_active), Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean deleteTerm() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    long classCount = term.getClassCount(TermViewerActivity.this);
                    if (classCount == 0) {
                        getContentResolver().delete(DataProvider.TERMS_URI, DBOpenHelper.TERMS_TABLE_ID + " = " + termId, null);

                        Toast.makeText(TermViewerActivity.this, getString(R.string.term_deleted), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else {
                        Toast.makeText(TermViewerActivity.this, getString(R.string.need_to_remove_courses), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_term)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
        return true;
    }

    public void showAppropriateMenuOptions() {
        if (term.active == 1) {
            menu.findItem(R.id.action_mark_term_active).setVisible(false);
        }
    }

    public void openClassList(View view) {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra(DataProvider.TERM_CONTENT_TYPE, termUri);
        startActivityForResult(intent, COURSE_LIST_ACTIVITY_CODE);
    }
}
