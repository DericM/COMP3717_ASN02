package ca.bcit.dmccadden.comp3717_asn02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class CourseOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = CourseOpenHelper.class.getName();
    private static final int    SCHEMA_VERSION     = 1;
    private static final String DB_NAME            = "courses.db";
    private static final String NAME_TABLE_NAME    = "courses";
    private static final String ID_COLUMN_NAME     = "_id";
    private static final String NAME_COLUMN_TERM   = "term";
    private static final String NAME_COLUMN_TITLE  = "title";
    private static final String NAME_COLUMN_COURSE = "course";
    private static final String NAME_COLUMN_DETAIL = "detail";
    private static CourseOpenHelper instance;

    private CourseOpenHelper(final Context ctx)
    {
        super(ctx, DB_NAME, null, SCHEMA_VERSION);
    }

    public synchronized static CourseOpenHelper getInstance(final Context context)
    {
        if(instance == null)
        {
            instance = new CourseOpenHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onConfigure(final SQLiteDatabase db)
    {
        super.onConfigure(db);

        setWriteAheadLoggingEnabled(true);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(final SQLiteDatabase db)
    {
        final String CREATE_NAME_TABLE;

        CREATE_NAME_TABLE = "CREATE TABLE IF NOT EXISTS "  + NAME_TABLE_NAME + " ( " +
                ID_COLUMN_NAME     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COLUMN_TERM   + " TEXT NOT NULL, " +
                NAME_COLUMN_COURSE + " TEXT NOT NULL, " +
                NAME_COLUMN_TITLE  + " TEXT NOT NULL, " +
                NAME_COLUMN_DETAIL + " TEXT NOT NULL)";
        db.execSQL(CREATE_NAME_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db,
                          final int oldVersion,
                          final int newVersion)
    {
    }

    public long getNumberOfNames(final SQLiteDatabase db)
    {
        final long numEntries;

        numEntries = DatabaseUtils.queryNumEntries(db, NAME_TABLE_NAME);

        return (numEntries);
    }




    public void insertCourse(final SQLiteDatabase db,
                             final String         term,
                             final String         course,
                             final String         title,
                             final String         details)
    {
        final ContentValues contentValues;

        contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN_TERM,   term);
        contentValues.put(NAME_COLUMN_COURSE, course);
        contentValues.put(NAME_COLUMN_TITLE,  title);
        contentValues.put(NAME_COLUMN_DETAIL, details);
        db.insert(NAME_TABLE_NAME, null, contentValues);
    }

    public int deleteName(final SQLiteDatabase db,
                          final String         name)
    {
        final int rows;

        rows = db.delete(NAME_TABLE_NAME,
                NAME_COLUMN_TERM + " = ?",
                new String[]
                        {
                                name,
                        });

        return (rows);
    }


    public Cursor getAllCourses(final Context context,
                              final SQLiteDatabase db)
    {
        final Cursor cursor;

        cursor = db.query(NAME_TABLE_NAME,
                null,
                null,     // selection, null = *
                null,     // selection args (String[])
                null,     // group by
                null,     // having
                null,     // order by
                null);    // limit

        cursor.setNotificationUri(context.getContentResolver(),CourseContentProvider.CONTENT_URI);

        return (cursor);
    }




    public ArrayList<String> getTerms(final Context context,
                           final SQLiteDatabase db)
    {
        final Cursor cursor;

        String[] tableColumns = new String[] {
                NAME_COLUMN_TERM,
                "SELECT * FROM " + NAME_TABLE_NAME
        };
        String groupby = NAME_COLUMN_TERM;

        cursor = db.query(NAME_TABLE_NAME,
                null,
                null,     // selection, null = *
                null,     // selection args (String[])
                groupby,     // group by
                null,     // having
                null,     // order by
                null);    // limit

        cursor.setNotificationUri(context.getContentResolver(), CourseContentProvider.CONTENT_URI);


        ArrayList<String> mArrayList = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(1));
        }
        return mArrayList;
    }





    public ArrayList<String> getTermCourses(final Context context,
                                 final SQLiteDatabase db,
                                 final String term)
    {
        final Cursor cursor;


        String[] tableColumns = new String[] {
                NAME_COLUMN_COURSE,
                "SELECT * FROM " + NAME_TABLE_NAME
        };
        String whereClause = NAME_COLUMN_TERM + " = ?";
        String[] whereArgs = new String[] {
                term
        };
        cursor = db.query(NAME_TABLE_NAME,
                null,
                whereClause,     // selection, null = *
                whereArgs,     // selection args (String[])
                null,     // group by
                null,     // having
                null,     // order by
                null);    // limit

        cursor.setNotificationUri(context.getContentResolver(), CourseContentProvider.CONTENT_URI);

        ArrayList<String> mArrayList = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(2));
        }
        return mArrayList;
    }



    public ArrayList<String> getCourseDetails(final Context context,
                                            final SQLiteDatabase db,
                                            final String course)
    {
        final Cursor cursor;


        String[] tableColumns = new String[] {
                NAME_COLUMN_DETAIL,
                "SELECT * FROM " + NAME_TABLE_NAME
        };
        String whereClause = NAME_COLUMN_COURSE + " = ?";
        String[] whereArgs = new String[] {
                course
        };

        cursor = db.query(NAME_TABLE_NAME,
                null,
                whereClause,     // selection, null = *
                whereArgs,     // selection args (String[])
                null,     // group by
                null,     // having
                null,     // order by
                null);    // limit

        cursor.setNotificationUri(context.getContentResolver(), CourseContentProvider.CONTENT_URI);

        ArrayList<String> mArrayList = new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // The Cursor is now set to the right position
            mArrayList.add(cursor.getString(3));
            mArrayList.add(cursor.getString(4));
        }
        return mArrayList;
    }




}
