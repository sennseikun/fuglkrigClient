package com.example.simon.client;

import android.os.AsyncTask;

/**
 * Created by thoma on 3/25/2017.
 */

public class AsyncUpdater extends AsyncTask<String,Void,String> {

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {

        if(params.length > 0){
            return params[0];
        }

        return "Return value of background task";
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
