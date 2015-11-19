package freelance.android.erick.demoapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import freelance.android.erick.demoapplication.R;
import freelance.android.erick.demoapplication.common.API;
import freelance.android.erick.demoapplication.common.DataBase;
import freelance.android.erick.demoapplication.model.Values;

/**
 * Created by erick on 19.11.15.
 */
public class Listener extends Service {
    private static final long SYNC_NOTIFY_INTERVAL = 60000;
    private Timer notificationTimer;
    private Runnable runnable;

    int countNotifications = 0;

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
                API.asyncGetMessages(new API.CallbackGetMessages() {
                    @Override
                    public void onSuccess(ArrayList<Values> model) {
                        if(null != model) {
                            for (int i = 0; i < model.size(); i++) {
                                if(!DataBase.getInstance().checkIfNotificationExist(model.get(i).getId(), model.get(i).getText())) {
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Listener.this.getApplicationContext())
                                            .setAutoCancel(true)
                                            .setColor(getResources().getColor(R.color.colorPrimary))
                                            .setContentTitle("New message from server!");

                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setTicker(model.get(i).getText())
                                            .setLargeIcon(null)
                                            .setContentText(model.get(i).getText() + " (" +  model.get(i).getCreated_at() + ")");

                                    Notification notification = builder.build();
                                    NotificationManager notificationManager = (NotificationManager) Listener.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(countNotifications, notification);

                                    DataBase.getInstance().insertRows(DataBase.VALUES, DataBase.setContentValues(model.get(i).getId(), model.get(i).getText(), model.get(i).getCreated_at()));
                                    countNotifications++;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
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
