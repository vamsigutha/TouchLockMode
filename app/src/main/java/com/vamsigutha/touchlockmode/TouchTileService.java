package com.vamsigutha.touchlockmode;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TouchTileService extends TileService {
    private final int STATE_ON = 1;
    private final int STATE_OFF = 0;
    private int toggle = STATE_OFF;
    Intent intent;
    Icon icon;
    private int getTapIndex;
    private int getAuthIndex;

    @Override
    public void onStartListening() {
        Log.e("msg","tile called");

        updateTile();
        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        getTapIndex = mPrefs.getInt("tapIndex",0);
        getAuthIndex = mPrefs.getInt("authIndex",0);

    }

    public void updateTile() {

        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        boolean state = mPrefs.getBoolean("state",false);
        Log.e("msg",""+state);
        if(!state){
            toggle = STATE_OFF;
            getQsTile().setState(Tile.STATE_INACTIVE);
            getQsTile().setLabel("Lock touch");
            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_lock_touch_off);
        }else{
            toggle = STATE_ON;
            getQsTile().setState(Tile.STATE_ACTIVE);
            getQsTile().setLabel("UnLock touch");
            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_lock_touch);
        }
        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }



    //Here we initialized intent because we need to stop same intent that we started.


    //Perform click functionality
    @Override
    public void onClick() {
        if(toggle == STATE_ON){
            toggle = STATE_OFF;
            getQsTile().setState(Tile.STATE_INACTIVE);
            getQsTile().setLabel("Lock touch");

                if(getAuthIndex==0){
                    stopSelf();
                }else if(getAuthIndex==1){
                    {

                        try {

                            Intent intent = new Intent(TouchTileService.this,DummyActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

//                            if(intent == null){
//                                intent = new Intent(getApplicationContext(), ChatHeadService.class);
//                            }
//                            stopService(intent);
                            SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
                            boolean state = mPrefs.getBoolean("state",false);
                            if(!state){
                                icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_lock_touch_off);
                            }else{
                                icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_lock_touch);
                            }



                        } catch (Exception error) {
                            //If some exception occurs means Screen lock is not set up please set screen lock
                            Log.e("msg",""+error);

                        }
                    }
                }




        }else{
            toggle = STATE_ON;
            getQsTile().setState(Tile.STATE_ACTIVE);
            getQsTile().setLabel("Unlock touch");
            intent = new Intent(getApplicationContext(), ChatHeadService.class);
            startService(intent);

            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_lock_touch);
        }
        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("msg", "tile destroyed");
    }






    //    public void allowTouch(){
//        try{
//            getQsTile().setState(Tile.STATE_INACTIVE);
//            getQsTile().setLabel("Lock touch");
//            toggle = STATE_OFF;
//            icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_lock_touch_off);
//            getQsTile().setIcon(icon);
//            getQsTile().updateTile();
//            Log.e("msg","NO exception");
//        }catch(Exception e){
//            Log.e("msg","exception");
//        }
//
//
//    }
}
