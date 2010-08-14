package no.ut.trip;

import no.ut.trip.content.PoiProvider;
import no.ut.trip.content.Pois;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PoisList extends ListActivity {
    private static final String TAG = "PoisList";

    public static final int MENU_ITEM_DELETE = Menu.FIRST;
    public static final int MENU_ITEM_INSERT = Menu.FIRST + 1;

    private static final String[] PROJECTION = new String[] { Pois._ID,
	    Pois.NAME, Pois.TYPE };

    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_TYPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

	Intent intent = getIntent();
	if (intent.getData() == null) {
	    intent.setData(Pois.CONTENT_URI);
	}

	getListView().setOnCreateContextMenuListener(this);

	Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null,
		null, PoiProvider.DEFAULT_SORT_ORDER);

	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		R.layout.list_item_poi, cursor, new String[] { Pois.NAME },
		new int[] { R.id.text });
	setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);

	menu.add(0, MENU_ITEM_INSERT, 0, R.string.menu_insert).setShortcut('3',
		'a').setIcon(android.R.drawable.ic_menu_add);

	Intent intent = new Intent(null, getIntent().getData());
	intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
		new ComponentName(this, PoisList.class), null, intent, 0, null);

	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);
	final boolean haveItems = getListAdapter().getCount() > 0;

	if (haveItems) {
	    Uri uri = ContentUris.withAppendedId(getIntent().getData(),
		    getSelectedItemId());

	    Intent[] specifics = new Intent[1];
	    specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
	    MenuItem[] items = new MenuItem[1];

	    Intent intent = new Intent(null, uri);
	    intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
	    menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, null,
		    specifics, intent, 0, items);

	    if (items[0] != null) {
		items[0].setShortcut('1', 'e');
	    }
	} else {
	    menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
	}

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MENU_ITEM_INSERT:
	    startActivity(new Intent(Intent.ACTION_INSERT, getIntent()
		    .getData()));
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
	    ContextMenuInfo menuInfo) {
	AdapterView.AdapterContextMenuInfo info;
	try {
	    info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	} catch (ClassCastException e) {
	    Log.e(TAG, "bad menuInfo", e);
	    return;
	}

	Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
	if (cursor == null) {
	    // For some reason the requested item isn't available, do nothing
	    return;
	}

	menu.setHeaderTitle(cursor.getString(COLUMN_NAME));
	menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	AdapterView.AdapterContextMenuInfo info;
	try {
	    info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	} catch (ClassCastException e) {
	    Log.e(TAG, "bad menuInfo", e);
	    return false;
	}

	switch (item.getItemId()) {
	case MENU_ITEM_DELETE:
	    Uri poiUri = ContentUris.withAppendedId(getIntent().getData(),
		    info.id);
	    getContentResolver().delete(poiUri, null, null);
	    return true;
	}

	return false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);

	String action = getIntent().getAction();
	if (Intent.ACTION_PICK.equals(action)
		|| Intent.ACTION_GET_CONTENT.equals(action)) {
	    // The caller is waiting for us to return a POI selected by
	    // the user. The have clicked on one, so return it now.
	    setResult(RESULT_OK, new Intent().setData(uri));
	} else {
	    // Launch activity to view/edit the currently selected item
	    startActivity(new Intent(Intent.ACTION_EDIT, uri));
	}
    }

}
