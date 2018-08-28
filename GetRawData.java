package com.afrodroid.android.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;


    public GetRawData() {
        mDownloadStatus = DownloadStatus.IDLE;

    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: =" + s);

    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
//
//          attempt to make url from String parameter
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//
//            String line;
//            while (null != (line = reader.readLine())) {
////          .readline() strips off the newline characters, so they have to be re added below
//                result.append(line).append("\n");
//
//            }

//            Here is an alternate way to write the above code
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }


        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid Url " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());

        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception. Needs permission?" + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream" + e.getMessage());
                }
            }

        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;


    }
}









