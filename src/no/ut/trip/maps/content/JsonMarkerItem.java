package no.ut.trip.maps.content;

import no.ut.trip.GeoPointVO;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonMarkerItem {
    String uuid;
    String type;
    String customType;
    GeoPointVO point;

    final JSONObject json;

    public JsonMarkerItem(JSONObject json) {
	this.json = json;

	this.init();
    }

    private void init() {
	try {
	    String uuid = json.getString("uuid");
	    String type = json.getString("type");
	    String customType = json.optString("custom_type", type);
	    Double latitude = json.getDouble("lat");
	    Double longitude = json.getDouble("lng");

	    GeoPointVO point = new GeoPointVO();
	    point.latitude = latitude;
	    point.longitude = longitude;

	    this.uuid = uuid;
	    this.type = type;
	    this.customType = customType;
	    this.point = point;
	} catch (JSONException e) {
	    throw new RuntimeException("Failed parsing JSON marker item", e);
	}
    }

    public String getUUID() {
	return uuid;
    }

    public GeoPointVO getPoint() {
	return point;
    }

    public String getType() {
	return type;
    }

    public String getCustomType() {
	return customType;
    }
}
