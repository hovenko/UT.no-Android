package no.ut.trip.maps.content;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonMarkerAdapter {
    final JSONObject json;

    public JsonMarkerAdapter(JSONObject json) {
	this.json = json;
    }

    public List<JsonMarkerItem> getItems() {
	List<JsonMarkerItem> list = new ArrayList<JsonMarkerItem>();
	try {
	    JSONArray items = json.getJSONArray("items");
	    for (int i = 0; i < items.length(); i++) {
		JSONObject item = items.getJSONObject(i);
		JsonMarkerItem marker = new JsonMarkerItem(item);
		list.add(marker);
	    }
	} catch (JSONException e) {
	    throw new RuntimeException("Failed parsing JSON structure", e);
	}

	return list;
    }
}
