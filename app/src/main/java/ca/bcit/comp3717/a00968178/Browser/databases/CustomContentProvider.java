package ca.bcit.comp3717.a00968178.Browser.databases;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import ca.bcit.comp3717.a00968178.Browser.BuildConfig;
import ca.bcit.comp3717.a00968178.Browser.LinksOpenHelper;


public class CustomContentProvider
    extends ContentProvider
{
    private static final String TAG = CustomContentProvider.class.getName();
    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private static final UriMatcher uriMatcher;

    //public static final String BASE_CONTENT_URI = "content://comp3717.bcit.ca.database/";

    private static final String INVALID_URI = "Unsupported Custom URI: ";

    //private static final String TABLE_NAME = ToursOpenHelper.NAME_TABLE_NAME;

    private static final int LINKS_URI = 0;
    private static final String[] TABLES = {"links"};

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        for(int i = 0; i < TABLES.length; i++) {
            uriMatcher.addURI(PACKAGE_NAME, TABLES[i], i);
        }
    }

    static
    {
        //CONTENT_URI = Uri.parse("content://"+PACKAGE_NAME+"/"+TABLE_NAME );
    }

    @Override
    public boolean onCreate()
    {
        //namesOpenHelper = ToursOpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(final Uri      uri,
                        final String[] projection,
                        final String   selection,
                        final String[] selectionArgs,
                        final String   sortOrder)
    {
        final Cursor cursor;

        //Log.d(TAG,"query: "+uri.getPath());
        //if(projection != null && projection.length > 0) Log.d(TAG, "proj: "+projection[0]);
        //Log.d(TAG, "selection: "+selection);
        //if(selectionArgs != null && selectionArgs.length > 0) Log.d(TAG, "args: "+selectionArgs[0]);

        //Log.d(TAG, "segmenT: "+uri.getLastPathSegment());

        switch (uriMatcher.match(uri))
        {

            case LINKS_URI:
            {
                Log.d(TAG, "getting links cursor...");
                OpenHelper helper = new LinksOpenHelper(getContext());
                //cursor = categoriesHelper.dumpTable(getContext());
                cursor = helper.getRows(projection, selection, selectionArgs, null, null, sortOrder, null);
                //helper.close();
                break;
            }


            default:
            {
                throw new IllegalArgumentException(INVALID_URI + uri);
            }
        }

        return (cursor);
    }

    @Override
    public String getType(final Uri uri)
    {
        Log.d(TAG,"getType: "+uri.getPath());
        final String type;

        switch(uriMatcher.match(uri))
        {

            case LINKS_URI:
                type = "vnd.android.cursor.dir/vnd."+PACKAGE_NAME+"."+LinksOpenHelper.DB_NAME;
                break;

            default:
                throw new IllegalArgumentException(INVALID_URI + uri);
        }

        return (type);
    }

    @Override
    public int delete(final Uri      uri,
                      final String   selection,
                      final String[] selectionArgs)
    {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(final Uri           uri,
                      final ContentValues values)
    {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(final Uri           uri,
                      final ContentValues values,
                      final String        selection,
                      final String[]      selectionArgs)
    {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
