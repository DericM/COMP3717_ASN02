package ca.bcit.dmccadden.comp3717_asn02;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class Courses extends Activity {

    private static final String TAG = Terms.class.getName();
    private CourseOpenHelper courseOpenHelper;
    private List<String>     courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        courseOpenHelper = CourseOpenHelper.getInstance(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.courseList);
        init();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1,
                courseList);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String itemName = (String) arg0.getItemAtPosition(position);
                Intent i = new Intent(Courses.this, Details.class);
                i.putExtra("course", itemName);
                startActivity(i);
            }
        });
    }


    private void init()
    {
        final SQLiteDatabase db;
        Bundle b = getIntent().getExtras();
        String term = b.getString("term", null);
        db = courseOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            courseList = courseOpenHelper.getTermCourses(this, db, term);
        }
        finally{
            db.endTransaction();
        }
        db.close();
    }
}
