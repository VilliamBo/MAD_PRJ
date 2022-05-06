package dk.au.mad22spring.AppProject.Group13.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

//import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad22spring.AppProject.Group13.R;
import dk.au.mad22spring.AppProject.Group13.model.Repository;

//Foreground service
public class FriendActivityNotificationService extends Service {



    private int counter = 1; //DEVELOPER TEST!!


    private static final String TAG = "NotificationService";

    private static final String SERVICE_CHANNEL = "service_channel";
    private static final int NOTIFICATION_ID = 69;
    private static final String NOTIFICATION_CHANNEL = "notification_channel";
    private boolean started = false;
    private ExecutorService exeService;
    //Repository to communicate with Firebase database
    private Repository repository;


    public FriendActivityNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Repository.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(FriendActivityNotificationService.this, SERVICE_CHANNEL)
                .setContentTitle("Welcome to the HangOutz app!") //Runs in background but is visible to user
                .setSmallIcon(R.drawable.ic_baseline_person_outline_24)
                .setChannelId(NOTIFICATION_CHANNEL)
                .setTicker("Some more info about your foreground service.")
                .build();


        startForeground(NOTIFICATION_ID, notification);

        //only starts if the service is not already started.
        if (!started) {
            started = true;
            updateNotification();
        }

        return START_STICKY;
    }

    //Inspired by stackOverflow thread:
    //https://stackoverflow.com/questions/5528288/how-do-i-update-the-notification-text-for-a-foreground-service-in-android
    private void updateNotification() {
        if(exeService == null) {
            exeService = Executors.newSingleThreadExecutor();
        }

        exeService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "run error: ", e);
                }

                if (repository.getLoggedInUserID() != null){
                    Notification notification = new NotificationCompat.Builder(FriendActivityNotificationService.this, SERVICE_CHANNEL)
                            .setContentTitle("Hang OUT!!") //Runs in background but is visible to user
                            .setContentText("Your friend User "+counter + " is doing ACTIVITY")
                            .setSmallIcon(R.drawable.ic_baseline_person_outline_24)
                            .setChannelId(NOTIFICATION_CHANNEL)
                            .setChannelId(NOTIFICATION_CHANNEL)
                            .setTicker("This service will notify you when a friend is doing activities")
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID, notification);

                    counter++;

                    if (started) {
                        updateNotification();
                    }

                }
            }
        });
    }

    //Error: Bad notification for startup. it need a notification channel
    //https://developer.android.com/training/notify-user/channels
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel (
                    NOTIFICATION_CHANNEL,
                    "HangOutz Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("HangOutz notification to inform about you friends activities");
            NotificationManager nm = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }
}