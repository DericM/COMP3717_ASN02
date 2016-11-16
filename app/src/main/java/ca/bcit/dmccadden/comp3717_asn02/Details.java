package ca.bcit.dmccadden.comp3717_asn02;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Details extends Activity {

    private static final String TAG = Terms.class.getName();
    private CourseOpenHelper courseOpenHelper;
    private List<String>     detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        courseOpenHelper = CourseOpenHelper.getInstance(getApplicationContext());
        TextView tvTitle = (TextView) findViewById(R.id.detailTitle);
        TextView tvContent = (TextView) findViewById(R.id.detailContent);
        init();
        tvTitle.setText(detailList.get(0));
        tvContent.setText(detailList.get(1));
    }



    private void init()
    {
        final SQLiteDatabase db;
        Bundle b = getIntent().getExtras();
        String course = b.getString("course", null);
        db = courseOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            detailList = courseOpenHelper.getCourseDetails(this, db, course);
        }
        finally{
            db.endTransaction();
        }
        db.close();
    }
}
