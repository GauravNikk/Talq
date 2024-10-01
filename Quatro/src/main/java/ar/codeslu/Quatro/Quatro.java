package ar.codeslu.Quatro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class Quatro<T extends QuatroUser> {

    //Calls Notification data
    public static String QuatroChannelName = "Incoming Calls";
    public static String QuatroChannelid = "Quatro";
    public static final int QuatroNotifiactionidNumber = 172;

    //Calls Actions
    public static final String QuatroActionType_Open = "OpenCallActivity";
    public static final String QuatroActionType_Key = "QuatroAction";
    public static final String QuatroActionType_Accpet = "AcceptCall";
    public static final String QuatroActionType_Decline = "DeclineCall";
    //Class members
    //Custom user implemented
    T user;
    //Callbacks to answer from notification or decline or open
    CallEvents<T> callEvents;
    Context context;
    NotificationManager mgr;
    //Activity to open while ringing
    Class<?> ringerActivity;
    //Custom Notificaiton class {Must extend Quatro Notification and Override Create notification builder to return your Custom Builder }
    QuatroNotification quatroNotification;
    //Keeps track of current Notification
    Map<String, Boolean> trackerMap;
    //Custom behaviour
    Intent acceptIntent,declineIntent,RingingIntent,openIntent;


    public Quatro() {
    }

    public Quatro(Context context, NotificationManager notificationManager) {
        //initializing
        this.mgr = notificationManager;
        this.trackerMap = new HashMap<>();
        this.callEvents = new CallEvents<T>(context);
        this.context = context;
        quatroNotification = new QuatroNotification();
        CreateNotificationChannel(mgr);
    }


    public void setCallEvents(CallEvents<T> callEvents) {
        this.callEvents = callEvents;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    private void CreateNotificationChannel(NotificationManager mgr) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel(QuatroChannelid, QuatroChannelName,
                    NotificationManager.IMPORTANCE_HIGH);

            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            channel.setSound(ringtoneUri, new AudioAttributes.Builder()
                    // Setting the AudioAttributes is important as it identifies the purpose of your
                    // notification sound.
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            mgr.createNotificationChannel(channel);
        }
    }

    public void setQuatroListener(CallEvents.QuatroListener<T> listener) {
        //Get Custom Behaviour
        callEvents.setListener(listener);
        //Add the behaviour to the reciever
        QuatroReciever.listener = callEvents;

    }

    private Notification.Builder CreateNotificationfromUserData(T user) {
        //OpenIntent
        PendingIntent pendingIntent = null;
        if (openIntent == null) {
             openIntent = new Intent(Intent.ACTION_MAIN, null);
//        fullScreenIntenet.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
            openIntent.setClass(context, QuatroReciever.class);
            openIntent.putExtra(Quatro.QuatroActionType_Key, Quatro.QuatroActionType_Open);
             pendingIntent = PendingIntent.getBroadcast(context, 100, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
//       Accept
        if (acceptIntent == null) {
             acceptIntent = new Intent(Intent.ACTION_MAIN, null);
            acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
            acceptIntent.setClass(context, QuatroReciever.class);
            acceptIntent.putExtra(Quatro.QuatroActionType_Key, Quatro.QuatroActionType_Accpet);
        }
        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(context, 100, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Decline
        if (declineIntent == null) {
            declineIntent = new Intent(Intent.ACTION_MAIN, null);
            declineIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
            declineIntent.setClass(context, QuatroReciever.class);
            declineIntent.putExtra(Quatro.QuatroActionType_Key, Quatro.QuatroActionType_Decline);
        }

        PendingIntent pendingIntentDecline = PendingIntent.getBroadcast(context, 100, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //asdasd
        Notification.Builder builder = null;
        builder = quatroNotification.CreateNotificationBuilder(context, user, pendingIntentAccept, pendingIntentDecline);

        // Create an intents which triggers your fullscreen incoming call user interface & actions.
        //FullScreenintent
//        if (this.ringerActivity != null) {
//            Intent fullScreenIntent = new Intent(Intent.ACTION_MAIN).setClass(getContext(), getRingerActivity());
//            fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
//            fullScreenIntent.setClass(context, ringerActivity);
//            fullScreenIntent.putExtra(Quatro.QuatroActionType_Key, Quatro.QuatroActionType_Open);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
//            stackBuilder.addNextIntentWithParentStack(fullScreenIntent);
//            PendingIntent fullScreenPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            ;
//            builder.setFullScreenIntent(fullScreenPendingIntent, true);
//        }
        if (RingingIntent !=null){

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
            stackBuilder.addNextIntentWithParentStack(RingingIntent);
            PendingIntent fullScreenPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            ;
            builder.setFullScreenIntent(fullScreenPendingIntent, true);
        }


        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate.
//         Setup notification content.

        builder.setContentIntent(pendingIntent);

        builder.setOngoing(true);


        return builder;
    }

    private void CreateIncomingCall(T user) {
        //Set the user
        setUser(user);
        //Set Call events
        callEvents.setUser(user);

        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.

        final Notification.Builder builder = CreateNotificationfromUserData(user);
        // Set notification content intent to take user to fullscreen UI if user taps on the
        // notification body.
        // Set notification as insistent to cause your ringtone to loop.
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Use builder.addAction(..) to add buttons to answer or reject the call.
        mgr.notify(Quatro.QuatroNotifiactionidNumber, builder.build());
    }

    public void setRingerActivity(Class<?> ringerActivity) {
        this.ringerActivity = ringerActivity;
    }

    public Context getContext() {
        return context;
    }

    public void Dismiss() {

        mgr.cancel(Quatro.QuatroNotifiactionidNumber);
    }

    public void StartActivity() {
        Intent intent = new Intent()
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setClass(context, ringerActivity.getClass());

        context.startActivity(intent);
    }

    public void AddCall(T user) {
            CreateIncomingCall(user);

    }

    public boolean exists(String callId) {
        boolean exists = false;
        if (trackerMap.get(callId) != null) {
            exists = true;
        } else {
            trackerMap.put(callId, true);
        }
        return exists;
    }

    public Class<?> getRingerActivity() {
        return ringerActivity;
    }


    public void setQuatroNotification(QuatroNotification quatroNotification) {
        this.quatroNotification = quatroNotification;
    }

    public void setIcon(int Icon) {
        quatroNotification.setIcon(Icon);
    }

    public void setcolor(int colorPrimary) {
        this.quatroNotification.setColor(colorPrimary);
    }

    public Intent getAcceptIntent() {
        return acceptIntent;
    }

    public void setAcceptIntent(Intent acceptIntent) {
        this.acceptIntent = acceptIntent;
    }

    public Intent getDeclineIntent() {
        return declineIntent;
    }

    public void setDeclineIntent(Intent declineIntent) {
        this.declineIntent = declineIntent;
    }

    public Intent getRingingIntent() {
        return RingingIntent;
    }

    public void setRingingIntent(Intent ringingIntent) {
        RingingIntent = ringingIntent;
    }

    public Intent getOpenIntent() {
        return openIntent;
    }

    public void setOpenIntent(Intent openIntent) {
        this.openIntent = openIntent;
    }
}
