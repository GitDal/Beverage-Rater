package dk.au.mad21spring.appproject.group6.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21spring.appproject.group6.BeverageRepository;
import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;

public class NotificationService extends LifecycleService {


    private static final String TAG = "NotificationService";
    public static final String SERVICE_CHANNEL = "BEVERAGE_RATER_NOTIFICATION_SERVICE_CHANNEL";
    public static final int SERVICE_MAIN_NOTIFICATION_ID = 1000;

    private ExecutorService execService;
    private BeverageRepository beverageRepository;

    private LiveData<List<Beverage>> pendingRequests;
    private boolean running = false;
    private int lastPendingRequestCount = 0;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        execService = Executors.newSingleThreadExecutor();
        beverageRepository = BeverageRepository.getBeverageRepository(this);
        pendingRequests = new MutableLiveData<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand: Starting service");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "BeverageRater-Notifications Service Channel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // Building initial notification:
        String contentTitle = getResources().getString(R.string.notification_default_content_title);
        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                .setContentTitle(contentTitle)
                .setSmallIcon(R.drawable.ic_mail)
                .build();

        startForeground(SERVICE_MAIN_NOTIFICATION_ID, notification);
        startBackgroundWork();

        return START_STICKY;
    }

    public void startBackgroundWork() {
        Log.d(TAG, "startBackgroundWork: running = true");
        running = true;
        beverageRepository.getAllPendingBeveragesRequestedByUser((MutableLiveData<List<Beverage>>) pendingRequests);
        pendingRequests.observe(this, this::handlePendingRequestsChanged);
    }

    private void handlePendingRequestsChanged(List<Beverage> beverages) {
        Log.d(TAG, "handlePendingRequestsChanged: beverages has changed");
        Log.d(TAG, "handlePendingRequestsChanged: currentUser is admin = " + beverageRepository.currentUser.IsAdmin);

        int index = 0;
        for (Beverage request:
                beverages) {
            Log.d(TAG, "handlePendingRequestsChanged: " + index + ": " + request.toString());
            index++;
        }

        if(lastPendingRequestCount > beverages.size()) {
            Log.d(TAG, "handlePendingRequestsChanged: A request was resolved!");
            execService.submit(this::showNotification);
        }

        lastPendingRequestCount = beverages.size();

        if(!running) {
            pendingRequests.removeObservers(this);
        }
    }

    private void showNotification() {

        String contentTitle = getResources().getString(R.string.notification_content_title);
        String contentText = getResources().getString(R.string.notification_request_resolved_content_text);

        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_mail)
                .build();

        NotificationManagerCompat.from(this).notify(SERVICE_MAIN_NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Service destroyed (running = false)");
        running = false;
        super.onDestroy();
    }
}