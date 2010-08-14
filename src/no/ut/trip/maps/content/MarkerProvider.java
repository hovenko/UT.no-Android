//package no.ut.trip.maps.content;
//
//import no.ut.trip.content.PoiDatabaseHelper;
//import no.ut.trip.content.Pois;
//import no.ut.trip.ws.MapMarkersClient;
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.text.TextUtils;
//
//public class MarkerProvider extends ContentProvider {
//
//    public static final String TAG = "MarkerProvider";
//
//    static public final String AUTHORITY = "no.ut.trip.maps.markerprovider";
//
//    static public final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
//
//    static public final String MIME_SINGLE = "vnd.android.cursor.item/vnd.ut.maps.marker";
//    static public final String MIME_MULTI = "vnd.android.cursor.dir/vnd.ut.maps.marker";
//
//    private static final int POIS = 1;
//    private static final int POI_ID = 2;
//
//    private static final UriMatcher uriMatcher;
//    static {
//	uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//	uriMatcher.addURI(AUTHORITY, "pois", POIS);
//	uriMatcher.addURI(AUTHORITY, "pois/#", POI_ID);
//    }
//
//    MapMarkersClient client;
//
//    @Override
//    public boolean onCreate() {
//	client = new MapMarkersClient();
//	return true;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection,
//	    String[] selectionArgs, String sortOrder) {
//	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//	qb.setTables(PoiDatabaseHelper.POIS_TABLE_NAME);
//
//	switch (uriMatcher.match(uri)) {
//	case POIS:
//	    break;
//
//	case POI_ID:
//	    qb.appendWhere(Pois._ID + "=" + uri.getPathSegments().get(1));
//	    break;
//
//	default:
//	    throw new IllegalArgumentException("Unknown URI " + uri);
//	}
//
//	// If no sort order is specified use the default
//	String orderBy;
//	if (TextUtils.isEmpty(sortOrder)) {
//	    orderBy = DEFAULT_SORT_ORDER;
//	} else {
//	    orderBy = sortOrder;
//	}
//
//	SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
//
//	Cursor c = qb.query(db, projection, selection, selectionArgs, null,
//		null, orderBy);
//
//	/*
//	 * Tell the cursor what uri to watch, so it knows when its source data
//	 * changes
//	 */
//	c.setNotificationUri(getContext().getContentResolver(), uri);
//	return c;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//	switch (uriMatcher.match(uri)) {
//	case POIS:
//	    return MIME_MULTI;
//
//	case POI_ID:
//	    return MIME_SINGLE;
//
//	default:
//	    throw new IllegalArgumentException("Unsupported URI: " + uri);
//	}
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues initialValues) {
//	// Validate the requested uri
//	if (uriMatcher.match(uri) != POIS) {
//	    throw new IllegalArgumentException("Unknown URI " + uri);
//	}
//
//	ContentValues values;
//	if (initialValues != null) {
//	    values = new ContentValues(initialValues);
//	} else {
//	    values = new ContentValues();
//	}
//
//	Long now = Long.valueOf(System.currentTimeMillis());
//
//	// Make sure that the fields are all set
//	if (values.containsKey(Pois.CREATED_DATE) == false) {
//	    values.put(Pois.CREATED_DATE, now);
//	}
//
//	if (values.containsKey(Pois.MODIFIED_DATE) == false) {
//	    values.put(Pois.MODIFIED_DATE, now);
//	}
//
//	if (values.containsKey(Pois.NAME) == false) {
//	    values.put(Pois.NAME, "");
//	}
//
//	if (values.containsKey(Pois.TYPE) == false) {
//	    values.put(Pois.TYPE, "");
//	}
//
//	if (values.containsKey(Pois.DESCRIPTION) == false) {
//	    values.put(Pois.DESCRIPTION, "");
//	}
//
//	if (values.containsKey(Pois.ARRIVAL) == false) {
//	    values.put(Pois.ARRIVAL, "");
//	}
//
//	if (values.containsKey(Pois.GEO) == false) {
//	    values.put(Pois.GEO, "");
//	}
//
//	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//	long rowId = db.insert(PoiDatabaseHelper.POIS_TABLE_NAME,
//		Pois.DESCRIPTION, values);
//
//	if (rowId > 0) {
//	    Uri poiUri = ContentUris.withAppendedId(Pois.CONTENT_URI, rowId);
//	    getContext().getContentResolver().notifyChange(poiUri, null);
//	    return poiUri;
//	}
//
//	throw new SQLException("Failed to insert row into " + uri);
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection,
//	    String[] selectionArgs) {
//	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//	int count;
//	switch (uriMatcher.match(uri)) {
//	case POIS:
//	    count = db.update(PoiDatabaseHelper.POIS_TABLE_NAME, values,
//		    selection, selectionArgs);
//	    break;
//
//	case POI_ID:
//	    String poiId = uri.getPathSegments().get(1);
//	    count = db.update(PoiDatabaseHelper.POIS_TABLE_NAME, values,
//		    Pois._ID
//			    + "="
//			    + poiId
//			    + (!TextUtils.isEmpty(selection) ? " AND ("
//				    + selection + ")" : ""), selectionArgs);
//	    break;
//
//	default:
//	    throw new IllegalArgumentException("Unknown URI " + uri);
//	}
//
//	getContext().getContentResolver().notifyChange(uri, null);
//	return count;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//	int count;
//
//	switch (uriMatcher.match(uri)) {
//	case POIS:
//	    count = db.delete(PoiDatabaseHelper.POIS_TABLE_NAME, selection,
//		    selectionArgs);
//	    break;
//
//	case POI_ID:
//	    String poiId = uri.getPathSegments().get(1);
//	    count = db.delete(PoiDatabaseHelper.POIS_TABLE_NAME, Pois._ID
//		    + "="
//		    + poiId
//		    + (!TextUtils.isEmpty(selection) ? " AND (" + selection
//			    + ")" : ""), selectionArgs);
//	    break;
//
//	default:
//	    throw new IllegalArgumentException("Unknown URI " + uri);
//	}
//
//	getContext().getContentResolver().notifyChange(uri, null);
//	return count;
//    }
// }
