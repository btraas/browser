package ca.bcit.comp3717.a00968178.Browser;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


/**
 * A simple Adapter for showing the number of datasets in a category
 */
public class LinkAdapter extends SimpleCursorAdapter {

    private Context context;
    private int layout;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Cursor c = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        int nameCol = c.getColumnIndex("name");
        String name = c.getString(nameCol);

        int urlCol = c.getColumnIndex(context.getString(R.string.link_key1));
        String url = c.getString(urlCol);

        TextView name_text = (TextView) v.findViewById(R.id.name);
        if (name_text != null) {
            name_text.setText(name + " ("+url+")");
        }

        v.setId(c.getInt(c.getColumnIndex("_id")));

        return v;
    }


    public LinkAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }


    @Override
    public void bindView(View v, Context context, Cursor c) {

        int nameCol = c.getColumnIndex("name");
        String name = c.getString(nameCol);

        int urlCol = c.getColumnIndex(context.getString(R.string.link_key1));
        String url = c.getString(urlCol);


        TextView name_text = (TextView) v.findViewById(R.id.name);
        if (name_text != null) {
            name_text.setText(name + " (" + url + ")");
        }

        v.setId(c.getInt(c.getColumnIndex("_id")));

    }


}