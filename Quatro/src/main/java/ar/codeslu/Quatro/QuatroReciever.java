package ar.codeslu.Quatro;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class QuatroReciever extends BroadcastReceiver {
    public static CallEvents listener;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager;
        notificationManager = context.getSystemService(NotificationManager.class);
        String action = intent.getExtras().getString(Quatro.QuatroActionType_Key);

        switch (action) {
            case Quatro.QuatroActionType_Open:
                notificationManager.cancel(Quatro.QuatroNotifiactionidNumber);

                if (listener!=null)
                listener.Open();
                break;
            case Quatro.QuatroActionType_Accpet:
//                Intent intent2 = new Intent(context, CallActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent2);
                if (listener!=null)
                listener.Accept();
                notificationManager.cancel(Quatro.QuatroNotifiactionidNumber);
                break;
            case Quatro.QuatroActionType_Decline:
                if (listener!=null)
                listener.Decline();
                notificationManager.cancel(Quatro.QuatroNotifiactionidNumber);
                break;


        }


}
}
