package ca.bcit.comp3717.a00968178.Browser;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ca.bcit.comp3717.a00968178.Browser.databases.OpenHelper;

public class LinksActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = LinksActivity.class.getName();
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        load();
    }

    private void load() {
        list = (ListView) findViewById(R.id.links_list);
        list.setOnItemClickListener(this);

        OpenHelper helper = new LinksOpenHelper(this);
        //Messaging.showMessage(this, ""+helper.getNumberOfRows());

        Cursor c = helper.getRows();
        Log.d(TAG, OpenHelper.cursorCSV(c));
        CursorAdapter adapter = new LinkAdapter(this, R.layout.link_row, c,
                new String[]
                {
                        LinksOpenHelper.NAME_COLUMN,
                },
                new int[]
                        {
                                android.R.id.text1,
                        });

        list.setAdapter(adapter);


/*
        final LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, new CustomLoaderCallbacks(LinksActivity.this, adapter,
                Uri.parse(OpenHelper.URI_BASE + LinksOpenHelper.TABLE_NAME)));

    */


    }


    @Override
    public void onItemClick(AdapterView<?> adapter, final View view, int position, long id)
    {
        Log.d(TAG, "onItemClick begin");

        // Must be called here because it's relative to *this* view (category row)
        TextView nameText = (TextView)(view.findViewById(R.id.name));

        // text -> Used for the title of the next view
        String text = nameText.getText().toString();

        //int linkId = position+1;

        final Intent intent;
        intent = new Intent(this, LinkWebviewActivity.class);

        final int linkId = view.getId();
        intent.putExtra("id", linkId);
        startActivity(intent);

        Log.d(TAG, "onItemClick end for id: "+id);
    }



}
