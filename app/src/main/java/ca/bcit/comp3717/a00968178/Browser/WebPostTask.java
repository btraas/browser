package ca.bcit.comp3717.a00968178.Browser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.PatternSyntaxException;


import au.com.bytecode.opencsv.CSVParser;

/**
 * Created by Brayden on 3/14/2017.
 */

public abstract class WebPostTask extends AsyncTask<String, String, Void> {

    private static String TAG = WebPostTask.class.getName();


    private ProgressDialog progressDialog;
    protected URL urlSelect = null;
    protected final Activity activity;
    protected final Context ctx;

    private String message = "Downloading your data...";
    private String result = null;

    public WebPostTask(Activity a, String url) throws ActivityNotFoundException {
        if(a == null) throw new ActivityNotFoundException("Activity is null!");

        this.activity = a;
        this.ctx = a.getApplicationContext();
        try {
            this.urlSelect = new URL(url);
        } catch(MalformedURLException e) {
            Messaging.showError(activity, "Not a valid URL!");
        }
    }




    protected void onPreExecute() {

        // null message indicates no UI shown, this is a background task.
        if(message != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog = new ProgressDialog(activity);


                    progressDialog.setMessage(message);
                    progressDialog.show();
                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface arg0) {
                            WebPostTask.this.cancel(true);
                        }
                    });
                }
            });
        }

    }




    @Override
    protected Void doInBackground(String[] params) {

        if(urlSelect == null) return null;

        InputStream inputStream;

        try {
            // Set up HTTP post
            Log.d(TAG, "Opening stream from "+urlSelect.toString());

            Messaging.hideInput(activity);
            inputStream = (urlSelect).openStream();
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";

        } catch (FileNotFoundException e3) {
            Messaging.showError(activity, activity.getString(R.string.no_connection));
            Log.e("FNFException", e3.toString());
            e3.printStackTrace();

        } catch (IOException e4) {
            Messaging.showError(activity, activity.getString(R.string.no_connection));
            Log.e("IOException", e4.toString());
            e4.printStackTrace();


        }
        catch (Exception e) {
            Messaging.showError(activity, "Download Failed");
            Log.e(TAG, "StringBuilding & BufferedReader " + "Error converting result " + e.toString());
            e.printStackTrace();
        }
        return null;
    } // protected Void doInBackground(String... params)

    protected void onPostExecute(Void v) {

        if (result == null) {
            if(this.progressDialog != null) this.progressDialog.dismiss();
            String msg = "No data received!";
            if(urlSelect == null) msg = "Invalid URL!";
            Messaging.showError(activity, msg);
            return;
        }


        try {
            JSONArray data = new JSONArray(result);
            callback(data);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            //Log.e(TAG, "not valid json: "+result);
            //Messaging.showError(activity, "Not valid JSON!");

            try {   // try parsing as CSV

                String[] splitArray;
                try {
                    splitArray = result.split("\\s+");
                } catch (PatternSyntaxException ex) {
                    Log.e(TAG, "PatternSyntaxException: "+ex.getMessage());
                    ex.printStackTrace();
                    throw new IOException();
                }

                JSONArray jArray = new JSONArray();
                CSVParser parser = new CSVParser();
                for(int i = 0; i < splitArray.length; ++i) {
                    String[] line = parser.parseLine(splitArray[i]);
                    JSONObject o = new JSONObject();
                    if(line.length == 0) {
                        Log.e(TAG, "no lines in CSV!");
                        throw new IOException();
                    }
                    String name = line[0];
                    String url = line.length > 1 ? line[1] : line[0];

                    o.put(activity.getString(R.string.name_key), name);
                    o.put(activity.getString(R.string.link_key1), url);
                    jArray.put(o);

                }
                callback(jArray);


            } catch (Exception e2) {
                Log.e(TAG, e2.getMessage());
                try {
                    URL url = new URL(result);
                    JSONArray jArray = new JSONArray();
                    JSONObject jObject = new JSONObject();
                    jObject.put(activity.getString(R.string.name_key), result);
                    jObject.put(activity.getString(R.string.link_key1), result);
                    jArray.put(jObject);
                    callback(jArray);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    Messaging.showError(activity, "Unable to parse data at URL!");
                }
            }


        } finally {
            if(this.progressDialog != null) this.progressDialog.dismiss();
        }

    } // protected void onPostExecute(Void v)

    protected abstract void callback(JSONArray result);


    public void execute() {
        this.execute(new String[] {});
    }
} //class MyAsyncTask extends AsyncTask<String, String, Void>