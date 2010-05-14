package no.ut.trip.widget.listing;

import java.util.List;

import no.nrk.listings.facet.FacetField;
import no.nrk.listings.result.Subject;
import no.ut.trip.Listings;
import no.ut.trip.xml.FacetGroup;
import no.ut.trip.xml.Listing;
import no.ut.trip.xml.PartialList;
import no.ut.trip.xml.Resource;
import no.ut.trip.xml.ResourceList;
import no.ut.trip.xml.ResultList;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListingListAdapter extends BaseExpandableListAdapter {
    final Listing listings;
    final Listings activity;

    final int GROUP_PARTIALS = 0;
    final int GROUP_LOCATIONS = 1;
    final int GROUP_RESULT = 2;

    static final Subject[] IGNORE_PARTIALS = { new Subject("group"),
	    new Subject("page") };

    final PartialList partials;
    final ResourceList locations;
    final ResultList result;

    final String[] labels = { "Fjern fra søk", "Områder", "Turforslag" };

    public ListingListAdapter(final Listings activity, final Listing listing) {
	this.listings = listing;
	this.activity = activity;

	partials = setupPartials();
	locations = setupLocations();
	result = setupResult();
    }

    private PartialList setupPartials() {
	PartialList partialList = listings.partials();
	return partialList.filterIgnores(IGNORE_PARTIALS);
    }

    private ResourceList setupLocations() {
	FacetGroup locationFacet = listings.facetsByType(new FacetField(
		"location"));

	return locationFacet.resources();
    }

    private ResultList setupResult() {
	return listings.result();
    }

    public List<? extends Object> getGroupList(int pos) {
	switch (pos) {
	case GROUP_PARTIALS:
	    return partials;

	case GROUP_LOCATIONS:
	    return locations;

	case GROUP_RESULT:
	    return result;
	}

	return null;
    }

    public Object getChild(int groupPosition, int childPosition) {
	List<? extends Object> list = getGroupList(groupPosition);

	if (list != null) {
	    return list.get(childPosition);
	}

	return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
	return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
	List<? extends Object> list = getGroupList(groupPosition);
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

	TextView textView = new TextView(activity);
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

	if (groupPosition == GROUP_RESULT) {
	    textView.setOnClickListener(new OnResultItemClickListener(activity,
		    res));
	} else {
	    textView.setOnClickListener(new OnResourceClickListener(activity,
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
