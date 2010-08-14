package no.ut.trip.ws;

import java.io.InputStream;

import no.nrk.listings.ListingDocument;
import no.nrk.listings.xml.ListingXmlDocument;

public class ListingsClient {
    static final String TAG = "ListingsClient";

    public static ListingDocument retrieve(String strUri) throws Exception {
	InputStream is = UtHttpClient.retrieve(strUri);
	ListingDocument document = new ListingXmlDocument(is);

	return document;
    }
}
