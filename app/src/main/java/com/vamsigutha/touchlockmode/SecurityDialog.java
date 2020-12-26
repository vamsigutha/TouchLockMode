package com.vamsigutha.touchlockmode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SecurityDialog extends DialogFragment {
    private String selection;
    private String[] authArray;
    private int setSelectedIndex;
    private int getSelectedIndex;
    private Context context;

    public SecurityDialog(Context context){
        this.context= context;
    }

    public interface AuthSelectedListener{
        public void onAuthSelected(String selectedAuth);
    }
    AuthSelectedListener authSelectedListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        SharedPreferences mPrefs = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        getSelectedIndex = mPrefs.getInt("authIndex",0);

        authArray = getActivity().getResources().getStringArray(R.array.security);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setSingleChoiceItems(R.array.security,getSelectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = authArray[which];
                setSelectedIndex = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences mPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("authMethod",selection);
                editor.putInt("authIndex", setSelectedIndex);
                editor.apply();
                authSelectedListener.onAuthSelected(selection);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try{
            authSelectedListener = (AuthSelectedListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " Must implement method");
        }
    }
}
