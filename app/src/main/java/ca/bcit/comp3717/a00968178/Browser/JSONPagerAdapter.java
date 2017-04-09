package ca.bcit.comp3717.a00968178.Browser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brayden on 3/26/2017.
 */

public class JSONPagerAdapter extends FragmentPagerAdapter {

    private JSONArray array;
    private String titleKey;

    public JSONPagerAdapter(FragmentManager fragmentManager, JSONArray array, String JSONtitleKey) {
        super(fragmentManager);
        this.array = array;
        this.titleKey = JSONtitleKey;
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        try {
            return LinkWebviewActivity.PlaceholderFragment.newInstance(array.getJSONObject(position));
        } catch (JSONException e) {
            return null;
        }
    }


    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        try {
            JSONObject item = array.getJSONObject(position);

            return item.getString(titleKey);

        } catch (JSONException e) {
            return titleKey + " unknown";
        }
    }
}