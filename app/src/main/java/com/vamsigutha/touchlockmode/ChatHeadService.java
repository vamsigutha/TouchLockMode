package com.vamsigutha.touchlockmode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.view.View.STATUS_BAR_HIDDEN;
import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;
    private int getTapIndex;
    private int getAuthIndex;
    private static final int LOCK_REQUEST_CODE = 221;
    Context context = this;


    public ChatHeadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();


        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("state",true);
        editor.apply();

        getTapIndex = mPrefs.getInt("tapIndex",0);
        getAuthIndex = mPrefs.getInt("authIndex",0);


        //Inflate the chat head layout we created
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head, null);
        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels){
            mChatHeadView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        }


        //Add the view to the window.
        WindowManager.LayoutParams params;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }else{

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        params.flags= WindowManager.LayoutParams.FLAG_FULLSCREEN;


        //Specify the chat head position
//        params.gravity = Gravity.TOP | Gravity.START;       //Initially view will be added to top-left corner
//        params.x = 0;
//        params.y = 100;

        params.height = -1;
        params.width = -1;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);



        //Set the close button.



        //Drag and move chat head using user's touch action.
        final ImageView chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);

//        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels){
//            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0,0,30,30);
//            chatHeadImage.setLayoutParams(layoutParams);
//
//        }

        chatHeadImage.setOnTouchListener(new View.OnTouchListener(){
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
                    new GestureDetector.SimpleOnGestureListener(){
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public boolean onDown(MotionEvent e) {
                            if(getTapIndex==0){
                                if(getAuthIndex==0){
                                    stopSelf();
                                }else if(getAuthIndex==1){

                                        try {
                                            //Start activity for result
//                                        MainActivity mainActivity = new MainActivity();
//                                        mainActivity.startActivityForResult(i, LOCK_REQUEST_CODE);
                                            Intent intent = new Intent(ChatHeadService.this,DummyActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);


                                        } catch (Exception error) {
                                            //If some exception occurs means Screen lock is not set up please set screen lock
                                            Log.e("msg",""+error);

                                        }

                                }
                            }
                            return true;
                        }



                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            if(getTapIndex==1){
                                if(getAuthIndex==0){
                                    stopSelf();
                                }else if(getAuthIndex==1){

                                    try {
                                        //Start activity for result
//                                        MainActivity mainActivity = new MainActivity();
//                                        mainActivity.startActivityForResult(i, LOCK_REQUEST_CODE);
                                        Intent intent = new Intent(ChatHeadService.this,DummyActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);


                                    } catch (Exception error) {
                                        //If some exception occurs means Screen lock is not set up please set screen lock
                                        Log.e("msg",""+error);

                                    }

                                }
                            }
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            if(getTapIndex==2){
                                if(getAuthIndex==0){
                                    stopSelf();
                                }else if(getAuthIndex==1){

                                    try {
                                        //Start activity for result
//                                        MainActivity mainActivity = new MainActivity();
//                                        mainActivity.startActivityForResult(i, LOCK_REQUEST_CODE);
                                        Intent intent = new Intent(ChatHeadService.this,DummyActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);


                                    } catch (Exception error) {
                                        //If some exception occurs means Screen lock is not set up please set screen lock
                                        Log.e("msg",""+error);

                                    }

                                }
                            }
                            super.onLongPress(e);
                        }
                    });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

//        chatHeadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //close the service and remove the chat head from the window
//                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P && finalBiometricPrompt !=null){
//                    finalBiometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
//                        @Override
//                        public void onAuthenticationError(int errorCode, CharSequence errString) {
//                            super.onAuthenticationError(errorCode, errString);
//                            Log.e("msg", "authentication error");
//                        }
//
//                        @Override
//                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
//                            super.onAuthenticationSucceeded(result);
//                            stopSelf();
//                        }
//
//                        @Override
//                        public void onAuthenticationFailed() {
//                            super.onAuthenticationFailed();
//                            Log.e("msg","authentication failed");
//                        }
//                    });
//                }else{
//                    stopSelf();
//                }
//
//            }
//        });
//        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
//            private int lastAction;
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//
//                        //remember the initial position.
//                        initialX = params.x;
//                        initialY = params.y;
//
//                        //get the touch location
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//
//                        lastAction = event.getAction();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        //As we implemented on touch listener with ACTION_MOVE,
//                        //we have to check if the previous action was ACTION_DOWN
//                        //to identify if the user clicked the view or not.
//                        if (lastAction == MotionEvent.ACTION_DOWN) {
//                            //Open the chat conversation click.
//                            Intent intent = new Intent(ChatHeadService.this, ChatActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            //close the service and remove the chat heads
//                            stopSelf();
//                        }
//                        lastAction = event.getAction();
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        //Calculate the X and Y coordinates of the view.
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//
//                        //Update the layout with new X & Y coordinate
//                        mWindowManager.updateViewLayout(mChatHeadView, params);
//                        lastAction = event.getAction();
//                        return true;
//                }
//                return false;
//            }
//        });

        mChatHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(view != chatHeadImage){
                    Toast.makeText(getApplicationContext(),"outside", Toast.LENGTH_SHORT).show();
                    Log.e("msg","outside");
                }
            }
        });
//        ImageView iv=(ImageView) mChatHeadView.findViewById(R.id.imageView);
        LottieAnimationView iv = (LottieAnimationView) mChatHeadView.findViewById(R.id.security_animation);

        mChatHeadView.setOnTouchListener(new View.OnTouchListener(){
            int lastAction;

            RelativeLayout.LayoutParams lp =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    lastAction = event.getAction();

                    int x = (int) event.getX() ;
                    int y = (int) event.getY();
               ; //Assuming you use a RelativeLayout

                    lp.setMargins(((int) event.getRawX()-300),((int) event.getRawY()-400),0,0);
                    lp.height = 600;
                    lp.width = 600;
                    iv.setLayoutParams(lp);
                    iv.setVisibility(View.VISIBLE);
//                    iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
//                    ((ViewGroup)v).addView(iv);
                    lastAction = event.getAction();
                    return true;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    iv.setVisibility(View.GONE);
                    return false;
                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    iv.setVisibility(View.VISIBLE);
                    lp.height = 600;
                    lp.width = 600;
                    lp.setMargins(((int) event.getRawX()-300),((int) event.getRawY()-400),0,0);

                    iv.setLayoutParams(lp);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }


        });


    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
        SharedPreferences mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("state",false);
        editor.apply();
//        TouchTileService touchTileService = new TouchTileService();
//        touchTileService.allowTouch();
    }

}
