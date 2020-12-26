package com.vamsigutha.touchlockmode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements TapDialog.TapSelectionListener, SecurityDialog.AuthSelectedListener{
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final int LOCK_REQUEST_CODE = 221;
    private String selectedTapMethod;
    private String selectedAuthMethod;
    TextView displayTapSelection;
    TextView displayAuthSelection;
    boolean previousChecked;
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        ConstraintLayout rootElement = findViewById(R.id.root_element);
        displayTapSelection  = findViewById(R.id.display_tap_selection);
        displayAuthSelection = findViewById(R.id.display_auth_selection);
        switchCompat = findViewById(R.id.toggle_notification);


        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        selectedTapMethod = mPrefs.getString("tapMethod","Single Tap");
        selectedAuthMethod = mPrefs.getString("authMethod","None");
        previousChecked = mPrefs.getBoolean("switch",true);

        displayTapSelection.setText(selectedTapMethod);
        displayAuthSelection.setText(selectedAuthMethod);

        switchCompat.setChecked(previousChecked);


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mPrefs.edit();
                if (isChecked){
                    startTouchService();
                    editor.putBoolean("switch",true);
                    editor.apply();
                }else{
                    stopTouchService();
                    editor.putBoolean("switch",false);
                    editor.apply();
                }
            }
        });


//        Calendar calendar = Calendar.getInstance();
//        int time = calendar.get(Calendar.HOUR_OF_DAY);
//
//        if(time >= 6 && time < 18){
//
//            rootElement.setBackgroundResource(R.drawable.day);
//
//        }else{
//
//            rootElement.setBackgroundColor(Color.parseColor("#E9F0FB"));
//
//        }



        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            if(previousChecked){
                startTouchService();
            }
        }

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
//            Toast.makeText(this,
//                            "Draw over other app permission not available. Closing the application",
//                            Toast.LENGTH_SHORT).show();
//
//                    finish();
//        }
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {

    }

    public void tapSelection(View view){
        new TapDialog(this).show(getSupportFragmentManager(), "tapdialog");
    }

    public void securitySelection(View view){
        new SecurityDialog(this).show(getSupportFragmentManager(),"securitydialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case CODE_DRAW_OVER_OTHER_APP_PERMISSION:
                //Check if the permission is granted or not.
                if (resultCode == RESULT_OK) {
                    initializeView();
                } else { //Permission is not available
                    Toast.makeText(this,
                            "Draw over other app permission not available. Closing the application",
                            Toast.LENGTH_SHORT).show();

                    finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onTapSelected(String selectedTap) {
        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        selectedTapMethod = mPrefs.getString("tapMethod","Single Tap");
        displayTapSelection.setText(selectedTapMethod);
    }

    @Override
    public void onAuthSelected(String selectedAuth) {
        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        selectedAuthMethod = mPrefs.getString("authMethod","None");
        displayAuthSelection.setText(selectedAuthMethod);
    }

    public void startTouchService(){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }

    public void stopTouchService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    public void rateApp(View view)
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}