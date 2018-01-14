package tk.tedcook.wgutermtracker;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class AlarmHandler extends BroadcastReceiver {

    public static final String courseAlarmFile = "courseAlarms";
    public static final String assessmentAlarmFile = "assessmentAlarms";
    public static final String alarmFile = "alarmFile";
    public static final String nextAlarmField = "nextAlarmId";

    @Override
    public void onReceive(Context context, Intent intent) {
        String destination = intent.getStringExtra("destination");
        if (destination == null || destination.isEmpty()) {
            destination = "";
        }

        int id = intent.getIntExtra("id", 0);
        String alarmTitle = intent.getStringExtra("title");
        String alarmText = intent.getStringExtra("text");
        int nextAlarmId = intent.getIntExtra("nextAlarmId", getAndIncrementNextAlarmId(context));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_calendar_clock)
                .setContentTitle(alarmTitle)
                .setContentText(alarmText);
        Intent resultIntent;
        Uri uri;
        SharedPreferences sharedPreferences;

        switch (destination) {
            case "course":
                Course course = DataManager.getCourse(context, id);
                if (course != null && course.notifications == 1) {
                    resultIntent = new Intent(context, CourseViewerActivity.class);
                    uri = Uri.parse(DataProvider.COURSES_URI + "/" + id);
                    resultIntent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
                }
                else {
                    return;
                }
                break;
            case "assessment":
                Assessment assessment = DataManager.getAssessment(context, id);
                if (assessment != null && assessment.notifications == 1) {
                    resultIntent = new Intent(context, AssessmentViewerActivity.class);
                    uri = Uri.parse(DataProvider.ASSESSMENTS_URI + "/" + id);
                    resultIntent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, uri);
                }
                else {
                    return;
                }
                break;
            default:
                resultIntent = new Intent(context, MainActivity.class);
                break;
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent).setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nextAlarmId, builder.build());
    }

    public static boolean scheduleCourseAlarm(Context context, long id, long time, String title, String text) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int nextAlarmId = getNextAlarmId(context);
        Intent intentAlarm = new Intent(context, AlarmHandler.class);
        intentAlarm.putExtra("id", id);
        intentAlarm.putExtra("title", title);
        intentAlarm.putExtra("text", text);
        intentAlarm.putExtra("destination", "course");
        intentAlarm.putExtra("nextAlarmId", nextAlarmId);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, nextAlarmId, intentAlarm, PendingIntent.FLAG_ONE_SHOT));

        SharedPreferences sp = context.getSharedPreferences(courseAlarmFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Long.toString(id), nextAlarmId);
        editor.commit();

        incrementNextAlarmId(context);
        return true;
    }

    public static boolean scheduleAssessmentAlarm(Context context, int id, long time, String tile, String text) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int nextAlarmId = getNextAlarmId(context);
        Intent intentAlarm = new Intent(context, AlarmHandler.class);
        intentAlarm.putExtra("id", id);
        intentAlarm.putExtra("title", tile);
        intentAlarm.putExtra("text", text);
        intentAlarm.putExtra("destination", "assessment");
        intentAlarm.putExtra("nextAlarmId", nextAlarmId);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        SharedPreferences sp = context.getSharedPreferences(assessmentAlarmFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Long.toString(id), nextAlarmId);
        editor.commit();

        incrementNextAlarmId(context);
        return true;
    }

    private static int getNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        return nextAlarmId;
    }

    private static void incrementNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        SharedPreferences.Editor alarmEditor = alarmPrefs.edit();
        alarmEditor.putInt(nextAlarmField, nextAlarmId + 1);
        alarmEditor.commit();
    }

    private static int getAndIncrementNextAlarmId(Context context) {
        int nextAlarmId = getNextAlarmId(context);
        incrementNextAlarmId(context);
        return nextAlarmId;
    }
}
