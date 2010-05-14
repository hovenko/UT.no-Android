package no.ut.trip.xml;

public class TextualContent {
    final protected String value;

    public TextualContent(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    @Override
    public String toString() {
	return value;
    }
}
