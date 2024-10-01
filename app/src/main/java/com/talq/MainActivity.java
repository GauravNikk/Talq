package com.talq;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.talq.adapters.Vpadapter;
import com.talq.fragment.CallFragment;
import com.talq.fragment.ChatFragment;
import com.talq.fragment.GroupFragment;
import com.talq.fragments.Calls;
import com.talq.fragments.Chats;
import com.talq.fragments.Groups;
import com.talq.global.AppBack;
import com.talq.global.Global;
import com.talq.menu.DrawerAdapter;
import com.talq.menu.DrawerItem;
import com.talq.menu.SimpleItem;
import com.talq.menu.SpaceItem;
import com.vanniktech.emoji.EmojiTextView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.Continuation;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.me.MessageIn;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vanniktech.emoji.EmojiTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.talq.Groups.Group;
import com.talq.adapters.Vpadapter;
import com.talq.auth.DataSet;
import com.talq.auth.Login;
import com.talq.fragments.Calls;
import com.talq.fragments.Chats;
import com.talq.fragments.Groups;
import com.talq.global.AppBack;
import com.talq.global.Global;
import com.talq.global.encryption;
import com.talq.lists.UserData;
import com.talq.story.AddStory;
import com.talq.story.Archieve;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.leolin.shortcutbadger.ShortcutBadger;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference mData, mChats, mGroups, mCalls;
    //views
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    ViewPager vp;
    EmojiTextView nameNav, statueNav;
    CircleImageView avatarNav;
    Button editBNav;
    ImageView hlal;
    //Fragments
    Calls calls;
    Chats chats;
    Groups groups;
    //Shared pref
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //Vars
    Uri imgLocalpath;
    //Application class
    private AppBack appback;
    //compress
    private Bitmap compressedImageFile;
    //adpaters
    Vpadapter Adapter;
    MeowBottomNavigation meowBottomNavigation;
    //encryption

    String called = "";
    android.app.AlertDialog dialog;

    private InterstitialAd mInterstitialAd;



    private static final int POS_HOME = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_SETTINGS = 2;
    private static final int POS_NOTIF = 3;
    private static final int POS_LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.currentactivity = this;

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // getActionBar().hide();
        //    getSupportActionBar().setDisplayShowTitleEnabled(false);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_SETTINGS),
                createItemFor(POS_NOTIF),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);


        //firebase init
        Global.conMain = this;
        Global.mainActivity = this;
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mChats = FirebaseDatabase.getInstance().getReference(Global.CHATS);
        mGroups = FirebaseDatabase.getInstance().getReference(Global.GROUPS);
        mCalls = FirebaseDatabase.getInstance().getReference(Global.CALLS);
        if (mAuth.getCurrentUser() != null)
            checkData();


        //encryption

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.int_ID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        //app global
        appback = (AppBack) getApplication();
        //Shared pref
        preferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //clear all notifications
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            if (notificationManager != null) {
                notificationManager.cancelAll();
                int count = 0;
                //store it again
                ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                ((AppBack) getApplication()).editSharePrefs().apply();
                ShortcutBadger.applyCount(this, count);
            }
        } catch (NullPointerException e) {
            //nothing
        }

//Tab layout init
        //Initializing..


        meowBottomNavigation = findViewById(R.id.meownav);
        vp = findViewById(R.id.Vp);
        vp.setOffscreenPageLimit(3);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(0, R.drawable.ic_call));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_chat));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_group));
        meowBottomNavigation.show(1, true);
        chats = new Chats();
        groups = new Groups();
        calls = new Calls();
        setupFm(getSupportFragmentManager()); //Setup Fragment
        vp.setCurrentItem(1);
        vp.addOnPageChangeListener(new PageChange());

        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                vp.setCurrentItem(model.getId());
                return null;
            }
        });

//        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(MeowBottomNavigation.Model item) {
//
//            }
//        });
//
//        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
//            @Override
//            public void onShowItem(MeowBottomNavigation.Model item) {
//                // your codes
//            }
//        });
//
//        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
//            @Override
//            public void onReselectItem(MeowBottomNavigation.Model item) {
//                // your codes
//            }
//        });

        //redirect
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else {

            if (Global.check_int(this)) {
                updateTokens();
                sharedadv(Global.check_int(this));
            }
            //main data init
            SharedPreferences preferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
            String phone = preferences.getString("phone_" + mAuth.getCurrentUser().getUid(), null);
            if (phone != null)
                Global.phoneLocal = phone;
        }

        Global.currentpageid = "";


        //dark mode init
        if (mAuth.getCurrentUser() != null) {
            if (!((AppBack) getApplication()).shared().getBoolean("dark" + mAuth.getCurrentUser().getUid(), false)) {
                hlal.setImageDrawable(getResources().getDrawable(R.drawable.hlal));
                Global.DARKSTATE = false;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Global.currentactivity = this;

            } else {
                hlal.setImageDrawable(getResources().getDrawable(R.drawable.hlal_fill));
                Global.DARKSTATE = true;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Global.currentactivity = this;
            }

//            ((AppBack) getApplication()).oneAccVerfy();

            //loader
            if (Global.DARKSTATE) {
                dialog = new SpotsDialog.Builder()
                        .setContext(this)
                        .setMessage(R.string.pleasW)
                        .setTheme(R.style.darkDialog)
                        .setCancelable(false)
                        .setCancelable(true)
                        .build();
            } else {
                dialog = new SpotsDialog.Builder()
                        .setContext(this)
                        .setMessage(R.string.pleasW)
                        .setCancelable(true)
                        .setCancelable(false)
                        .build();
            }
        }

//data init from shared pref
        if (mAuth.getCurrentUser() != null)
            getshared();
        //online checker
        ((AppBack) this.getApplication()).startOnline();

        try {

            if (getIntent() != null) {
                if (getIntent().getExtras().getInt("codetawgeh", 0) == 1 && mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(this, Chat.class);
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    intent.putExtra("id", getIntent().getExtras().getString("id"));
                    intent.putExtra("ava", getIntent().getExtras().getString("ava"));
                    Global.currname = getIntent().getExtras().getString("name");
                    Global.currentpageid = getIntent().getExtras().getString("id");
                    Global.currFid = getIntent().getExtras().getString("id");
                    Global.currAva = getIntent().getExtras().getString("ava");
                    startActivity(intent);
                } else if (getIntent().getExtras().getInt("codetawgeh", 0) == 2 && mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(this, Group.class);
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    intent.putExtra("id", getIntent().getExtras().getString("id"));
                    intent.putExtra("ava", getIntent().getExtras().getString("ava"));
                    Global.currname = getIntent().getExtras().getString("name");
                    Global.currentpageid = getIntent().getExtras().getString("id");
                    Global.currFid = getIntent().getExtras().getString("id");
                    Global.currAva = getIntent().getExtras().getString("ava");
                    startActivity(intent);
                }
                else if (getIntent().getExtras().getInt("codetawgeh", 0) == 3 && mAuth.getCurrentUser() != null) {
                    Dexter.withContext(this)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()){
                                        startActivity(new Intent(MainActivity.this, AddStory.class));

                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();


                }
            }
        } catch (NullPointerException e) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference(Global.USERS);

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }

    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            if (Global.check_int(this)) {
                if (mInterstitialAd.isLoaded() && Global.ADMOB_ENABLE) {
                    mInterstitialAd.show();
                }
                //clear all notifications
                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                try {
                    if (notificationManager != null) {
                        notificationManager.cancelAll();
                        int count = 0;
                        //store it again
                        ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                        ((AppBack) getApplication()).editSharePrefs().apply();
                        ShortcutBadger.applyCount(this, count);
                    }
                } catch (NullPointerException e) {
                    //nothing
                }
                Map<String, Object> map = new HashMap<>();
                map.put(Global.Online, false);
                mData.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Global.local_on = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
                            disableshourtcuts();

                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                });
            } else
                Toast.makeText(appback, R.string.check_int, Toast.LENGTH_SHORT).show();
            //finish();
        }
        slidingRootNav.closeMenu();

        switch (position){
            case 0:
                Fragment selectedScreen1 = ChatFragment.createFor(screenTitles[position]);
                showFragment(selectedScreen1);
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, Contacts.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, EditProfile.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, Setting.class));
                break;
            case 4:
                startActivity(new Intent(MainActivity.this, Qr.class));
                break;

            default:
                Fragment selectedScreen4 = ChatFragment.createFor(screenTitles[position]);
                showFragment(selectedScreen4);
                break;

        }

    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }


     public void sharedadv(boolean check) {
        if (check) {
            Global.idLocal = mAuth.getCurrentUser().getUid();
            Query query = mData.child(mAuth.getCurrentUser().getUid());
            query.keepSynced(true);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mAuth.getCurrentUser() != null) {

                        if (dataSnapshot.exists()) {
                            String statueT = "";
                            UserData userData = dataSnapshot.getValue(UserData.class);
                            if (userData.getName() != null) {
                                String name = userData.getName();
                                editor.putString("name_" + mAuth.getCurrentUser().getUid(), name);
                                editor.apply();
                                nameNav.setText(name);
                                Global.nameLocal = name;
                            }
                            if (userData.getStatue() != null) {
                                String statue = userData.getStatue();
                                editor.putString("statue_" + mAuth.getCurrentUser().getUid(), statue);
                                editor.apply();
                                Global.statueLocal = statue;

                                if (statue.length() > Global.STATUE_LENTH)
                                    statueT = statue.substring(0, Global.STATUE_LENTH) + "...";

                                else
                                    statueT = statue;

                                statueT = "\"" + statueT + "\"";

                                statueNav.setText(statueT);
                            }
                            if (userData.getPhone() != null) {
                                String phone = userData.getPhone();
                                editor.putString("phone_" + mAuth.getCurrentUser().getUid(), phone);
                                editor.apply();
                            }
                            if (userData.getTime() != 0) {
                                long last = userData.getTime();
                                editor.putLong("laston_" + mAuth.getCurrentUser().getUid(), last);
                                editor.apply();
                            }
                            if (userData.getAvatar() != null) {
                                String ava = userData.getAvatar();
                                editor.putString("ava_" + mAuth.getCurrentUser().getUid(), ava);
                                editor.apply();
                                Global.avaLocal = ava;
                                if (ava.equals("no")) {
                                    Picasso.get()
                                            .load(R.drawable.profile)
                                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                                            .into(avatarNav);
                                } else {
                                    Picasso.get()
                                            .load(ava)
                                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                                            .into(avatarNav);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            getshared();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        Global.currentactivity = this;

        try {
            if (Global.wl != null) {
                if (Global.wl.isHeld()) {
                    Global.wl.release();
                }
                Global.wl = null;
            }
        } catch (NullPointerException e) {

        }


        if (mAuth.getCurrentUser() != null) {
            checkData();
            AppBack myApp = (AppBack) this.getApplication();
            if (myApp.wasInBackground) {
                //init data
                Map<String, Object> map = new HashMap<>();
                map.put(Global.Online, true);
                if(mAuth.getCurrentUser() != null)
                    mData.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                Global.local_on = true;
                //lock screen
                ((AppBack) getApplication()).lockscreen(((AppBack) getApplication()).shared().getBoolean("lock", false));
            }
            //clear all notifications
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                if (notificationManager != null) {
                    notificationManager.cancelAll();
                    int count = 0;
                    //store it again
                    ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                    ((AppBack) getApplication()).editSharePrefs().apply();
                    ShortcutBadger.applyCount(this, count);
                }
            } catch (NullPointerException e) {
                //nothing
            }


            myApp.stopActivityTransitionTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
            ((AppBack) this.getApplication()).startActivityTransitionTimer();
            Global.currentactivity = null;

    }

    @Override
    protected void onDestroy() {
        ((AppBack) this.getApplication()).startActivityTransitionTimer();
        super.onDestroy();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgLocalpath = result.getUri();
                uploadprofile(imgLocalpath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void uploadprofile(Uri imgLocalpath) {
        dialog.show();
        //compress the photo
        File newImageFile = new File(imgLocalpath.getPath());
        try {

            compressedImageFile = new Compressor(MainActivity.this)
                    .setMaxHeight(500)
                    .setMaxWidth(500)
                    .setQuality(50)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
            dialog.dismiss();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumbData = baos.toByteArray();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.AvatarS + "/Ava_" + mAuth.getCurrentUser().getUid() + ".jpg");
        UploadTask uploadTask = riversRef.putBytes(thumbData);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, R.string.image_fail, Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    Map<String, Object> map = new HashMap<>();
                    map.put(Global.avatar, String.valueOf(downloadUrl));
                    mData.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (String.valueOf(downloadUrl).equals("no")) {
                                    Picasso.get()
                                            .load(R.drawable.profile)
                                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                                            .into(avatarNav);
                                } else {
                                    Picasso.get()
                                            .load(downloadUrl)
                                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                                            .into(avatarNav);
                                }
                                editor.putString("ava_" + mAuth.getCurrentUser().getUid(), String.valueOf(downloadUrl));
                                editor.apply();
                                dialog.dismiss();
                                try {
                                    if (Global.diaG != null) {
                                        for (int i = 0; i < Global.diaG.size(); i++) {
                                            mChats.child(Global.diaG.get(i).getId()).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            dialog.dismiss();
                                                            Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    }
                                    //change calls
                                    Map<String, Object> map33 = new HashMap<>();
                                    map33.put("ava", String.valueOf(downloadUrl));
                                    if (Global.callList != null) {
                                        for (int i = 0; i < Global.callList.size(); i++) {
                                            if (!Global.callList.get(i).getFrom().equals(mAuth.getCurrentUser().getUid()))
                                                called = Global.callList.get(i).getFrom();
                                            else
                                                called = Global.callList.get(i).getTo();
                                            mCalls.child(called).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            data.getRef().updateChildren(map33);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }
                                    //change all last messages in group
                                    Map<String, Object> map2 = new HashMap<>();
                                    map2.put("lastsenderava", String.valueOf(downloadUrl));
                                    if (Global.diaGGG != null) {
                                        for (int i = 0; i < Global.diaGGG.size(); i++) {
                                            if (Global.diaGGG.get(i).getLastsender().equals(mAuth.getCurrentUser().getUid())) {
                                                mGroups.child(Global.diaGGG.get(i).getId()).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                dialog.dismiss();
                                                                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }

                                    //update group messages

                                    Map<String, Object> map3 = new HashMap<>();
                                    map3.put("avatar", encryption.encryptOrNull(String.valueOf(downloadUrl)));
                                    if (Global.diaGGG != null) {
                                        for (int i = 0; i < Global.diaGGG.size(); i++) {
                                            mGroups.child(Global.diaGGG.get(i).getId()).child(Global.Messages).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                        MessageIn message = data.getValue(MessageIn.class);
                                                        if (message.getFrom().equals(mAuth.getCurrentUser().getUid())) {
                                                            data.getRef().updateChildren(map3);
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    dialog.dismiss();

                                                }
                                            });
                                        }
                                    }
                                } catch (NullPointerException e) {
                                    dialog.dismiss();

                                }


                                Toast.makeText(MainActivity.this, R.string.image_update, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, R.string.image_fail, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();

                                }
                            });

                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, R.string.image_fail, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void getshared() {
        String statueT = "";
        SharedPreferences preferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        String ava = preferences.getString("ava_" + mAuth.getCurrentUser().getUid(), "not");
        String name = preferences.getString("name_" + mAuth.getCurrentUser().getUid(), "not");
        String statue = preferences.getString("statue_" + mAuth.getCurrentUser().getUid(), "not");
        if (!ava.equals("not") && !name.equals("not") && !statue.equals("not")) {
            Global.avaLocal = ava;
            Global.nameLocal = name;
            Global.statueLocal = statue;

            nameNav.setText(name);
            if (statue.length() > Global.STATUE_LENTH)
                statueT = statue.substring(0, Global.STATUE_LENTH) + "...";

            else
                statueT = statue;
            statueT = "\"" + statueT + "\"";
            statueNav.setText(statueT);
            if (ava.equals("no")) {
                Picasso.get()
                        .load(R.drawable.profile)
                        .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                        .into(avatarNav);
            } else {
                Picasso.get()
                        .load(ava)
                        .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                        .into(avatarNav);
            }
        }
    }

    private void updateTokens() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            if (token != null) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("tokens", token);
                                DatabaseReference mToken = FirebaseDatabase.getInstance().getReference(Global.tokens);
                                mToken.child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }

                        }


                    }
                });


    }

    private void checkData() {
        Query query = mData.child(mAuth.getCurrentUser().getUid());
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                if (userData.getName() == null) {
                    Intent intent = new Intent(getApplicationContext(), DataSet.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    meowBottomNavigation.show(0, true);
                    break;
                case 1:
                    meowBottomNavigation.show(1, true);
                    break;
                case 2:
                    meowBottomNavigation.show(2, true);
                    break;

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    //fragment setup
    public void setupFm(FragmentManager fragmentManager) {
        Adapter = new Vpadapter(fragmentManager);
        //Add All Fragment To List
        Adapter.add(calls, getResources().getString(R.string.calls));
        Adapter.add(chats, getResources().getString(R.string.chats));
        Adapter.add(groups, getResources().getString(R.string.groups));
        vp.setAdapter(Adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void disableshourtcuts()
    {
        List<String> idds = new ArrayList<>();
        idds.add("addstory");
        idds.add("group");
        idds.add("user1");
        idds.add("user2");
        ShortcutManager shortcutManager2 = getSystemService(ShortcutManager.class);
        shortcutManager2.disableShortcuts(idds);
    }
}
