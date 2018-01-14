package tk.tedcook.wgutermtracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class AssessmentEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private Assessment assessment;
    private long courseId;
    private EditText etAssessmentCode;
    private EditText etAssessmentName;
    private EditText etAssessmentDescription;
    private EditText etAssessmentDatetime;
    private DatePickerDialog assessmentDateDialog;
    private TimePickerDialog assessmentTimeDialog;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etAssessmentCode = (EditText) findViewById(R.id.etAssessmentCode);
        etAssessmentName = (EditText) findViewById(R.id.etAssessmentName);
        etAssessmentDescription = (EditText) findViewById(R.id.etAssessmentDescription);
        etAssessmentDatetime = (EditText) findViewById(R.id.etAssessmentDatetime);

        Uri assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        if (assessmentUri == null) {
            setTitle(getString(R.string.new_assessment));
            action = Intent.ACTION_INSERT;
            Uri courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
            courseId = Long.parseLong(courseUri.getLastPathSegment());
            assessment = new Assessment();
        }
        else {
            setTitle(getString(R.string.edit_assessment));
            action = Intent.ACTION_EDIT;
            Long assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            assessment = DataManager.getAssessment(this, assessmentId);
            courseId = assessment.courseId;
            fillAssessmentForm();
        }
        setupDateAndTimePickers();
    }

    private void fillAssessmentForm() {
        if (assessment != null) {
            etAssessmentCode.setText(assessment.code);
            etAssessmentName.setText(assessment.name);
            etAssessmentDescription.setText(assessment.description);
            etAssessmentDatetime.setText(assessment.datetime);
        }
    }

    private void setupDateAndTimePickers() {
        etAssessmentDatetime.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        assessmentDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar2 = Calendar.getInstance();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                etAssessmentDatetime.setText(DateUtil.dateFormat.format(newDate.getTime()));
                assessmentTimeDialog = new TimePickerDialog(AssessmentEditorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        }
                        else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String minuteString = Integer.toString(minute);
                        if (minute < 10) {
                            minuteString = "0" + minuteString;
                        }
                        String datetime = etAssessmentDatetime.getText().toString() + " " + hourOfDay + ":" + minuteString
                                + " " + AM_PM + " " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
                        etAssessmentDatetime.setText(datetime);
                    }
                }, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), false);
                assessmentTimeDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        etAssessmentDatetime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    assessmentDateDialog.show();
                }
            }
        });
    }


    public void saveAssessmentChanges(View view) {
        getValuesFromFields();
        switch (action) {
            case Intent.ACTION_INSERT:
                DataManager.insertAssessment(this, courseId, assessment.code, assessment.name, assessment.description,
                        assessment.datetime);
                setResult(RESULT_OK);
                finish();
                break;
            case Intent.ACTION_EDIT:
                assessment.saveChanges(this);
                setResult(RESULT_OK);
                finish();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void getValuesFromFields() {
        assessment.code = etAssessmentCode.getText().toString().trim();
        assessment.name = etAssessmentName.getText().toString().trim();
        assessment.description = etAssessmentDescription.getText().toString().trim();
        assessment.datetime = etAssessmentDatetime.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        if (view == etAssessmentDatetime) {
            assessmentDateDialog.show();
        }
    }
}
