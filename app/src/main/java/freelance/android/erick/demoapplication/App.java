package freelance.android.erick.demoapplication;

import android.app.Application;
import android.content.Intent;

import freelance.android.erick.demoapplication.service.Listener;

/**
 * Created by erick on 19.11.15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent listenerService = new Intent(this, Listener.class);
        listenerService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(listenerService);
    }
}
