package no.ut.trip.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PoiDatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "PoiDatabaseHelper";

    private static final String DATABASE_NAME = "pois.db";
    private static final int DATABASE_VERSION = 3;
    public static final String POIS_TABLE_NAME = "pois";
    public static final String IMAGES_TABLE_NAME = "images";

    private static final String POIS_TABLE_CREATE = "CREATE TABLE "
	    + POIS_TABLE_NAME + " ("

	    + Pois._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

	    + Pois.CREATED_DATE + " INTEGER, "

	    + Pois.MODIFIED_DATE + " INTEGER, "

	    + Pois.NAME + " TEXT, "

	    + Pois.DESCRIPTION + " TEXT, "

	    + Pois.ARRIVAL + " TEXT, "

	    + Pois.GEO + " TEXT, "

	    + Pois.TYPE + " TEXT);";

    private static final String IMAGES_TABLE_CREATE = "CREATE TABLE "
	    + IMAGES_TABLE_NAME + " ("

	    + Pois.Images._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

	    + Pois.Images.POI_ID + " INTEGER, "

	    + Pois.Images.MEDIA_URI + " TEXT);";

    public PoiDatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(POIS_TABLE_CREATE);
	db.execSQL(IMAGES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		+ newVersion + ", hopefully your data will still be intact");

	if (oldVersion < 3) {
	    Log.i(TAG, "Creating table: " + IMAGES_TABLE_NAME);
	    db.execSQL(IMAGES_TABLE_CREATE);
	}
    }
}
