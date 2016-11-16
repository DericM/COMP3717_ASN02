package ca.bcit.dmccadden.comp3717_asn02;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CourseContentProvider extends ContentProvider
{
    private static final UriMatcher uriMatcher;
    private static final int NAMES_URI = 1;
    private CourseOpenHelper namesOpenHelper;
    public static final Uri CONTENT_URI;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("comp3717_asn02.bcit.ca", "courses", NAMES_URI);
    }

    static
    {
        CONTENT_URI = Uri.parse("content://comp3717_asn02.bcit.ca/courses" );
    }

    @Override
    public boolean onCreate()
    {
        namesOpenHelper = CourseOpenHelper.getInstance(getContext());

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

        switch (uriMatcher.match(uri))
        {
            case NAMES_URI:
            {
                final SQLiteDatabase db;

                db     = namesOpenHelper.getWritableDatabase();
                cursor = namesOpenHelper.getAllCourses(getContext(), db);
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        }

        return (cursor);
    }

    @Override
    public String getType(final Uri uri)
    {
        final String type;

        switch(uriMatcher.match(uri))
        {
            case NAMES_URI:
                type = "vnd.android.cursor.dir/vnd.comp3717_asn02.bcit.ca.courses";
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
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