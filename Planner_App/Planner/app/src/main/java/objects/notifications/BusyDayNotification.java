package objects.notifications;

        import android.annotation.SuppressLint;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.os.Build;
        import android.view.View;
        import android.widget.TextView;

        import androidx.core.app.NotificationCompat;
        import androidx.core.content.ContextCompat;

        import com.tjlabs.planner.R;
        import com.tjlabs.planner.activities.EditDateActivity;

        import java.util.ArrayList;

        import objects.CalendarDay;

public class BusyDayNotification {
    View view;



    public NotificationCompat.Builder builder(CalendarDay calendarDay){
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(view.getContext(), view.getContext().getString(R.string.channel_ID))
                .setSmallIcon(R.drawable.notepad)
                .setContentTitle("Busy Day")
                .setContentText("you are busy today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createIntent(calendarDay))
                .setAutoCancel(true);
        return builder2;
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = view.getContext().getString(R.string.channel_name);
            String description = view.getContext().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( view.getContext().getString(R.string.channel_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            @SuppressLint("RestrictedApi") NotificationManager notificationManager = ContextCompat.getSystemService(view.getContext(), NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public PendingIntent createIntent(CalendarDay calendarDay){
        Intent intent = new Intent(view.getContext(), EditDateActivity.class);
        intent.putExtra("Date", calendarDay.toString());
        ArrayList<String> tasks = new ArrayList<>(calendarDay.getNotes());
        intent.putExtra("Tasks", tasks);
        view.getContext().startActivity(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 0, intent, 0);
        return  pendingIntent;
    }

    public BusyDayNotification(View view1){
        view = view1;
    }

}
