package com.tjlabs.planner.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tjlabs.planner.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;

import adapters.TaskAdapter;
import objects.CalendarDay;
import objects.NewTask;
import objects.TaskTextChangeListener;

public class EditDateActivity extends AppCompatActivity {
    ArrayList<String> taskList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get sent intent information
        Intent intent = getIntent();
        final String[] selectedDate = {intent.getStringExtra("Date")};
        taskList = new ArrayList(intent.getStringArrayListExtra("Tasks"));
        //access data files
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Notes", MODE_PRIVATE);
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
        final TaskAdapter[][] taskAdapter = {{new TaskAdapter(taskList, this)}};
        recyclerView.setAdapter(taskAdapter[0][0]);

        //edit switch listener
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            TaskTextChangeListener taskTextChangeListener;
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if switch is on then attach listeners to task textViews
                if (isChecked) {
                    //make add line button visible
                    addLineFloatingActionButton.setVisibility(View.VISIBLE);
                    calendarFloatingActionButton.setVisibility(View.GONE);
                    //RecyclerView layout
                    LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                    for (int i = layout.findFirstVisibleItemPosition(); i <= layout.findLastVisibleItemPosition(); i++) {
                        View view = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(i);
                        //if view is ever null it means the view is not displayed on the page
                        final TextView textView = (TextView) view.findViewById(R.id.multiAutoCompleteTextView);
                        final int finalI = i;
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewTask newTask = new NewTask();
                                newTask.setDateString(editDate.getText().toString());
                                //get the task description
                                String taskDescription = sharedPreferences.getString(editDate.getText().toString()+ "_"+ finalI, "Not Available");
                                String time = sharedPreferences.getString(editDate.getText().toString()+ "_"+ finalI + "_time", "Time not found");
                                String location =  sharedPreferences.getString(editDate.getText().toString()+ "_"+ finalI + "_location", "Location not found");;
                                newTask.setTask(taskDescription);
                                newTask.setLocation(location);
                                newTask.setTime(time);
                                newTask.showPopupWindow(v);
                                System.out.println("View at position " + finalI + " was clicked!");
                            }
                        });
                    }
//////            todo: breaks here after count of 6!!
////                        textView.setEnabled(true);
////                        final int finalI = i;
////                        textView.setOnLongClickListener(new View.OnLongClickListener() {
////                            @Override
////                            public boolean onLongClick(View v) {
////                                //todo: do something to make the selected note information visible
////                                recyclerView.scrollToPosition(finalI);
////                                System.out.println("View was long clicked!!!!!");
////                                return true;
////                            }
////                        });
////                        //need to verify index with what number textview we are actually using in taskList. Recycle view will on use first visible slots and reuse them when scrolled
////                        i = verifyIndex(i, textView.getText().toString(), layout);
////                        taskTextChangeListener = new TaskTextChangeListener(recyclerView, taskList,i, selectedDate[0], editor);
////                        textView.addTextChangedListener(taskTextChangeListener);
////                    }
                }
                //if switch is off, detach task textView listeners
                else {
                    //check to make sure everything is saved
                    editor.apply();
                    //remove add line button
                    addLineFloatingActionButton.setVisibility(View.GONE);
                    //make calendar button visible
                    calendarFloatingActionButton.setVisibility(View.VISIBLE);
                    LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                    for (int i = layout.findFirstVisibleItemPosition(); i <= layout.findLastVisibleItemPosition(); i++) {
                        View view = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(i);
                        final TextView textView = (TextView) view.findViewById(R.id.multiAutoCompleteTextView);
                        //remove listeners
//                        textView.removeTextChangedListener(taskTextChangeListener);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                }
            }
        });


//        Scroll Listener
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager layout = (LinearLayoutManager) recyclerView.getLayoutManager();
                for (int i = layout.findFirstVisibleItemPosition(); i <= layout.findLastVisibleItemPosition(); i++) {
                    View view = layout.findViewByPosition(i);
                    TextView textView = view.findViewById(R.id.multiAutoCompleteTextView);
                    final int finalI = i;
                    if (editSwitch.isChecked()) {
                        //textView.setEnabled(true);
                        //textChangeListener(textView, selectedDate[0], recyclerView, editor, taskAdapter[0][0], i);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("View at position " + finalI + " was clicked!");
                            }
                        });
                    } else {
//                        removeChangeListener(textView, selectedDate[0],recyclerView,editor, taskAdapter[0][0],i);
//                        textView.setEnabled(false);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                }

            }
        });

        //Next Button
        nextDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                if (editSwitch.isChecked()) {
                    editSwitch.setChecked(false);
                }
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate nextDate = calendarDay.getNextDay();
                CalendarDay nextDay = new CalendarDay(nextDate.getMonthValue(), nextDate.getDayOfMonth(), nextDate.getYear());
                selectedDate[0] = nextDay.toString();
                editDate.setText(nextDay.toString());

                //check for existence of nextDay, if there display, if not create it in editor and put a blank in the first spot
                if (!sharedPreferences.contains(nextDay.toString())) {
                    nextDay.setNotes(newUsedDay(nextDay, editor));
                } else {
                    nextDay.setNotes(findAllTasks(nextDay, sharedPreferences));
                }
                taskList = new ArrayList<>(nextDay.getNotes());
                taskAdapter[0][0] = new TaskAdapter(taskList, this);
                recyclerView.setAdapter(taskAdapter[0][0]);
            }
        });
        //prev button
        prevDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                if (editSwitch.isChecked()) {
                    editSwitch.toggle();
                }
                CalendarDay calendarDay = new CalendarDay(selectedDate[0]);
                LocalDate prevDate = calendarDay.getPreviousDay();
                CalendarDay prevDay = new CalendarDay(prevDate.getMonthValue(), prevDate.getDayOfMonth(), prevDate.getYear());
                selectedDate[0] = prevDay.toString();
                editDate.setText(prevDay.toString());
                //check for existence of prevDay, if there display, if not create it in editor and put a blank in the first spot
                if (!sharedPreferences.contains(prevDay.toString())) {
                    prevDay.setNotes(newUsedDay(prevDay, editor));
                } else {
                    prevDay.setNotes(findAllTasks(prevDay, sharedPreferences));
                }
                taskList = new ArrayList<>(prevDay.getNotes());
                taskAdapter[0][0] = new TaskAdapter(taskList, this);
                recyclerView.setAdapter(taskAdapter[0][0]);
            }
        });

        //add line button
        addLineFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (editSwitch.isChecked()) {
                    NewTask newTask = new NewTask();
                    newTask.setDateString(editDate.getText().toString());
                    newTask.showPopupWindow(v);
                }
            }
        });



        //Calendar View button
        calendarFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editSwitch.isChecked()) {
                    returnToCalendarView();
                }
            }
        });
    }

    //Return to calendar view
    void returnToCalendarView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private LinkedHashSet<String> newUsedDay(CalendarDay day, SharedPreferences.Editor editor) {
        LinkedHashSet<String> hash_set = new LinkedHashSet<String>();
        hash_set.add(" ");
        editor.putString(day.toString(), "Used");
        editor.putString(day.toString() + "_0", "");
        editor.apply();
        return hash_set;
    }

    private LinkedHashSet<String> findAllTasks(CalendarDay day, SharedPreferences sharedPreferences) {
        Boolean foundAll = false;
        int index = 0;
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        while (!foundAll) {
            if (sharedPreferences.contains(day.toString() + "_" + Integer.toString(index))) {
                hashSet.add(sharedPreferences.getString(day.toString() + "_" + index, null));
                index++;
            } else {
                foundAll = true;
            }
        }
        return hashSet;
    }

    private int verifyIndex(int i, final String value, final LinearLayoutManager layout) {
        int itemsShown = layout.findLastVisibleItemPosition()+1;
        if(!taskList.get(i).equals(value)){
            i= verifyIndex(i+itemsShown,value,layout);
        }
        return i;
    }

}
