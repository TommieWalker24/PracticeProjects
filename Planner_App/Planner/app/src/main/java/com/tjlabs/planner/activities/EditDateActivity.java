package com.tjlabs.planner.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tjlabs.planner.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import adapters.TaskAdapter;
import objects.CalendarDay;

public class EditDateActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get sent intent information
        Intent intent = getIntent();
        final String[] selectedDate = {intent.getStringExtra("Date")};
        final ArrayList<String>[] taskList = new ArrayList[]{intent.getStringArrayListExtra("Tasks")};
        //access data files
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Notes",MODE_PRIVATE );
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //Set activity
        setContentView(R.layout.edit_date_activity);
        //init textView
        final TextView editDate = findViewById(R.id.dateText);
        editDate.setText(selectedDate[0]);
        //init buttons
        Button nextDateButton = findViewById(R.id.nextDate);
        Button prevDateButton = findViewById(R.id.previosDate);
        final FloatingActionButton addLineFloatingActionButton = findViewById(R.id.addTaskFloatingActionButton);
        addLineFloatingActionButton.setVisibility(View.GONE);
        final FloatingActionButton calendarFloatingActionButton = findViewById(R.id.calendarFloatingActionButton);
        calendarFloatingActionButton.setVisibility(View.VISIBLE);
        //init switch
        final Switch editSwitch = findViewById(R.id.switch1);

        //init RecyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //Adapter for RecyclerView
        final TaskAdapter[][] taskAdapter = {{new TaskAdapter(taskList[0], this)}};
        recyclerView.setAdapter(taskAdapter[0][0]);

        //edit switch listener
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if switch is on then attach listeners to task textViews
                if(isChecked){
                    //make add line button visible
                    addLineFloatingActionButton.setVisibility(View.VISIBLE);
                    calendarFloatingActionButton.setVisibility(View.GONE);
                    //RecyclerView layout
                    LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                    for(int i  = layout.findFirstVisibleItemPosition(); i <= layout.findLastVisibleItemPosition(); i++){
                        View view = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(i);
                        //if view is ever null it means the view is not displayed on the page
                        final TextView textView = (TextView) view.findViewById(R.id.multiAutoCompleteTextView);
                        textView.setEnabled(true);
                        final int finalI = i;
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //todo: do something to make the selected note visible
                                recyclerView.scrollToPosition(finalI);
                            }
                        });
                        final String[] before = new String[1];
                        final String[] after = new String[1];
                        textChangeListener(textView,selectedDate[0], sharedPreferences, editor, taskAdapter[0][0]);
                    }
                }
                //if switch is off, detach task textView listeners
                else{
                    //remove add line button
                    addLineFloatingActionButton.setVisibility(View.GONE);
                    calendarFloatingActionButton.setVisibility(View.VISIBLE);
                    for(int i = 0; i< taskList[0].size(); i++){
                        View view = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(i);
                        if(view == null){
                            recyclerView.scrollToPosition(i);
                            view = recyclerView.getChildAt(i);
                            if (view == null){
                                break;
                            }
                        }
                        //when switch is on and next button tries to turn it off, the view above is null
                        final TextView textView = (TextView) view.findViewById(R.id.multiAutoCompleteTextView);
                        textView.setEnabled(false);
                    }
                }


            }
        });


        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                    for(int i  = layout.findFirstVisibleItemPosition(); i <= layout.findLastVisibleItemPosition(); i++) {
                        View view = layout.findViewByPosition(i);
                        TextView textView = view.findViewById(R.id.multiAutoCompleteTextView);
                        if (editSwitch.isChecked()) {
                            textView.setEnabled(true);
                            textChangeListener(textView,selectedDate[0], sharedPreferences, editor, taskAdapter[0][0]);
                        }
                        else{
                            textView.setEnabled(false);
                        }
                    }
                }
        });

        //Next Button
        nextDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                if(editSwitch.isChecked()){
                    editSwitch.setChecked(false);
                }
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate nextDate = calendarDay.getNextDay();
                CalendarDay nextDay = new CalendarDay( nextDate.getMonthValue(),nextDate.getDayOfMonth(), nextDate.getYear());
                selectedDate[0] = nextDay.toString();
                editDate.setText(nextDay.toString());

                //check for existence of nextDay, if there display, if not create it in editor and put a blank in the first spot
                if(!sharedPreferences.contains(nextDay.toString())){
                    HashSet<String> hash_Set = new HashSet<String>();
                    hash_Set.add("");
                    calendarDay.setNotes(hash_Set);
                    editor.putStringSet(nextDay.toString(), hash_Set);
                    editor.apply();
                    nextDay.setNotes(hash_Set);
                }
                nextDay.setNotes((HashSet<String>) sharedPreferences.getStringSet(nextDay.toString(), null));
                taskList[0] = new ArrayList<>(nextDay.getNotes());
                taskAdapter[0][0] = new TaskAdapter(taskList[0],this);
                recyclerView.setAdapter(taskAdapter[0][0]);
             //todo:   taskAdapter.notify();
            }
        });
        //prev button
        prevDateButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                if(editSwitch.isChecked()){
                    editSwitch.toggle();
                }
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate prevDate = calendarDay.getPreviousDay();
                CalendarDay prevDay = new CalendarDay( prevDate.getMonthValue(),prevDate.getDayOfMonth(), prevDate.getYear());
                selectedDate[0] = prevDay.toString();
                editDate.setText(prevDay.toString());
                //check for existence of prevDay, if there display, if not create it in editor and put a blank in the first spot
                if(!sharedPreferences.contains(prevDay.toString())){
                    HashSet<String> hash_Set = new HashSet<String>();
                    hash_Set.add("");
                    calendarDay.setNotes(hash_Set);
                    editor.putStringSet(prevDay.toString(), hash_Set);
                    editor.apply();
                    prevDay.setNotes(hash_Set);
                }
                prevDay.setNotes((HashSet<String>) sharedPreferences.getStringSet(prevDay.toString(), null));
                taskList[0] = new ArrayList<>(prevDay.getNotes());
                taskAdapter[0][0] = new TaskAdapter(taskList[0],this);
                recyclerView.setAdapter(taskAdapter[0][0]);
              //todo:  taskAdapter.notify();
            }
        });

        //add line button
        addLineFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(editSwitch.isChecked()){
                    //turn edit mode off
                    editSwitch.performClick();
                    Set<String> hash_Set = sharedPreferences.getStringSet(selectedDate[0], null);
                    hash_Set.add("");
                    editor.putStringSet(selectedDate[0], hash_Set);
                    editor.apply();
                    ArrayList<String> compareList =  new ArrayList<>(Objects.requireNonNull(sharedPreferences.getStringSet(selectedDate[0], null)));
                    for(String item: compareList){
                        if(!taskList[0].contains(item)){
                            taskList[0].add(item);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(taskList[0].size()-1);
                            taskAdapter[0][0].notifyDataSetChanged();
                        }
                    }
                    //turn edit mode back on to re-attach listeners for all items in list
                    editSwitch.performClick();
                }
            }
        });

        //Calendar View button
        calendarFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editSwitch.isChecked()){
                    returnToCalendarView();
                }
            }
        });
    }
    public void returnToCalendarView(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    void textChangeListener(TextView textView, final String selectedDate, final SharedPreferences sharedPreferences, final SharedPreferences.Editor editor, final TaskAdapter taskAdapter){
        textView.addTextChangedListener(new TextWatcher() {
            String before;
            String after;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                after = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CalendarDay calendarDay = new CalendarDay(selectedDate);
                Set<String> hash_Set = sharedPreferences.getStringSet(calendarDay.toString(), null);
                assert hash_Set != null;
                hash_Set.remove(before);
                hash_Set.add(after);
                editor.remove(calendarDay.toString());
                editor.apply();

                editor.putStringSet(calendarDay.toString(), hash_Set);
                editor.apply();

//                                int foundIndex = taskList[0].indexOf(before[0]);
//                                taskList[0].set(foundIndex, after[0]);
                // taskAdapter[0][0].notifyDataSetChanged();

            }
        });
    }

}
