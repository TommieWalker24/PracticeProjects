package com.tjlabs.planner.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tjlabs.planner.R;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import objects.CalendarDay;

public class MainActivity extends AppCompatActivity {
    //define globals here
//    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Notes",MODE_PRIVATE );
//   // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//    SharedPreferences.Editor editor = sharedPreferences.edit();
   // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
   // SharedPreferences.Editor editor = sharedPref.edit();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Notes",MODE_PRIVATE );
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //Set which layout to render
        setContentView(R.layout.calendar_view);
        //get the CalendarView from the calendar_view.xml file by id
        final CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView date  = findViewById(R.id.dateInsert);
        CalendarDay currentDate = new CalendarDay(LocalDate.now().getMonthValue(),LocalDate.now().getDayOfMonth(), LocalDate.now().getYear());
        date.setText(currentDate.toString());

        //Listener for the calendar view
        CalendarView.OnDateChangeListener listener =new CalendarView.OnDateChangeListener() {
            @Override
            //specifies what to do when the date is changed
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                new Thread(new Runnable() {
                    public void run() {
                        CalendarDay calendarDay = new CalendarDay(month, dayOfMonth, year);
                        date.setText(calendarDay.toString());
                        String calendarId = calendarDay.toString();

                        if (sharedPreferences.contains(calendarId)) {
                            reviewDate(calendarView);
                        } else {
                            Set<String> hash_Set = new HashSet<String>();
                            hash_Set.add("this");
                            hash_Set.add("is");
                            hash_Set.add("new");
                            editor.putStringSet(calendarId, hash_Set);
                            editor.apply();
                        }
                    }

                }).start();
            }
        };

        //attach a listener to the calendar view whenever the date is changed
        calendarView.setOnDateChangeListener(listener);
    }

    //function to start the EditDateActivity and to send it the selectedDate
    public void reviewDate(View view){
        Intent intent = new Intent(this, EditDateActivity.class);
        TextView dateTextView = findViewById(R.id.dateInsert);
        String selectDate = dateTextView.getText().toString();
        intent.putExtra("Date", selectDate);

        startActivity(intent);
    }
}
