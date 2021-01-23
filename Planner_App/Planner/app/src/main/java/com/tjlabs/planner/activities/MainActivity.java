package com.tjlabs.planner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tjlabs.planner.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import objects.CalendarDay;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Notes",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //Set which layout to render
        setContentView(R.layout.calendar_view);
        //get the CalendarView from the calendar_view.xml file by id
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView date  = findViewById(R.id.dateInsert);
        CalendarDay currentDate = new CalendarDay(LocalDate.now().getMonthValue(),LocalDate.now().getDayOfMonth(), LocalDate.now().getYear());
        date.setText(currentDate.toString());
//        BusyDayNotification busyDayNotification = new BusyDayNotification(calendarView);
//        busyDayNotification.createNotificationChannel();

        //Listener for the calendar view
        CalendarView.OnDateChangeListener listener =new CalendarView.OnDateChangeListener() {
            @Override
            //specifies what to do when the date is changed
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                new Thread(new Runnable() {
                    public void run() {
                        //month is in array 0-11, add 1 to get current month
                        CalendarDay calendarDay = new CalendarDay(month+1, dayOfMonth, year);
                        date.setText(calendarDay.toString());
                        String calendarId = calendarDay.toString();

                        if (sharedPreferences.contains(calendarId)) {
                            Boolean foundAll = false;
                            int index = 0;
                            LinkedHashSet<String> hashSet = new LinkedHashSet<>();
                            while(!foundAll){
                                if(sharedPreferences.contains(calendarId+"_"+(index))){
                                    hashSet.add(sharedPreferences.getString(calendarId+"_"+index, null));
                                    index++;
                                }
                                else{
                                    foundAll =true;
                                }
                            }
                            calendarDay.setNotes(hashSet);
                        }
                        else {
                            LinkedHashSet<String> hash_Set = new LinkedHashSet<>();
                            hash_Set.add("");
                            calendarDay.setNotes(hash_Set);
                            //mark date as something that contains notes
                            editor.putString(calendarId, "Used");
                            // key of the calendar id and underscore int specifies the place of the not in a list
                            editor.putString(calendarId+"_0","");
                            editor.apply();
                        }
                        reviewDate(calendarDay);
                    }

                }).start();
            }
        };

        //attach a listener to the calendar view whenever the date is changed
        calendarView.setOnDateChangeListener(listener);
    }

    //function to start the EditDateActivity and to send it the selectedDate
    public void reviewDate(CalendarDay calendarDay){
        Intent intent = new Intent(this, EditDateActivity.class);
        TextView dateTextView = findViewById(R.id.dateInsert);
        String selectDate = dateTextView.getText().toString();
        intent.putExtra("Date", selectDate);
        ArrayList<String> tasks = new ArrayList<>(calendarDay.getNotes());
        intent.putExtra("Tasks", tasks);
        startActivity(intent);
    }
}
