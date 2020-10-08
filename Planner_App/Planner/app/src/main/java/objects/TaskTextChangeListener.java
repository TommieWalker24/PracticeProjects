package objects;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskTextChangeListener implements TextWatcher {
        private String before;
        private String after;
        RecyclerView recyclerView;
        ArrayList<String> taskList;
        int index;
        String selectedDate;
        SharedPreferences.Editor editor;

//        int checkedIndex = index;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(recyclerView.getScrollState() == 0){
            before = s.toString();
 //           checkedIndex = verifyIndex(index, before, (LinearLayoutManager)recyclerView.getLayoutManager());
        }
    }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(recyclerView.getScrollState() == 0){
            after = s.toString();
            //checkedIndex = verifyIndex(index, textView.getText().toString(), (LinearLayoutManager)recyclerView.getLayoutManager());
            taskList.set(index, after);
        }
    }

        @Override
        public void afterTextChanged(Editable s) {
        if(recyclerView.getScrollState() == 0){
            ArrayList<String> list = taskList;
            CalendarDay calendarDay = new CalendarDay(selectedDate);
            editor.remove(calendarDay.toString() + "_" + index);
            editor.apply();
            editor.putString(calendarDay.toString() + "_" + index, after);
            editor.apply();
        }

    }

    public TaskTextChangeListener(RecyclerView recyclerView, ArrayList<String> taskList, int index, String selectedDate, SharedPreferences.Editor editor) {
        this.recyclerView = recyclerView;
        this.taskList = taskList;
        this.index = index;
        this.selectedDate = selectedDate;
        this.editor = editor;
    }
}
