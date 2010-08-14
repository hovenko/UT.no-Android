package no.ut.trip.maps;

public class MapEvent {
    public static final int ACTION_ANIMATE = 1;
    public static final int ACTION_ANIMATE_STOP = 2;

    private int action;

    public MapEvent(int action) {
	this.action = action;
    }

    public int getAction() {
	return action;
    }
}
