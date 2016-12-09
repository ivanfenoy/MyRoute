package ar.com.ivanfenoy.myroute.interfaces;


import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.R;

public class Error404Exception extends Exception {

    @Override
    public String getMessage() {
        return App.getContext().getResources().getString(R.string.error_404);
    }
}
