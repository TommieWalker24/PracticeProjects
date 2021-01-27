package objects;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import com.tjlabs.planner.R;
import com.tjlabs.planner.activities.EditDateActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

import objects.notifications.BusyDayNotification;

//import objects.notifications.BusyDayNotification;

public class NewTask {
    boolean exists;
    String dateString;
    String task;
    String taskTime;
    String location;
    boolean pm;
    boolean edit;
    int index;

    public void showPopupWindow(final View view){
        final SharedPreferences sharedPreferences = view.getContext().getApplicationContext().getSharedPreferences("Notes", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newTaskView = inflater.inflate(R.layout.new_task_card,null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        //create new window
        final PopupWindow popupWindow = new PopupWindow(newTaskView,width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        //initialize card attributes
        TextView title = newTaskView.findViewById(R.id.title);
        final TextView date = newTaskView.findViewById(R.id.editTextDate);
        date.setText(dateString);
        final TextView time = newTaskView.findViewById(R.id.editTime);
        if(taskTime != null){
            time.setText(taskTime);
        }
        final MultiAutoCompleteTextView description = newTaskView.findViewById(R.id.editDescription);
        if(task != null){
            description.setText(task);
        }
        final TextView address = newTaskView.findViewById(R.id.editLocation);
        if(location != null){
            address.setText(location.toString());
        }
        final RadioButton pmButton = newTaskView.findViewById(R.id.radioButtonPM);
        RadioButton amButton = newTaskView.findViewById(R.id.radioButtonAM);
        if(pm){
            pmButton.setChecked(true);
        }else{
            amButton.setChecked(true);
        }
        Button applyButton = newTaskView.findViewById(R.id.taskApplyButton);


        //if this exists, view only
        if(exists == true && edit == false){
            //disable form
            time.setEnabled(false);
            description.setEnabled(false);
            description.setClickable(true);
            address.setEnabled(false);
            address.setClickable(true);
            pmButton.setEnabled(false);
            amButton.setEnabled(false);
            applyButton.setEnabled(false);
        }
        //want field to edit but not add new entry when submitted
        else if(exists == true && edit == true){
            applyButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    final CalendarDay calendarDay = new CalendarDay(dateString);
                    if (sharedPreferences.contains(dateString)) {
                        Boolean foundAll = false;
                        int index = 0;
                        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
                        while(!foundAll){
                            if(sharedPreferences.contains(dateString+"_"+(index))){
                                hashSet.add(sharedPreferences.getString(dateString+"_"+index, null));
                                index++;
                            }
                            else{
                                foundAll =true;
                            }
                        }
                        calendarDay.setNotes(hashSet);
                    }
                    ArrayList<String> tasks = new ArrayList<>(calendarDay.getNotes());
                    editor.putString(dateString + "_" + index, description.getText().toString());
                    editor.putString(dateString+"_"+index+"_time", time.getText().toString());
                    editor.putString(dateString+"_"+index+"_location",address.getText().toString());
                    editor.putBoolean(dateString+"_"+index+"_pm",pmButton.isChecked());
                    editor.apply();

                    tasks.set(index, description.getText().toString());
                    LinkedHashSet <String> lh = new LinkedHashSet<String>();
                    for(String s: tasks){
                        lh.add(s);
                    }
                    calendarDay.setNotes(lh);
//                    calendarDay.addNote(description.getText().toString());
                    final Intent intent = new Intent(view.getContext(), EditDateActivity.class);
                    intent.putExtra("Date", dateString);
                    intent.putExtra("Tasks", tasks);

                    Toast.makeText(view.getContext(), "Task Edited", Toast.LENGTH_SHORT).show();
                    final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(view.getContext());
                    final BusyDayNotification busyDayNotification = new BusyDayNotification(view);
                    busyDayNotification.createNotificationChannel();
                    AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                    AlarmManager.OnAlarmListener alarmListener = new AlarmManager.OnAlarmListener() {
                        @Override
                        public void onAlarm() {
                            notificationManagerCompat.notify(Integer.parseInt(view.getContext().getString(R.string.channel_ID)), busyDayNotification.builder(calendarDay).build());

                        }
                    };
                    Handler handler = new Handler();

                    Calendar calendar = Calendar.getInstance();
                    Date current = new Date();

                    String[] selectDate = calendarDay.toString().split("/");
                    String[] timeArray = time.getText().toString().split(":");
                    calendar.set(Calendar.MONTH,Integer.parseInt(selectDate[0]) -1 );
                    calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(selectDate[1]));

                    calendar.set(Calendar.YEAR, Integer.parseInt(selectDate[2]));
                    if(pmButton.isChecked()){
                        calendar.set(Calendar.HOUR, 12+Integer.parseInt(time.getText().toString().split(":")[0]));
                    }
                    else {
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.getText().toString().split(":")[0]));
                    }

                    calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]) -1);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    Date setDate = calendar.getTime();
                    long diffInMillies = Math.abs(setDate.getTime() -  current.getTime());
                    //sets the time to initiate notification
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+ diffInMillies,"notifications", alarmListener,handler );

                    popupWindow.dismiss();
                    view.getContext().startActivity(intent);
                }
            });
        }
        //if this is new entry
        else{
            applyButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    final CalendarDay calendarDay = new CalendarDay(dateString);
                    if (sharedPreferences.contains(dateString)) {
                        Boolean foundAll = false;
                        int index = 0;
                        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
                        while(!foundAll){
                            if(sharedPreferences.contains(dateString+"_"+(index))){
                                hashSet.add(sharedPreferences.getString(dateString+"_"+index, null));
                                index++;
                            }
                            else{
                                foundAll =true;
                            }
                        }
                        calendarDay.setNotes(hashSet);
                    }
                    ArrayList<String> tasks = new ArrayList<>(calendarDay.getNotes());
                    editor.putString(dateString + "_" + tasks.size(), description.getText().toString());
                    editor.putString(dateString+"_"+tasks.size()+"_time", time.getText().toString());
                    editor.putString(dateString+"_"+tasks.size()+"_location",address.getText().toString());
                    editor.putBoolean(dateString+"_"+tasks.size()+"_pm",pmButton.isChecked());
                    editor.apply();

                    tasks.add(description.getText().toString());
                    calendarDay.addNote(description.getText().toString());
                    final Intent intent = new Intent(view.getContext(), EditDateActivity.class);
                    intent.putExtra("Date", dateString);
                    intent.putExtra("Tasks", tasks);

                    Toast.makeText(view.getContext(), "Task added", Toast.LENGTH_SHORT).show();
                    final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(view.getContext());
                    final BusyDayNotification busyDayNotification = new BusyDayNotification(view);
                    busyDayNotification.createNotificationChannel();
                    AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                    AlarmManager.OnAlarmListener alarmListener = new AlarmManager.OnAlarmListener() {
                        @Override
                        public void onAlarm() {
                            notificationManagerCompat.notify(Integer.parseInt(view.getContext().getString(R.string.channel_ID)), busyDayNotification.builder(calendarDay).build());

                        }
                    };
                    Handler handler = new Handler();

                    Calendar calendar = Calendar.getInstance();
                    Date current = new Date();

                    String[] selectDate = calendarDay.toString().split("/");
                    String[] timeArray = time.getText().toString().split(":");
                    calendar.set(Calendar.MONTH,Integer.parseInt(selectDate[0]) -1 );
                    calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(selectDate[1]));

                    calendar.set(Calendar.YEAR, Integer.parseInt(selectDate[2]));
                    if(pmButton.isChecked()){
                        calendar.set(Calendar.HOUR, 12+Integer.parseInt(time.getText().toString().split(":")[0]));
                    }
                    else {
                        calendar.set(Calendar.HOUR, Integer.parseInt(time.getText().toString().split(":")[0]));
                    }

                    calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]) -1);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    Date setDate = calendar.getTime();
                    long diffInMillies = Math.abs(setDate.getTime() -  current.getTime());
                    //sets the time to initiate notification
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+ diffInMillies,"notifications", alarmListener,handler );

                    popupWindow.dismiss();
                    view.getContext().startActivity(intent);
                }
            });
        }


        //close the window when touched outside of view
        newTaskView.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void setDateString(String date){
        this.dateString = date;
    }
    public void setTask(String task){
        this.task = task;
    }
    public void setTime(String time){
        this.taskTime = time;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public void setExists(boolean exists){this.exists = exists;}
    public void setPM(boolean bool){this.pm = bool;}
    public void setIndex(int i){this.index = i;}
    public void setEdit(boolean edit){this.edit = edit;}


}
