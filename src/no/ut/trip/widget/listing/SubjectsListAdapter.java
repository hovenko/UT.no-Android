package no.ut.trip.widget.listing;

import java.util.ArrayList;
import java.util.List;

import no.ut.trip.EntrySubjects;
import no.ut.trip.R;
import no.ut.trip.xml.FacetGroup;
import no.ut.trip.xml.FacetList;
import no.ut.trip.xml.Listing;
import no.ut.trip.xml.Resource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    final Listing listings;
    final EntrySubjects activity;

    final List<Resource> subjects;

    public SubjectsListAdapter(final EntrySubjects activity,
	    final Listing listing) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	mInflater = LayoutInflater.from(activity);

	this.listings = listing;
	this.activity = activity;

	subjects = setupSubjects();
    }

    protected ArrayList<Resource> setupSubjects() {
	ArrayList<Resource> subjects = new ArrayList<Resource>();
	FacetList facetList = listings.facets();
	FacetGroup subjectsFacet = facetList.facetByType("subject");
	if (subjectsFacet == null) {
	    return subjects;
	}

	for (Resource res : subjectsFacet.resources()) {
	    subjects.add(res);
	}

	return subjects;
    }

    public List<? extends Resource> getList() {
	return subjects;
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

    public boolean hasStableIds() {
	return true;
    }

    public int getCount() {
	return getList().size();
    }

    public Object getItem(int position) {
	return getList().get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    protected Bitmap getBitmapByResource(Resource res) {
	String value = res.getValue();
	int id = R.drawable.subject_default;

	if ("cottage".equals(value)) {
	    id = R.drawable.subject_cottage;
	}

	Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
		id);

	return bitmap;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.list_item_subject, null);

	    holder = new ViewHolder();
	    holder.icon = (ImageView) convertView.findViewById(R.id.icon);
	    holder.text = (TextView) convertView.findViewById(R.id.text);

	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	Resource res = (Resource) getItem(position);
	holder.text.setText(res.getValue());
	holder.icon.setImageBitmap(getBitmapByResource(res));

	return convertView;
    }

    static class ViewHolder {
	TextView text;
	ImageView icon;
    }
}
