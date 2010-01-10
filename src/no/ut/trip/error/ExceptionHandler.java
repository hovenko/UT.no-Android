package no.ut.trip.error;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ExceptionHandler {

    protected Activity activity;
    protected Throwable throwable;
    protected String message;
    protected AlertDialog alert;
    protected boolean rethrow = false;

    public ExceptionHandler(Activity activity, String msg, Throwable throwable) {
        this.activity = activity;
        this.throwable = throwable;
        this.message = msg;

        setup();
    }

    public ExceptionHandler setRethrow(boolean t) {
        this.rethrow = t;
        return this;
    }

    void setup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message + ": " + throwable.getMessage())
                .setCancelable(false).setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // nothing
                                if (rethrow) {
                                    throw new RuntimeException(throwable);
                                }
                            }
                        });
        alert = builder.create();
    }

    public ExceptionHandler show() {
        alert.show();
        return this;
    }

}
