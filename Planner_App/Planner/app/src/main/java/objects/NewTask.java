package objects;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.core.app.NotificationManagerCompat;
import com.tjlabs.planner.R;
import com.tjlabs.planner.activities.EditDateActivity;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import objects.notifications.BusyDayNotification;

//import objects.notifications.BusyDayNotification;

public class NewTask {
    String dateString;
    String task;
    String taskTime;
    String location;

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
        Button applyButton = newTaskView.findViewById(R.id.taskApplyButton);
        RadioButton pmButton = newTaskView.findViewById(R.id.radioButtonPM);
        RadioButton amButton = newTaskView.findViewById(R.id.radioButtonAM);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay calendarDay = new CalendarDay(dateString);
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
                editor.apply();
                tasks.add(description.getText().toString());
                Intent intent = new Intent(view.getContext(), EditDateActivity.class);
                intent.putExtra("Date", dateString);
                intent.putExtra("Tasks", tasks);

                Toast.makeText(view.getContext(), "Task added", Toast.LENGTH_SHORT).show();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(view.getContext());
                BusyDayNotification busyDayNotification = new BusyDayNotification(view);
                busyDayNotification.createNotificationChannel();
                notificationManagerCompat.notify(Integer.parseInt(view.getContext().getString(R.string.channel_ID)), busyDayNotification.builder().build());
                popupWindow.dismiss();
                view.getContext().startActivity(intent);
            }
        });


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


}
