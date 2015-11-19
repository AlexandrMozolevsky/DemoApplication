package freelance.android.erick.demoapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by erick on 19.11.15.
 */
public class Listener extends Service {
    private static final long SYNC_NOTIFY_INTERVAL = 60000;
    private Timer notificationTimer;
    private Runnable runnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        runnable = new Runnable() {
            @Override
            public void run() {

            }
        };

        notificationTimer = new Timer();
        notificationTimer.scheduleAtFixedRate(new NotificationTimerSchedule(), 0, SYNC_NOTIFY_INTERVAL);
    }

    public class NotificationTimerSchedule extends TimerTask {
        @Override
        public void run() {
            new Thread(runnable).start();
        }
    }
    @Override
     public void onDestroy() {
        super.onDestroy();
        notificationTimer.cancel();
    }
}
