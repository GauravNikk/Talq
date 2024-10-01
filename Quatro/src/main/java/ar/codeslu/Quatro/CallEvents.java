package ar.codeslu.Quatro;

import android.content.Context;

public class  CallEvents<T extends QuatroUser > {
    Context context;
    T user;
    QuatroListener<T> listener;
    
    public CallEvents(Context context, T user) {
        this.context = context;
        this.user = user;
    }

    public CallEvents(Context context) {
        this.context = context;
    }

    void Accept() {
        listener.onAnswer(context, user);
    }
    void onNotificationShowed() {
        listener.onNotificationShowed(context, user);
    }

    void Open() {
        listener.onOpen(context, user);
    }

    void Decline() {
        listener.onDecline(context, user);
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public void setListener(QuatroListener<T> listener) {
        this.listener = listener;
    }


    public interface QuatroListener<T> {
        void onAnswer(Context context, T user);

        void onOpen(Context context, T user);

        void onDecline(Context context, T user);
        void onNotificationShowed(Context context, T user);

    }

}
