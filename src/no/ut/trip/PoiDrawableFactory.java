package no.ut.trip;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class PoiDrawableFactory {

    private Context context;

    public PoiDrawableFactory(Context context) {
	this.context = context;
    }

    public Drawable getDrawableByType(final String type) {
	int id = -1;

	if ("article".equals(type)) {
	    id = R.drawable.article;
	} else if ("video".equals(type)) {
	    id = R.drawable.video;
	} else if ("attraksjon".equals(type)) {
	    id = R.drawable.custom_attraksjon;
	} else if ("akebakke".equals(type)) {
	    id = R.drawable.custom_akebakke;
	} else if ("søndagstur".equals(type)) {
	    id = R.drawable.custom_sondagstur;
	} else if ("badeplass".equals(type)) {
	    id = R.drawable.custom_badeplass;
	} else if ("utsiktspunkt".equals(type)) {
	    id = R.drawable.custom_utsiktspunkt;
	}

	if (id == -1) {
	    return null;
	}

	Drawable drawable = context.getResources().getDrawable(id);
	return drawable;
    }
}
