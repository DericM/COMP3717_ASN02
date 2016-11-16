package ca.bcit.dmccadden.comp3717_asn02;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.List;

import static ca.bcit.dmccadden.comp3717_asn02.R.layout.activity_terms;


public class Terms extends Activity {

    private static final String TAG = Terms.class.getName();
    private CourseOpenHelper     courseOpenHelper;
    private SimpleCursorAdapter adapter;

    private List<String> termsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_terms);
        final LoaderManager manager;
        courseOpenHelper = CourseOpenHelper.getInstance(getApplicationContext());
        manager = getLoaderManager();
        manager.initLoader(0, null, new CourseLoaderCallbacks());
        init();
        ListView listView = (ListView) findViewById(R.id.termList);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1,
                termsList);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                String itemName = (String) adapter.getItemAtPosition(position);
                Intent i = new Intent(Terms.this, Courses.class);
                i.putExtra("term", itemName);
                startActivity(i);
            }
        });

    }



    private void init()
    {
        final SQLiteDatabase db;
        final long           numEntries;
        db         = courseOpenHelper.getWritableDatabase();
        numEntries = courseOpenHelper.getNumberOfNames(db);
        if(numEntries == 0)
        {
            db.beginTransaction();
            try
            {
                String[] terms = getResources().getStringArray(R.array.terms);
                for (String t : terms){
                    int termID = getResources().getIdentifier(
                            t,
                            "array",
                            getPackageName());
                    String[] courses = getResources().getStringArray(termID);
                    for(String c : courses){
                        int courseID = getResources().getIdentifier(
                                c,
                                "array",
                                getPackageName());
                        String[] details = getResources().getStringArray(courseID);
                        courseOpenHelper.insertCourse(db,
                                                      t,
                                                      c,
                                                      details[0],
                                                      details[1]);
                    }
                }
                db.setTransactionSuccessful();
            }
            finally
            {
                db.endTransaction();
            }
        }
        db.beginTransaction();
        try{
            termsList = courseOpenHelper.getTerms(this, db);
        }
        finally{
            db.endTransaction();
        }
        db.close();
    }



    private class CourseLoaderCallbacks
            implements LoaderManager.LoaderCallbacks<Cursor>
    {
        @Override
        public Loader<Cursor> onCreateLoader(final int    id,
                                             final Bundle args)
        {
            final Uri uri;
            final CursorLoader loader;

            uri    = CourseContentProvider.CONTENT_URI;
            loader = new CursorLoader(Terms.this, uri, null, null, null, null);

            return (loader);
        }

        @Override
        public void onLoadFinished(final Loader<Cursor> loader,
                                   final Cursor         data)
        {
            if(data != null)
                adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(final Loader<Cursor> loader)
        {
            adapter.swapCursor(null);
        }
    }
}
