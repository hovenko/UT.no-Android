package no.ut.trip.widget.listing;

import java.util.ArrayList;
import java.util.List;

import no.nrk.listings.ListingDocument;
import no.nrk.listings.result.Item;
import no.nrk.listings.result.ItemResource;
import no.nrk.listings.result.ResultList;
import no.ut.trip.Listings;
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

public class ResultListAdapter extends BaseAdapter {
    final LayoutInflater mInflater;

    final ListingDocument listings;
    final Listings activity;

    // final String[] labels = { "Søketreff" };

    public ResultListAdapter(final Listings activity,
	    final ListingDocument listing) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	mInflater = LayoutInflater.from(activity);

	this.listings = listing;
	this.activity = activity;
    }

    private List<Item> getList() {
	ResultList result = listings.getResults();

	List<Item> list = new ArrayList<Item>();
	list.addAll(result);
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

    public Item getItem(int position) {
	return getList().get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    protected Bitmap getBitmapByItem(Item item) {
	int id = R.drawable.subject_default;

	Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
		id);

	return bitmap;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    // TODO make a new layout for result items
	    convertView = mInflater.inflate(R.layout.list_item_subject, null);

	    holder = new ViewHolder();
	    holder.icon = (ImageView) convertView.findViewById(R.id.icon);
	    holder.text = (TextView) convertView.findViewById(R.id.text);

	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	Item item = getItem(position);
	ItemResource res = item.getXmlResource();
	holder.text.setText("" + res.getLabel());
	holder.icon.setImageBitmap(getBitmapByItem(item));

	convertView.setOnClickListener(new OnResultItemClickListener(activity,
		item));

	return convertView;
    }

    static class ViewHolder {
	TextView text;
	ImageView icon;
    }

}
