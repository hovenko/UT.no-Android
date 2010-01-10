package no.ut.trip;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import no.ut.trip.error.ExceptionHandler;
import no.ut.trip.xml.FacetGroup;
import no.ut.trip.xml.FacetList;
import no.ut.trip.xml.Listing;
import no.ut.trip.xml.Partial;
import no.ut.trip.xml.PartialList;
import no.ut.trip.xml.Resource;
import no.ut.trip.xml.ResourceList;
import no.ut.trip.xml.ResultItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Listings extends Activity {

    static final int DIALOG_PROGRESS = 0;

    static final String TAG = "Listings";

    ProgressDialog progressDialog = null;

    Listing listings = null;

    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_PROGRESS:
            progressDialog = new ProgressDialog(Listings.this);
            progressDialog.setMessage(getText(R.string.process_loading_data));
            progressDialog.setIndeterminate(true);
            return progressDialog;

        default:
            return null;
        }
    }

    // Define the Handler that receives messages from the thread and update the
    // progress
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            boolean complete = msg.getData().getBoolean("complete");
            if (complete) {
                dismissDialog(DIALOG_PROGRESS);
                setupListings(listings);
            }
        }
    };

    /**
     * Nested class that performs progress calculations (counting)
     */
    private class ProgressThread extends Thread {
        Handler mHandler;
        final Uri intentUri;

        ProgressThread(Handler h, Uri intentUri) {
            mHandler = h;
            this.intentUri = intentUri;
        }

        public void run() {
            try {
                String strUri = intentUri.toString();
                Log.d("http", "Retrieving URL: " + strUri);

                URI uri = new URI(strUri);
                HttpClient client = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpGet get = new HttpGet(uri);
                HttpResponse response = client.execute(get, localContext);
                InputStream is = response.getEntity().getContent();
                listings = new Listing(is);
            } catch (Exception e) {
                Log.e(TAG, "Failed to get trip listings", e);
                new ExceptionHandler(Listings.this,
                        "Failed to get trip listings", e).setRethrow(true)
                        .show();
                return;
            }

            Log.v(TAG, "Closing progress dialog");
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putBoolean("complete", true);
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listings);

        showDialog(DIALOG_PROGRESS);

        Intent intent = getIntent();
        Uri intentUri = intent.getData();

        ProgressThread progressThread = new ProgressThread(handler, intentUri);
        progressThread.start();
    }

    private void setupListings(final Listing listings) {
        Log.v(TAG, "setupListings()");

        FacetGroup locations = listings.facets().facetByType("location");
        if (locations != null) {
            ResourceList resources = locations.resources();
            setupResourcesList(resources);
        }

        setupListingsExpandable(listings);

        // PartialList partials = listings.partials();
        // setupPartialsList(partials);
    }

    private void setupListingsExpandable(final Listing listings) {
        ExpandableListView list = (ExpandableListView) findViewById(R.id.list_facet);

        ExpandableListAdapter adapter = new MyExpandableListAdapter(listings);

        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);

        // OnItemClickListener handler = new
        // OnResourceItemClickListener(resources);
        // list.setOnItemClickListener(handler);
    }

    private void setupPartialsList(final PartialList partials) {
        // ListView list = (ListView) findViewById(R.id.list_partials);
        //
        // ArrayList<String> lstStrings = new ArrayList<String>();
        //
        // for (Partial partial : partials) {
        // lstStrings.add(partial.getValue());
        // }
        //
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(list
        // .getContext(), android.R.layout.simple_list_item_1, lstStrings);
        //
        // list.setAdapter(adapter);
        // list.setTextFilterEnabled(true);
        //
        // OnItemClickListener handler = new OnPartialClickListener(partials);
        // list.setOnItemClickListener(handler);
    }

    private void setupResourcesList(final ResourceList resources) {
        // ExpandableListView list = (ExpandableListView)
        // findViewById(R.id.list_facet);
        //
        // ArrayList<String> lstStrings = new ArrayList<String>();
        //
        // for (Resource resource : resources) {
        // lstStrings.add(resource.getValue());
        // }
        //
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(list
        // .getContext(), android.R.layout.simple_list_item_1, lstStrings);
        //
        // list.setAdapter(adapter);
        // list.setTextFilterEnabled(true);
        //
        // OnItemClickListener handler = new
        // OnResourceItemClickListener(resources);
        // list.setOnItemClickListener(handler);
    }

    final class MyExpandableListAdapter extends BaseExpandableListAdapter {
        final Listing listings;

        final int GROUP_PARTIALS = 0;
        final int GROUP_LOCATIONS = 1;
        final int GROUP_TRIPS = 2;

        final String[] ignore_partials = { "group", "page" };

        final PartialList partials;
        final List<Resource> locations;
        final List<Resource> trips;

        final String[] labels = { "Fjern fra søk", "Områder", "Turforslag" };

        public MyExpandableListAdapter(Listing listings) {
            this.listings = listings;

            partials = setupPartials();
            locations = setupLocations();
            trips = setupTrips();
        }

        private PartialList setupPartials() {
            PartialList filtered = new PartialList();
            PartialList partialList = listings.partials();

            for (Partial partial : partialList) {
                boolean toAdd = true;

                for (String ignore : ignore_partials) {
                    if (ignore.equals(partial.getType())) {
                        toAdd = false;
                    }
                }

                if (toAdd) {
                    filtered.add(partial);
                }
            }

            return filtered;
        }

        private ArrayList<Resource> setupLocations() {
            ArrayList<Resource> locations = new ArrayList<Resource>();
            FacetList facetList = listings.facets();
            FacetGroup locationFacet = facetList.facetByType("location");
            if (locationFacet == null) {
                return locations;
            }

            for (Resource res : locationFacet.resources()) {
                locations.add(res);
            }

            return locations;
        }

        private ArrayList<Resource> setupTrips() {
            ArrayList<Resource> list = new ArrayList<Resource>();
            for (ResultItem item : listings.result().itemsByType("trip")) {
                list.add(item.resource());
            }
            return list;
        }

        public List<? extends Resource> getGroupList(int pos) {
            switch (pos) {
            case GROUP_PARTIALS:
                return partials;

            case GROUP_LOCATIONS:
                return locations;

            case GROUP_TRIPS:
                return trips;
            }

            return null;
        }

        public Object getChild(int groupPosition, int childPosition) {
            List<? extends Resource> list = getGroupList(groupPosition);

            if (list != null) {
                return list.get(childPosition);
            }

            return null;
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            List<? extends Resource> list = getGroupList(groupPosition);
            return list.size();
        }

        public Object getGroup(int groupPosition) {
            return getGroupList(groupPosition);
        }

        public int getGroupCount() {
            return 3;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // public Button getGenericView() {
        // // Layout parameters for the ExpandableListView
        // AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
        // ViewGroup.LayoutParams.FILL_PARENT, 64);
        //
        // Button button = new Button(Listings.this);
        // button.setLayoutParams(lp);
        // button.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // button.setPadding(36, 0, 0, 0);
        //
        // return button;
        // }

        public TextView getGenericTextView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);

            TextView textView = new TextView(Listings.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = getGenericTextView();
            Resource res = (Resource) getChild(groupPosition, childPosition);
            textView.setText(res.getValue());

            if (groupPosition == GROUP_TRIPS) {
                textView.setOnClickListener(new OnResultItemClickListener(res));
            } else {
                textView.setOnClickListener(new OnResourceClickListener(res));
            }
            return textView;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericTextView();
            textView.setText(labels[groupPosition]);

            if (!isExpanded && getGroupList(groupPosition).size() == 0) {
                textView.setVisibility(View.GONE);
            }

            return textView;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    final class OnResourceClickListener implements OnClickListener {
        private final Resource resource;

        public OnResourceClickListener(final Resource resource) {
            this.resource = resource;
        }

        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            Uri uri = Uri.parse(resource.getURL().toString());
            intent.setDataAndType(uri, "application/xml");
            startActivity(intent);
        }
    }

    final class OnResultItemClickListener implements OnClickListener {
        private final Resource resource;

        public OnResultItemClickListener(final Resource resource) {
            this.resource = resource;
        }

        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Listings.this);
            builder.setMessage(resource.getValue()).setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // nothing
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

            // Intent intent = new Intent(Intent.ACTION_SEARCH);
            // Uri uri = Uri.parse(resource.getURL().toString());
            // intent.setDataAndType(uri, "application/xml");
            // startActivity(intent);
        }
    }

    final class OnResourceItemClickListener implements OnItemClickListener {
        private final ResourceList resources;

        public OnResourceItemClickListener(final ResourceList resources) {
            this.resources = resources;
        }

        public void onItemClick(AdapterView<? extends Adapter> parent, View v,
                int position, long id) {
            Resource res = resources.get(position);
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            Uri uri = Uri.parse(res.getURL().toString());
            intent.setDataAndType(uri, "application/xml");
            startActivity(intent);
        }
    }

    final class OnPartialClickListener implements OnItemClickListener {
        private final PartialList partials;

        public OnPartialClickListener(final PartialList partials) {
            this.partials = partials;
        }

        public void onItemClick(AdapterView<? extends Adapter> parent, View v,
                int position, long id) {
            Partial partial = partials.get(position);
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            Uri uri = Uri.parse(partial.getURL().toString());
            intent.setDataAndType(uri, "application/xml");
            startActivity(intent);
        }
    }

}