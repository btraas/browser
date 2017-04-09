package ca.bcit.comp3717.a00968178.Browser;

import android.content.Context;

import ca.bcit.comp3717.a00968178.Browser.databases.OpenHelper;

/**
 * Created by darcy on 2016-10-16.
 */

public final class LinksOpenHelper
    extends OpenHelper
{
    public static final String NAME_COLUMN = "name";

    private static final String TAG = LinksOpenHelper.class.getName();
    public static final String DB_NAME = "links.db";
    public static final String TABLE_NAME = "links";
    private static final String ID_COLUMN_NAME = "_id";
    private static final String[] COLUMNS = {
            "name TEXT NOT NULL",
            " TEXT NOT NULL"

    };

    public LinksOpenHelper(final Context ctx)
    {
        super(ctx, DB_NAME, TABLE_NAME, ID_COLUMN_NAME, COLUMNS, ID_COLUMN_NAME );
        String linkKey = ctx.getString(R.string.link_key1);

        if(!super.columnNames[1].equals(linkKey)) {
            super.columnNames[1] = linkKey;
            super.columnDefs[1] = linkKey + COLUMNS[1];
        }

    }


}
