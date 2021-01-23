package objects.notifications;

        import android.annotation.SuppressLint;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.os.Build;
        import android.view.View;

        import androidx.core.app.NotificationCompat;
        import androidx.core.content.ContextCompat;

        import com.tjlabs.planner.R;

public class BusyDayNotification {
    View view;



    public NotificationCompat.Builder builder(){
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(view.getContext(), view.getContext().getString(R.string.channel_ID))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Busy Day")
                .setContentText("you are busy today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
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

    public BusyDayNotification(View view1){
        view = view1;
    }

}
