package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tjlabs.planner.R;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private List<String> taskList;
    private Context context;
    private View.OnClickListener listener;
    public TaskAdapter(List<String> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }
    public TaskAdapter(List<String> taskList, View.OnClickListener listener){
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_task_card,parent, false);
        return new ViewHolder(v){};
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String listItem = taskList.get(position);

        holder.textViewTask.setText(listItem);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = (TextView) itemView.findViewById(R.id.multiAutoCompleteTextView);
        }
    }

    public Context getContext() {
        return context;
    }
}
