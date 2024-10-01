package com.talq.story;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.talq.R;
import com.talq.global.AppBack;
import com.talq.global.Global;
import com.talq.story.filters.FilterListener;
import com.talq.story.filters.FilterViewAdapter;
import com.talq.story.tools.EditingToolsAdapter;
import com.talq.story.tools.ToolType;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;

public class SroyAdd extends AppCompatActivity implements View.OnClickListener
        , EditingToolsAdapter.OnItemSelected, FilterListener,
        StickerBSFragment.StickerListener, EmojiBSFragment.EmojiListener,
        OnPhotoEditorListener, PropertiesBSFragment.Properties {
    ///This Activity is just for testing new features and buggy story components

    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;
    private Button addS;
    private RecyclerView mRvTools, mRvFilters;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;

    //firebase
    FirebaseAuth mAuth;
    DatabaseReference mData, mUserDB, mPhone;

    //encrypt

    //compress
    ArrayList<String> localContacts, ContactsId;
    String idStory = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        Dexter.withContext(SroyAdd.this).withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            finish();
                        } else {
                            Toast.makeText(SroyAdd.this, "WWoooho", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        Toast.makeText(SroyAdd.this, "rationale", Toast.LENGTH_SHORT).show();
                    }
                }).check();

        makeFullScreen();
        Global.currentactivity = this;
        initViews();

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mUserDB = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mPhone = FirebaseDatabase.getInstance().getReference(Global.Phones);

        ((AppBack) getApplication()).getBlock();
        ((AppBack) getApplication()).getMute();

        Global.stickerIcon = true;

        //encryption

        //arrays init
        localContacts = new ArrayList<>();
        ContactsId = new ArrayList<>();
        addS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.check_int(SroyAdd.this)) {
                    Dexter.withContext(SroyAdd.this)
                            .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {

                                    if (report.areAllPermissionsGranted()) {
                                        addToStory();

                                    } else
                                        Toast.makeText(SroyAdd.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                    token.continuePermissionRequest();

                                }
                            }).check();
                } else
                    Toast.makeText(SroyAdd.this, R.string.check_int, Toast.LENGTH_SHORT).show();

            }
        });


        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
        initViews();

    }

    private void addToStory() {
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgCamera;
        ImageView imgGallery;
        ImageView imgSave;
        ImageView imgClose;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRvTools = findViewById(R.id.rvConstraintTools);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);
        addS = findViewById(R.id.addS);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        imgCamera.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.imgClose:
                onBackPressed();
                break;
        }
    }

    private void saveImage() {
        Toast.makeText(this, "waittt", Toast.LENGTH_SHORT).show();
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()
                + File.separator + "Story"+System.currentTimeMillis() + ".png");

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SaveSettings saveSettings = new SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mPhotoEditor.saveAsFile(file.getPath(), saveSettings, new PhotoEditor.OnSaveListener() {
            @Override
            public void onSuccess(@NonNull String imagePath) {
                Toast.makeText(SroyAdd.this, "Saved", Toast.LENGTH_SHORT).show();
                Toast.makeText(SroyAdd.this, imagePath, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.wtf("asdaa",exception.getMessage());            }
        });
    }

    @Override
    public void onToolSelected(ToolType toolType) {

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {

    }

    @Override
    public void onStickerClick(Bitmap bitmap, int position) {

    }

    @Override
    public void onEmojiClick(String emojiUnicode) {

    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onColorChanged(int colorCode) {

    }

    @Override
    public void onOpacityChanged(int opacity) {

    }

    @Override
    public void onBrushSizeChanged(int brushSize) {

    }
    public void     makeFullScreen() {
    }

}