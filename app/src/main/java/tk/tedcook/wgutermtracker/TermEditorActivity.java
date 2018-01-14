package tk.tedcook.wgutermtracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MAIN_ACTIVITY_CODE = 1;
    private String action;
    private String termFilter;
    private Term term;

    private EditText termNameField;
    private EditText termStartDateField;
    private EditText termEndDateField;

    private DatePickerDialog termStartDateDialog;
    private DatePickerDialog termEndDateDialog;
    private SimpleDateFormat dateFormat;

    private DataProvider database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = new DataProvider();

        termNameField = (EditText) findViewById(R.id.termNameEditText);
        termStartDateField = (EditText) findViewById(R.id.termStartDateEditText);
        termStartDateField.setInputType(InputType.TYPE_NULL);
        termEndDateField = (EditText) findViewById(R.id.termEndDateEditText);
        termEndDateField.setInputType(InputType.TYPE_NULL);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        if (uri == null) {
            action = intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_term));
        }
        else {
            action = Intent.ACTION_EDIT;
            setTitle(getString(R.string.edit_term_title));
            long termId = Long.parseLong(uri.getLastPathSegment());
            term = DataManager.getTerm(this, termId);
            fillTermForm(term);
        }
        setupDatePickers();
    }

    private void fillTermForm(Term term) {
        termNameField.setText(term.name);
        termStartDateField.setText(term.start);
        termEndDateField.setText(term.end);
    }

    private void getTermFromForm() {
        term.name = termNameField.getText().toString().trim();
        term.start = termStartDateField.getText().toString().trim();
        term.end = termEndDateField.getText().toString().trim();
    }

    private void setupDatePickers() {
        termStartDateField.setOnClickListener(this);
        termEndDateField.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        termStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termStartDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        termEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termEndDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        termStartDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    termStartDateDialog.show();
                }
            }
        });
    }

    public void saveTermChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            term = new Term();
            getTermFromForm();

            DataManager.insertTerm(this, term.name, term.start, term.end, term.active);
            Toast.makeText(this, getString(R.string.term_saved), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
        else if (action == Intent.ACTION_EDIT) {
            getTermFromForm();
            term.saveChanges(this);
            Toast.makeText(this, getString(R.string.term_updated), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == termStartDateField) {
            termStartDateDialog.show();
        }
        if (view == termEndDateField) {
            termEndDateDialog.show();
        }
    }
}
