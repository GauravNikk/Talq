package ar.codeslu.Quatro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class QuatroRingingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quatro_ringing);
        ShowinLocked();
        Log.wtf("Quatro","Opened");

    }
    void ShowinLocked() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            Window window = getWindow();
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            KeyguardManager as = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            as.requestDismissKeyguard(this, null);
        }

    }
}