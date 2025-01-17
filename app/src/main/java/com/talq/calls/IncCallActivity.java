package com.talq.calls;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import com.talq.R;
import com.talq.global.AppBack;
import com.talq.global.Global;
import com.talq.lists.Usercalldata;

import ar.codeslu.Quatro.QuatroRingingActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class IncCallActivity extends QuatroRingingActivity {
    static final String TAG = IncCallActivity.class.getSimpleName();
    private String mCallId;
    TextView remoteUser;
    ImageButton ans, dec;
    String friendId,nameE,avaE,channel_id,myid;
    TextView name;
    BlurImageView bg;
    CircleImageView img;
    private AudioPlayer mAudioPlayer;
    DatabaseReference mUser,mlogs;
    FirebaseAuth mAuth;
boolean dest = true;

ValueEventListener child;
Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        Log.wtf("Quatro","Opened");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_inc_call);

        Global.IncVActivity = this;

        Global.currentactivity = this;

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();

        remoteUser = (TextView) findViewById(R.id.remoteUser);
        dec = findViewById(R.id.declineButton);
        ans = findViewById(R.id.answerButton);

        name = (TextView) findViewById(R.id.remoteUser);
        img =findViewById(R.id.circleImageView);
        bg =  findViewById(R.id.bg);


        mUser = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mlogs = FirebaseDatabase.getInstance().getReference(Global.CALLS);
        mAuth = FirebaseAuth.getInstance();


        if(getIntent() != null)
        {
            friendId =  getIntent().getStringExtra("id");
            nameE = getIntent().getExtras().getString("name");
            avaE = getIntent().getExtras().getString("ava");
            channel_id = getIntent().getExtras().getString("channel_id");
            myid = getIntent().getExtras().getString("myid");
            name.setText(nameE);
            if (String.valueOf(getIntent().getExtras().getString("ava")).equals("no")) {
                Picasso.get()
                        .load(R.drawable.profile)
                        .placeholder(R.drawable.profile) .error(R.drawable.errorimg)

                        .into(img);
                Picasso.get()
                        .load(R.drawable.backg)
                        .placeholder(R.drawable.backg) .error(R.drawable.errorimg)

                        .into(bg, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                bg.setBlur(22);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            } else {
                Picasso.get()
                        .load(getIntent().getExtras().getString("ava"))
                        .placeholder(R.drawable.profile) .error(R.drawable.errorimg)

                        .into(img);
                Picasso.get()
                        .load(getIntent().getExtras().getString("ava"))
                        .placeholder(R.drawable.backg) .error(R.drawable.errorimg)

                        .into(bg, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                bg.setBlur(22);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        }
        query =mUser.child(friendId);
        child = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(myid!=null) {

                    Usercalldata usercalldata = dataSnapshot.getValue(Usercalldata.class);
                    try {
                        if (!usercalldata.isIncall()){
                            Toast.makeText(IncCallActivity.this, "Call ended", Toast.LENGTH_SHORT).show();
                            finish();


                        }
                    } catch (NullPointerException e) {
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioPlayer.stopRingtone();
                if(myid!= null) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("incall", false);
                    mlogs.child(myid).child(friendId).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mlogs.child(friendId).child(myid).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });

        ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dest = false;
                mAudioPlayer.stopRingtone();
                Intent jumptocall = new Intent(IncCallActivity.this, CallingActivityVideo.class);
                jumptocall.putExtra("name", nameE);
                jumptocall.putExtra("ava", avaE);
                jumptocall.putExtra("out", false);
                jumptocall.putExtra("channel_id", channel_id);
                jumptocall.putExtra("UserId", friendId);
                startActivity(jumptocall);
                finish();

            }
        });


    }
    @Override
    protected void onPause() {
        super.onPause();
        mAudioPlayer.stopRingtone();
        Global.currentactivity = null;
        ((AppBack) this.getApplication()).startActivityTransitionTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        query.removeEventListener(child);
        Global.IncVActivity = null;
        mAudioPlayer.stopRingtone();
        if(mAuth.getCurrentUser()!= null) {
            if (dest) {
                Map<String, Object> map = new HashMap<>();
                map.put("incall", false);
                mlogs.child(myid).child(friendId).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mlogs.child(friendId).child(myid).child(channel_id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Global.IncVActivity = this;
        super.onResume();
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
