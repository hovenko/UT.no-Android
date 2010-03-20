package no.ut.trip.widget.listing;

import java.util.ArrayList;
import java.util.List;

import no.ut.trip.Listings;
import no.ut.trip.xml.FacetGroup;
import no.ut.trip.xml.FacetList;
import no.ut.trip.xml.Listing;
import no.ut.trip.xml.Partial;
import no.ut.trip.xml.PartialList;
import no.ut.trip.xml.Resource;
import no.ut.trip.xml.ResultItem;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListingListAdapter extends BaseExpandableListAdapter {
    final Listing listings;
    final Listings adapter;

    final int GROUP_PARTIALS = 0;
    final int GROUP_LOCATIONS = 1;
    final int GROUP_TRIPS = 2;

    final String[] ignore_partials = { "group", "page" };

    final PartialList partials;
    final List<Resource> locations;
    final List<Resource> trips;

    final String[] labels = { "Fjern fra søk", "Områder", "Turforslag" };

    public ListingListAdapter(final Listings adapter, final Listing listing) {
	this.listings = listing;
	this.adapter = adapter;

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
	for (ResultItem item : listings.result()) {
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

    public TextView getGenericTextView() {
	// Layout parameters for the ExpandableListView
	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
		ViewGroup.LayoutParams.FILL_PARENT, 64);

	TextView textView = new TextView(adapter);
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
	    textView.setOnClickListener(new OnResultItemClickListener(adapter,
		    res));
	} else {
	    textView.setOnClickListener(new OnResourceClickListener(adapter,
		    res));
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
