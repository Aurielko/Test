package com.example.ezaharova.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import android.support.annotation.CallSuper;

public class MainActivity extends Activity {
    private TaskFragment taskFragment;
    private WebView web;
    private ProgressBar progressBar;

    public static final String TAG_TASK_FRAGMENT = "task";
    public static final String TAG = "Task";

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (BuildConfig.DEBUG) Log.d(TAG, "Click: " + url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                MainActivity.this.startActivity(browserIntent);
                MainActivity.this.finish();
                return true;
            }
        });
        progressBar = findViewById(R.id.progress_bar);

        FragmentManager fragmentManager = getFragmentManager();
        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            fragmentManager.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        if (taskFragment.getAnswer() == null) {
            progressBar.setVisibility(View.VISIBLE);
            taskFragment.startTask();
        } else {
            answer(taskFragment.getAnswer());
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        web.setWebViewClient(null);
        web.destroy();
        super.onDestroy();
    }

    public void answer(Answer answer) {
        progressBar.setVisibility(View.INVISIBLE);
        if (answer == null) return;
        if (answer.isOk()) {
            web.loadUrl(answer.getUrlString());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.err_title)
                    .setMessage(answer.getMessage())
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    });
            builder.create().show();
        }
    }
}