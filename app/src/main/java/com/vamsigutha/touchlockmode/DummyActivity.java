package com.vamsigutha.touchlockmode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DummyActivity extends Activity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final int LOCK_REQUEST_CODE = 221;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dummy);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        Intent i = keyguardManager.createConfirmDeviceCredentialIntent("Unlock",
                "Confirm your screen lock PIN,Pattern or Password");
        startActivityForResult(i, LOCK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case LOCK_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    stopService(new Intent(this,ChatHeadService.class));
                    Log.e("msg","in lock request code");

                    SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putBoolean("state",false);
                    editor.apply();
                }
                finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }
}