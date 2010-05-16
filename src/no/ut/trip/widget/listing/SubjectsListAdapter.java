package no.ut.trip.widget.listing;

import java.util.ArrayList;
import java.util.List;

import no.nrk.listings.QueryResource;
import no.nrk.listings.facet.FacetResource;
import no.nrk.listings.facet.FacetSet;
import no.nrk.listings.query.DefaultQueryToken;
import no.nrk.listings.query.QueryToken;
import no.ut.trip.EntrySubjects;
import no.ut.trip.R;
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

    final EntrySubjects activity;

    final FacetSet subjects;

    public SubjectsListAdapter(final EntrySubjects activity,
	    final FacetSet subjects) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	mInflater = LayoutInflater.from(activity);

	this.activity = activity;
	this.subjects = subjects;
    }

    public List<FacetResource> getList() {
	List<FacetResource> list = new ArrayList<FacetResource>();
	list.addAll(subjects);
	return list;
    }

    public TextView createGenericTextView() {
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

    protected Bitmap getBitmapByResource(QueryResource res) {
	QueryToken subject = res.getToken();
	int id = R.drawable.subject_default;

	if (new DefaultQueryToken("cottage").equals(subject)) {
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

	Object obj = getItem(position);
	QueryResource res;

	if (obj instanceof QueryResource) {
	    res = (QueryResource) obj;
	} else {
	    throw new RuntimeException("Unknown child: " + obj.getClass());
	}

	holder.text.setText("" + res.getLabel());
	holder.icon.setImageBitmap(getBitmapByResource(res));

	convertView.setOnClickListener(new OnResourceClickListener(activity,
		res));

	return convertView;
    }

    static class ViewHolder {
	TextView text;
	ImageView icon;
    }
}
