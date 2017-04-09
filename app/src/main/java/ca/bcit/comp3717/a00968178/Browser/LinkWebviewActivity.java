package ca.bcit.comp3717.a00968178.Browser;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import ca.bcit.comp3717.a00968178.Browser.databases.OpenHelper;

public class LinkWebviewActivity extends AppCompatActivity {

    private static final String TAG = LinkWebviewActivity.class.getName();


    private JSONPagerAdapter pagerAdapter;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Links");

        LinksOpenHelper helper = new LinksOpenHelper(this);
        Cursor c = helper.getRows();
        JSONArray array = OpenHelper.cursorJSONArray(c);
        c.close();
        helper.close();

        pagerAdapter = new JSONPagerAdapter(getSupportFragmentManager(),
                array, getString(R.string.name_key));


        // Set up the ViewPager with the sections adapter.
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(pagerAdapter);


        int linkId = 0;
        if(getIntent().hasExtra("id")) {
            linkId = getIntent().getIntExtra("id", 0);
        }


        pager.setCurrentItem(linkId-1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // we can do this because this is only ever invoked from LinksActivity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        //private static final String ARG_STOP_NUMBER = "stop_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(JSONObject object) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            String json = object.toString();

            args.putString("json_string", json);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_webview, container, false);

            JSONObject link = null;
            try {
                link = new JSONObject(getArguments().getString("json_string"));

                String givenUrl = link.getString(getString(R.string.link_key1));


                try {
                    givenUrl = Util.resolveURL(givenUrl);

                } catch (URISyntaxException e) {
                    Messaging.showMessage(this.getActivity(), "Invalid URL: "+givenUrl);
                    return rootView;
                }



                WebView webView = (WebView) rootView.findViewById(R.id.link_webview);



                webView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(givenUrl);

            } catch (Exception e) {

                e.printStackTrace();
                //Messaging.showError((Activity)container.getContext(), e.getMessage());
                return rootView;

            }
            return rootView;
        }
    }



}
