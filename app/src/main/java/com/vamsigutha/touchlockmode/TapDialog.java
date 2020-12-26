package com.vamsigutha.touchlockmode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TapDialog extends DialogFragment {

    private String selection;
    private Context context;
    private String selectedTapMethod;
    private int getTapIndex;
    private int setTapIndex;

    public TapDialog(Context context) {
        this.context = context;

    }

    public interface TapSelectionListener{
        public void onTapSelected(String selectedTap);
    }

    TapSelectionListener tapSelectionListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        SharedPreferences mPrefs = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        getTapIndex = mPrefs.getInt("tapIndex",0);


        String[] taps  = getActivity().getResources().getStringArray(R.array.taps);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setSingleChoiceItems(R.array.taps,getTapIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = taps[which];
                setTapIndex = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences mPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("tapMethod",selection);
                editor.putInt("tapIndex", setTapIndex);
                editor.apply();
                tapSelectionListener.onTapSelected(selection);
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
            tapSelectionListener = (TapSelectionListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " Must implement method");
        }

    }
}
