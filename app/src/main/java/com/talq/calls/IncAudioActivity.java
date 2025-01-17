package com.talq.calls;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import com.talq.R;
import com.talq.global.AppBack;
import com.talq.global.Global;
import com.talq.lists.Usercalldata;

import de.hdodenhof.circleimageview.CircleImageView;

public class IncAudioActivity extends AppCompatActivity {
    static final String TAG = IncAudioActivity.class.getSimpleName();
    TextView name;
    BlurImageView bg;
    CircleImageView img;
    String nameE, avaE, idd, channel_id,myid;
    private AudioPlayer mAudioPlayer;
    DatabaseReference mUser;
    FirebaseAuth mAuth;
    DatabaseReference mlogs;
    boolean dest = true;
ValueEventListener child;
Query query;

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
Log.wtf("Quack","NEW INTENT");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Log.wtf("Quatro","Opened");


        setContentView(R.layout.activity_inc_audio);
        Global.IncAActivity = this;


        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();


        mUser = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mlogs = FirebaseDatabase.getInstance().getReference(Global.CALLS);
        mAuth = FirebaseAuth.getInstance();


        ImageButton answer = findViewById(R.id.btn_accept);
        answer.setOnClickListener(mClickListener);
        ImageButton decline = findViewById(R.id.btn_reject);
        decline.setOnClickListener(mClickListener);

        name = (TextView) findViewById(R.id.username);
        img = findViewById(R.id.circleImageView);
        bg = findViewById(R.id.bg);


        if (getIntent() != null) {
            idd = getIntent().getExtras().getString("id");
            name.setText(getIntent().getExtras().getString("name"));
            nameE = getIntent().getExtras().getString("name");
            avaE = getIntent().getExtras().getString("ava");
            channel_id = getIntent().getExtras().getString("channel_id");
            myid = getIntent().getExtras().getString("myid");
            Log.wtf("QuackNewIntent",getIntent().getExtras().getString("name") + "");
            if (String.valueOf(getIntent().getExtras().getString("ava")).equals("no")) {
                Picasso.get()
                        .load(R.drawable.backg)
                        .placeholder(R.drawable.backg) .error(R.drawable.errorimg)

                        .into(bg, new Callback() {
                            @Override
                            public void onSuccess() {
                                bg.setBlur(22);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                Picasso.get()
                        .load(R.drawable.profile)
                        .placeholder(R.drawable.profile) .error(R.drawable.errorimg)

                        .into(img);
            } else {
                Picasso.get()
                        .load(getIntent().getExtras().getString("ava"))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.errorimg)
                        .into(img);
                Picasso.get()
                        .load(getIntent().getExtras().getString("ava"))
                        .placeholder(R.drawable.backg)
                        .error(R.drawable.errorimg)
                        .into(bg, new Callback() {
                            @Override
                            public void onSuccess() {
                                bg.setBlur(22);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        }else{
            Log.wtf("Quack","empty");
        }

        Log.wtf("WTf error",myid + "////"+idd+ "///" + channel_id +"////");

       query = mlogs.child(myid).child(idd).child(channel_id);
        child = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(mAuth.getCurrentUser()!=null) {

                    Usercalldata usercalldata = dataSnapshot.getValue(Usercalldata.class);

                    try {
                        if (!usercalldata.isIncall())
                            finish();
                    } catch (NullPointerException e) {
                        finish();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUser.child(idd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(mAuth.getCurrentUser()!=null) {

                    Usercalldata usercalldata = dataSnapshot.getValue(Usercalldata.class);
                    try {
                        if (!usercalldata.isIncall()) {
                            finish();
                        }
                    } catch (NullPointerException e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accept:
                    answerClicked();
                    break;
                case R.id.btn_reject:
                    declineClicked();
                    break;
            }
        }
    };

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        dest = false;
        Intent jumptocall = new Intent(IncAudioActivity.this, CallingActivity.class);
        jumptocall.putExtra("name", nameE);
        jumptocall.putExtra("ava", avaE);
        jumptocall.putExtra("out", false);
        jumptocall.putExtra("channel_id", channel_id);
        jumptocall.putExtra("UserId", idd);
        startActivity(jumptocall);
        finish();

    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        if(mAuth.getCurrentUser()!= null) {

            Map<String, Object> map = new HashMap<>();
            map.put("incall", false);
            mlogs.child(myid).child(idd).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mlogs.child(idd).child(myid).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAudioPlayer.stopRingtone();
        ((AppBack) this.getApplication()).startActivityTransitionTimer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAudioPlayer.stopRingtone();
        query.removeEventListener(child);
        Global.IncAActivity = null;
        if(mAuth.getCurrentUser()!= null) {
            if (dest) {

                Map<String, Object> map = new HashMap<>();
                map.put("incall", false);
                mlogs.child(myid).child(idd).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mlogs.child(idd).child(myid).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.IncAActivity = this;
        Global.currentactivity = this;
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference(Global.USERS);
        AppBack myApp = (AppBack) this.getApplication();
        if (myApp.wasInBackground) {
            //init data
            Map<String, Object> map = new HashMap<>();
            map.put(Global.Online, true);
            if(mAuth.getCurrentUser() != null)
                mData.child(myid).updateChildren(map);
            Global.local_on = true;
            //lock screen
            ((AppBack) getApplication()).lockscreen(((AppBack) getApplication()).shared().getBoolean("lock", false));
        }

        myApp.stopActivityTransitionTimer();
    }

}
