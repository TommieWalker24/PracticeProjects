package com.tjlabs.planner.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.TextView;

import com.tjlabs.planner.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import objects.CalendarDay;

public class EditDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String[] selectedDate = {intent.getStringExtra("Date")};

        setContentView(R.layout.edit_date_activity);

        final TextView editDate = findViewById(R.id.dateText);
        editDate.setText(selectedDate[0]);
        Button nextDateButton = findViewById(R.id.nextDate);
        Button prevDateButton = findViewById(R.id.previosDate);
        Switch editSwitch = findViewById(R.id.switch1);

        //RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Next Button
        nextDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate nextDate = calendarDay.getNextDay();
                CalendarDay nextDay = new CalendarDay( nextDate.getMonthValue(),nextDate.getDayOfMonth(), nextDate.getYear());
                selectedDate[0] = nextDay.toString();
                editDate.setText(nextDay.toString());
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
            }
        });
    }
}
