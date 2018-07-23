package com.example.ezaharova.test;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import static com.example.ezaharova.test.MainActivity.TAG;


public class TaskFragment extends Fragment {

    private MyTask task;
    private String deviceId;
    private String url;
    private Answer answer;

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Activity activity = getActivity();
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        url = activity.getString(R.string.url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
        }
        deviceId = telephonyManager.getDeviceId();
        if (BuildConfig.DEBUG) Log.d(TAG, "deviceId: " + deviceId + " URL: " + url);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel(true);
    }

    public void startTask() {
        if (task != null && task.isRunning()) return;
        task = new MyTask(this);
        task.execute();
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
        if (isAdded() && getActivity() != null) ((MainActivity) getActivity()).answer(answer);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUrl() {
        return url;
    }

}