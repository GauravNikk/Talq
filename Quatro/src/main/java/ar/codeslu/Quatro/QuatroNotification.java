package ar.codeslu.Quatro;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class QuatroNotification {
    int icon = R.drawable.ic_answer;
    int color = 0;


    public void setIcon(int icon) {
        this.icon = icon;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public <T extends QuatroUser> Notification.Builder CreateNotificationBuilder(Context context, T user, PendingIntent AcceptIntent, PendingIntent DeclineIntent) {

        Notification.Builder builder = new Notification.Builder(context, Quatro.QuatroChannelid);
        builder.setSmallIcon(R.drawable.ic_baseline_call_24);
        builder.setContentTitle(user.getName());
        if (user.isVoice()) {
            builder.setContentText("Voice Call");
        } else {
            builder.setContentText("Video Call");
        }
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setAutoCancel(true);
        builder.setCategory(Notification.CATEGORY_CALL);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setColorized(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(Quatro.QuatroChannelid);
        }

        builder.setSmallIcon(icon);
        if (color == 0 ){
            builder.setColor(R.color.green_answer);
        }else
        builder.setColor(color);
        return builder;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
