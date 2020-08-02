package com.tjlabs.planner.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.TextView;

import com.tjlabs.planner.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapters.TaskAdapter;
import objects.CalendarDay;

public class EditDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String[] selectedDate = {intent.getStringExtra("Date")};
        final ArrayList<String> taskList = intent.getStringArrayListExtra("Tasks");
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Notes",MODE_PRIVATE );
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        setContentView(R.layout.edit_date_activity);

        final TextView editDate = findViewById(R.id.dateText);
        editDate.setText(selectedDate[0]);
        Button nextDateButton = findViewById(R.id.nextDate);
        Button prevDateButton = findViewById(R.id.previosDate);
        Switch editSwitch = findViewById(R.id.switch1);

        //RecyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final TaskAdapter[] taskAdapter = {new TaskAdapter(taskList, this)};
        recyclerView.setAdapter(taskAdapter[0]);

        //Next Button
        nextDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate nextDate = calendarDay.getNextDay();
                CalendarDay nextDay = new CalendarDay( nextDate.getMonthValue(),nextDate.getDayOfMonth(), nextDate.getYear());
                selectedDate[0] = nextDay.toString();
                editDate.setText(nextDay.toString());

                //check for existence of nextDay, if there display, if not create it in editor and put a blank in the first spot
                if(!sharedPreferences.contains(nextDay.toString())){
                    Set<String> hash_Set = new HashSet<String>();
                    hash_Set.add(" ");
                    calendarDay.setNotes((HashSet<String>) hash_Set);
                    editor.putStringSet(nextDay.toString(), hash_Set);
                    editor.apply();
                    nextDay.setNotes((HashSet<String>) hash_Set);
                }
                nextDay.setNotes((HashSet<String>) sharedPreferences.getStringSet(nextDay.toString(), null));
                ArrayList<String> newTaskList = new ArrayList<>(nextDay.getNotes());
                taskAdapter[0] = new TaskAdapter(newTaskList,this);
                recyclerView.setAdapter(taskAdapter[0]);
            }
        });

        prevDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate prevDate = calendarDay.getPreviousDay();
                CalendarDay prevDay = new CalendarDay( prevDate.getMonthValue(),prevDate.getDayOfMonth(), prevDate.getYear());
                selectedDate[0] = prevDay.toString();
                editDate.setText(prevDay.toString());
                //check for existence of prevDay, if there display, if not create it in editor and put a blank in the first spot
                if(!sharedPreferences.contains(prevDay.toString())){
                    Set<String> hash_Set = new HashSet<String>();
                    hash_Set.add(" ");
                    calendarDay.setNotes((HashSet<String>) hash_Set);
                    editor.putStringSet(prevDay.toString(), hash_Set);
                    editor.apply();
                    prevDay.setNotes((HashSet<String>) hash_Set);
                }
                prevDay.setNotes((HashSet<String>) sharedPreferences.getStringSet(prevDay.toString(), null));
                ArrayList<String> newTaskList = new ArrayList<>(prevDay.getNotes());
                taskAdapter[0] = new TaskAdapter(newTaskList,this);
                recyclerView.setAdapter(taskAdapter[0]);
            }
        });
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
