package no.ut.trip.widget.listing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nrk.listings.ListingDocument;
import no.nrk.listings.Resource;
import no.nrk.listings.facet.FacetResource;
import no.nrk.listings.facet.PartialResource;
import no.nrk.listings.partial.PartialList;
import no.nrk.listings.query.DefaultQueryField;
import no.nrk.listings.result.Item;
import no.nrk.listings.result.ResultList;
import no.nrk.listings.result.Subject;
import no.ut.trip.Listings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListingListAdapter extends BaseExpandableListAdapter {
    final ListingDocument listings;
    final Listings activity;

    final int GROUP_PARTIALS = 0;
    final int GROUP_LOCATIONS = 1;
    final int GROUP_RESULT = 2;

    static final Subject[] IGNORE_PARTIALS = { new Subject("group"),
	    new Subject("page") };

    final List<PartialResource> partials;
    final List<FacetResource> locations;
    final List<Item> result;

    final String[] labels = { "Fjern fra søk", "Områder", "Søketreff" };

    public ListingListAdapter(final Listings activity,
	    final ListingDocument listing) {
	this.listings = listing;
	this.activity = activity;

	partials = setupPartials();
	locations = setupLocations();
	result = setupResult();
    }

    private List<PartialResource> setupPartials() {
	PartialList partialList = listings.getPartials();
	PartialList filtered = partialList.filterIgnores(IGNORE_PARTIALS);

	List<PartialResource> list = new ArrayList<PartialResource>();
	list.addAll(filtered);
	return list;
    }

    private List<FacetResource> setupLocations() {
	Set<FacetResource> facets = listings.getFacets().facetsByType(
		new DefaultQueryField("location"));

	List<FacetResource> list = new ArrayList<FacetResource>();
	list.addAll(facets);
	return list;
    }

    private List<Item> setupResult() {
	ResultList result = listings.getResults();

	List<Item> list = new ArrayList<Item>();
	list.addAll(result);
	return list;
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
	Object child = getChild(groupPosition, childPosition);

	if (child instanceof Resource) {
	    Resource res = (Resource) child;
	    textView.setText("" + res.getLabel());
	} else if (child instanceof Item) {
	    Item item = (Item) child;
	    textView.setText("" + item.getHtmlResource().getLabel());
	} else {
	    throw new RuntimeException("Unknown child: " + child.getClass());
	}

	if (groupPosition == GROUP_RESULT) {
	    textView.setOnClickListener(new OnResultItemClickListener(activity,
		    (Item) child));
	} else {
	    textView.setOnClickListener(new OnResourceClickListener(activity,
		    (Resource) child));
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
