package com.example.ezaharova.test;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.ezaharova.test.MainActivity.TAG;

public class MyTask extends AsyncTask<Void, Answer, Void> {
    public static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    public static final String PARAM_ID = "id";
    private boolean running;
    private TaskFragment taskFragment;

    public MyTask(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    protected void onPreExecute() {
        running = true;
    }

    @Override
    protected Void doInBackground(Void... nothing) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(taskFragment.getUrl())
                .post(RequestBody.create(CONTENT_TYPE, PARAM_ID + "=" + taskFragment.getDeviceId()))
                .build();
        String body;
        try {
            Response response = client.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            publishProgress(new Answer(e.getMessage()));
            return null;
        }
        if (BuildConfig.DEBUG) Log.d(TAG, "response:\n" + body);
        Gson gson = new GsonBuilder().create();
        SystemClock.sleep(5000);
        publishProgress(gson.fromJson(body, Answer.class));
        return null;
    }

    @Override
    protected void onProgressUpdate(Answer... answers) {
        taskFragment.setAnswer(answers[0]);
    }

    @Override
    protected void onPostExecute(Void nothing) {
        running = false;
    }

    @Override
    protected void onCancelled() {
        running = false;
        taskFragment.setAnswer(null);
    }
}
