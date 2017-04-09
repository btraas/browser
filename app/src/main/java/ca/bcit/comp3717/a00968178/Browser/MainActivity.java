package ca.bcit.comp3717.a00968178.Browser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import ca.bcit.comp3717.a00968178.Browser.databases.OpenHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static String JSON_URL;
    private EditText urlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlView = (EditText) findViewById(R.id.urlText);
        JSON_URL = urlView.getText().toString();
    }


    public void onGo(View v) {

        JSON_URL = urlView.getText().toString().toLowerCase();

        // remove // if begins with this (protocol-independent links)
        if(JSON_URL.startsWith("//")) {
            Log.d(TAG, "Attempted protocol-independent url");
            JSON_URL = JSON_URL.substring(2);
        }

        //prepend with http:// if given string doesn't start with http
        if(!JSON_URL.startsWith(getString(R.string.protocol))) {
            JSON_URL = getString(R.string.default_protocol) + JSON_URL;
        }

        WebPostTask task = new WebPostTask(this, JSON_URL) {
            @Override
            protected void callback(JSONArray result) {
                OpenHelper helper = new LinksOpenHelper(this.activity);
                helper.rebuildTable();

                Log.d(TAG, result.toString());

                try {

                    if(result.length() == 0)
                        throw new IllegalArgumentException("No links in JSON url!");

                    for(int i = 0; i < result.length(); ++i) {
                        try {
                            JSONObject object = result.getJSONObject(i);
                            if (object.has(getString(R.string.link_key1)) &&
                                    object.has(getString(R.string.name_key))) {     // try by default keys
                                helper.insertJSON(object);                       // (name="name" & link="pictureUrl")
                            } else if(object.has(getString(R.string.link_key2)) &&
                                    object.has(getString(R.string.name_key))) {     // try by default keys 2
                                object.put(getString(R.string.link_key1), object.remove(getString(R.string.link_key2)));
                                helper.insertJSON(object);                       // (name="name" & link="pictureUrl")
                            } else if(object.length() >= 2) {
                                JSONObject buildJSON = new JSONObject();
                                Iterator<String> it = object.keys();            // try by JSON first & second children

                                String key1 = object.getString(it.next());
                                String key2 = object.getString(it.next());

                                // hmm. can't find a better way to see if this exists unless we actually load the url.
                                String name = key1;
                                String url = key2;



                                buildJSON.put(getString(R.string.name_key), name);
                                buildJSON.put(getString(R.string.link_key1), url);
                                helper.insertJSON(buildJSON);
                            } else if(object.length() == 1) {
                                JSONObject buildJSON = new JSONObject();
                                Iterator<String> it = object.keys();            // try by JSON first child

                                String url = object.getString(it.next());
                                buildJSON.put(getString(R.string.name_key), url);
                                buildJSON.put(getString(R.string.link_key1), url);
                                helper.insertJSON(buildJSON);
                            } else { // obj len == 0
                                throw new IllegalArgumentException("Empty JSON object at JArray["+i+"]");
                            }


                        } catch (JSONException e) {
                            try {
                                URL url = new URL(result.getString(i));         // try by raw url string

                                JSONObject buildJSON = new JSONObject();
                                buildJSON.put(getString(R.string.name_key), url.toString());
                                buildJSON.put(getString(R.string.link_key1), url.toString());
                                helper.insertJSON(buildJSON);

                            } catch (MalformedURLException e2) {
                                throw new JSONException("JArray["+i+"] is not valid JSON or URL.");
                            }
                        }
                    }

                    //helper.insertJSON(result);
                    Intent intent = new Intent(activity, LinksActivity.class);
                    activity.startActivity(intent);
                } catch (JSONException e) {
                    Messaging.showError(activity, e.getMessage());
                } catch (IllegalArgumentException e) {
                    Messaging.showError(activity, e.getMessage());
                }

            }
        };
        task.execute();
    }

}
